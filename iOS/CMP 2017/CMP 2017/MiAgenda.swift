//
//  MiAgenda.swift
//  CMP 2017
//
//  Created by mac on 21/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit


class MiAgenda: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
 
    
    var datos = [[String : Any]]()
    var imgs = [Any?]()
    
    @IBOutlet weak var tableView: UITableView!

    @IBOutlet weak var SegmentControlDias: UISegmentedControl!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        let nav = self.navigationController?.navigationBar
        nav?.tintColor = UIColor.white
        nav?.setBackgroundImage(navBackgroundImage, for:.default)
        
        nav?.titleTextAttributes = [NSForegroundColorAttributeName: #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)]
        nav?.topItem?.title = UserDefaults.standard.value(forKey: "name") as! String?
        
        
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return datos.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! MiAgendaTableViewCell
        
        
        
           // cell.textLabel?.text = self.datos[indexPath.row]
        
           // cell.textLabel?.text = self.datos[indexPath.section][indexPath.row]
        
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
 
        
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
    
    }
    
    func Buscardatos() {
        self.datos = CoreDataHelper.fetchData(entityName: "Patrocinadores", keyName: "patrocinador")!
       // self.imgs = CoreDataHelper.fetchItem(entityName: "Patrocinadores", keyName: "imgPatrocinador")!
    }
    
}
