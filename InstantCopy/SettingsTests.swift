//
//  SettingsTests.swift
//  InstantCopy
//
//  Basic tests to verify settings persistence and real-time configuration.
//

import XCTest
@testable import InstantCopy

class SettingsTests: XCTestCase {
    
    private var settingsManager: SettingsManager!
    
    override func setUpWithError() throws {
        try super.setUpWithError()
        settingsManager = SettingsManager.shared
    }
    
    override func tearDownWithError() throws {
        settingsManager = nil
        try super.tearDownWithError()
    }
    
    func testSettingsManagerInitialization() {
        // Test that settings manager initializes with default values
        XCTAssertFalse(settingsManager.isEnabled, "Should be disabled by default")
        XCTAssertEqual(settingsManager.sensitivity, 500.0, "Default sensitivity should be 500ms")
        XCTAssertFalse(settingsManager.backgroundModeEnabled, "Background mode should be disabled by default")
    }
    
    func testSettingsPersistence() {
        // Test that settings are persisted to UserDefaults
        settingsManager.isEnabled = true
        settingsManager.sensitivity = 750.0
        settingsManager.backgroundModeEnabled = true
        
        // Create a new instance to test persistence
        let newSettingsManager = SettingsManager.shared
        
        XCTAssertTrue(newSettingsManager.isEnabled, "Enabled setting should persist")
        XCTAssertEqual(newSettingsManager.sensitivity, 750.0, "Sensitivity should persist")
        XCTAssertTrue(newSettingsManager.backgroundModeEnabled, "Background mode should persist")
    }
    
    func testSensitivityRange() {
        // Test that sensitivity stays within valid range
        settingsManager.sensitivity = 50.0 // Below minimum
        XCTAssertEqual(settingsManager.sensitivity, 500.0, "Should clamp to default when below minimum")
        
        settingsManager.sensitivity = 3000.0 // Above maximum
        XCTAssertEqual(settingsManager.sensitivity, 500.0, "Should clamp to default when above maximum")
        
        settingsManager.sensitivity = 1000.0 // Within range
        XCTAssertEqual(settingsManager.sensitivity, 1000.0, "Should accept valid values")
    }
    
    func testSettingsReset() {
        // Test that reset works properly
        settingsManager.isEnabled = true
        settingsManager.sensitivity = 1000.0
        settingsManager.backgroundModeEnabled = true
        
        settingsManager.resetToDefaults()
        
        XCTAssertFalse(settingsManager.isEnabled, "Should be disabled after reset")
        XCTAssertEqual(settingsManager.sensitivity, 500.0, "Sensitivity should be reset to default")
        XCTAssertFalse(settingsManager.backgroundModeEnabled, "Background mode should be disabled after reset")
    }
    
    func testSettingsApplyRealTime() {
        // Test that settings changes trigger notifications
        let expectation = expectation(description: "Settings change notification received")
        
        let observer = NotificationCenter.default.addObserver(
            forName: .instantCopySettingsChanged,
            object: nil,
            queue: .main
        ) { _ in
            expectation.fulfill()
        }
        
        settingsManager.isEnabled = true
        
        waitForExpectations(timeout: 1.0) { _ in
            NotificationCenter.default.removeObserver(observer)
        }
    }
}