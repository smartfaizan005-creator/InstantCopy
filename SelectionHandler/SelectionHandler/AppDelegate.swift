import UIKit
import UserNotifications

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    private let selectionDetector = SelectionDetector()
    private let pasteboardManager = PasteboardManager()
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        setupBackgroundCapabilities()
        setupNotifications()
        setupAccessibility()
        return true
    }
    
    func applicationDidEnterBackground(_ application: UIApplication) {
        selectionDetector.startBackgroundDetection()
    }
    
    func applicationWillEnterForeground(_ application: UIApplication) {
        selectionDetector.stopBackgroundDetection()
    }
    
    private func setupBackgroundCapabilities() {
        // Ensure background processing modes are enabled
        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            if let _ = UIApplication.shared.setKeepAliveTimeout(600, handler: {
                self.selectionDetector.performBackgroundRefresh()
            }) {
                print("Background keep-alive enabled")
            }
        }
    }
    
    private func setupNotifications() {
        UNUserNotificationCenter.current().delegate = self
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            if granted {
                print("Notification permission granted")
            }
        }
    }
    
    private func setupAccessibility() {
        // Enable accessibility notifications for system-wide text selection
        UIAccessibility.post(notification: .announcement, argument: "Selection handler activated")
    }
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification) async -> UNNotificationPresentationOptions {
        return [.banner, .sound]
    }
}