//
//  ContentView.swift
//  InstantCopy
//
//  Main content view with app icon for accessing settings.
//

import SwiftUI

struct ContentView: View {
    @State private var showingSettings = false
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 20) {
                Spacer()
                
                // App Icon Section - Tappable
                VStack(spacing: 12) {
                    Image(systemName: "doc.on.doc.fill")
                        .font(.system(size: 80))
                        .foregroundColor(.blue)
                    
                    Text("InstantCopy")
                        .font(.title)
                        .fontWeight(.bold)
                    
                    Text("Tap to configure settings")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }
                .padding(40)
                .background(Color(.systemGray6))
                .cornerRadius(20)
                .onTapGesture {
                    showingSettings = true
                }
                .accessibilityLabel("InstantCopy app icon. Tap to open settings.")
                .accessibilityHint("Opens InstantCopy configuration options")
                
                Spacer()
            }
            .padding()
            .navigationTitle("InstantCopy")
            .navigationBarTitleDisplayMode(.large)
        }
        .sheet(isPresented: $showingSettings) {
            SettingsView()
                .presentationDetents([.medium, .large])
        }
    }
}