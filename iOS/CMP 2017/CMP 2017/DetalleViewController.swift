//
//  DetalleViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 12/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class DetalleViewController: UIViewController {
    
    @IBOutlet weak var titulo: UILabel!
    @IBOutlet weak var lugar: UILabel!
    @IBOutlet weak var recomendaciones: UILabel!
    @IBOutlet weak var btn1: UIButton!
    @IBOutlet weak var btn2: UIButton!
    @IBOutlet weak var btn3: UIButton!
    
    // 1 Programa
    var seccion = 0
    var detalle = [String: Any]()

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        self.titulo.text = detalle["nombre"] as! String?
        self.lugar.text = detalle["lugar"] as! String?
        self.recomendaciones.text = detalle["recomendaciones"] as! String?

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func btnUno(_ sender: Any) {
    }
    
    @IBAction func btnDos(_ sender: Any) {
    }
    
    @IBAction func btnTres(_ sender: Any) {
    }
    
    @IBAction func menu(_ sender: Any) {
        let vc = storyboard!.instantiateViewController(withIdentifier: "MenuPrincipal")
        self.present( vc , animated: true, completion: nil)
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
