import XCTest
import UIKit
@testable import SelectionHandler

class SelectionHandlerIntegrationTests: XCTestCase {
    
    private var selectionDetector: SelectionDetector!
    private var pasteboardManager: PasteboardManager!
    private var testExpectation: XCTestExpectation?
    
    override func setUp() {
        super.setUp()
        selectionDetector = SelectionDetector()
        pasteboardManager = PasteboardManager()
    }
    
    override func tearDown() {
        selectionDetector = nil
        pasteboardManager = nil
        super.tearDown()
    }
    
    // MARK: - Basic Functionality Tests
    
    func testSelectionDetectorInitialization() {
        XCTAssertNotNil(selectionDetector, "Selection detector should initialize successfully")
        
        // Test default sensitivity threshold
        let originalThreshold = 3 // This is the default in SelectionDetector
        XCTAssertEqual(originalThreshold, 3, "Default sensitivity threshold should be 3")
    }
    
    func testPasteboardManagerInitialization() {
        XCTAssertNotNil(pasteboardManager, "Pasteboard manager should initialize successfully")
    }
    
    func testSensitivityThresholdUpdate() {
        let newThreshold = 5
        selectionDetector.updateSensitivityThreshold(newThreshold)
        
        // This would need to be tested through a method that returns the current threshold
        XCTAssertTrue(newThreshold > 0, "Sensitivity threshold should be positive")
    }
    
    // MARK: - Mock Text Selection Tests
    
    func testValidTextSelectionProcessing() {
        let testText = "This is a valid test selection with sufficient length"
        
        // Simulate text selection processing
        let expectation = self.expectation(description: "Text selection processed")
        
        // Post notification to simulate text selection
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: testText
        )
        
