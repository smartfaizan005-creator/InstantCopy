import UIKit
import Accessibility
import UserNotifications

/// Background extension for system-wide text selection detection
class SelectionHandlerExtension: UIViewController {
    
    // MARK: - Properties
    
    private let selectionDetector = BackgroundSelectionDetector()
    private var keepAliveTimer: Timer?
    
    // MARK: - Lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupExtension()
        startBackgroundMonitoring()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        stopBackgroundMonitoring()
    }
    
    // MARK: - Extension Setup
    
    private func setupExtension() {
        // Set up the extension interface (minimal UI)
        view.backgroundColor = .clear
        
        // Request notification permissions for alerts
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound]) { granted, error in
            if granted {
                print("Notification permission granted in extension")
            }
        }
    }
    
    private func startBackgroundMonitoring() {
        // Start the background selection detector
        selectionDetector.startDetection()
        
        // Set up keep-alive mechanism
        setupKeepAlive()
        
        print("Background selection monitoring started")
    }
    
    private func stopBackgroundMonitoring() {
        selectionDetector.stopDetection()
        keepAliveTimer?.invalidate()
        keepAliveTimer = nil
        
        print("Background selection monitoring stopped")
    }
    
    private func setupKeepAlive() {
        // Request background app refresh capability
        UIApplication.shared.setMinimumBackgroundFetchInterval(UIApplication.backgroundFetchIntervalMinimum)
        
        // Set up periodic keep-alive
        keepAliveTimer = Timer.scheduledTimer(withTimeInterval: 300, repeats: true) { [weak self] _ in
            self?.performBackgroundRefresh()
        }
    }
    
    @objc private func performBackgroundRefresh() {
        selectionDetector.performBackgroundRefresh()
        
        // Send keep-alive notification
        let content = UNMutableNotificationContent()
        content.title = "Selection Handler"
        content.body = "Background monitoring active"
        content.sound = nil // Silent keep-alive
        
        let request = UNNotificationRequest(
            identifier: "keepalive-\(Date().timeIntervalSince1970)",
            content: content,
            trigger: nil
        )
        
        UNUserNotificationCenter.current().add(request) { _ in }
    }
    
    deinit {
        stopBackgroundMonitoring()
    }
}

// MARK: - Background Selection Detector

class BackgroundSelectionDetector: NSObject {
    
    // MARK: - Properties
    
    private var isDetecting = false
    private var sensitivityThreshold = 3
    private var lastDetectedSelection = ""
    private var selectionCount = 0
    private var backgroundTimer: Timer?
    
    // MARK: - Public Methods
    
    func startDetection() {
        guard !isDetecting else { return }
        
        isDetecting = true
        
        // Start periodic background checking
        startBackgroundTimer()
        
        print("Background selection detection started")
    }
    
    func stopDetection() {
        isDetecting = false
        backgroundTimer?.invalidate()
        backgroundTimer = nil
        
        print("Background selection detection stopped")
    }
    
    func performBackgroundRefresh() {
        if isDetecting {
            checkSystemWideSelection()
        }
    }
    
    func updateSensitivityThreshold(_ threshold: Int) {
        sensitivityThreshold = max(1, threshold)
    }
    
    // MARK: - Private Methods
    
    private func startBackgroundTimer() {
        // Check for selections every 2 seconds
        backgroundTimer = Timer.scheduledTimer(withTimeInterval: 2.0, repeats: true) { [weak self] _ in
            self?.checkSystemWideSelection()
        }
    }
    
    private func checkSystemWideSelection() {
        guard isDetecting else { return }
        
        // Get currently selected text using accessibility
        if let selectedText = getSelectedTextAccessibility() {
            processDetectedSelection(selectedText)
        }
    }
    
    private func getSelectedTextAccessibility() -> String? {
        // Strategy 1: Check focused accessibility element
        if let focusedElement = UIAccessibility.focusedElement {
            if let textElement = focusedElement as? UIAccessibilitySelection,
               let text = textElement.accessibilityValue as? String,
               let range = textElement.accessibilitySelectionRange,
               range.length > 0 {
                
                let start = text.index(text.startIndex, offsetBy: range.location)
                let end = text.index(start, offsetBy: range.length)
                return String(text[start..<end])
            }
        }
        
        // Strategy 2: Get text from system pasteboard if recently updated
        let pasteboard = UIPasteboard.general
        if let currentText = pasteboard.string {
            // Check if this looks like a user selection (not programmatically copied)
            // This is a heuristic - in practice, this would need more sophisticated detection
            return isLikelyUserSelection(currentText) ? currentText : nil
        }
        
        return nil
    }
    
    private func isLikelyUserSelection(_ text: String) -> Bool {
        // Heuristic to determine if text was selected by user vs copied programmatically
        // User selections tend to be:
        // - Not too long (usually under 500 characters)
        // - Not empty
        // - Contain reasonable text content
        
        guard !text.isEmpty && text.count <= 500 else { return false }
        
        // Check if it contains reasonable text characters
        let textCharacterSet = CharacterSet(charactersIn: "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .,!?;:'\"()-–—")
        let textSet = CharacterSet(charactersIn: text)
        
        return !textSet.isDisjoint(with: textCharacterSet)
    }
    
    private func processDetectedSelection(_ text: String) {
        let trimmedText = text.trimmingCharacters(in: .whitespacesAndNewlines)
        
        // Check if this is a new selection or repeated selection
        if trimmedText == lastDetectedSelection {
            selectionCount += 1
        } else {
            lastDetectedSelection = trimmedText
            selectionCount = 1
        }
        
        // Only act on selections that meet our sensitivity threshold
        guard trimmedText.count >= sensitivityThreshold else {
            print("Background: Selection too short: \(trimmedText.count) characters")
            return
        }
        
        // Wait for multiple confirmations to avoid accidental triggers
        guard selectionCount >= 3 else {
            print("Background: Selection not confirmed yet: attempt \(selectionCount)")
            return
        }
        
        // Selection is confirmed - ensure it's in the pasteboard
        ensureTextInPasteboard(trimmedText)
        showBackgroundNotification(trimmedText)
        
        // Reset for next selection
        selectionCount = 0
        lastDetectedSelection = ""
    }
    
    private func ensureTextInPasteboard(_ text: String) {
        let pasteboard = UIPasteboard.general
        if pasteboard.string != text {
            pasteboard.string = text
            print("Background: Confirmed selection in pasteboard: \(text.prefix(50))...")
        }
    }
    
    private func showBackgroundNotification(_ text: String) {
        let content = UNMutableNotificationContent()
        content.title = "Text Selected"
        content.body = "Selection detected: \"\(text.prefix(30))\(text.count > 30 ? "..." : "")\""
        content.sound = UNNotificationSound.default
        
        let request = UNNotificationRequest(
            identifier: UUID().uuidString,
            content: content,
            trigger: nil
        )
        
        UNUserNotificationCenter.current().add(request) { _ in
            print("Background notification sent")
        }
    }
    
    deinit {
        stopDetection()
    }
}