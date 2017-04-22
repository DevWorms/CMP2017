//
//  NotificacionesViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 13/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit
import AWSMobileHubHelper

class NotificacionesViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet weak var tableView: UITableView!
    
    var datos = [[String : Any]]()

    
    //push
    fileprivate var pushManager: AWSPushManager!
    
    #if RELEASE
    let ServiceKey: String = "Prod";
    #else
    let ServiceKey: String = "Devo";
    #endif
    // para push notification
    
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
        
        let strUrl = "http://cmp.devworms.com/api/notificacion/all/\(userID!)/\(apiKey!)"
        print(strUrl)
        
        if Accesibilidad.isConnectedToNetwork() == true {
            URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJson).resume()
            
        } else {
            let vc_alert = UIAlertController(title: "Sin conexión a internet", message: "Asegúrate de estar conectado a internet.", preferredStyle: .alert)
            vc_alert.addAction(UIAlertAction(title: "OK",
                                             style: UIAlertActionStyle.default,
                                             handler: nil))
            self.present(vc_alert, animated: true, completion: nil)
        }
        ////// copiar desde aqui para puush notifications
        setupPushManager()
        
    }
    
    
    func setupPushManager() {
        pushManager = AWSPushManager(forKey: ServiceKey)
        pushManager?.delegate = self
        pushManager?.registerForPushNotifications()
        if let topicARNs = pushManager?.topicARNs {
            pushManager.registerTopicARNs(topicARNs)
        }
    }
    
    // hasta aqui
    
    
   
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
                            for dato in jsonResult["notificaciones"] as! [[String:Any]] {
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! NotificacionTableViewCell
        
        cell.titulo.text = datos[indexPath.row]["notificacion"] as! String?
        
        if (datos[indexPath.row]["leido"] as! String) == "0" {
            cell.img.image = #imageLiteral(resourceName: "04mensaje")
        } else {
            cell.img.image = #imageLiteral(resourceName: "05mensaje_recibido")
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
 
        let vc_alert = UIAlertController(title: "Notificación", message: datos[indexPath.row]["notificacion"] as! String, preferredStyle: .alert)
        vc_alert.addAction(UIAlertAction(title: "OK",
                                         style: UIAlertActionStyle.default,
                                         handler: nil))
        self.present(vc_alert, animated: true, completion: nil)
        
        let apiKey = UserDefaults.standard.value(forKey: "api_key")
        let userID = UserDefaults.standard.value(forKey: "user_id")
        let idNotification = datos[indexPath.row]["id"]

        let strUrlN = "http://cmp.devworms.com/api/notificacion/markasread/\(userID!)/\(apiKey!)/\(idNotification!)"

     
        URLSession.shared.dataTask(with: URL(string: strUrlN)!, completionHandler: parseJsonNotificacionLeida).resume()
  
    }
    
    func parseJsonNotificacionLeida(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                        let apiKey = UserDefaults.standard.value(forKey: "api_key")!
                        let userID = UserDefaults.standard.value(forKey: "user_id")
                        
                        let strUrl = "http://cmp.devworms.com/api/notificacion/all/\(userID!)/\(apiKey)"
                        
                        print(strUrl)
                       
                        URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: self.parseJson).resume()
        
                    
                
                    
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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}



// extenciones para recibir norificaciones dentro de la view actual
// MARK:- AWSPushManagerDelegate

extension NotificacionesViewController : AWSPushManagerDelegate {
    
    func pushManagerDidRegister(_ pushManager: AWSPushManager) {
        print("Successfully enabled Push Notifications.")
        
        print("Subscribing to the first topic among the configured topics (all-device topic).");
        if let defaultSubscribeTopic = pushManager.topicARNs?.first {
            let topic = pushManager.topic(forTopicARN: defaultSubscribeTopic)
            topic.subscribe()
        }
    }
    
    func pushManager(_ pushManager: AWSPushManager, didFailToRegisterWithError error: Error) {
        print("Failed to Register for Push Notification: \(error)")
    }
    
    func pushManager(_ pushManager: AWSPushManager, didReceivePushNotification userInfo: [AnyHashable: Any]) {
        print("Received a Push Notification: \(userInfo.description)")
        
        
        let aps = userInfo[AnyHashable("aps")]! as! NSDictionary
        
        let alert = aps["alert"]! as! String
        
        
        showAlertWithTitle("Mensaje", message: alert as! String)
    }
    
    /*  private func getAlert(notification: [NSObject:AnyObject]) -> (String, String) {
     let aps = notification["aps" as NSString] as? [String:AnyObject]
     let alert = aps?["alert"] as? [String:AnyObject]
     let title = alert?["title"] as? String
     let body = alert?["body"] as? String
     return (title ?? "-", body ?? "-")
     }
     */
    
    func pushManagerDidDisable(_ pushManager: AWSPushManager) {
        print("Successfully disabled Push Notification.")
    }
    
    func pushManager(_ pushManager: AWSPushManager, didFailToDisableWithError error: Error) {
        print("Failed to subscribe to a topic: \(error)")
    }
    
    
}

// MARK:- AWSPushTopicDelegate

extension NotificacionesViewController: AWSPushTopicDelegate {
    
    func topicDidSubscribe(_ topic: AWSPushTopic) {
        print("Successfully subscribed to a topic: \(topic.topicName)")
    }
    
    func topic(_ topic: AWSPushTopic, didFailToSubscribeWithError error: Error) {
        print("Failed to subscribe to topic: \(topic.topicName)")
    }
    
    func topicDidUnsubscribe(_ topic: AWSPushTopic) {
        print("Successfully unsubscribed from a topic: \(topic)")
    }
    
    func topic(_ topic: AWSPushTopic, didFailToUnsubscribeWithError error: Error) {
        print("Failed to subscribe to a topic: \(error)")
    }
}

// MARK:- Utility methods

extension NotificacionesViewController {
    
    fileprivate func showAlertWithTitle(_ title: String, message: String) {
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let cancelAction = UIAlertAction(title: "OK", style: .default, handler: nil)
        alertController.addAction(cancelAction)
        present(alertController, animated: true, completion: nil)
    }
}


