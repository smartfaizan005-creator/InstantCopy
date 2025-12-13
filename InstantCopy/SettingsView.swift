//
//  SettingsView.swift
//  InstantCopy
//
//  Settings screen for configuring InstantCopy behavior.
//

import SwiftUI

struct SettingsView: View {
    @StateObject private var settingsManager = SettingsManager.shared
    @Environment(\.dismiss) private var dismiss
    
    var body: some View {
        NavigationStack {
            Form {
                // Master Enable Section
                Section {
                    Toggle("Enable InstantCopy", isOn: $settingsManager.isEnabled)
                        .accessibilityLabel("Enable or disable InstantCopy functionality")
                        .accessibilityValue(settingsManager.isEnabled ? "Enabled" : "Disabled")
                    
                    if settingsManager.isEnabled {
                        Text("InstantCopy is now active and will monitor for copy gestures")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    } else {
                        Text("InstantCopy is disabled")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                } header: {
                    Text("Main Settings")
                } footer: {
                    Text("Toggle to enable or disable all InstantCopy functionality")
                }
                
                // Sensitivity Section
                Section {
                    VStack(alignment: .leading, spacing: 12) {
                        HStack {
                            Text("Sensitivity")
                                .font(.headline)
                            
                            Spacer()
                            
                            Text("\(Int(settingsManager.sensitivity))ms")
                                .font(.headline)
                                .monospacedDigit()
                                .foregroundColor(.blue)
                        }
                        
                        Slider(
                            value: $settingsManager.sensitivity,
                            in: 100...2000,
                            step: 50
                        ) {
                            Text("Sensitivity threshold")
                        } minimumValueLabel: {
                            Text("100ms")
                                .font(.caption)
                                .monospacedDigit()
                        } maximumValueLabel: {
                            Text("2000ms")
                                .font(.caption)
                                .monospacedDigit()
                        }
                        .accessibilityLabel("Press duration sensitivity")
                        .accessibilityValue("\(Int(settingsManager.sensitivity)) milliseconds")
                        
                        Text("Adjust how long you need to press to trigger a copy gesture")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                } header: {
                    Text("Sensitivity")
                } footer: {
                    Text("Lower values make InstantCopy more responsive but may increase accidental triggers. Higher values require longer presses but are more deliberate.")
                }
                .disabled(!settingsManager.isEnabled)
                
                // Background Mode Section
                Section {
                    Toggle("Background Mode", isOn: $settingsManager.backgroundModeEnabled)
                        .accessibilityLabel("Enable or disable background processing")
                        .accessibilityValue(settingsManager.backgroundModeEnabled ? "Enabled" : "Disabled")
                    
                    if settingsManager.backgroundModeEnabled {
                        Text("InstantCopy will continue monitoring when the app is backgrounded")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    } else {
                        Text("InstantCopy will pause when the app is backgrounded")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                } header: {
                    Text("Background Processing")
                } footer: {
                    Text("Background mode allows InstantCopy to keep running even when the app is not visible, but may consume more battery.")
                }
                .disabled(!settingsManager.isEnabled)
                
                // Status Section
                Section {
                    HStack {
                        Text("Status")
                        Spacer()
                        StatusIndicator()
                    }
                    .accessibilityElement(.combined)
                    .accessibilityLabel("Current status: \(settingsManager.isEnabled ? "Enabled" : "Disabled")")
                } header: {
                    Text("Current Status")
                }
            }
            .navigationTitle("InstantCopy Settings")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Done") {
                        dismiss()
                    }
                    .accessibilityLabel("Close settings")
                }
            }
        }
        .onAppear {
            // Apply current settings when view appears
            settingsManager.applySettings()
        }
        .onDisappear {
            // Settings are automatically saved via @Published property observers
        }
    }
}

// MARK: - Status Indicator Component

struct StatusIndicator: View {
    @StateObject private var settingsManager = SettingsManager.shared
    
    var body: some View {
        HStack(spacing: 8) {
            Circle()
                .fill(settingsManager.isEnabled ? Color.green : Color.gray)
                .frame(width: 8, height: 8)
            
            Text(settingsManager.isEnabled ? "Active" : "Inactive")
                .font(.caption)
                .fontWeight(.medium)
        }
    }
}

// MARK: - Preview

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
    }
}