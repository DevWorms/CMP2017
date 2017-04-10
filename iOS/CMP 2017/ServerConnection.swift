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
        CoreDataHelper.deleteEntity(entityName: "Rutas")
        CoreDataHelper.deleteEntity(entityName: "Acompanantes")
        CoreDataHelper.deleteEntity(entityName: "Deportivos")
        CoreDataHelper.deleteEntity(entityName: "Patrocinadores")
        CoreDataHelper.deleteEntity(entityName: "Expositores")
        CoreDataHelper.deleteEntity(entityName: "Programas")
        
        //Cargar BD
        self.getBanners()
        self.getSitios()
        self.getRutas()
        self.getAcompañantes()
        self.getDeportivos()
        self.getPatrocinadores()
        //self.getExpositores()
        self.getProgramas()
        
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
                            
                            CoreDataHelper.saveData(data: dato, entityName: "Banners", keyName: "banner")
                            
                            DispatchQueue.global(qos: .userInitiated).async { // 1
                                let dataImg = try? Data(contentsOf: URL(string: dato["url"] as! String)!)
                                
                                DispatchQueue.main.async { // 2
                                    
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
                            
                            CoreDataHelper.saveData(data: dato, entityName: "Sitios", keyName: "sitio")
                            
                            DispatchQueue.global(qos: .userInitiated).async { // 1
                                
                                if let img = dato["imagen"] as? [String: Any] {
                                    
                                    let dataImg = try? Data(contentsOf: URL(string: img["url"] as! String)!)
                                    
                                    DispatchQueue.main.async { // 2
                                        
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
    
    // MARK: - Rutas
    
    private func getRutas() {
        if Accesibilidad.isConnectedToNetwork() == true {
            
            let strUrl = "http://cmp.devworms.com/api/ruta/all/\(userID)/\(apiKey)"
            print(strUrl)
            
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJsonRutas).resume()
            
        } else {
            /*let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
             vc_alert.addAction(UIAlertAction(title: "OK",
             style: UIAlertActionStyle.default,
             handler: nil))
             context.present(vc_alert, animated: true, completion: nil)*/
            
        }
    }
    
    private func parseJsonRutas(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    DispatchQueue.main.async {
                        
                        if let jsonResult = json as? [String: Any] {
                            for dato in jsonResult["rutas"] as! [[String:Any]] {
                                
                                CoreDataHelper.saveData(data: dato, entityName: "Rutas", keyName: "ruta")
                                
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
    
    // MARK: - Acompañantes
    
    private func getAcompañantes() {
        if Accesibilidad.isConnectedToNetwork() == true {
            
            let strUrl = "http://cmp.devworms.com/api/acompanantes/all/\(userID)/\(apiKey)"
            print(strUrl)
            
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJsonAcompañantes).resume()
            
        } else {
            /*let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
             vc_alert.addAction(UIAlertAction(title: "OK",
             style: UIAlertActionStyle.default,
             handler: nil))
             context.present(vc_alert, animated: true, completion: nil)*/
            
        }
    }
    
    private func parseJsonAcompañantes(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    if let jsonResult = json as? [String: Any] {
                        for (index, dato) in (jsonResult["acompanantes"] as! [[String:Any]]).enumerated() {
                            
                            CoreDataHelper.saveData(data: dato, entityName: "Acompanantes", keyName: "acompanante")
                            
                            DispatchQueue.global(qos: .userInitiated).async { // 1
                                
                                if let img = dato["foto"] as? [String: Any] {
                                    
                                    let dataImg = try? Data(contentsOf: URL(string: img["url"] as! String)!)
                                    
                                    DispatchQueue.main.async { // 2
                                        
                                        CoreDataHelper.updateData(index: index, data: dataImg!, entityName: "Acompanantes", keyName: "imgAcompanante")
                                        
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

    // MARK: - Deportivos
    
    private func getDeportivos() {
        if Accesibilidad.isConnectedToNetwork() == true {
            
            let strUrl = "http://cmp.devworms.com/api/deportivos/all/\(userID)/\(apiKey)"
            print(strUrl)
            
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJsonDeportivos).resume()
            
        } else {
            /*let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
             vc_alert.addAction(UIAlertAction(title: "OK",
             style: UIAlertActionStyle.default,
             handler: nil))
             context.present(vc_alert, animated: true, completion: nil)*/
            
        }
    }
    
    private func parseJsonDeportivos(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    if let jsonResult = json as? [String: Any] {
                        for (index, dato) in (jsonResult["eventos"] as! [[String:Any]]).enumerated() {
                            
                            CoreDataHelper.saveData(data: dato, entityName: "Deportivos", keyName: "evento")
                            
                            DispatchQueue.global(qos: .userInitiated).async { // 1
                                
                                if let img = dato["foto"] as? [String: Any] {
                                    
                                    let dataImg = try? Data(contentsOf: URL(string: img["url"] as! String)!)
                                    
                                    DispatchQueue.main.async { // 2
                                        
                                        CoreDataHelper.updateData(index: index, data: dataImg!, entityName: "Deportivos", keyName: "imgDeportivo")
                                        
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
    
    // MARK: - Patrocinadores
    
    private func getPatrocinadores() {
        if Accesibilidad.isConnectedToNetwork() == true {
            
            let strUrl = "http://cmp.devworms.com/api/patrocinador/all/\(userID)/\(apiKey)"
            print(strUrl)
            
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJsonPatrocinadores).resume()
            
        } else {
            /*let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
             vc_alert.addAction(UIAlertAction(title: "OK",
             style: UIAlertActionStyle.default,
             handler: nil))
             context.present(vc_alert, animated: true, completion: nil)*/
            
        }
    }
    
    private func parseJsonPatrocinadores(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    if let jsonResult = json as? [String: Any] {
                        for (index, dato) in (jsonResult["patrocinadores"] as! [[String:Any]]).enumerated() {
                            
                            CoreDataHelper.saveData(data: dato, entityName: "Patrocinadores", keyName: "patrocinador")
                            
                            DispatchQueue.global(qos: .userInitiated).async { // 1
                                
                                if let img = dato["logo"] as? [String: Any] {
                                    
                                    let dataImg = try? Data(contentsOf: URL(string: img["url"] as! String)!)
                                    
                                    DispatchQueue.main.async { // 2
                                        
                                        CoreDataHelper.updateData(index: index, data: dataImg!, entityName: "Patrocinadores", keyName: "imgPatrocinador")
                                        
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
    
    // MARK: - Expositores
    
    private func getExpositores() {
        if Accesibilidad.isConnectedToNetwork() == true {
            
            let strUrl = "http://cmp.devworms.com/api/expositor/all/\(userID)/\(apiKey)"
            print(strUrl)
            
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJsonExpositores).resume()
            
        } else {
            /*let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
             vc_alert.addAction(UIAlertAction(title: "OK",
             style: UIAlertActionStyle.default,
             handler: nil))
             context.present(vc_alert, animated: true, completion: nil)*/
            
        }
    }
    
    private func parseJsonExpositores(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    if let jsonResult = json as? [String: Any] {
                        for (index, dato) in (jsonResult["expositores"] as! [[String:Any]]).enumerated() {
                            
                            CoreDataHelper.saveData(data: dato, entityName: "Expositores", keyName: "expositor")
                            
                            DispatchQueue.global(qos: .userInitiated).async { // 1
                                
                                if let img = dato["logo"] as? [String: Any] {
                                    
                                    let dataImg = try? Data(contentsOf: URL(string: img["url"] as! String)!)
                                    
                                    DispatchQueue.main.async { // 2
                                        
                                        CoreDataHelper.updateData(index: index, data: dataImg!, entityName: "Expositores", keyName: "imgExpositor")
                                        
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

    // MARK: - Programas
    
    private func getProgramas() {
        if Accesibilidad.isConnectedToNetwork() == true {
            
            let strUrl = "http://cmp.devworms.com/api/programa/all/\(userID)/\(apiKey)"
            print(strUrl)
            
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJsonProgramas).resume()
            
        } else {
            /*let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
             vc_alert.addAction(UIAlertAction(title: "OK",
             style: UIAlertActionStyle.default,
             handler: nil))
             context.present(vc_alert, animated: true, completion: nil)*/
            
        }
    }
    
    private func parseJsonProgramas(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    if let jsonResult = json as? [String: Any] {
                        for (index, dato) in (jsonResult["programas"] as! [[String:Any]]).enumerated() {
                            
                            CoreDataHelper.saveData(data: dato, entityName: "Programas", keyName: "programa")
                            
                            DispatchQueue.global(qos: .userInitiated).async { // 1
                                
                                if let img = dato["foto"] as? [String: Any] {
                                    
                                    let dataImg = try? Data(contentsOf: URL(string: img["url"] as! String)!)
                                    
                                    DispatchQueue.main.async { // 2
                                        
                                        CoreDataHelper.updateData(index: index, data: dataImg!, entityName: "Programas", keyName: "imgPrograma")
                                        
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
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
}