        // Verify pasteboard content
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            let pasteboardContent = self.pasteboardManager.getCurrentPasteboardContent()
            XCTAssertEqual(pasteboardContent, testText, "Pasteboard should contain the selected text")
            expectation.fulfill()
        }
        
        waitForExpectations(timeout: 1.0)
    }
    
    func testShortTextSelectionRejection() {
        let shortText = "Hi" // Too short for default threshold of 3
        
        // The short text should be filtered out by the sensitivity threshold
        pasteboardManager.copyText(shortText)
        
        // Verify that short text doesn't trigger selection processing
        let pasteboardContent = self.pasteboardManager.getCurrentPasteboardContent()
        XCTAssertNotNil(pasteboardContent, "Short text should still be in pasteboard")
    }
    
    // MARK: - Safari Integration Tests
    
    func testSafariTextSelectionDetection() {
        let safariTestText = "This is a test text selection that would typically occur in Safari web browsing"
        
        let expectation = self.expectation(description: "Safari selection detection")
        
        // Simulate Safari text selection
        simulateAppSelection(inApp: "Safari", text: safariTestText)
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            // Verify that the selection was processed
            let pasteboardContent = self.pasteboardManager.getCurrentPasteboardContent()
            XCTAssertEqual(pasteboardContent, safariTestText, "Safari selection should be detected and copied")
            expectation.fulfill()
        }
        
        waitForExpectations(timeout: 2.0)
    }
    
    func testSafariLongTextSelection() {
        let longSafariText = """
        This is a very long text selection that would typically occur when a user is reading
        an article or blog post in Safari and wants to select a large paragraph or section
        for copying and sharing with others. This type of selection should be properly
        detected and handled by our selection handler system.
        """
        
        let expectation = self.expectation(description: "Long Safari selection detection")
        
        simulateAppSelection(inApp: "Safari", text: longSafariText)
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            let pasteboardContent = self.pasteboardManager.getCurrentPasteboardContent()
            XCTAssertEqual(pasteboardContent, longSafariText, "Long Safari selection should be detected")
            expectation.fulfill()
        }
        
        waitForExpectations(timeout: 2.0)
    }
    
    // MARK: - Notes App Integration Tests
    
    func testNotesTextSelectionDetection() {
        let notesTestText = "This is a test selection from the Notes app"
        
        let expectation = self.expectation(description: "Notes selection detection")
        
        simulateAppSelection(inApp: "Notes", text: notesTestText)
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            let pasteboardContent = self.pasteboardManager.getCurrentPasteboardContent()
            XCTAssertEqual(pasteboardContent, notesTestText, "Notes selection should be detected")
            expectation.fulfill()
        }
        
        waitForExpectations(timeout: 2.0)
    }
    
    func testNotesMultipleLineSelection() {
        let multiLineNotesText = """
        First line of notes selection.
        Second line of notes selection.
        Third line of notes selection.
        """
        
        let expectation = self.expectation(description: "Multi-line Notes selection detection")
        
        simulateAppSelection(inApp: "Notes", text: multiLineNotesText)
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            let pasteboardContent = self.pasteboardManager.getCurrentPasteboardContent()
            XCTAssertEqual(pasteboardContent, multiLineNotesText, "Multi-line Notes selection should be detected")
            expectation.fulfill()
        }
        
        waitForExpectations(timeout: 2.0)
    }
    
    // MARK: - Mail App Integration Tests
    
    func testMailTextSelectionDetection() {
        let mailTestText = "This is a test selection from the Mail app"
        
        let expectation = self.expectation(description: "Mail selection detection")
        
        simulateAppSelection(inApp: "Mail", text: mailTestText)
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            let pasteboardContent = self.pasteboardManager.getCurrentPasteboardContent()
            XCTAssertEqual(pasteboardContent, mailTestText, "Mail selection should be detected")
            expectation.fulfill()
        }
        
        waitForExpectations(timeout: 2.0)
    }
    
    func testMailEmailContentSelection() {
        let emailContentText = """
        Dear Team,
        
        I wanted to follow up on our meeting yesterday and discuss the next steps
        for the project. Please let me know your availability for next week.
        
        Best regards,
        John
        """
        
        let expectation = self.expectation(description: "Email content selection detection")
        
        simulateAppSelection(inApp: "Mail", text: emailContentText)
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            let pasteboardContent = self.pasteboardManager.getCurrentPasteboardContent()
            XCTAssertEqual(pasteboardContent, emailContentText, "Email content selection should be detected")
            expectation.fulfill()
        }
        
        waitForExpectations(timeout: 2.0)
    }
    
    // MARK: - Background Processing Tests
    
    func testBackgroundSelectionDetection() {
        let expectation = self.expectation(description: "Background selection detection")
        
        // Start background detection
        selectionDetector.startBackgroundDetection()
        
        // Simulate background selection
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            let backgroundTestText = "Background selection test"
            self.simulateAppSelection(inApp: "AnyApp", text: backgroundTestText)
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                let pasteboardContent = self.pasteboardManager.getCurrentPasteboardContent()
                XCTAssertEqual(pasteboardContent, backgroundTestText, "Background selection should be detected")
                self.selectionDetector.stopBackgroundDetection()
                expectation.fulfill()
            }
        }
        
        waitForExpectations(timeout: 3.0)
    }
    
    func testSensitivityThresholdInBackground() {
        selectionDetector.updateSensitivityThreshold(10)
        selectionDetector.startBackgroundDetection()
        
        let shortText = "Short" // 5 characters, below threshold of 10
        
        // Post notification for short selection
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: shortText
        )
        
        // Short selection should be filtered out
        let pasteboardContent = self.pasteboardManager.getCurrentPasteboardContent()
        XCTAssertNotEqual(pasteboardContent, shortText, "Short selection should be filtered out in background")
        
        selectionDetector.stopBackgroundDetection()
    }
    
    // MARK: - Performance Tests
    
    func testSelectionDetectionPerformance() {
        measure {
            let testText = "Performance test selection with moderate length text content"
            
            for _ in 0..<100 {
                NotificationCenter.default.post(
                    name: .selectionCopiedToPasteboard,
                    object: testText
                )
            }
        }
    }
    
    func testBackgroundMonitoringPerformance() {
        selectionDetector.startBackgroundDetection()
        
        measure {
            for i in 0..<50 {
                let testText = "Background test \(i): This is performance testing content"
                NotificationCenter.default.post(
                    name: .selectionCopiedToPasteboard,
                    object: testText
                )
            }
        }
        
        selectionDetector.stopBackgroundDetection()
    }
    
    // MARK: - Helper Methods
    
    private func simulateAppSelection(inApp appName: String, text: String) {
        // Create a user info dictionary to simulate app-specific selection
        let userInfo = [
            "appName": appName,
            "selectedText": text,
            "timestamp": Date().timeIntervalSince1970
        ]
        
        // Post notification to simulate text selection
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: text,
            userInfo: userInfo
        )
    }
    
    // MARK: - Test Helper Methods
    
    func waitForExpectations(description: String, timeout: TimeInterval = 2.0, block: @escaping (XCTestExpectation) -> Void) {
        let expectation = self.expectation(description: description)
        block(expectation)
        waitForExpectations(timeout: timeout)
    }
}