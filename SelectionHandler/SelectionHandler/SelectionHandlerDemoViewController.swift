import UIKit

/// Demo harness view controller for testing and validation
class SelectionHandlerDemoViewController: UIViewController {
    
    // MARK: - Properties
    
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var logTextView: UITextView!
    @IBOutlet weak var startButton: UIButton!
    @IBOutlet weak var demoModeSegmentedControl: UISegmentedControl!
    @IBOutlet weak var appSelector: UIPickerView!
    
    private let selectionDetector = SelectionDetector()
    private let pasteboardManager = PasteboardManager()
    private let demoApps = ["Safari", "Notes", "Mail", "Messages", "Twitter", "AnyApp"]
    private var selectedAppIndex = 0
    private var isDemoRunning = false
    private var backgroundMode = false
    
    // MARK: - Lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        setupDelegates()
        setupObservers()
        logMessage("Demo harness initialized")
    }
    
    // MARK: - Setup Methods
    
    private func setupUI() {
        // Configure log text view
        logTextView.isEditable = false
        logTextView.font = .monospacedSystemFont(ofSize: 12)
        logTextView.text = "Selection Handler Demo\n===================\n\n"
        
        // Configure demo mode selector
        demoModeSegmentedControl.removeAllSegments()
        demoModeSegmentedControl.insertSegment(withTitle: "Foreground", at: 0, animated: false)
        demoModeSegmentedControl.insertSegment(withTitle: "Background", at: 1, animated: false)
        demoModeSegmentedControl.selectedSegmentIndex = 0
        
        // Configure app selector
        appSelector.dataSource = self
        appSelector.delegate = self
        
        updateStatus()
    }
    
    private func setupDelegates() {
        appSelector.dataSource = self
        appSelector.delegate = self
    }
    
    private func setupObservers() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(selectionDetected(_:)),
            name: .selectionCopiedToPasteboard,
            object: nil
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(pasteboardChanged(_:)),
            name: .pasteboardContentChanged,
            object: nil
        )
    }
    
    // MARK: - Demo Control Methods
    
    @IBAction func startDemoTapped(_ sender: UIButton) {
        isDemoRunning.toggle()
        
        if isDemoRunning {
            startDemo()
        } else {
            stopDemo()
        }
        
        updateStatus()
    }
    
    @IBAction func demoModeChanged(_ sender: UISegmentedControl) {
        backgroundMode = sender.selectedSegmentIndex == 1
        logMessage("Demo mode changed to: \(backgroundMode ? "Background" : "Foreground")")
        
        if isDemoRunning {
            if backgroundMode {
                selectionDetector.startBackgroundDetection()
            } else {
                selectionDetector.stopBackgroundDetection()
            }
        }
    }
    
    @IBAction func clearLogTapped(_ sender: UIButton) {
        logTextView.text = "Selection Handler Demo\n===================\n\n"
    }
    
    @IBAction func sendTestSelectionTapped(_ sender: UIButton) {
        sendTestSelection()
    }
    
    func startDemoBackgroundMode() {
        backgroundMode = true
        startDemo()
    }
    
    func stopDemoBackgroundMode() {
        stopDemo()
        backgroundMode = false
    }
    
    // MARK: - Private Methods
    
    private func startDemo() {
        startButton.setTitle("Stop Demo", for: .normal)
        selectionDetector.startBackgroundDetection()
        pasteboardManager.startMonitoring()
        
        logMessage("Demo started - \(backgroundMode ? "Background" : "Foreground") mode")
        
        if backgroundMode {
            startDemoSimulation()
        }
    }
    
    private func stopDemo() {
        startButton.setTitle("Start Demo", for: .normal)
        selectionDetector.stopBackgroundDetection()
        pasteboardManager.stopMonitoring()
        
        logMessage("Demo stopped")
    }
    
    private func updateStatus() {
        if isDemoRunning {
            statusLabel.text = "Demo Running - \(backgroundMode ? "Background" : "Foreground")"
            statusLabel.textColor = backgroundMode ? .systemOrange : .systemGreen
        } else {
            statusLabel.text = "Demo Stopped"
            statusLabel.textColor = .systemRed
        }
    }
    
    private func logMessage(_ message: String) {
        let timestamp = DateFormatter.localizedString(from: Date(), dateStyle: .none, timeStyle: .medium)
        let logEntry = "[\(timestamp)] \(message)\n"
        
        DispatchQueue.main.async {
            self.logTextView.text += logEntry
            self.logTextView.scrollToBottom()
        }
    }
    
    private func startDemoSimulation() {
        guard backgroundMode else { return }
        
        // Run automated demo scenarios
        let demoScenarios = [
            ("Safari", "Website content selection"),
            ("Notes", "Note content selection"),
            ("Mail", "Email content selection"),
            ("Messages", "Message content selection")
        ]
        
        var scenarioIndex = 0
        
        Timer.scheduledTimer(withTimeInterval: 5.0, repeats: true) { [weak self] timer in
            guard let self = self, self.backgroundMode, self.isDemoRunning else {
                timer.invalidate()
                return
            }
            
            if scenarioIndex < demoScenarios.count {
                let (app, content) = demoScenarios[scenarioIndex]
                self.simulateAppSelection(app: app, content: content)
                self.logMessage("Demo: Simulated selection in \(app)")
                scenarioIndex += 1
            } else {
                // Reset for continuous demo
                scenarioIndex = 0
            }
        }
    }
    
    private func sendTestSelection() {
        let selectedApp = demoApps[selectedAppIndex]
        let testContent = "Demo test selection from \(selectedApp) at \(DateFormatter.localizedString(from: Date(), dateStyle: .none, timeStyle: .medium))"
        
        simulateAppSelection(app: selectedApp, content: testContent)
        logMessage("Manual test selection sent to \(selectedApp)")
    }
    
    private func simulateAppSelection(app: String, content: String) {
        let userInfo = [
            "appName": app,
            "content": content,
            "timestamp": Date().timeIntervalSince1970,
            "demoMode": backgroundMode
        ] as [String : Any]
        
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: content,
            userInfo: userInfo
        )
    }
    
    // MARK: - Notification Handlers
    
    @objc private func selectionDetected(_ notification: Notification) {
        if let text = notification.object as? String {
            let appName = notification.userInfo?["appName"] as? String ?? "Unknown"
            let contentPreview = String(text.prefix(50))
            logMessage("✅ Selection detected in \(appName): \"\(contentPreview)\(text.count > 50 ? "..." : "")\"")
        }
    }
    
    @objc private func pasteboardChanged(_ notification: Notification) {
        if let content = notification.object as? String {
            let contentPreview = String(content.prefix(30))
            logMessage("📋 Pasteboard updated: \"\(contentPreview)\(content.count > 30 ? "..." : "")\"")
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}

// MARK: - UIPickerViewDataSource

extension SelectionHandlerDemoViewController: UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return demoApps.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return demoApps[row]
    }
}

// MARK: - UIPickerViewDelegate

extension SelectionHandlerDemoViewController: UIPickerViewDelegate {
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        selectedAppIndex = row
        let appName = demoApps[row]
        logMessage("Selected app for testing: \(appName)")
    }
}

// MARK: - UITextView Extension

extension UITextView {
    func scrollToBottom() {
        let range = NSRange(location: 0, length: 0)
        scrollRangeToVisible(range)
        
        DispatchQueue.main.async {
            let bottom = NSMakeRange(self.text.count - 1, 1)
            self.scrollRangeToVisible(bottom)
        }
    }
}