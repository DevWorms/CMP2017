//
//  DetalleViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 12/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class DetalleViewController: UIViewController {
    
    @IBOutlet weak var foto: UIImageView!
    @IBOutlet weak var titulo: UILabel!
    @IBOutlet weak var lugar: UILabel!
    @IBOutlet weak var recomendaciones: UILabel!
    @IBOutlet weak var horario: UILabel!
    
    @IBOutlet weak var lbl1: UILabel!
    @IBOutlet weak var lbl2: UILabel!
    @IBOutlet weak var lbl3: UILabel!
    
    @IBOutlet weak var btn1: UIButton!
    @IBOutlet weak var btn2: UIButton!
    @IBOutlet weak var btn3: UIButton!
    
    // 1 Programa
    // 2 Expositores
    // 3 acompañantes
    // 4 deportivo
    // 5 patrocinadores
    var seccion = 0
    var detalle = [String: Any]()
    var imgData: Any?
      var misExpositores = [String: Any]()

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        if let img = self.imgData as? Data {
            self.foto.image = UIImage(data: img)
        }
        
        self.titulo.text = detalle["nombre"] as! String?
        
        switch self.seccion {
        case 1,3,4:
            btn3.isHidden = true
            
            self.lugar.text = detalle["lugar"] as! String?
            self.recomendaciones.text = detalle["recomendaciones"] as! String?
            
        case 2:
        
            btn2.imageView?.image = #imageLiteral(resourceName: "04Agregar_a_mis_expositores")
            
            self.lbl2.text = "Contacto"
            self.lugar.text = (detalle["url"] as! String) + "\n" + (detalle["telefono"] as! String) + "\n" + (detalle["email"] as! String)
            self.lbl3.text = "Acerca de:"
            self.recomendaciones.text = detalle["acerca"] as! String?
            
        case 5:
            btn2.imageView?.image = #imageLiteral(resourceName: "05Boton_Presentacion_de_la_empresa")
            btn3.isHidden = true
            
            self.lbl2.text = "Contacto"
            self.lugar.text = (detalle["url"] as! String) + "\n" + (detalle["telefono"] as! String) + "\n" + (detalle["email"] as! String)
            self.lbl3.text = "Acerca de:"
            self.recomendaciones.text = detalle["acerca"] as! String?

        default:
            break
        }
        
        self.titulo.sizeToFit()
        
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func btnUno(_ sender: Any) {
    }
    
    @IBAction func btnDos(_ sender: Any) {
        if  self.seccion == 2 {
        print("hola presion")
         
                
                print ("Datos: \(item)" )
                
            }
           
        }
        
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
