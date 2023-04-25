//
//  ViewController.swift
//  longtap
//
//  Created by Danyal on 06/01/2021.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var btn: UIButton!
    @IBOutlet weak var popupView: UIView!
    
    @IBOutlet weak var popUpHeight: NSLayoutConstraint!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        self.popupView.layer.cornerRadius = 20
        let longGesture = UILongPressGestureRecognizer(target: self, action: #selector(long))
        btn.addGestureRecognizer(longGesture)
        let tapListner = UITapGestureRecognizer(target: self, action: #selector(dismissPopUp))
        self.view.addGestureRecognizer(tapListner)
        
        
        
    }
    
    func addPopUp(){
//        let blurEffect = UIBlurEffect(style: UIBlurEffect.Style.light)
//        let blurEffectView = UIVisualEffectView(effect: blurEffect)
//        blurEffectView.frame = view.bounds
//        blurEffectView.tag = 3
//        blurEffectView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
//        view.addSubview(blurEffectView)
//        UIView.animate(withDuration: 0.3) {
//            self.popupView.alpha = 1
//        }
        
    
        UIView.animate(withDuration: 0.2, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 1, options: .transitionCrossDissolve, animations: {
            self.popupView.alpha = 1
            self.popUpHeight.constant = self.view.frame.height*0.3
            self.view.layoutIfNeeded()
        }) { _ in
        }
//
//        UIView.animate(withDuration: 0.3, delay: 0, options: .transitionCrossDissolve) {
//            self.popupView.alpha = 1
//            self.popUpHeight.constant = self.view.frame.height*0.4
//            self.view.layoutIfNeeded()
//        } completion: { (isTrue) in
//
//        }

        
    }
    func removePopUp(){
//        for subview in self.view.subviews{
//            if subview.tag == 3{
//                subview.removeFromSuperview()
//            }
//        }
        
//        UIView.animate(withDuration: 0.3) {
//            self.popupView.alpha = 0
//        }
        
//        UIView.animate(withDuration: 0.3, delay: 0, options: .transitionCrossDissolve) {
//
//
//        } completion: { (isTrue) in
//
//        }
        
        UIView.animate(withDuration: 0.2, delay: 0, usingSpringWithDamping: 0.5, initialSpringVelocity: 5, options: .transitionCrossDissolve, animations: {
            self.popupView.alpha = 0
            self.popUpHeight.constant = 0//self.view.frame.height*0.4
            self.view.layoutIfNeeded()
        }) { _ in
        }
        
        
        
    }
    
    @objc func long() {
        addPopUp()
        print("Long press")
        return
    }
    
    
    @objc func dismissPopUp() {
        self.removePopUp()
    }



}

