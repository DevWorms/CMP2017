//
//  ServerConnection.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 05/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import Foundation
import UIKit


//let apiKey = UserDefaults.standard.value(forKey: "api_key")!
//let userID = UserDefaults.standard.value(forKey: "user_id")!

let apiKey = 0
let userID = 1

let appDelegate = UIApplication.shared.delegate as! AppDelegate
let managedContext = appDelegate.managedObjectContext

class ServerConnection {
    
    private var mView = UIViewController()
    
    // MARK: - CMP
    
    func getCMP(myView: UIViewController) {
        
        if Accesibilidad.isConnectedToNetwork() == true {
            
            self.mView = myView
            
            let alert = UIAlertController(title: nil, message: "Descargando...", preferredStyle: .alert)
            alert.view.tintColor = UIColor.black
            
            let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50)) as UIActivityIndicatorView
            loadingIndicator.hidesWhenStopped = true
            loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
            loadingIndicator.startAnimating()
            
            /*let callAction = UIAlertAction(title: "Cancelar", style: .Default, handler: {
             action in
             
             self.hurryPrintMethods.cancelConnection()
             return
             })
             alert.addAction(callAction)*/
            
            alert.view.addSubview(loadingIndicator)
            myView.present(alert, animated: true, completion: llamadas)
            
            
        } else {
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            myView.present(vc_alert, animated: true, completion: nil)
        }
        
    }
    
    private func llamadas() {
        
        //Eliminar BD
        CoreDataHelper.deleteEntity(entityName: "Banners")
        CoreDataHelper.deleteEntity(entityName: "Sitios")
        
        //Cargar BD
        self.getBanners()
        self.getSitios()
        
        //quita el alert
        self.mView.dismiss(animated: false, completion: nil)
    }
    
    // MARK: - Banners
    
    private func getBanners() {
        if Accesibilidad.isConnectedToNetwork() == true {
            
            let strUrl = "http://cmp.devworms.com/api/banners/all/\(userID)/\(apiKey)"
            print(strUrl)
            
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJsonBanner).resume()
            
        } else {
            /*let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            context.present(vc_alert, animated: true, completion: nil)*/

        }
    }
    
    private func parseJsonBanner(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    
                    //print(json)
                    
                    if let jsonResult = json as? [String: Any] {
                        for (index, dato) in (jsonResult["banners"] as! [[String:Any]]).enumerated() {
                            
                            DispatchQueue.global(qos: .userInitiated).async { // 1
                                let dataImg = try? Data(contentsOf: URL(string: dato["url"] as! String)!)
                                
                                DispatchQueue.main.async { // 2
                                    
                                    CoreDataHelper.saveData(data: dato, entityName: "Banners", keyName: "banner")
                                    
                                    CoreDataHelper.updateData(index: index, data: dataImg!, entityName: "Banners", keyName: "imgBanner")
                                    
                                }
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
                        /*let vc_alert = UIAlertController(title: nil, message: jsonResult["mensaje"] as? String, preferredStyle: .alert)
                         vc_alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
                         self.present(vc_alert, animated: true, completion: nil)*/
                        print("Error json: \(jsonResult["mensaje"])")
                    }
                    
                    
                } else {
                    print("HTTP Status Code: 400 o 500")
                    print("El JSON de respuesta es inválido.")
                }
            }
        }
    }
    
    // MARK: - Sitios
    
    private func getSitios() {
        if Accesibilidad.isConnectedToNetwork() == true {
            
            let strUrl = "http://cmp.devworms.com/api/puebla/sitio/all/\(userID)/\(apiKey)"
            print(strUrl)
            
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJsonSitios).resume()
            
        } else {
            /*let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
             vc_alert.addAction(UIAlertAction(title: "OK",
             style: UIAlertActionStyle.default,
             handler: nil))
             context.present(vc_alert, animated: true, completion: nil)*/
            
        }
    }
    
    private func parseJsonSitios(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    
                    //print(json)
                    
                    if let jsonResult = json as? [String: Any] {
                        for (index, dato) in (jsonResult["sitios"] as! [[String:Any]]).enumerated() {
                            
                            DispatchQueue.global(qos: .userInitiated).async { // 1
                                
                                if let img = dato["imagen"] as? [String: Any] {
                                    
                                    let dataImg = try? Data(contentsOf: URL(string: img["url"] as! String)!)
                                    
                                    DispatchQueue.main.async { // 2
                                        
                                        CoreDataHelper.saveData(data: dato, entityName: "Sitios", keyName: "sitio")
                                        
                                        CoreDataHelper.updateData(index: index, data: dataImg!, entityName: "Sitios", keyName: "imgSitio")
                                        
                                    }
                                }
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
                            /*let vc_alert = UIAlertController(title: nil, message: jsonResult["mensaje"] as? String, preferredStyle: .alert)
                             vc_alert.addAction(UIAlertAction(title: "OK", style: .cancel , handler: nil))
                             self.present(vc_alert, animated: true, completion: nil)*/
                            print("Error json: \(jsonResult["mensaje"])")
                        }
                        
                        
                    } else {
                        print("HTTP Status Code: 400 o 500")
                        print("El JSON de respuesta es inválido.")
                    }
                }
            }
        }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
