//
//  MenuMapaViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 12/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class MenuMapaViewController: UIViewController {
    var idMapa = 0
    var urlMapa = ""
    var tipoMapa = 0
    
    var imgs = [Any?]()
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
         self.imgs = CoreDataHelper.fetchItem(entityName: "MapaRecinto", keyName: "imgMapa")!
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func menu(_ sender: Any) {
        let vc = storyboard!.instantiateViewController(withIdentifier: "MenuPrincipal")
        self.present( vc , animated: true, completion: nil)
    }
   
    @IBAction func mapaRecinto(_ sender: UIButton) {
        
       
        self.tipoMapa = 5
        self.urlMapa = "n"
        self.performSegue(withIdentifier: "webView", sender: nil)
      
    }

    @IBAction func mapaExpositor(_ sender: UIButton) {
        self.tipoMapa = 6
        self.urlMapa = "http://congreso.digital/public-map.php"
        self.performSegue(withIdentifier: "webView", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        if segue.identifier == "webView" {
            print( self.urlMapa)
            (segue.destination as! MapaSimpleViewController).tipoMapa = self.tipoMapa
            (segue.destination as! MapaSimpleViewController).urlWeb = self.urlMapa
            (segue.destination as! MapaSimpleViewController).imgData =  self.imgs[0]
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
