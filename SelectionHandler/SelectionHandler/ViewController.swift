import UIKit

class ViewController: UIViewController {
    
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var sensitivitySlider: UISlider!
    @IBOutlet weak var startButton: UIButton!
    @IBOutlet weak var lastSelectionLabel: UILabel!
    
    private let selectionDetector = SelectionDetector()
    private let pasteboardManager = PasteboardManager()
    private var isMonitoring = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        setupObservers()
        updateStatus()
    }
    
    private func setupUI() {
        sensitivitySlider.minimumValue = 1
        sensitivitySlider.maximumValue = 20
        sensitivitySlider.value = 3
        updateSensitivityLabel()
        
        lastSelectionLabel.text = "No recent selections"
        lastSelectionLabel.numberOfLines = 0
    }
    
    private func setupObservers() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(selectionCopied(_:)),
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
    
    @IBAction func startMonitoringTapped(_ sender: UIButton) {
        isMonitoring.toggle()
        
        if isMonitoring {
            selectionDetector.startBackgroundDetection()
            pasteboardManager.startMonitoring()
            startButton.setTitle("Stop Monitoring", for: .normal)
        } else {
            selectionDetector.stopBackgroundDetection()
            pasteboardManager.stopMonitoring()
            startButton.setTitle("Start Monitoring", for: .normal)
        }
        
        updateStatus()
    }
    
    @IBAction func sensitivityChanged(_ sender: UISlider) {
        let value = Int(sender.value)
        selectionDetector.updateSensitivityThreshold(value)
        updateSensitivityLabel()
    }
    
    @IBAction func clearPasteboardTapped(_ sender: UIButton) {
        pasteboardManager.clearPasteboard()
        lastSelectionLabel.text = "Pasteboard cleared"
    }
    
    @IBAction func testSelectionTapped(_ sender: UIButton) {
        let testText = "This is a test selection for validation purposes"
        pasteboardManager.copyText(testText)
        lastSelectionLabel.text = "Test copied: \(testText)"
    }
    
    private func updateStatus() {
        statusLabel.text = isMonitoring ? "Monitoring Active" : "Monitoring Stopped"
        statusLabel.textColor = isMonitoring ? .systemGreen : .systemRed
    }
    
    private func updateSensitivityLabel() {
        let value = Int(sensitivitySlider.value)
        sensitivitySlider.accessibilityLabel = "Sensitivity threshold: \(value) characters"
    }
    
    @objc private func selectionCopied(_ notification: Notification) {
        if let text = notification.object as? String {
            DispatchQueue.main.async {
                self.lastSelectionLabel.text = "Last selection: \(text.prefix(100))"
            }
        }
    }
    
    @objc private func pasteboardChanged(_ notification: Notification) {
        if let content = notification.object as? String {
            DispatchQueue.main.async {
                self.lastSelectionLabel.text = "Pasteboard changed: \(content.prefix(100))"
            }
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}