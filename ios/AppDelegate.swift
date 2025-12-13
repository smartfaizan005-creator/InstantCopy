import UIKit
import Foundation

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?
    
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        // Enable background modes
        application.setMinimumBackgroundFetchInterval(UIApplication.backgroundFetchIntervalMinimum)
        
        return true
    }
    
    func application(_ application: UIApplication,
                     performFetchWithCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        // Perform background clipboard monitoring
        completionHandler(.newData)
    }
    
    func applicationDidFinishLaunching(_ application: UIApplication) {
        // Request accessibility permissions if needed
        let accessibilityIdentifier = "com.instantcopy.clipboard"
        if !UIAccessibility.isVoiceOverRunning {
            // Initialize settings
            initializeSettings()
        }
    }
    
    func applicationDidEnterBackground(_ application: UIApplication) {
        // Continue monitoring in background if enabled
        let userDefaults = UserDefaults.standard
        let isEnabled = userDefaults.bool(forKey: "enabled")
        let autoStart = userDefaults.bool(forKey: "auto_start")
        
        if isEnabled && autoStart {
            // Keep background monitoring active
        }
    }
    
    private func initializeSettings() {
        let userDefaults = UserDefaults.standard
        if !userDefaults.bool(forKey: "settings_initialized") {
            userDefaults.set(true, forKey: "enabled")
            userDefaults.set(500, forKey: "sensitivity")
            userDefaults.set(true, forKey: "auto_start")
            userDefaults.set(true, forKey: "settings_initialized")
            userDefaults.synchronize()
        }
    }
}
