//
//  MenuUsuarioViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 12/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class MenuUsuarioViewController: ViewController {

    @IBOutlet weak var miPerfil: UIButton!
    @IBOutlet weak var miAgenda: UIButton!
    @IBOutlet weak var misExpositores: UIButton!
    @IBOutlet weak var misEncuestas: UIButton!
    @IBOutlet weak var notif: UIButton!
    @IBOutlet weak var actualizar: UIButton!
    
    @IBOutlet weak var claseButton: UIButton!
    @IBOutlet weak var scrollView: UIScrollView!
    override func viewWillLayoutSubviews(){
        super.viewWillLayoutSubviews()
        self.scrollView.contentSize = CGSize(width: view.bounds.width, height: miPerfil.bounds.height * 7)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "09Fondo.png")!)
        
        let invitado = UserDefaults.standard.value(forKey: "invitado") as! Bool
        
        if invitado {
            miPerfil.isEnabled = false
            miAgenda.isEnabled = false
            misExpositores.isEnabled = false
            misEncuestas.isEnabled = false
            notif.isEnabled = false
            actualizar.isEnabled = false
            
            miPerfil.setTitleColor(#colorLiteral(red: 0.6000000238, green: 0.6000000238, blue: 0.6000000238, alpha: 1), for: .normal)
            miAgenda.setTitleColor(#colorLiteral(red: 0.6000000238, green: 0.6000000238, blue: 0.6000000238, alpha: 1), for: .normal)
            misExpositores.setTitleColor(#colorLiteral(red: 0.6000000238, green: 0.6000000238, blue: 0.6000000238, alpha: 1), for: .normal)
            misEncuestas.setTitleColor(#colorLiteral(red: 0.6000000238, green: 0.6000000238, blue: 0.6000000238, alpha: 1), for: .normal)
            notif.setTitleColor(#colorLiteral(red: 0.6000000238, green: 0.6000000238, blue: 0.6000000238, alpha: 1), for: .normal)
            actualizar.setTitleColor(#colorLiteral(red: 0.6000000238, green: 0.6000000238, blue: 0.6000000238, alpha: 1), for: .normal)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func close(_ sender: Any) {
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func actualizarInfo(_ sender: Any) {
        UserDefaults.standard.setValue(0, forKey: "descarga")
        UserDefaults.standard.setValue(1, forKey: "tipoDescar")
        let methodsCMP = ServerConnection()
        methodsCMP.getCMP(myView: self)
    }
    @IBAction func misExpositores(_ sender: Any) {
          self.performSegue(withIdentifier: "misexpositores", sender: nil)
    }
    @IBAction func misEncuestas(_ sender: Any) {
        self.performSegue(withIdentifier: "encuestas", sender: nil)
    }
    @IBAction func miAgenda(_ sender: Any) {
        self.performSegue(withIdentifier: "miagenda", sender: nil)
    }
    @IBAction func cerrarSesion(_ sender: Any) {
        UserDefaults.standard.set("", forKey: "api_key")
        UserDefaults.standard.set("", forKey: "user_id")
        UserDefaults.standard.set("", forKey: "name")
        UserDefaults.standard.setValue(1, forKey: "descarga")
        UserDefaults.standard.setValue(0, forKey: "tipoDescar")
        
        let vc = storyboard!.instantiateViewController(withIdentifier: "Inicio")
        self.present( vc , animated: true, completion: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "misexpositores" {
            //(segue.destination as! BuscadorViewController).seccion = 6
            let nav = segue.destination as! UINavigationController
            let svc = nav.topViewController as! BuscadorViewController
            svc.seccion = 6;
        }
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
