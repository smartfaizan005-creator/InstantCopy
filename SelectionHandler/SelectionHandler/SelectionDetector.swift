import UIKit
import Accessibility
import WebKit

/// Core class for detecting text selections system-wide using accessibility APIs
class SelectionDetector: NSObject {
    
    // MARK: - Properties
    
    private var isDetecting = false
    private var sensitivityThreshold = 3 // Minimum characters for valid selection
    private var lastDetectedSelection = ""
    private var selectionCount = 0
    private var observationToken: Any?
    
    // MARK: - Initialization
    
    override init() {
        super.init()
        setupAccessibilityObservations()
    }
    
    deinit {
        stopDetection()
    }
    
    // MARK: - Public Methods
    
    func startBackgroundDetection() {
        guard !isDetecting else { return }
        
        isDetecting = true
        
        // Start observing accessibility notifications
        startObservingAccessibilityNotifications()
        
        // Set up timer for periodic checks
        startPeriodicChecking()
        
        print("Selection detection started")
    }
    
    func stopBackgroundDetection() {
        stopDetection()
        print("Selection detection stopped")
    }
    
    func performBackgroundRefresh() {
        if isDetecting {
            checkCurrentSelection()
        }
    }
    
    func updateSensitivityThreshold(_ threshold: Int) {
        sensitivityThreshold = max(1, threshold)
    }
    
    // MARK: - Private Methods
    
    private func setupAccessibilityObservations() {
        // Subscribe to accessibility notifications that indicate text selection
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(accessibilitySelectionChanged(_:)),
            name: UIAccessibility.textSelectionChangedNotification,
            object: nil
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(accessibilityFocusedElementChanged(_:)),
            name: UIAccessibility.elementFocusedNotification,
            object: nil
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(accessibilityRegionChanged(_:)),
            name: UIAccessibility.regionChangedNotification,
            object: nil
        )
    }
    
    private func startObservingAccessibilityNotifications() {
        // Enable accessibility voice over hints
        UIAccessibility.post(notification: .announcement, argument: "Text selection monitoring active")
    }
    
    private func startPeriodicChecking() {
        Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true) { [weak self] _ in
            self?.checkCurrentSelection()
        }
    }
    
    private func stopDetection() {
        isDetecting = false
        // Timer will be automatically invalidated when the object is deallocated
    }
    
    private func checkCurrentSelection() {
        guard isDetecting else { return }
        
        // Try to get selected text using accessibility APIs
        if let selectedText = getSelectedTextAccessibility() {
            processDetectedSelection(selectedText)
        }
    }
    
    private func getSelectedTextAccessibility() -> String? {
        // Try multiple strategies to get selected text
        
        // Strategy 1: Get the focused element and check its selected text
        if let focusedElement = UIAccessibility.focusedElement {
            if let textElement = focusedElement as? UIAccessibilitySelection,
               let selectedText = textElement.accessibilityValue as? String,
               let range = textElement.accessibilitySelectionRange,
               range.length > 0 {
                
                let start = selectedText.index(selectedText.startIndex, offsetBy: range.location)
                let end = selectedText.index(start, offsetBy: range.length)
                return String(selectedText[start..<end])
            }
        }
        
        // Strategy 2: Check for any UI element with selection
        let selectedText = getTextFromFirstResponder()
        return selectedText
        
        // Strategy 3: System-wide clipboard monitoring (fallback)
        // This would require background app refresh entitlement
    }
    
    private func getTextFromFirstResponder() -> String? {
        // Get the current first responder and try to extract selected text
        guard let window = UIApplication.shared.windows.first,
              let firstResponder = window.findFirstResponder() else {
            return nil
        }
        
        if let textView = firstResponder as? UITextView {
            let selectedRange = textView.selectedRange
            if selectedRange.length > 0 {
                let text = textView.text as NSString
                return text.substring(with: selectedRange)
            }
        } else if let textField = firstResponder as? UITextField {
            if let selectedText = textField.text?.nsString.substring(with: textField.selectedTextRange ?? .empty) {
                return selectedText
            }
        } else if let webView = firstResponder as? WKWebView {
            // WebView selection detection would go here
            return getWebViewSelection(webView)
        }
        
        return nil
    }
    
    private func getWebViewSelection(_ webView: WKWebView) -> String? {
        // JavaScript to get selected text in web view
        let javascript = """
        window.getSelection().toString();
        """
        
        // This would need to be executed asynchronously
        webView.evaluateJavaScript(javascript) { result, _ in
            if let selectedText = result as? String, !selectedText.isEmpty {
                DispatchQueue.main.async {
                    self.processDetectedSelection(selectedText)
                }
            }
        }
        
        return nil
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
            print("Selection too short: \(trimmedText.count) characters")
            return
        }
        
        // Wait for multiple confirmations to avoid accidental triggers
        guard selectionCount >= 2 else {
            print("Selection not confirmed yet: attempt \(selectionCount)")
            return
        }
        
        // Selection is confirmed, copy to pasteboard
        copyToPasteboard(trimmedText)
        showSelectionNotification(trimmedText)
        
        // Reset for next selection
        selectionCount = 0
        lastDetectedSelection = ""
    }
    
    private func copyToPasteboard(_ text: String) {
        UIPasteboard.general.string = text
        print("Copied to pasteboard: \(text.prefix(50))...")
        
        // Post notification for other parts of the app
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: text
        )
    }
    
    private func showSelectionNotification(_ text: String) {
        let content = UNMutableNotificationContent()
        content.title = "Text Selected"
        content.body = "Copied \"\(text.prefix(30))\(text.count > 30 ? "..." : "")\" to pasteboard"
        content.sound = UNNotificationSound.default
        
        let request = UNNotificationRequest(
            identifier: UUID().uuidString,
            content: content,
            trigger: nil
        )
        
        UNUserNotificationCenter.current().add(request)
    }
    
    // MARK: - Accessibility Notification Handlers
    
    @objc private func accessibilitySelectionChanged(_ notification: Notification) {
        guard isDetecting else { return }
        
        if let userInfo = notification.userInfo,
           let selectedText = userInfo[UIAccessibility.textSelectionAttribute] as? String {
            processDetectedSelection(selectedText)
        }
    }
    
    @objc private func accessibilityFocusedElementChanged(_ notification: Notification) {
        guard isDetecting else { return }
        
        // Delay to allow the selection to stabilize
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            self.checkCurrentSelection()
        }
    }
    
    @objc private func accessibilityRegionChanged(_ notification: Notification) {
        guard isDetecting else { return }
        
        // This could indicate a new text selection
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
            self.checkCurrentSelection()
        }
    }
}

// MARK: - Extension for UIWindow first responder search

extension UIWindow {
    func findFirstResponder() -> UIResponder? {
        if self.isFirstResponder {
            return self
        }
        
        for subview in self.subviews {
            if let responder = subview.findFirstResponder() {
                return responder
            }
        }
        
        return nil
    }
}

extension UIView {
    func findFirstResponder() -> UIResponder? {
        if self.isFirstResponder {
            return self
        }
        
        for subview in self.subviews {
            if let responder = subview.findFirstResponder() {
                return responder
            }
        }
        
        return nil
    }
}

// MARK: - Notification Names

extension Notification.Name {
    static let selectionCopiedToPasteboard = Notification.Name("selectionCopiedToPasteboard")
}