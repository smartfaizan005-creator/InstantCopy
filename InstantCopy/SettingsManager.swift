//
//  SettingsManager.swift
//  InstantCopy
//
//  Manages app settings persistence and provides real-time configuration updates.
//

import Foundation
import Combine

/// Manages InstantCopy settings with UserDefaults persistence and real-time updates
@MainActor
class SettingsManager: ObservableObject {
    
    // MARK: - Published Properties
    @Published var isEnabled: Bool {
        didSet {
            UserDefaults.standard.set(isEnabled, forKey: "isInstantCopyEnabled")
            applySettings()
        }
    }
    
    @Published var sensitivity: Double {
        didSet {
            UserDefaults.standard.set(sensitivity, forKey: "instantCopySensitivity")
            applySettings()
        }
    }
    
    @Published var backgroundModeEnabled: Bool {
        didSet {
            UserDefaults.standard.set(backgroundModeEnabled, forKey: "instantCopyBackgroundMode")
            applySettings()
        }
    }
    
    // MARK: - Constants
    static let shared = SettingsManager()
    
    private let sensitivityRange: ClosedRange<Double> = 100...2000 // 100ms to 2000ms
    private let defaultSensitivity: Double = 500 // 500ms default
    
    // MARK: - Initialization
    private init() {
        // Load settings from UserDefaults with defaults
        self.isEnabled = UserDefaults.standard.bool(forKey: "isInstantCopyEnabled")
        self.sensitivity = UserDefaults.standard.double(forKey: "instantCopySensitivity")
        if self.sensitivity == 0 {
            self.sensitivity = defaultSensitivity
        }
        self.backgroundModeEnabled = UserDefaults.standard.bool(forKey: "instantCopyBackgroundMode")
        
        // Ensure sensitivity is within valid range
        if !sensitivityRange.contains(self.sensitivity) {
            self.sensitivity = defaultSensitivity
        }
        
        // Apply initial settings
        applySettings()
    }
    
    // MARK: - Public Methods
    
    /// Reset all settings to defaults
    func resetToDefaults() {
        isEnabled = false
        sensitivity = defaultSensitivity
        backgroundModeEnabled = false
        applySettings()
    }
    
    /// Get current sensitivity in milliseconds
    func getSensitivityInMilliseconds() -> Double {
        return sensitivity
    }
    
    /// Check if InstantCopy is currently active
    func isInstantCopyActive() -> Bool {
        return isEnabled
    }
    
    /// Check if background processing is enabled
    func isBackgroundModeActive() -> Bool {
        return backgroundModeEnabled
    }
    
    // MARK: - Private Methods
    
    /// Apply current settings to the background service
    private func applySettings() {
        let settings = InstantCopySettings(
            isEnabled: isEnabled,
            sensitivity: sensitivity,
            backgroundModeEnabled: backgroundModeEnabled
        )
        
        // Notify the background service of settings changes
        NotificationCenter.default.post(
            name: .instantCopySettingsChanged,
            object: settings
        )
        
        // Update background service if needed
        BackgroundService.shared.updateConfiguration(with: settings)
    }
}

// MARK: - Settings Model

/// Settings model for InstantCopy
struct InstantCopySettings {
    let isEnabled: Bool
    let sensitivity: Double // in milliseconds
    let backgroundModeEnabled: Bool
}

// MARK: - Notification Names

extension Notification.Name {
    static let instantCopySettingsChanged = Notification.Name("instantCopySettingsChanged")
}

// MARK: - UserDefaults Keys Extension

extension UserDefaults {
    convenience init?(suiteName: String) {
        self.init(suiteName: suiteName)
    }
}