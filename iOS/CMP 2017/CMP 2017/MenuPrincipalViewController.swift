//
//  MenuPrincipalViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 08/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit
import AWSMobileHubHelper


class MenuPrincipalViewController: UITableViewController {
    
    @IBOutlet weak var programaBtn: UIButton!
    @IBOutlet weak var acompañantesBtn: UIButton!
    @IBOutlet weak var patrocinadoresBtn: UIButton!
    @IBOutlet weak var transportacionBtn: UIButton!
    @IBOutlet weak var conoceBtn: UIButton!
    @IBOutlet weak var mapaBtn: UIButton!
    @IBOutlet weak var socialesBtn: UIButton!
    @IBOutlet weak var expositoresBtn: UIButton!
    @IBOutlet weak var banner: UIImageView!
    
    //var datos = [[String : Any]]()
    var imgs = [Data]()
    var imagenes = [UIImage]()
    var noImg = 0
    
    //push
    fileprivate var pushManager: AWSPushManager!
    
    #if RELEASE
    let ServiceKey: String = "Prod";
    #else
    let ServiceKey: String = "Devo";
    #endif
    // para push notification
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.isNavigationBarHidden = false
    }

    
  
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        let nav = self.navigationController?.navigationBar
        nav?.tintColor = UIColor.white
        
        nav?.setBackgroundImage(navBackgroundImage, for:.default)
        
        nav?.titleTextAttributes = [NSForegroundColorAttributeName: #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)]
        nav?.topItem?.title = UserDefaults.standard.value(forKey: "name") as! String?
        
        cargarImg()
        
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
    
    func cargarImg() {
        
        self.imgs = CoreDataHelper.fetchItem(entityName: "Banners", keyName: "imgBanner") as! [Data]
        
        for img in imgs {
            self.imagenes.append( UIImage(data: img)! )
        }
        
        update()
        
        Timer.scheduledTimer(timeInterval: 5.0, target: self, selector: #selector(self.update), userInfo: nil, repeats: true);
        
    }
    
    func update() {
        
        banner.image = imagenes[noImg]
        
        if noImg < imagenes.count - 1{
            noImg += 1
        } else {
            noImg = 0
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "acompañantes" {
            (segue.destination as! ResultadosViewController).seccion = 3
        } else if segue.identifier == "deportivos" {
            (segue.destination as! ResultadosViewController).seccion = 4
        } else if segue.identifier == "patrocinadores" {
            (segue.destination as! BuscadorViewController).seccion = 5
        }
    }

    // para cuadrar las imagenes
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        
        return pantallaSizeHeight(row: indexPath.row);//Choose your custom row height
    }
    
    func pantallaSizeHeight(row: Int)->CGFloat!
    {
        var strPantalla = 143 //iphone 5
        
        if(row == 0){
            strPantalla = 163
        }
        
        if (UIDevice.current.userInterfaceIdiom == .pad)
        {
            if(row == 0){
                strPantalla = 446
            }
            else{
                strPantalla = 350
            }
           
        }
        else
        {
            
            if UIScreen.main.bounds.size.width > 320 {
                if UIScreen.main.scale == 3 { //iphone 6 plus
                    
                    strPantalla = 143
                }
                else{
                    strPantalla = 143 //iphone 6
                }
            }
        }
        return CGFloat(strPantalla)
    }

}


// extenciones para recibir norificaciones dentro de la view actual
// MARK:- AWSPushManagerDelegate

extension MenuPrincipalViewController : AWSPushManagerDelegate {
    
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

extension MenuPrincipalViewController: AWSPushTopicDelegate {
    
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

extension MenuPrincipalViewController {
    
    fileprivate func showAlertWithTitle(_ title: String, message: String) {
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let cancelAction = UIAlertAction(title: "OK", style: .default, handler: nil)
        alertController.addAction(cancelAction)
        present(alertController, animated: true, completion: nil)
    }
}



