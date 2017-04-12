//
//  ServerConnection.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 05/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import Foundation
import UIKit

//http://stackoverflow.com/questions/5473/how-can-i-undo-git-reset-hard-head1 recuperar commits (historial local y versionado)
//let apiKey = UserDefaults.standard.value(forKey: "api_key")!
//let userID = UserDefaults.standard.value(forKey: "user_id")!

private let apiKey = 0
private let userID = 1

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
        CoreDataHelper.deleteEntity(entityName: "Rutas")
        CoreDataHelper.deleteEntity(entityName: "Acompanantes")
        CoreDataHelper.deleteEntity(entityName: "Deportivos")
        CoreDataHelper.deleteEntity(entityName: "Patrocinadores")
        CoreDataHelper.deleteEntity(entityName: "Expositores")
        CoreDataHelper.deleteEntity(entityName: "Programas")
        CoreDataHelper.deleteEntity(entityName: "Categorias")
        
        //Cargar BD
        
        // Banners
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/banners/all/\(userID)/\(apiKey)", jsonString: "banners", datoString: nil, imgString: "url", entityName: "Banners", keyName: "banner", keyNameImg: "imgBanner", completion: { (Bool) in } )
        
        // Sitios
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/puebla/sitio/all/\(userID)/\(apiKey)", jsonString: "sitios", datoString: "imagen", imgString: "url", entityName: "Sitios", keyName: "sitio", keyNameImg: "imgSitio", completion: { (Bool) in
            
        })
        
        // Rutas
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/ruta/all/\(userID)/\(apiKey)", jsonString: "rutas", datoString: "pdf", imgString: "url", entityName: "Rutas", keyName: "ruta", keyNameImg: "imgRuta", completion: { (Bool) in
            
        })
        
        // Acompañantes
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/acompanantes/all/\(userID)/\(apiKey)", jsonString: "acompanantes", datoString: "foto", imgString: "url", entityName: "Acompanantes", keyName: "acompanante", keyNameImg: "imgAcompanante", completion: { (Bool) in
            
        })
        
        // Deportivos
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/deportivos/all/\(userID)/\(apiKey)", jsonString: "eventos", datoString: "foto", imgString: "url", entityName: "Deportivos", keyName: "evento", keyNameImg: "imgDeportivo", completion: { (Bool) in
            
        })
        
        // Patrocinadores
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/patrocinador/all/\(userID)/\(apiKey)", jsonString: "patrocinadores", datoString: "logo", imgString: "url", entityName: "Patrocinadores", keyName: "patrocinador", keyNameImg: "imgPatrocinador", completion: { (Bool) in
            
        })
        
        // Programas
        
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/programa/all/\(userID)/\(apiKey)", jsonString: "programas", datoString: "foto", imgString: "url", entityName: "Programas", keyName: "programa", keyNameImg: "imgPrograma", completion: { (Bool) in
            
        })
        
        // Categorias
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/categoria/all/\(userID)/\(apiKey)", jsonString: "categorias", datoString: nil, imgString: nil, entityName: "Categorias", keyName: "categoria", keyNameImg: nil, completion: { (Bool) in
            
        })
        
        // Expositores
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/expositor/all/\(userID)/\(apiKey)", jsonString: "expositores", datoString: "logo", imgString: "url", entityName: "Expositores", keyName: "expositor", keyNameImg: "imgExpositor") { (Bool) in
            self.mView.dismiss(animated: false, completion: nil)
        }
        
        
        
    }
    
    private func getGeneral(strUrl: String, jsonString: String, datoString: String?, imgString: String?, entityName: String, keyName: String, keyNameImg: String?, completion: @escaping (Bool) -> Void) {
        if Accesibilidad.isConnectedToNetwork() == true {
            
            print(strUrl)
            
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: { (data: Data?, urlResponse: URLResponse?, error: Error?) in
                if error != nil {
                    print(error!)
                } else if urlResponse != nil {
                    if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                        if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                            //print(json)
                            
                            if let jsonResult = json as? [String: Any] {
                                for (index, dato) in (jsonResult[ jsonString ] as! [[String:Any]]).enumerated() {
                                    
                                    if datoString != nil { // si encuentra etiqueta de imagen
                                        if let img = dato[ datoString! ] as? [String: Any] { // si la etiqueta se puede castear a un arreglo
                                            
                                            let dataImg = try? Data(contentsOf: URL(string: img[ imgString! ] as! String)!)
                                            
                                            CoreDataHelper.saveData(entityName: entityName, data: dato, keyName: keyName, dataImg: dataImg!, keyNameImg: keyNameImg)
                                            
                                        } else { // si no se puede procesar la imagen
                                            CoreDataHelper.saveData(entityName: entityName, data: dato, keyName: keyName, dataImg: nil, keyNameImg: keyNameImg)
                                        }
                                        
                                    } else { // si solo cuenta con url para la imagen
                                        
                                        if imgString != nil { // url no esta vacio
                                            let dataImg = try? Data(contentsOf: URL(string: dato[ imgString! ] as! String)!)
                                            
                                            CoreDataHelper.saveData(entityName: entityName, data: dato, keyName: keyName, dataImg: dataImg!, keyNameImg: keyNameImg)
                                        } else { // no tiene url
                                            CoreDataHelper.saveData(entityName: entityName, data: dato, keyName: keyName, dataImg: nil, keyNameImg: nil)
                                        }
                                        
                                    }
                                    
                                    if index == (jsonResult[ jsonString ] as! [[String:Any]]).count - 1 {
                                        
                                        print("hurra se cargo todo creo :S")
                                        completion(true)
                                    }
                                    
                                }
                            }
                            
                        } else {
                            print("HTTP Status Code: 200")
                            print("El JSON de respuesta es inválido.")
                        }
                    } else {
                        
                        if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                            if let jsonResult = json as? [String: Any] {
                                print("Error json: \(jsonResult["mensaje"])")
                            }
                            
                        } else {
                            print("HTTP Status Code: 400 o 500")
                            print("El JSON de respuesta es inválido.")
                        }
                    }
                }
            }).resume()
            
        } else {
            print("Sin conexión a internet: \(entityName)")
        }
    }
    
    
    
}

