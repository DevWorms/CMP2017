//
//  ConocePueViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 12/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class ConocePueViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func mapaPuebla(_ sender: Any) {
        self.performSegue(withIdentifier: "puebla", sender: nil)
    }
    
    @IBAction func clima(_ sender: Any) {
        self.performSegue(withIdentifier: "clima", sender: nil)
    }
    
    @IBAction func menu(_ sender: Any) {
        let vc = storyboard!.instantiateViewController(withIdentifier: "MenuPrincipal")
        self.present( vc , animated: true, completion: nil)
    }

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "clima" {
            (segue.destination as! MapaSimpleViewController).tipoMapa = 1
        } else if segue.identifier == "puebla" {
            (segue.destination as! MapaSimpleViewController).tipoMapa = 2
        }
    }

}
