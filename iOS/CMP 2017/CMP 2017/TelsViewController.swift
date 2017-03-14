//
//  TelsViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 12/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class TelsViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func menu(_ sender: Any) {
        let vc = storyboard!.instantiateViewController(withIdentifier: "MenuPrincipal")
        self.present( vc , animated: true, completion: nil)
    }

    @IBAction func ambulancia(_ sender: Any) {
        UIApplication.shared.openURL(URL(string: "tel://055")!)
    }
    
    @IBAction func policia(_ sender: Any) {
        UIApplication.shared.openURL(URL(string: "tel://066")!)
    }
    
    @IBAction func bomberos(_ sender: Any) {
        UIApplication.shared.openURL(URL(string: "tel://080")!)
    }
    
    @IBAction func taxisSitio(_ sender: Any) {
        UIApplication.shared.openURL(URL(string: "tel://21137431")!)
    }
    
    @IBAction func taxisPuebla(_ sender: Any) {
        UIApplication.shared.openURL(URL(string: "tel://21542222")!)
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
