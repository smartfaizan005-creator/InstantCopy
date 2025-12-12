import XCTest
import UIKit
import WebKit
@testable import SelectionHandler

class SelectionHandlerDemoTests: XCTestCase {
    
    private var demoViewController: SelectionHandlerDemoViewController!
    
    override func setUp() {
        super.setUp()
        demoViewController = SelectionHandlerDemoViewController()
    }
    
    override func tearDown() {
        demoViewController = nil
        super.tearDown()
    }
    
    // MARK: - Demo Harness Tests
    
    func testDemoViewControllerInitialization() {
        XCTAssertNotNil(demoViewController, "Demo view controller should initialize")
    }
    
    func testDemoAppSimulation() {
        let expectation = self.expectation(description: "Demo app simulation")
        
        // Simulate selection in different app contexts
        let testScenarios = [
            ("Safari", "Website content selection"),
            ("Notes", "Note taking selection"),
            ("Mail", "Email reading selection"),
            ("Messages", "Text messaging selection"),
            ("Twitter", "Social media content selection")
        ]
        
        var completedScenarios = 0
        
        for (appName, testContent) in testScenarios {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.1 * Double(completedScenarios)) {
                self.simulateSelection(inApp: appName, content: testContent)
                completedScenarios += 1
                
                if completedScenarios == testScenarios.count {
                    expectation.fulfill()
                }
            }
        }
        
        waitForExpectations(timeout: 5.0)
        
        // Verify all scenarios were processed
        XCTAssertEqual(completedScenarios, testScenarios.count, "All demo scenarios should complete")
    }
    
    func testDemoHarnessBackgroundMode() {
        let expectation = self.expectation(description: "Demo background mode")
        
        // Start demo in background mode
        demoViewController.startDemoBackgroundMode()
        
        // Simulate multiple selections while in background
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            self.simulateBackgroundSelection()
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                self.demoViewController.stopDemoBackgroundMode()
                expectation.fulfill()
            }
        }
        
        waitForExpectations(timeout: 5.0)
    }
    
    // MARK: - Safari Demo Tests
    
    func testSafariWebsiteSelectionDemo() {
        let expectation = self.expectation(description: "Safari website demo")
        
        let websiteScenarios = [
            "Article headline text selection",
            "Paragraph content selection for copying",
            "Multiple paragraph selection for sharing",
            "URL text selection"
        ]
        
        var completedWebsiteTests = 0
        
        for scenario in websiteScenarios {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5 * Double(completedWebsiteTests)) {
                self.simulateSafariSelection(scenario)
                completedWebsiteTests += 1
                
                if completedWebsiteTests == websiteScenarios.count {
                    expectation.fulfill()
                }
            }
        }
        
        waitForExpectations(timeout: 10.0)
    }
    
    // MARK: - Notes Demo Tests
    
    func testNotesAppSelectionDemo() {
        let expectation = self.expectation(description: "Notes app demo")
        
        let notesScenarios = [
            "Single word selection in note",
            "Sentence selection for emphasis",
            "Paragraph selection for sharing",
            "Multiple paragraph selection",
            "List item selection",
            "Code or formatting selection"
        ]
        
        var completedNotesTests = 0
        
        for scenario in notesScenarios {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3 * Double(completedNotesTests)) {
                self.simulateNotesSelection(scenario)
                completedNotesTests += 1
                
                if completedNotesTests == notesScenarios.count {
                    expectation.fulfill()
                }
            }
        }
        
        waitForExpectations(timeout: 8.0)
    }
    
    // MARK: - Mail Demo Tests
    
    func testMailAppSelectionDemo() {
        let expectation = self.expectation(description: "Mail app demo")
        
        let mailScenarios = [
            "Sender name selection",
            "Email subject line selection",
            "Email body paragraph selection",
            "Contact information selection",
            "URL or link selection in email"
        ]
        
        var completedMailTests = 0
        
        for scenario in mailScenarios {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.4 * Double(completedMailTests)) {
                self.simulateMailSelection(scenario)
                completedMailTests += 1
                
                if completedMailTests == mailScenarios.count {
                    expectation.fulfill()
                }
            }
        }
        
        waitForExpectations(timeout: 10.0)
    }
    
    // MARK: - Edge Cases Demo Tests
    
    func testEmptySelectionDemo() {
        let expectation = self.expectation(description: "Empty selection demo")
        
        // Simulate empty selection
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: ""
        )
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            expectation.fulfill()
        }
        
        waitForExpectations(timeout: 1.0)
    }
    
    func testVeryLongSelectionDemo() {
        let expectation = self.expectation(description: "Very long selection demo")
        
        let veryLongText = String(repeating: "This is a very long selection text. ", count: 100)
        
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: veryLongText
        )
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            expectation.fulfill()
        }
        
        waitForExpectations(timeout: 1.0)
    }
    
    func testSpecialCharactersSelectionDemo() {
        let expectation = self.expectation(description: "Special characters selection demo")
        
        let specialText = "Special characters: !@#$%^&*()[]{}|;:,.<>?/~`"
        
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: specialText
        )
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            expectation.fulfill()
        }
        
        waitForExpectations(timeout: 1.0)
    }
    
    // MARK: - Helper Methods for Demo Simulation
    
    private func simulateSelection(inApp appName: String, content: String) {
        let userInfo = ["appName": appName, "content": content]
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: content,
            userInfo: userInfo
        )
    }
    
    private func simulateBackgroundSelection() {
        let backgroundContent = "Background selection content"
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: backgroundContent
        )
    }
    
    private func simulateSafariSelection(_ content: String) {
        let userInfo = ["appName": "Safari", "content": content, "browserType": "webView"]
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: content,
            userInfo: userInfo
        )
    }
    
    private func simulateNotesSelection(_ content: String) {
        let userInfo = ["appName": "Notes", "content": content, "contentType": "note"]
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: content,
            userInfo: userInfo
        )
    }
    
    private func simulateMailSelection(_ content: String) {
        let userInfo = ["appName": "Mail", "content": content, "contentType": "email"]
        NotificationCenter.default.post(
            name: .selectionCopiedToPasteboard,
            object: content,
            userInfo: userInfo
        )
    }
}