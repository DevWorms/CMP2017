//
//  detalleEncuestaViewController.swift
//  CMP 2017
//
//  Created by mac on 13/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit
class DetalleEncuestaViewController: UIViewController {
    var idEncuesta = 0
    
    @IBOutlet weak var ratingBar: CosmosView!
    @IBOutlet weak var ratingLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        let nav = self.navigationController?.navigationBar
        nav?.tintColor = UIColor.white
        nav!.setBackgroundImage(navBackgroundImage, for:.default)
        
        nav?.titleTextAttributes = [NSForegroundColorAttributeName: #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)]
        nav?.topItem?.title = UserDefaults.standard.value(forKey: "name") as! String?
        ratingBar.didFinishTouchingCosmos = didFinishTouchingCosmos
        ratingBar.rating = 1
   
        
    }
    
    private func didFinishTouchingCosmos(_ rating: Double) {
        
        self.ratingLabel.text = "rating estrella es \(rating)"
       
    }
}
