//
//  ViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 08/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
       
        
        
        
        
        // Do any additional setup after loading the view, typically from a nib.
        
        //self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondoCMP.png")!)
      
         UserDefaults.standard.set(0, forKey: "tipoDescar")
        
        let methodsCMP = ServerConnection()
        
        methodsCMP.getCMP(myView: self) //descarga datos para offline
 
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "invitado" {
            UserDefaults.standard.set(true, forKey: "invitado")
            UserDefaults.standard.setValue(0, forKey: "api_key")
            UserDefaults.standard.setValue(1, forKey: "user_id")
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        self.navigationController?.isNavigationBarHidden = false
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.isNavigationBarHidden = true
    }
}
    
   

