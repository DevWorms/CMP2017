//
//  MiPerfilViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 12/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class MiPerfilViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var name: UITextField!
    @IBOutlet weak var clave: UITextField!
    @IBOutlet weak var lastName: UITextField!
    
    var nav: UINavigationBar!
    
    let apiKey = UserDefaults.standard.value(forKey: "api_key")
    let userID = UserDefaults.standard.value(forKey: "user_id")
    
    var editInfo: Bool!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        nav = (self.navigationController?.navigationBar)!
        nav.tintColor = UIColor.white
        nav.setBackgroundImage(navBackgroundImage, for:.default)
        
        cambiarNombreBarra()
        
        let swipeDown = UISwipeGestureRecognizer(target: self, action: #selector(self.swipeKeyBoard(sender:)))
        swipeDown.direction = UISwipeGestureRecognizerDirection.down
        self.view.addGestureRecognizer(swipeDown)
        
        name.delegate = self
        lastName.delegate = self
        clave.delegate = self
        
        self.llenarDatos()
    }
    
    func llenarDatos(){
        let strUrl = "http://cmp.devworms.com/api/user/profile/\(userID!)/\(apiKey!)"
      
        print(strUrl)
        
        if Accesibilidad.isConnectedToNetwork() == true {
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJsonLlenado).resume()
            
        } else {
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            self.present(vc_alert, animated: true, completion: nil)
        }

    
    }
    
    func parseJsonLlenado(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    if let jsonResult = json as? [String: Any] {
                        
                        DispatchQueue.main.async {
                            
                            
                                let result = jsonResult["user"] as! [String: Any]
                                
                              
                                
                                self.name.text = result["name"] as! String
                                self.lastName.text = result["last_name"] as! String
                                self.clave.text = result["clave"] as! String
                                
                                self.cambiarNombreBarra()
                            
                            
                       
                            
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
    
    func cambiarNombreBarra() {
        nav.titleTextAttributes = [NSForegroundColorAttributeName: #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)]
        nav.topItem?.title = UserDefaults.standard.value(forKey: "name") as! String?
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - UITextFieldDelegate
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    func swipeKeyBoard(sender:AnyObject) {
        //Baja el textField
        self.view.endEditing(true)
    }
    
    @IBAction func menu(_ sender: Any) {
        let vc = storyboard!.instantiateViewController(withIdentifier: "MenuPrincipal")
        self.present( vc , animated: true, completion: nil)
    }

    @IBAction func cambiarPerfil(_ sender: Any) {
        if Accesibilidad.isConnectedToNetwork() == true {
            print("Internet connection OK")
            
            if     !((name.text?.isEmpty)!)
                && !((lastName.text?.isEmpty)!) {
                
               let parameterString = "user_id=\(userID!)&api_key=\(apiKey!)&name=\(name.text!)&last_name=\(lastName.text!)&clave=\(clave.text!)"
                
                print(parameterString)
                
                let strUrl = "http://cmp.devworms.com/api/user/edit"
                
                if let httpBody = parameterString.data(using: String.Encoding.utf8) {
                    var urlRequest = URLRequest(url: URL(string: strUrl)!)
                    urlRequest.httpMethod = "POST"
                    
                    self.editInfo = true
                    
                    URLSession.shared.uploadTask(with: urlRequest, from: httpBody, completionHandler: parseJson).resume()
                } else {
                    print("Error de codificación de caracteres.")
                }
                
            }
            else{
                let vc_alert = UIAlertController(title: nil, message: "Información incompleta", preferredStyle: .alert)
                
                vc_alert.addAction(UIAlertAction(title: "OK",
                                                 style: UIAlertActionStyle.default,
                                                 handler: nil))
                self.present(vc_alert, animated: true, completion: nil)
                
            }
            
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
                    if let jsonResult = json as? [String: Any] {
                        
                        DispatchQueue.main.async {
                            
                            if self.editInfo == true {
                                let result = jsonResult["user"] as! [String: Any]
                                
                                UserDefaults.standard.setValue("Hola \(result["name"]!)", forKey: "name")
                                
                                self.name.text = ""
                                self.lastName.text = ""
                                self.clave.text = ""
                                
                                self.cambiarNombreBarra()
                            }
                            
                            let vc_alert = UIAlertController(title: nil, message: jsonResult["mensaje"] as? String, preferredStyle: .alert)
                            vc_alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
                            self.present(vc_alert, animated: true, completion: nil)
                            
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
    
    @IBAction func cambiarContraseña(_ sender: Any) {
        //1. Create the alert controller.
        let alert = UIAlertController(title: "Reestablecer Contraseña", message: "Ingresa tu nueva contraseña:", preferredStyle: .alert)
        
        //2. Add the text field. You can configure it however you need.
        alert.addTextField { (textField) in
            textField.delegate = self
        }
        
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel , handler: nil))
        
        // 3. Grab the value from the text field, and print it when the user clicks OK.
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { [weak alert] (_) in
            let textField = alert!.textFields![0] // Force unwrapping because we know it exists.
            print("Text field: \(textField.text)")
            
            
            
            if Accesibilidad.isConnectedToNetwork() == true {
                print("Internet connection OK")
                
                if     !((textField.text?.isEmpty)!) {
                    
                    let parameterString = "user_id=\(self.userID!)&api_key=\(self.apiKey!)&password=\(textField.text!)"
                    
                    print(parameterString)
                    
                    let strUrl = "http://cmp.devworms.com/api/user/resetpassword"
                    
                    if let httpBody = parameterString.data(using: String.Encoding.utf8) {
                        var urlRequest = URLRequest(url: URL(string: strUrl)!)
                        urlRequest.httpMethod = "POST"
                        
                        self.editInfo = false
                        
                        URLSession.shared.uploadTask(with: urlRequest, from: httpBody, completionHandler: self.parseJson).resume()
                    } else {
                        print("Error de codificación de caracteres.")
                    }
                    
                }
                else{
                    let vc_alert = UIAlertController(title: nil, message: "Información incompleta", preferredStyle: .alert)
                    
                    vc_alert.addAction(UIAlertAction(title: "OK",
                                                     style: UIAlertActionStyle.default,
                                                     handler: nil))
                    self.present(vc_alert, animated: true, completion: nil)
                    
                }
                
            } else {
                let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
                vc_alert.addAction(UIAlertAction(title: "OK",
                                                 style: UIAlertActionStyle.default,
                                                 handler: nil))
                self.present(vc_alert, animated: true, completion: nil)
            }
            
            
            
        }))
        
        // 4. Present the alert.
        self.present(alert, animated: true, completion: nil)
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
