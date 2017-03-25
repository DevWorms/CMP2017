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
    @IBOutlet weak var banner: UIImageView!
    
    var datos = [[String : Any]]()
    var imgs = [String]()
    var imagenes = [UIImage]()
    var noImg = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        let nav = self.navigationController?.navigationBar
        nav?.tintColor = UIColor.white
        nav!.setBackgroundImage(navBackgroundImage, for:.default)
        
        nav?.titleTextAttributes = [NSForegroundColorAttributeName: #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)]
        nav?.topItem?.title = UserDefaults.standard.value(forKey: "name") as! String?
        
        ///
        let invitado = UserDefaults.standard.value(forKey: "invitado") as! Bool
        
        if invitado {
            programaBtn.isEnabled = false
            acompañantesBtn.isEnabled = false
            transportacionBtn.isEnabled = false
            conoceBtn.isEnabled = false
            mapaBtn.isEnabled = false
            socialesBtn.isEnabled = false
            
            programaBtn.setImage(UIImage(named: "01Programa-1.png"), for: .normal)
            acompañantesBtn.setImage(UIImage(named: "03Eventos_Acompañantes-1.png"), for: .normal)
            transportacionBtn.setImage(UIImage(named: "07Transportacion-1.png"), for: .normal)
            conoceBtn.setImage(UIImage(named: "08Conoce_Puebla-1.png"), for: .normal)
            mapaBtn.setImage(UIImage(named: "06Mapas-1.png"), for: .normal)
            socialesBtn.setImage(UIImage(named: "04Eventos_Deportivos-1.png"), for: .normal)
        }
        
        if Accesibilidad.isConnectedToNetwork() == true {
            let apiKey = UserDefaults.standard.value(forKey: "api_key")
            let userID = UserDefaults.standard.value(forKey: "user_id")
            
            let strUrl = "http://cmp.devworms.com/api/banners/all/\(userID!)/\(apiKey!)"
            print(strUrl)
            
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJson).resume()
            
        } else {
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            self.present(vc_alert, animated: true, completion: nil)
        }
    }
    
    func cargarImg() {
        
        for img in imgs {
            // thread para que cargue en segundo plano la imagen
            DispatchQueue.global(qos: .userInitiated).async { // 1
                let data = try? Data(contentsOf: URL(string: img)!)
                
                DispatchQueue.main.async { // 2
                    self.imagenes.append( UIImage(data: data!)! )
                }
            }
            
            
        }
        
        let timer = Timer.scheduledTimer(timeInterval: 1.2, target: self, selector: #selector(self.update), userInfo: nil, repeats: true);
        
    }
    
    func update() {
        
        banner.image = imagenes[noImg]
        
        if noImg < imagenes.count - 1{
            noImg += 1
        } else {
            noImg = 0
        }
    }
    
    func parseJson(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    DispatchQueue.main.async {
                        
                        if self.datos.count > 0 {
                            self.datos.removeAll()
                        }
                        
                        if let jsonResult = json as? [String: Any] {
                            for dato in jsonResult["banners"] as! [[String:Any]] {
                                self.datos.append(dato)
                            }
                        }
                        
                        for img in self.datos {
                            self.imgs.append(img["url"] as! String)
                        }
                        
                        self.cargarImg()
                    }
                    
                } else {
                    print("HTTP Status Code: 200")
                    print("El JSON de respuesta es inválido.")
                }
            } else {
                
                DispatchQueue.main.async {
                    if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                        if let jsonResult = json as? [String: Any] {
                            let vc_alert = UIAlertController(title: nil, message: jsonResult["mensaje"] as? String, preferredStyle: .alert)
                            vc_alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
                            self.present(vc_alert, animated: true, completion: nil)
                        }
                        
                        
                    } else {
                        print("HTTP Status Code: 400 o 500")
                        print("El JSON de respuesta es inválido.")
                    }
                }
            }
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "acompañantes" {
            (segue.destination as! ResultadosViewController).seccion = 3
        } else if segue.identifier == "deportivos" {
            (segue.destination as! ResultadosViewController).seccion = 4
        } else if segue.identifier == "patrocinadores" {
            (segue.destination as! BuscadorViewController).seccion = 5
        }
    }

}
