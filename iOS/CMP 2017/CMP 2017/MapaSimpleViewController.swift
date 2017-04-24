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
    
    var imgData: Any?
    var urlWeb = ""
    
    var alert = UIAlertController()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        self.webView.delegate = self
        
        self.webView.scalesPageToFit = true
        self.webView.scrollView.minimumZoomScale = 1.0
        self.webView.scrollView.maximumZoomScale = 6.0
        
        if self.tipoMapa == 1 {
            titleLabel.text = "Clima"
            
        } else if self.tipoMapa == 2 {
            titleLabel.text = "Mapa de Puebla"
            
        } else if self.tipoMapa == 3 {
            titleLabel.text = "Ruta"
        } else if self.tipoMapa == 4 {
            titleLabel.text = "Sitios de Interes"
        
        }
        
        if Accesibilidad.isConnectedToNetwork() == true {
            
            alert = UIAlertController(title: nil, message: "Cargando...", preferredStyle: .alert)
            alert.view.tintColor = UIColor.black
                
            let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50)) as UIActivityIndicatorView
            loadingIndicator.hidesWhenStopped = true
            loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
            loadingIndicator.startAnimating()
                
            alert.view.addSubview(loadingIndicator)
            
            self.present(alert, animated: true, completion: self.inicial)
            
            /*let callAction = UIAlertAction(title: "Cancelar", style: .Default, handler: {
             action in
             
             self.hurryPrintMethods.cancelConnection()
             return
             })
             alert.addAction(callAction)*/
            
        } else {
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            self.present(vc_alert, animated: true, completion: nil)
        }
    }
    
    func inicial() {
        if self.tipoMapa == 1 {
            webView.loadRequest(URLRequest(url: URL(string: "https://es-us.noticias.yahoo.com/clima")!))
            
        } else if self.tipoMapa == 2 {
            if let img = UIImage(named: "MapaPuebla.png") {
                let data = UIImagePNGRepresentation(img) as Data?
                
                webView.load(data!, mimeType: "image/png", textEncodingName: "UTF-8", baseURL: NSURL() as URL)
                
            }
            
        } else if self.tipoMapa == 3 {
            
            if let img = self.imgData as? Data {
              
                webView.load(img, mimeType: "image/png", textEncodingName: "UTF-8", baseURL: NSURL() as URL)
            }
            
        } else if self.tipoMapa == 4 {
             webView.loadRequest(URLRequest(url: URL(string: urlWeb)!))
        }
    }
    
    func webViewDidFinishLoad(_ webView: UIWebView) {
        
        alert.dismiss(animated: false, completion: nil)
        
    }
    
    func webView(_ webView: UIWebView, didFailLoadWithError error: Error) {
        alert.message = "Error"
        alert.dismiss(animated: false, completion: nil)
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
