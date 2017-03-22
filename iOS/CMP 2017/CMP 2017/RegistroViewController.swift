//
//  RegistroViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 08/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class RegistroViewController: UIViewController, UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource {
    @IBOutlet weak var name: UITextField!
    @IBOutlet weak var lastName: UITextField!
    @IBOutlet weak var mail: UITextField!
    @IBOutlet weak var password: UITextField!
    @IBOutlet weak var claveInscripcion: UITextField!
    @IBOutlet weak var tipoUser: UIPickerView!
    @IBOutlet weak var asociacion: UIPickerView!
    
    var pickerTipo = ["Visitante", "Trabajador de Pemex", "Estudiante", "Staff", "Otro"]
    var pickerAsociacion = ["Asociación 1", "Asociación 2", "Asociación 3", "Asociación 4", "Ninguna"]
    
    var selectedTipo = "Visitante"
    var selectedAsociacion = "Asociación 1"

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let swipeDown = UISwipeGestureRecognizer(target: self, action: #selector(self.swipeKeyBoard(sender:)))
        swipeDown.direction = UISwipeGestureRecognizerDirection.down
        self.view.addGestureRecognizer(swipeDown)
        
        name.delegate = self
        lastName.delegate = self
        mail.delegate = self
        password.delegate = self
        claveInscripcion.delegate = self
        
        tipoUser.delegate = self
        tipoUser.dataSource = self
        asociacion.delegate = self
        asociacion.dataSource = self
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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

    
    @IBAction func signUp(_ sender: Any) {
        if     !((mail.text?.isEmpty)!)
            && !((password.text?.isEmpty)!)
            && !((name.text?.isEmpty)!)
            && !((lastName.text?.isEmpty)!)
            && !((claveInscripcion.text?.isEmpty)!) {
            
            let parameterString = "name=\(name.text!)&last_name=\(lastName.text!)&email=\(mail.text!)&password=\(password.text!)&clave=\(claveInscripcion.text!)&type=\(selectedTipo)&association=\(selectedAsociacion)"
            
            print(parameterString)
            
            let strUrl = "http://cmp.devworms.com/api/user/signup"
            
            if let httpBody = parameterString.data(using: String.Encoding.utf8) {
                var urlRequest = URLRequest(url: URL(string: strUrl)!)
                urlRequest.httpMethod = "POST"
                
                URLSession.shared.uploadTask(with: urlRequest, from: httpBody, completionHandler: parseJsonSignUp).resume()
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
    }
    
    func parseJsonSignUp(data: Data?, urlResponse: URLResponse?, error: Error?) {
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
    
    // MARK: - UIPickerViewDelegate
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if pickerView == self.asociacion {
            return self.pickerAsociacion.count
        } else if pickerView == self.tipoUser {
            return self.pickerTipo.count
        } else {
            return 1
        }
    }
    
    // MARK: - UIPickerViewDataSource
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView == self.asociacion {
            return self.pickerAsociacion[row]
        } else  {
            return self.pickerTipo[row]
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if pickerView == self.asociacion {
            selectedAsociacion = self.pickerAsociacion[row]
        } else  {
            selectedTipo = self.pickerTipo[row]
        }
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

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "registro" {
            UserDefaults.standard.set(false, forKey: "invitado")
        }
    }

}
