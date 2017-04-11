//
//  TransportacionViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 12/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class TransportacionViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!
    
    var datos = [[String : Any]]()
    var imgs = [Any?]()
    var imgAmostrar: Any?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        self.datos = CoreDataHelper.fetchData(entityName: "Rutas", keyName: "ruta")!
        self.imgs = CoreDataHelper.fetchItem(entityName: "Rutas", keyName: "imgRuta")!
        
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! TransportacionTableViewCell
        
        cell.noRuta.text = "Ruta \(indexPath.row+1)"
        cell.titulo.text = datos[indexPath.row]["titulo"] as! String?
        cell.descripcion.text = datos[indexPath.row]["descripcion"] as! String?
        
        cell.btnCell.tag = indexPath.row
        cell.btnCell.addTarget(self, action: #selector(self.buttonClicked(_:)), for: UIControlEvents.touchUpInside)
        
        
        return cell
    }
    
    func buttonClicked(_ sender:UIButton) {
        
        self.imgAmostrar = imgs[sender.tag]
        
        self.performSegue(withIdentifier: "ruta", sender: nil)
    }

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        if segue.identifier == "ruta" {
            (segue.destination as! MapaSimpleViewController).tipoMapa = 3
            (segue.destination as! MapaSimpleViewController).imgData = self.imgAmostrar
        }
    }

}
