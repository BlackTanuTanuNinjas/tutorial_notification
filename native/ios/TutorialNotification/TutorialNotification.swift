//
//  TutorialNotification.swift
//  TutorialNotification
//

import SwiftUI

// 追加
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate {

    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions
                     launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        
        let center = UNUserNotificationCenter.current()
        center.delegate = self
        requestNotificationAuthorization()
        return true
        
    }
    
    private func requestNotificationAuthorization() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { (granted, error) in
            if granted {
                print("通知の権限が許可された")
            } else {
                print("通知の権限が拒否された")
            }
        }
    }
}

extension AppDelegate {
    // フォアグランド
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler([.alert, .sound, .list])
    }

    // バックグランド
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        completionHandler()
    }
}

// ここまで

@main
struct TutorialNotification: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    @Environment(\.scenePhase) var scenePhase
    
    var content = ContentView()
    var body: some Scene {
        WindowGroup {
            self.content
        }
        .onChange(of: scenePhase) { _, phase in
            switch phase {
            case .background:
                print(".background")
            case .active:
                print(".active")
                if let bridge = Bridge.instance {
                    bridge.reinit()
                }
            default: break
            }
        }

    }
}
