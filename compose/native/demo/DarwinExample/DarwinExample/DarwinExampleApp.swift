//
//  DarwinExampleApp.swift
//  DarwinExample
//
//  Created by Tadeas Kriz on 16.06.2021.
//

import SwiftUI
import demo

@main
struct DarwinExampleApp: App {
    var body: some Scene {
        WindowGroup {
            ZStack {
                ComposableView()
            }
        }
    }
}

class RootView: UIView {
}

import UIKit

struct ComposableView: UIViewControllerRepresentable {
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        
    }

    func makeUIViewController(context: Context) -> some UIViewController {
        return IosAppKt.IosApp.viewController
    }
}

//
//import UIKit
//
//class X: UIView {
//
//    override func didMoveToWindow() {
//        <#code#>
//    }
//
//    override func willMove(toWindow newWindow: UIWindow?) {
//        <#code#>
//    }
//
//}
