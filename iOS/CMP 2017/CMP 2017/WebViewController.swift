//
//  WebViewController.swift
//  CMP 2017
//
//  Created by mac on 24/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//
import UIKit
class WebViewController: UIViewController, UIWebViewDelegate {
    
    
    
    @IBOutlet weak var webView: UIWebView!

    var alert = UIAlertController()
    var urlWeb = ""
    var idMapa = 0
    var tipo = 0
    
    override func viewDidLoad() {
         super.viewDidLoad()
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        self.webView.delegate = self
        
        self.webView.scalesPageToFit = true
        self.webView.scrollView.minimumZoomScale = 1.0
        self.webView.scrollView.maximumZoomScale = 6.0
        
        if tipo == 1 {
            urlWeb = "http://congreso.digital/public-map.php#\(self.idMapa)"
            
        }else if tipo == 2{
        
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
         
            
        } else {
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            self.present(vc_alert, animated: true, completion: nil)
        }
        
           }
    
    func inicial() {
        
        self.webView.loadRequest(URLRequest(url: URL(string: self.urlWeb)!))
        
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
    

    
}
