import UIKit

/// Manages the system-wide pasteboard with automatic text selection copying
class PasteboardManager: NSObject {
    
    // MARK: - Properties
    
    private let pasteboard = UIPasteboard.general
    private var lastCopiedContent = ""
    private var isMonitoring = false
    
    // MARK: - Initialization
    
    override init() {
        super.init()
        setupPasteboardMonitoring()
    }
    
    // MARK: - Public Methods
    
    func startMonitoring() {
        guard !isMonitoring else { return }
        
        isMonitoring = true
        print("Pasteboard monitoring started")
    }
    
    func stopMonitoring() {
        isMonitoring = false
        print("Pasteboard monitoring stopped")
    }
    
    func getCurrentPasteboardContent() -> String? {
        return pasteboard.string
    }
    
    func clearPasteboard() {
        pasteboard.string = nil
    }
    
    func copyText(_ text: String) {
        pasteboard.string = text
        lastCopiedContent = text
        print("Text copied to pasteboard: \(text.prefix(50))...")
    }
    
    // MARK: - Private Methods
    
    private func setupPasteboardMonitoring() {
        // Set up observer for pasteboard changes
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(pasteboardChanged(_:)),
            name: UIPasteboardChangedNotification,
            object: nil
        )
    }
    
    @objc private func pasteboardChanged(_ notification: Notification) {
        guard isMonitoring else { return }
        
        // Check if the pasteboard content has actually changed
        if let currentContent = pasteboard.string,
           currentContent != lastCopiedContent {
            
            lastCopiedContent = currentContent
            print("Pasteboard changed: \(currentContent.prefix(50))...")
            
            // Post notification for app components
            NotificationCenter.default.post(
                name: .pasteboardContentChanged,
                object: currentContent
            )
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}

// MARK: - Extension for Pasteboard-related notifications

extension Notification.Name {
    static let pasteboardContentChanged = Notification.Name("pasteboardContentChanged")
}