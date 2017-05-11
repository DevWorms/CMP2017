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
var apiKey  = ""
var userID  = 0

//private let apiKey = 0
//private let userID = 1

let appDelegate = UIApplication.shared.delegate as! AppDelegate
let managedContext = appDelegate.managedObjectContext
var descarga = 0
var tipoDescar = 0

var actualizarEvenAcompa = 0
var actualizarExpositores = 0
var actualizarPatrocinadores = 0
var actualizarProgramas = 0
var actualizarRutas = 0
var actualizarEvenSociaDepo = 0
var actualizarSitiosInte = 0



class ServerConnection {
    
    private var mView = UIViewController()
    
    // MARK: - CMP
    
    func getCMP(myView: UIViewController) {
        
        if Accesibilidad.isConnectedToNetwork() == true {
            
            self.mView = myView
            
            let alert = UIAlertController(title: nil, message: " \n Descargando información, por favor espera puede tardar 1 o 2 minutos", preferredStyle: .alert)
            alert.view.tintColor = UIColor.black
            
            let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRect(x: 110, y: -5, width: 50, height: 50)) as UIActivityIndicatorView
            loadingIndicator.hidesWhenStopped = true
            loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
            loadingIndicator.startAnimating()
            
            /*let callAction = UIAlertAction(title: "Cancelar", style: .Default, handler: {
             action in
             
             self.hurryPrintMethods.cancelConnection()
             return
             })
             alert.addAction(callAction)*/
            if let descarga = UserDefaults.standard.value(forKey: "descarga")as? Int {
           
                if descarga != 1 {
                    apiKey = UserDefaults.standard.value(forKey: "api_key")! as! String
                    userID = UserDefaults.standard.value(forKey: "user_id")! as! Int
                    alert.view.addSubview(loadingIndicator)
                    myView.present(alert, animated: true, completion: actualizar)
                }
            
           
            }else{
                apiKey = "0"
                userID = 1
                alert.view.addSubview(loadingIndicator)
                myView.present(alert, animated: true, completion: llamadas)
            }
            
            
            
        } else {
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            myView.present(vc_alert, animated: true, completion: nil)
        }
        
    }
    private func actualizar(){
        //Checar actualizaciones
        print("Buscando actualizacion")
        if let tipoDescar = UserDefaults.standard.value(forKey: "tipoDescar")as? Int {
            
            if tipoDescar == 1 {
                
                URLSession.shared.dataTask(with: URL(string: "http://cmp.devworms.com/api/updates/check/\(userID)/\(apiKey)")!, completionHandler: parseJson).resume()
            } else {
                self.llamadas()
            }
            
            
        }

    }
    
    private func llamadas() {
        
        
        
        //Eliminar BD
        CoreDataHelper.deleteEntity(entityName: "Banners")
        CoreDataHelper.deleteEntity(entityName: "Categorias")
        CoreDataHelper.deleteEntity(entityName: "MapaRecinto")
        
        
        // Banners
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/banners/all/\(userID)/\(apiKey)", jsonString: "banners", datoString: nil, imgString: "url", entityName: "Banners", keyName: "banner", keyNameImg: "imgBanner", Simple : 0 , completion: { (Bool) in } )
        
        // MapaRecinto
        
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/mapa/recinto/\(userID)/\(apiKey)", jsonString: "mapa", datoString: nil, imgString: "url", entityName: "MapaRecinto", keyName: "urlMapa", keyNameImg: "imgMapa",Simple : 1 , completion: { (Bool) in } )
        
        // Categorias
        self.getGeneral(strUrl: "http://cmp.devworms.com/api/categoria/all/\(userID)/\(apiKey)", jsonString: "categorias", datoString: nil, imgString: nil, entityName: "Categorias", keyName: "categoria", keyNameImg: nil,Simple : 0 ,completion: { (Bool) in
            
        })
        print("tipoDescar = \(tipoDescar)")
        
         print("actualizarSitiosInte = \(actualizarSitiosInte)")
         print("actualizarRutas = \(actualizarRutas)")
         print("actualizarEvenAcompa = \(actualizarEvenAcompa)")
         print("actualizarEvenSociaDepo = \(actualizarEvenSociaDepo)")
         print("actualizarPatrocinadores = \(actualizarPatrocinadores)")
         print("actualizarProgramas = \(actualizarProgramas)")
      
        
        if actualizarSitiosInte == 1 || tipoDescar == 0  {
            
            CoreDataHelper.deleteEntity(entityName: "Sitios")
            
            // Sitios
            self.getGeneral(strUrl: "http://cmp.devworms.com/api/puebla/sitio/all/\(userID)/\(apiKey)", jsonString: "sitios", datoString: "imagen", imgString: "url", entityName: "Sitios", keyName: "sitio", keyNameImg: "imgSitio", Simple : 0 , completion: { (Bool) in
                
            })
        }
        if actualizarRutas == 1 || tipoDescar == 0  {

            CoreDataHelper.deleteEntity(entityName: "Rutas")
            
            // Rutas
            self.getGeneral(strUrl: "http://cmp.devworms.com/api/ruta/all/\(userID)/\(apiKey)", jsonString: "rutas", datoString: "image", imgString: "url", entityName: "Rutas", keyName: "ruta", keyNameImg: "imgRuta",Simple : 0 , completion: { (Bool) in
                
            })
        }
        if actualizarEvenAcompa == 1 || tipoDescar == 0  {

            CoreDataHelper.deleteEntity(entityName: "Acompanantes")
            // Acompañantes
            self.getGeneral(strUrl: "http://cmp.devworms.com/api/acompanantes/all/\(userID)/\(apiKey)", jsonString: "acompanantes", datoString: "foto", imgString: "url", entityName: "Acompanantes", keyName: "acompanante", keyNameImg: "imgAcompanante",Simple : 0 , completion: { (Bool) in
                
            })
        }
        if actualizarEvenSociaDepo == 1 || tipoDescar == 0  {

            CoreDataHelper.deleteEntity(entityName: "Deportivos")
            
            
            // Deportivos
            self.getGeneral(strUrl: "http://cmp.devworms.com/api/deportivos/all/\(userID)/\(apiKey)", jsonString: "eventos", datoString: "foto", imgString: "url", entityName: "Deportivos", keyName: "evento", keyNameImg: "imgDeportivo",Simple : 0 , completion: { (Bool) in
                
            })
        }
        if actualizarPatrocinadores == 1 || tipoDescar == 0  {

            CoreDataHelper.deleteEntity(entityName: "Patrocinadores")
            
            // Patrocinadores
            self.getGeneral(strUrl: "http://cmp.devworms.com/api/patrocinador/all/\(userID)/\(apiKey)", jsonString: "patrocinadores", datoString: "logo", imgString: "url", entityName: "Patrocinadores", keyName: "patrocinador", keyNameImg: "imgPatrocinador",Simple : 0 , completion: { (Bool) in
                
            })
        }
        if actualizarProgramas == 1 || tipoDescar == 0  {
            
            CoreDataHelper.deleteEntity(entityName: "Programas")
            // Programas
            
            self.getGeneral(strUrl: "http://cmp.devworms.com/api/programa/all/\(userID)/\(apiKey)", jsonString: "programas", datoString: "foto", imgString: "url", entityName: "Programas", keyName: "programa", keyNameImg: "imgPrograma",Simple : 0 , completion: { (Bool) in
                
            })
        }
        if actualizarExpositores == 1 || tipoDescar == 0  {

            CoreDataHelper.deleteEntity(entityName: "Expositores")
            
            // Expositores
            self.getGeneral(strUrl: "http://cmp.devworms.com/api/expositor/all/\(userID)/\(apiKey)", jsonString: "expositores", datoString: "logo", imgString: "url", entityName: "Expositores", keyName: "expositor", keyNameImg: "imgExpositor",Simple : 0) { (Bool) in
                self.mView.dismiss(animated: false, completion: nil)
                UserDefaults.standard.setValue(1, forKey: "descarga")
            }
        }else{
            // Expositores
            self.getGeneral(strUrl: "http://cmp.devworms.com/api/expositor/all/\(userID)/\(apiKey)", jsonString: "expositores", datoString: "logo", imgString: "url", entityName: "Expositores", keyName: "expositor", keyNameImg: "imgExpositor",Simple : 0) { (Bool) in
                self.mView.dismiss(animated: false, completion: nil)
                UserDefaults.standard.setValue(1, forKey: "descarga")
              
            }
        }
        
    
        
        
    }
    
    private func getGeneral(strUrl: String, jsonString: String, datoString: String?, imgString: String?, entityName: String, keyName: String, keyNameImg: String?,Simple: Int, completion: @escaping (Bool) -> Void) {
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
                                if Simple == 1{
                                    let jsonMapa = jsonResult[ jsonString ]  as? [String: Any]
                                    let urlMapaInfo = jsonMapa?["url"] as! String
                                    
                                    
                                    if let img = jsonResult[ jsonString ] as? [String: Any] { // si la etiqueta se puede castear a un arreglo
                                        
                                        let dataImg = try? Data(contentsOf: URL(string: img[ imgString! ] as! String)!)
                                        
                                        CoreDataHelper.saveData(entityName: entityName, data: urlMapaInfo, keyName: keyName, dataImg: dataImg!, keyNameImg: keyNameImg)
                                        
                                    } else { // si no se puede procesar la imagen
                                        
                                        
                                        CoreDataHelper.saveData(entityName: entityName, data: urlMapaInfo, keyName: keyName, dataImg: nil, keyNameImg: keyNameImg)
                                        
                                    }

                                    
                                    
                                }else{
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
    
    func parseJson(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    print(json)
                    
                    DispatchQueue.main.async {
                        
                     
                        
                        if let jsonResult = json as? [String: Any] {
                            
                           let status = jsonResult["status"] as! Int
                            
                            if status == 1 {
                                
                          
                                for dato in jsonResult["actualizaciones"] as! [[String:Any]] {
                                
                                    print("modulo: \(dato["modulo"]!)")
                                   let modulo = dato["modulo"]! as! String
                                    
                                    //Devuelve el modulo que el user debe consumir para actualizar: 1 = Eventos de acompañantes 4 = Expositores 5 = Patrocinadores 6 = Programas 7 = Rutas / Transportación 8 = Eventos Sociales y Deportivos 9 = Conoce Puebla, teléfonos 10 = Conoce Puebla, sitios de interés
                                    
                                    if modulo == "1" {
                                        actualizarEvenAcompa = 1
                                        print ("actualizar acompa")
                                    } else if modulo  == "4" {
                                        actualizarExpositores = 1
                                           print ("actualizar expo")
                                    }else if modulo  == "5" {
                                        actualizarPatrocinadores = 1
                                           print ("actualizar patro")
                                    }else if modulo  == "6" {
                                        actualizarProgramas = 1
                                           print ("actualizar programas")
                                    }else if modulo  == "7" {
                                        actualizarRutas = 1
                                           print ("actualizar rutas")
                                    }else if modulo == "8" {
                                        actualizarEvenSociaDepo = 1
                                           print ("actualizar social")
                                    }else if modulo  == "10" {
                                        actualizarSitiosInte = 1
                                           print ("actualizar sitios")
                                    }
                                    
                             
                                }
                                tipoDescar = 1
                                self.llamadas()
                                
                            } else
                            {
                                 print("Sin actuaizaciones")
                                self.mView.dismiss(animated: false, completion: nil)
                                
                                let vc_alert = UIAlertController(title: "Actualizaciones", message: "No hay actualizaciones disponibles", preferredStyle: .alert)
                                vc_alert.addAction(UIAlertAction(title: "OK",
                                                                 style: UIAlertActionStyle.default,
                                                                 handler: nil))
                                self.mView.present(vc_alert, animated: true, completion: nil)
                            
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


