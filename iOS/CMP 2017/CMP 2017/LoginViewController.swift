//
//  LoginViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 08/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class LoginViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var mail: UITextField!
    @IBOutlet weak var password: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let swipeDown = UISwipeGestureRecognizer(target: self, action: #selector(self.swipeKeyBoard(sender:)))
        swipeDown.direction = UISwipeGestureRecognizerDirection.down
        self.view.addGestureRecognizer(swipeDown)
        
        mail.delegate = self
        password.delegate = self
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
    
    func loadCargarPerfilUsuario() {
        
        let apiKey = UserDefaults.standard.value(forKey: "api_key")
        let userID = UserDefaults.standard.value(forKey: "user_id")
        
        let strUrl = "http://cmp.devworms.com/api/user/profile/\(userID!)/\(apiKey!)"
        print(strUrl)
        
        URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJson).resume()
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
                            
                            let result = jsonResult["user"] as! [String: Any]
                            
                            UserDefaults.standard.setValue("Hola \(result["name"]!)", forKey: "name")
                            
                            self.performSegue(withIdentifier: "login", sender: nil)
                            
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
    
    @IBAction func login(_ sender: Any) {
        
        if Accesibilidad.isConnectedToNetwork() == true {
            print("Internet connection OK")
            
            if     !((mail.text?.isEmpty)!)
                && !((password.text?.isEmpty)!) {
                
                let parameterString = "user=\(mail.text!)&password=\(password.text!)"
                
                print(parameterString)
                
                let strUrl = "http://cmp.devworms.com/api/user/login"
                
                if let httpBody = parameterString.data(using: String.Encoding.utf8) {
                    var urlRequest = URLRequest(url: URL(string: strUrl)!)
                    urlRequest.httpMethod = "POST"
                    
                    URLSession.shared.uploadTask(with: urlRequest, from: httpBody, completionHandler: parseJsonLogin).resume()
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
    
    func parseJsonLogin(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    if let jsonResult = json as? [String: Any] {
                        
                       DispatchQueue.main.async {
                            
                            UserDefaults.standard.setValue(jsonResult["api_key"], forKey: "api_key")
                            UserDefaults.standard.setValue(jsonResult["user_id"], forKey: "user_id")
                        
                            self.loadCargarPerfilUsuario()
                        
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

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "login" {
            UserDefaults.standard.set(false, forKey: "invitado")
        }
    }

}
