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
    
    @IBOutlet weak var imgDetalleEncuesta: UIImageView!
    @IBOutlet weak var btnCalificar: UIButton!
    @IBOutlet weak var ratingBar3: CosmosView!
    @IBOutlet weak var ratingBar2: CosmosView!
    @IBOutlet weak var ratingBar: CosmosView!
    @IBOutlet weak var ratingLabel2: UILabel!
    @IBOutlet weak var ratingLabel3: UILabel!
    @IBOutlet weak var ratingLabel1: UILabel!
    
    var valorRB1 = 1.0
    var valorRB2 = 1.0
    var valorRB3 = 1.0
    var datos = [[String : Any]]()
     var alert = UIAlertController()
    let apiKey = UserDefaults.standard.value(forKey: "api_key")
    let userID = UserDefaults.standard.value(forKey: "user_id")
    
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
        
        ratingBar.didFinishTouchingCosmos = didFinishTouchingCosmos1
        ratingBar.rating = 1
        
        ratingBar2.didFinishTouchingCosmos = didFinishTouchingCosmos2
        ratingBar2.rating = 1
        
        ratingBar3.didFinishTouchingCosmos = didFinishTouchingCosmos3
        ratingBar3.rating = 1
        
        
        let apiKey = UserDefaults.standard.value(forKey: "api_key")
        let userID = UserDefaults.standard.value(forKey: "user_id")
        
        let strUrl = "http://cmp.devworms.com/api/encuesta/detail/\(userID!)/\(apiKey!)/\(self.idEncuesta)!"
        print(strUrl)
        
        if Accesibilidad.isConnectedToNetwork() == true {
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJson).resume()
            
        } else {
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            self.present(vc_alert, animated: true, completion: nil)
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
                            let encuestas = jsonResult["encuesta"] as! [String : Any]
                            let imagenXL = encuestas["filexl"] as! [String : Any]
                            
                            let imgURL: URL = URL(string: imagenXL["url"] as! String)!
                            let request: URLRequest = URLRequest(url: imgURL)
                            
                            let session = URLSession.shared
                            let task = session.dataTask(with: request, completionHandler: {
                                (data, response, error) -> Void in
                                
                                if (error == nil && data != nil)
                                {
                                    //self.imagenes[objReceta] = UIImage(data: data!)
                                    
                                    func display_image()
                                    {
                                        self.imgDetalleEncuesta.image = UIImage(data: data!)
                               
                                        
                                    }
                                    
                                    DispatchQueue.main.async(execute: display_image)
                                }
                                
                            })
                            
                            task.resume()
                            
                            
                            
                            
                            var index = 0
                            for dato in encuestas["preguntas"] as! [[String:Any]] {
                                self.datos.append(dato)
                                
                                index = index + 1
                                if index == 1{
                                    self.ratingLabel1.text = dato["pregunta"] as! String
                                } else if index == 2{
                                    self.ratingLabel2.text = dato["pregunta"] as! String
                                }
                                else{
                                    self.ratingLabel3.text = dato["pregunta"] as! String
                                }

                            }
                         
                         
                        }
                        
                        
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
    

    
    private func didFinishTouchingCosmos1(_ rating: Double) {
       valorRB1 = rating
        
    }
    private func didFinishTouchingCosmos2(_ rating: Double) {
        
         valorRB2 = rating
    }
    private func didFinishTouchingCosmos3(_ rating: Double) {
        
         valorRB3 = rating
    }
    
    @IBAction func btnCalificar(_ sender: Any) {
        
       
        
        alert = UIAlertController(title: nil, message: "Cargando...", preferredStyle: .alert)
        alert.view.tintColor = UIColor.black
        
        let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50)) as UIActivityIndicatorView
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
        loadingIndicator.startAnimating()
        
        alert.view.addSubview(loadingIndicator)
        
        self.present(alert, animated: true, completion: self.inicial)
        
        
    }
    
    func inicial()  {
        let parameterString = "user_id=\(userID!)&api_key=\(apiKey!)&encuesta_id=\(self.idEncuesta)&respuesta_1=\(self.valorRB1)&respuesta_2=\(self.valorRB2)&respuesta_3=\(self.valorRB3)"
        
        print(parameterString)
        
        let strUrl = "http://cmp.devworms.com/api/encuesta/response"
        
        if let httpBody = parameterString.data(using: String.Encoding.utf8) {
            var urlRequest = URLRequest(url: URL(string: strUrl)!)
            urlRequest.httpMethod = "POST"
            
            URLSession.shared.uploadTask(with: urlRequest, from: httpBody, completionHandler: parseJsonCalificar).resume()
        } else {
            
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            self.present(vc_alert, animated: true, completion: nil)
        }

    }
    func parseJsonCalificar(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    if let jsonResult = json as? [String: Any] {
                        
                        DispatchQueue.main.async {
                         print("Ya lo envio")
                            let vc_alert = UIAlertController(title: nil, message: jsonResult["mensaje"] as? String, preferredStyle: .alert)
                            vc_alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
                            self.present(vc_alert, animated: true, completion: nil)
                            
                            
                            
                        }
                        
                          self.alert.dismiss(animated: false, completion: nil)
                    }
                    
                    
                } else {
                    print("HTTP Status Code: 200")
                    print("El JSON de respuesta es inválido.")
                }
            } else {
                
                DispatchQueue.main.async {
                    if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                        if let jsonResult = json as? [String: Any] {
                       print("no envio")
                            let vc_alert = UIAlertController(title: nil, message: jsonResult["mensaje"] as? String, preferredStyle: .alert)
                            vc_alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
                            self.present(vc_alert, animated: true, completion: nil)
                                  self.alert.dismiss(animated: false, completion: nil)
                        }
                        
                        
                    } else {
                        print("HTTP Status Code: 400 o 500")
                        print("El JSON de respuesta es inválido.")
                    }
                }
            }
        }
       
        
    }

}
