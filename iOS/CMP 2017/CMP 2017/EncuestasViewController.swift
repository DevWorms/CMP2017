//
//  EncuestasViewController.swift
//  CMP 2017
//
//  Created by mac on 13/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class EncuestaViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    
    var datos = [[String : Any]]()
    var idEncuesta: Int!
     var idEncuesta1: Int!
    var json:[String : Any] = [:]
    var jsonSelec:[String : Any] = [:]
    var alert = UIAlertController()
    var strUrl = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        let nav = self.navigationController?.navigationBar
        nav?.tintColor = UIColor.white
        nav!.setBackgroundImage(navBackgroundImage, for:.default)
        
        nav?.titleTextAttributes = [NSForegroundColorAttributeName: #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)]
        nav?.topItem?.title = UserDefaults.standard.value(forKey: "name") as! String?
        
        let apiKey = UserDefaults.standard.value(forKey: "api_key")
        let userID = UserDefaults.standard.value(forKey: "user_id")
        
        self.strUrl = "http://cmp.devworms.com/api/encuesta/all/\(userID!)/\(apiKey!)"
        print(strUrl)
        
        if Accesibilidad.isConnectedToNetwork() == true {
            alert = UIAlertController(title: nil, message: "Cargando...", preferredStyle: .alert)
            alert.view.tintColor = UIColor.black
            
            let loadingIndicator: UIActivityIndicatorView = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50)) as UIActivityIndicatorView
            loadingIndicator.hidesWhenStopped = true
            loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
            loadingIndicator.startAnimating()
            
            alert.view.addSubview(loadingIndicator)
            
            self.present(alert, animated: true, completion: self.inicial )
            
            
            
        } else {
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            self.present(vc_alert, animated: true, completion: nil)
        }
    }
    
    func inicial()  {
     
      URLSession.shared.dataTask(with: URL(string: self.strUrl)!, completionHandler: parseJson).resume()
    }
    
    func parseJson(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    DispatchQueue.main.async {
                        
                        if self.datos.count > 0 {
                            self.datos.removeAll()
                        }
                        
                        if let jsonResult = json as? [String: Any] {
                            self.alert.dismiss(animated: false, completion: nil)
                            for dato in jsonResult["encuestas"] as! [[String:Any]] {
                                self.datos.append(dato)
                            }
                        }
                        
                        self.tableView.reloadData()
                    }
                    
                } else {
                    print("HTTP Status Code: 200")
                    print("El JSON de respuesta es inválido.")
                }
            } else {
                
                DispatchQueue.main.async {
                    if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                        if let jsonResult = json as? [String: Any] {
                            self.alert.dismiss(animated: false, completion: nil)
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
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return datos.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! EncuestaTableViewCell
        self.json  = self.datos[indexPath.row]
        let filesm = json["filesm"] as! [String : Any]
        let urlImage = filesm["url"] as! String
        
        //DispatchQueue.main.async(execute: display_image)
        
        
        let imgURL: URL = URL(string: urlImage)!
        let request: URLRequest = URLRequest(url: imgURL)
        
        let session = URLSession.shared
        let task = session.dataTask(with: request, completionHandler: {
            (data, response, error) -> Void in
            
            if (error == nil && data != nil)
            {
                //self.imagenes[objReceta] = UIImage(data: data!)
                
                func display_image()
                {
                    cell.imgEncuesta.image = UIImage(data: data!)
                    
                    cell.btnCalificar.tag = indexPath.row
                    cell.btnCalificar.addTarget(self, action: #selector(self.buttonClicked(_:)), for: UIControlEvents.touchUpInside)
                    
                    self.idEncuesta = self.json["id"] as! Int
                    print ("id: \(self.idEncuesta!)")
                    
                    
                    
                    
                }
                
                DispatchQueue.main.async(execute: display_image)
            }
            
        })
        
        task.resume()
        
        return cell
    }
    
    func buttonClicked(_ sender:UIButton) {
        jsonSelec = self.datos[sender.tag]
            print ("Tag \(sender.tag)")
        self.idEncuesta = self.jsonSelec["id"] as! Int
        print ("id \(self.idEncuesta!)")
        
        self.performSegue(withIdentifier: "detalleEncuestas", sender: nil)
        
        
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "detalleEncuestas" {
           print ("id manda\(self.idEncuesta!)")
            (segue.destination as! DetalleEncuestaViewController).idEncuesta = self.idEncuesta!
            
            /*let nav = segue.destination as! UINavigationController
             let svc = nav.topViewController as! DetalleEncuestaViewController
             svc.idEncuesta = self.idEncuesta;*/
            /*let destino = segue.destination as! DetalleEncuestaViewController
             
             destino.idEncuesta = self.idEncuesta*/
            
            
        }
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

