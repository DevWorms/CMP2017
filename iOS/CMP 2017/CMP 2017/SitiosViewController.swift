//
//  SitiosViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 13/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class SitiosViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!
    
    var datos = [[String : Any]]()
    var imgs = [Data]()
    var urlWeb = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        self.datos = CoreDataHelper.fetchData(entityName: "Sitios", keyName: "sitio")!
        
        self.imgs = CoreDataHelper.fetchItem(entityName: "Sitios", keyName: "imgSitio") as! [Data]
        
        //self.tableView.reloadData()
        
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! SitioTableViewCell
        
        cell.foto.image = UIImage(data: imgs[indexPath.row])
        
        cell.titulo.text = datos[indexPath.row]["titulo"] as! String?
        cell.descripcion.text = datos[indexPath.row]["descripcion"] as! String?
        
        cell.link.setTitle((datos[indexPath.row]["url"] as! String?), for: .normal)
        cell.link.tag = indexPath.row
        
       
        cell.link.addTarget(self, action: #selector(self.buttonClicked(_:)), for: UIControlEvents.touchUpInside)
        //touch btn
        
      

        let tap = UITapGestureRecognizer(target: self, action: #selector(self.labelClicked))
       cell.googlemaps.isUserInteractionEnabled = true
       cell.googlemaps.tag = indexPath.row
       cell.googlemaps.addGestureRecognizer(tap)
   
        
        
        
        
        return cell
    }
    func labelClicked(_ sender:UITapGestureRecognizer) {
     
        var tag = sender.view!.tag
        
        self.urlWeb = (datos[tag]["maps_link"] as! String?)!
        
        self.performSegue(withIdentifier: "sitio", sender: nil)
        
        
    }
    
    func buttonClicked(_ sender:UIButton) {
        
        
        self.urlWeb = (datos[sender.tag]["url"] as! String?)!
        
        self.performSegue(withIdentifier: "sitio", sender: nil)
        
        
    }
    // para cuadrar las imagenes
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return pantallaSizeHeight();//Choose your custom row height
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        if segue.identifier == "sitio" {
            (segue.destination as! MapaSimpleViewController).tipoMapa = 4
            (segue.destination as! MapaSimpleViewController).urlWeb =  self.urlWeb
        }
    }
    
    func pantallaSizeHeight()->CGFloat!
    {
        var strPantalla = 285 //iphone 5
        if (UIDevice.current.userInterfaceIdiom == .pad)
        {
            strPantalla = 471
        }
        else
        {
            
            if UIScreen.main.bounds.size.width > 320 {
                if UIScreen.main.scale == 3 { //iphone 6 plus
                    strPantalla = 285
                }
                else{
                    strPantalla = 285 //iphone 6
                }
            }
        }
        return CGFloat(strPantalla)
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
