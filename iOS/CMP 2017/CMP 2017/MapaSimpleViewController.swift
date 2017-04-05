//
//  MapaSimpleViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 13/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class MapaSimpleViewController: UIViewController, UIWebViewDelegate {

    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var webView: UIWebView!
    
    // 1 clima
    // 2 mapa puebla
    // 3 rutas
    var tipoMapa = 0
    
    var pdfEnviado = String()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        self.webView.delegate = self
        
        if Accesibilidad.isConnectedToNetwork() == true {
            
            if webView.isLoading {
                let alert = UIAlertController(title: nil, message: "Cargando...", preferredStyle: .alert)
                alert.view.tintColor = UIColor.black
                
                let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50)) as UIActivityIndicatorView
                loadingIndicator.hidesWhenStopped = true
                loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
                loadingIndicator.startAnimating()
                
                alert.view.addSubview(loadingIndicator)
                self.present(alert, animated: true, completion: nil)
            }
                        
            /*let callAction = UIAlertAction(title: "Cancelar", style: .Default, handler: {
             action in
             
             self.hurryPrintMethods.cancelConnection()
             return
             })
             alert.addAction(callAction)*/
            
            
            
            if self.tipoMapa == 1 {
                titleLabel.text = "Clima"
            
                webView.loadRequest(URLRequest(url: URL(string: "https://es-us.noticias.yahoo.com/clima")!))
                
            } else if self.tipoMapa == 2 {
                titleLabel.text = "Mapa de Puebla"
                
                let apiKey = UserDefaults.standard.value(forKey: "api_key")
                let userID = UserDefaults.standard.value(forKey: "user_id")
                
                let strUrl = "http://cmp.devworms.com/api/puebla/mapa/\(userID!)/\(apiKey!)"
                print(strUrl)
                
                URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJson).resume()
                
            } else if self.tipoMapa == 3 {
                titleLabel.text = "Ruta"
                
                webView.loadRequest(URLRequest(url: URL(string: self.pdfEnviado)!))
            }
            
        } else {
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            self.present(vc_alert, animated: true, completion: nil)
        }
    }
    
    func webViewDidFinishLoad(_ webView: UIWebView) {
        //quita el alert
        self.dismiss(animated: false, completion: nil)
    }
    
    func parseJson(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    DispatchQueue.main.async {
                        
                        if let jsonResult = json as? [String: Any] {
                            if let pdf = jsonResult["mapa"] as? [String:Any] {
                                self.webView.loadRequest(URLRequest(url: URL(string: pdf["url"] as! String )!))
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

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func menu(_ sender: Any) {
        let vc = storyboard!.instantiateViewController(withIdentifier: "MenuPrincipal")
        self.present( vc , animated: true, completion: nil)
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
