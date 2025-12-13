import UIKit

class ViewController: UIViewController {
    
    @IBOutlet weak var enableSwitch: UISwitch!
    @IBOutlet weak var autoStartSwitch: UISwitch!
    @IBOutlet weak var sensitivitySlider: UISlider!
    @IBOutlet weak var sensitivityLabel: UILabel!
    
    private let userDefaults = UserDefaults.standard
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupUI()
        loadSettings()
    }
    
    private func setupUI() {
        title = "InstantCopy Settings"
        
        // Enable switch
        enableSwitch.addTarget(self, action: #selector(enableSwitchChanged), for: .valueChanged)
        
        // Auto-start switch
        autoStartSwitch.addTarget(self, action: #selector(autoStartSwitchChanged), for: .valueChanged)
        
        // Sensitivity slider
        sensitivitySlider.minimumValue = 100
        sensitivitySlider.maximumValue = 3000
        sensitivitySlider.addTarget(self, action: #selector(sensitivitySliderChanged), for: .valueChanged)
    }
    
    private func loadSettings() {
        enableSwitch.isOn = userDefaults.bool(forKey: "enabled")
        autoStartSwitch.isOn = userDefaults.bool(forKey: "auto_start")
        let sensitivity = userDefaults.integer(forKey: "sensitivity")
        sensitivitySlider.value = Float(sensitivity > 0 ? sensitivity : 500)
        updateSensitivityLabel()
    }
    
    @objc private func enableSwitchChanged() {
        userDefaults.set(enableSwitch.isOn, forKey: "enabled")
        userDefaults.synchronize()
    }
    
    @objc private func autoStartSwitchChanged() {
        userDefaults.set(autoStartSwitch.isOn, forKey: "auto_start")
        userDefaults.synchronize()
    }
    
    @objc private func sensitivitySliderChanged() {
        updateSensitivityLabel()
        userDefaults.set(Int(sensitivitySlider.value), forKey: "sensitivity")
        userDefaults.synchronize()
    }
    
    private func updateSensitivityLabel() {
        sensitivityLabel.text = "Sensitivity: \(Int(sensitivitySlider.value))ms"
    }
}
