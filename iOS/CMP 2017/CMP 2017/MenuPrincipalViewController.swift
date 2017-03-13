//
//  MenuPrincipalViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 08/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class MenuPrincipalViewController: UIViewController {
    
    @IBOutlet weak var programaBtn: UIButton!
    @IBOutlet weak var acompañantesBtn: UIButton!
    @IBOutlet weak var patrocinadoresBtn: UIButton!
    @IBOutlet weak var transportacionBtn: UIButton!
    @IBOutlet weak var conoceBtn: UIButton!
    @IBOutlet weak var mapaBtn: UIButton!
    @IBOutlet weak var socialesBtn: UIButton!
    @IBOutlet weak var expositoresBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        let nav = self.navigationController?.navigationBar
        
        nav?.tintColor = UIColor.white
        
        nav!.setBackgroundImage(navBackgroundImage, for:.default)
        
        ///
        let invitado = UserDefaults.standard.value(forKey: "invitado") as! Bool
        
        if invitado {
            programaBtn.isEnabled = false
            acompañantesBtn.isEnabled = false
            transportacionBtn.isEnabled = false
            conoceBtn.isEnabled = false
            mapaBtn.isEnabled = false
            socialesBtn.isEnabled = false
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
