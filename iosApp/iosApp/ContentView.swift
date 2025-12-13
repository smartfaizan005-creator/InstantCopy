import SwiftUI
import shared

struct ContentView: View {
    @StateObject private var viewModel = SettingsViewModel()
    
    var body: some View {
        NavigationView {
            Form {
                Section {
                    Toggle(isOn: $viewModel.isEnabled) {
                        VStack(alignment: .leading) {
                            Text("Master Enable")
                                .font(.headline)
                            Text("Enable press detection functionality")
                                .font(.caption)
                                .foregroundColor(.secondary)
                        }
                    }
                    .accessibilityLabel("Master Enable toggle")
                }
                
                Section {
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Press Duration Threshold")
                            .font(.headline)
                        
                        HStack {
                            Text("Fast")
                                .font(.caption)
                                .foregroundColor(.secondary)
                            Spacer()
                            Text("Slow")
                                .font(.caption)
                                .foregroundColor(.secondary)
                        }
                        
                        Slider(
                            value: $viewModel.pressDurationThreshold,
                            in: 0.1...2.0,
                            step: 0.1
                        )
                        .accessibilityLabel("Press duration threshold slider")
                        .accessibilityValue("\(String(format: "%.1f", viewModel.pressDurationThreshold)) seconds")
                        
                        Text("\(String(format: "%.1f", viewModel.pressDurationThreshold))s")
                            .font(.body)
                            .frame(maxWidth: .infinity, alignment: .center)
                    }
                    .padding(.vertical, 8)
                }
                
                Section {
                    Toggle(isOn: $viewModel.autoStartEnabled) {
                        VStack(alignment: .leading) {
                            Text("Auto-Start")
                                .font(.headline)
                            Text("Start automatically when device boots")
                                .font(.caption)
                                .foregroundColor(.secondary)
                        }
                    }
                    .accessibilityLabel("Auto-Start toggle")
                    
                    Toggle(isOn: $viewModel.backgroundServiceEnabled) {
                        VStack(alignment: .leading) {
                            Text("Background Service")
                                .font(.headline)
                            Text("Keep running in background")
                                .font(.caption)
                                .foregroundColor(.secondary)
                        }
                    }
                    .accessibilityLabel("Background Service toggle")
                }
            }
            .navigationTitle("Settings")
        }
    }
}

class SettingsViewModel: ObservableObject {
    private let repository: AppSettingsRepositoryImpl
    
    @Published var isEnabled: Bool = false {
        didSet {
            repository.setEnabled(enabled: isEnabled)
        }
    }
    
    @Published var pressDurationThreshold: Float = 0.5 {
        didSet {
            repository.setPressDurationThreshold(threshold: pressDurationThreshold)
        }
    }
    
    @Published var autoStartEnabled: Bool = false {
        didSet {
            repository.setAutoStartEnabled(enabled: autoStartEnabled)
        }
    }
    
    @Published var backgroundServiceEnabled: Bool = false {
        didSet {
            repository.setBackgroundServiceEnabled(enabled: backgroundServiceEnabled)
        }
    }
    
    init() {
        let storage = IosSettingsStorage()
        self.repository = AppSettingsRepositoryImpl(settingsStorage: storage)
        
        repository.onSettingsChanged { [weak self] settings in
            DispatchQueue.main.async {
                guard let self = self else { return }
                
                if self.isEnabled != settings.isEnabled {
                    self.isEnabled = settings.isEnabled
                    if settings.isEnabled {
                        self.startAccessibilityService()
                    } else {
                        self.stopAccessibilityService()
                    }
                }
                
                if self.pressDurationThreshold != settings.pressDurationThreshold {
                    self.pressDurationThreshold = settings.pressDurationThreshold
                }
                
                if self.autoStartEnabled != settings.autoStartEnabled {
                    self.autoStartEnabled = settings.autoStartEnabled
                    self.updateAutoStart(enabled: settings.autoStartEnabled)
                }
                
                if self.backgroundServiceEnabled != settings.backgroundServiceEnabled {
                    self.backgroundServiceEnabled = settings.backgroundServiceEnabled
                    if settings.backgroundServiceEnabled {
                        self.startBackgroundService()
                    } else {
                        self.stopBackgroundService()
                    }
                }
            }
        }
        
        let currentSettings = repository.settings.value
        self.isEnabled = currentSettings.isEnabled
        self.pressDurationThreshold = currentSettings.pressDurationThreshold
        self.autoStartEnabled = currentSettings.autoStartEnabled
        self.backgroundServiceEnabled = currentSettings.backgroundServiceEnabled
    }
    
    private func startAccessibilityService() {
    }
    
    private func stopAccessibilityService() {
    }
    
    private func startBackgroundService() {
    }
    
    private func stopBackgroundService() {
    }
    
    private func updateAutoStart(enabled: Bool) {
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
