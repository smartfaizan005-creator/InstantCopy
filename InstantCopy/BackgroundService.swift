//
//  BackgroundService.swift
//  InstantCopy
//
//  Manages background processing and responds to real-time configuration changes.
//

import Foundation
import UIKit
import Combine

/// Manages background processing for InstantCopy
@MainActor
class BackgroundService: ObservableObject {
    
    // MARK: - Shared Instance
    static let shared = BackgroundService()
    
    // MARK: - Published Properties
    @Published var isActive: Bool = false
    @Published var isConfigured: Bool = false
    
    // MARK: - Private Properties
    private var cancellables = Set<AnyCancellable>()
    private var currentSettings: InstantCopySettings?
    private var backgroundTask: UIBackgroundTaskIdentifier = .invalid
    
    // MARK: - Initialization
    private init() {
        setupNotificationObservers()
        checkInitialConfiguration()
    }
    
    // MARK: - Public Methods
    
    /// Update service configuration with new settings
    func updateConfiguration(with settings: InstantCopySettings) {
        currentSettings = settings
        isConfigured = true
        
        if settings.isEnabled {
            startBackgroundMonitoring()
        } else {
            stopBackgroundMonitoring()
        }
        
        print("InstantCopy background service configured: \(settings.isEnabled ? "enabled" : "disabled")")
    }
    
    /// Check if the service is currently monitoring
    func isMonitoring() -> Bool {
        return isActive && currentSettings?.isEnabled == true
    }
    
    /// Get current sensitivity threshold
    func getCurrentSensitivity() -> Double {
        return currentSettings?.sensitivity ?? 500.0
    }
    
    /// Check if background mode is active
    func isBackgroundModeActive() -> Bool {
        return currentSettings?.backgroundModeEnabled ?? false
    }
    
    // MARK: - Private Methods
    
    private func setupNotificationObservers() {
        // Listen for settings changes
        NotificationCenter.default.publisher(for: .instantCopySettingsChanged)
            .receive(on: RunLoop.main)
            .sink { [weak self] notification in
                if let settings = notification.object as? InstantCopySettings {
                    self?.updateConfiguration(with: settings)
                }
            }
            .store(in: &cancellables)
        
        // Listen for app lifecycle events
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(applicationDidEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: nil
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(applicationWillEnterForeground),
            name: UIApplication.willEnterForegroundNotification,
            object: nil
        )
    }
    
    private func checkInitialConfiguration() {
        // Check if there's an existing settings configuration
        if let settings = getCurrentSettingsFromDefaults() {
            updateConfiguration(with: settings)
        }
    }
    
    private func getCurrentSettingsFromDefaults() -> InstantCopySettings? {
        let isEnabled = UserDefaults.standard.bool(forKey: "isInstantCopyEnabled")
        let sensitivity = UserDefaults.standard.double(forKey: "instantCopySensitivity")
        let backgroundModeEnabled = UserDefaults.standard.bool(forKey: "instantCopyBackgroundMode")
        
        // If no settings exist, return nil
        if !isEnabled && sensitivity == 0 && !backgroundModeEnabled {
            return nil
        }
        
        return InstantCopySettings(
            isEnabled: isEnabled,
            sensitivity: sensitivity > 0 ? sensitivity : 500.0,
            backgroundModeEnabled: backgroundModeEnabled
        )
    }
    
    private func startBackgroundMonitoring() {
        guard let settings = currentSettings else { return }
        
        isActive = true
        
        // If background mode is enabled, register for background processing
        if settings.backgroundModeEnabled {
            registerBackgroundTask()
        }
        
        print("InstantCopy background monitoring started with sensitivity: \(settings.sensitivity)ms")
    }
    
    private func stopBackgroundMonitoring() {
        isActive = false
        
        // End any background task
        endBackgroundTask()
        
        print("InstantCopy background monitoring stopped")
    }
    
    private func registerBackgroundTask() {
        guard UIApplication.shared.applicationState == .background else { return }
        
        backgroundTask = UIApplication.shared.beginBackgroundTask { [weak self] in
            self?.endBackgroundTask()
        }
    }
    
    private func endBackgroundTask() {
        if backgroundTask != .invalid {
            UIApplication.shared.endBackgroundTask(backgroundTask)
            backgroundTask = .invalid
        }
    }
    
    // MARK: - App Lifecycle Handlers
    
    @objc private func applicationDidEnterBackground() {
        guard let settings = currentSettings,
              settings.isEnabled && settings.backgroundModeEnabled else { return }
        
        registerBackgroundTask()
        print("InstantCopy background mode activated")
    }
    
    @objc private func applicationWillEnterForeground() {
        endBackgroundTask()
        print("InstantCopy returned to foreground")
    }
}

// MARK: - Background Task Simulation

extension BackgroundService {
    
    /// Simulate processing (for development/testing)
    func simulateCopyDetection() {
        guard isMonitoring() else { return }
        
        let sensitivity = getCurrentSensitivity()
        print("InstantCopy simulating copy detection with \(sensitivity)ms threshold")
        
        // Simulate the copy gesture detection process
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            self.handleCopyGesture()
        }
    }
    
    private func handleCopyGesture() {
        guard isMonitoring() else { return }
        
        let sensitivity = getCurrentSensitivity()
        
        // Simulate detecting a copy gesture
        print("InstantCopy detected copy gesture (threshold: \(sensitivity)ms)")
        
        // Here you would typically:
        // 1. Access the system clipboard
        // 2. Monitor for paste operations
        // 3. Implement the instant copy functionality
        
        NotificationCenter.default.post(
            name: .instantCopyGestureDetected,
            object: ["timestamp": Date().timeIntervalSince1970]
        )
    }
}

// MARK: - Notification Names

extension Notification.Name {
    static let instantCopyGestureDetected = Notification.Name("instantCopyGestureDetected")
}