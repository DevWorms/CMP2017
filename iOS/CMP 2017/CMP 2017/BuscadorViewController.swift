//
//  BuscadorViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 13/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class BuscadorViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate {
    
    @IBOutlet weak var btnNume: UIButton!
    @IBOutlet weak var btnAlfa: UIButton!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var searchBar: UISearchBar!
    //var abcArray = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","Ñ","O","P","Q","R","S","T","U","V","W","X","Y","Z", "#"]
    //http://shrikar.com/swift-ios-tutorial-uisearchbar-and-uisearchbardelegate/
    
    var abcArray = [String]()
    
    var datos = [[String : Any]]()
    
    var expositores = [[String : Any]]()
    
    // variables finales sin basura
    var expositoresArray = [[String]]()
    var idExpositores = [[Int]]()
    var expositorAmostrar = [String : Any]()
    
    var searchActive : Bool = false
    var filtered:[String] = []
    var filteredProvisional:[String] = []
    var filteredID:[Int] = []
    
    var imgs = [Any?]()
    var imgAmostrar: Any?
    
    var diaPrograma = ""
    var tipoPrograma = ""
    var seccion = 2
    var alphabet = true
    
    ///programa
    
    var datosGlobal = [[String : Any]]()
   
    var fechas = [String]()
    var datoXfecha = [[], [], [], [], [], []]
    var idXdato = [[], [], [], [], [], []]
    // variables finales sin basura
    var datoFecha = [[String]]()
    var idDato = [[Int]]()
    
    var datoAmostrar = [String : Any]()


    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        let nav = self.navigationController?.navigationBar
        nav?.tintColor = UIColor.white
        nav?.setBackgroundImage(navBackgroundImage, for:.default)
        
        nav?.titleTextAttributes = [NSForegroundColorAttributeName: #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)]
        nav?.topItem?.title = UserDefaults.standard.value(forKey: "name") as! String?
        if self.seccion == 1{
            self.btnAlfa.isHidden = true
            self.btnNume.isHidden = true
            
        }
        let swipeDown = UISwipeGestureRecognizer(target: self, action: #selector(self.swipeKeyBoard(sender:)))
        swipeDown.direction = UISwipeGestureRecognizerDirection.down
        self.view.addGestureRecognizer(swipeDown)
        
        searchBar.delegate = self
     
        self.alfabeticamente(alph: true)
     
    }
    
    @IBAction func alfabetoAction(_ sender: Any) {
        self.alfabeticamente(alph: true)
    }
    
    @IBAction func standAction(_ sender: Any) {
        self.alfabeticamente(alph: false)
    }
    
    
    func alfabeticamente(alph: Bool) {
        
        /*
         let arr = [ ["hola": 2, "hola2" : "mm1"], ["hola": 3, "hola2" : "mm2"], ["hola": 1, "hola2" : "mm0"] ]
         
         print(arr.sorted(by: { (a,b) in (a["hola"] as! Int) < (b["hola"] as! Int) }))
         print(arr.sorted(by: { (a,b) in (a["hola2"] as! String) < (b["hola2"] as! String) }))
         */
        
        if self.expositores.count > 0 {
            self.expositores.removeAll()
            self.abcArray.removeAll()
            self.idExpositores.removeAll()
            self.expositoresArray.removeAll()
            self.expositorAmostrar.removeAll()
            self.datos.removeAll()
        }
        
        if alph {
            
            alphabet = true
            
            if self.seccion == 5 { // patrocinadores
                self.datos = CoreDataHelper.fetchData(entityName: "Patrocinadores", keyName: "patrocinador")!
                self.imgs = CoreDataHelper.fetchItem(entityName: "Patrocinadores", keyName: "imgPatrocinador")!
                self.expositores = self.datos.sorted(by: { (a,b) in (a["nombre"] as! String) < (b["nombre"] as! String) })
                
                
            }else if self.seccion == 6{
                self.datos = CoreDataHelper.fetchData(entityName: "MisExpositores", keyName: "misExpositores")!
                  self.imgs = CoreDataHelper.fetchItem(entityName: "MisExpositores", keyName: "imgMisExpositores")!
                self.expositores = self.datos.sorted(by: { (a,b) in (a["nombre"] as! String) < (b["nombre"] as! String) })

            }else if self.seccion == 1{
                
                self.datosGlobal = CoreDataHelper.fetchData(entityName: "Programas", keyName: "programa")!
                self.imgs = CoreDataHelper.fetchItem(entityName: "Programas", keyName: "imgPrograma")!
                self.datosGlobal = self.datosGlobal.sorted(by: { (a,b) in (a["fecha"] as! String) < (b["fecha"] as! String) })
                
                if diaPrograma == "" && tipoPrograma == "" { //  Todos
                   self.datos = self.datosGlobal
                    
                    
                } else { // busqueda
                    
                    if diaPrograma != "" && tipoPrograma != "" { // por dia y tipo
                        for item in self.datosGlobal {
                            if item["fecha"] as! String == diaPrograma {
                                if let a = item["categoria"] as? [String:Any] {
                                    if a["id"] as! Int == Int(tipoPrograma)! {
                                        self.datos.append(item)
                                    }
                                    
                                }
                            }
                        }
                        
                    } else if diaPrograma != "" && tipoPrograma == "" { // por dia
                        for item in self.datosGlobal {
                            if item["fecha"] as! String == diaPrograma {
                                self.datos.append(item)
                            }
                        }
                        
                    } else if diaPrograma == "" && tipoPrograma != "" { // por tipo
                        for item in self.datosGlobal {
                            if let a = item["categoria"] as? [String:Any] {
                                if a["id"] as! Int == Int(tipoPrograma)! {
                                    self.datos.append(item)
                                }
                                
                            }
                        }
                        
                    }
                }

                    
                
            }else {
                self.datos = CoreDataHelper.fetchData(entityName: "Expositores", keyName: "expositor")!
                self.imgs = CoreDataHelper.fetchItem(entityName: "Expositores", keyName: "imgExpositor")!
                self.expositores = self.datos.sorted(by: { (a,b) in (a["nombre"] as! String) < (b["nombre"] as! String) })
            }
            
            // llenar las secciones
            if self.seccion != 1{
                for letra in self.expositores {
                
                    if !self.abcArray.contains( String(describing: (letra["nombre"] as! String).characters.first!).uppercased() ) {
                        self.abcArray.append( String(describing: (letra["nombre"] as!  String).characters.first!).uppercased() )
                    }
                }
            }else{
            
                for date in self.datos {
                    if self.fechas.contains(date["fecha"] as! String) {
                    
                    } else {
                        self.fechas.append(date["fecha"] as! String)
                    }
                
                    if date["fecha"] as! String == "2017-06-05" {
                        self.datoXfecha[0].append(date["nombre"] as! String)
                        self.idXdato[0].append(date["id"] as! Int)
                    }else if date["fecha"] as! String == "2017-06-06" {
                        self.datoXfecha[1].append(date["nombre"] as! String)
                        self.idXdato[1].append(date["id"] as! Int)
                    }else if date["fecha"] as! String == "2017-06-07" {
                        self.datoXfecha[2].append(date["nombre"] as! String)
                        self.idXdato[2].append(date["id"] as! Int)
                    }else if date["fecha"] as! String == "2017-06-08" {
                        self.datoXfecha[3].append(date["nombre"] as! String)
                        self.idXdato[3].append(date["id"] as! Int)
                    }else if date["fecha"] as! String == "2017-06-09" {
                        self.datoXfecha[4].append(date["nombre"] as! String)
                        self.idXdato[4].append(date["id"] as! Int)
                    }else if date["fecha"] as! String == "2017-06-10" {
                        self.datoXfecha[5].append(date["nombre"] as! String)
                        self.idXdato[5].append(date["id"] as! Int)
                    }
                }
            
            // agregar los datos que no estan vacios
                if self.datoXfecha[0].count != 0 {
                    self.datoFecha.append(self.datoXfecha[0] as! [String])
                    self.idDato.append(self.idXdato[0] as! [Int])
                }
                if self.datoXfecha[1].count != 0 {
                    self.datoFecha.append(self.datoXfecha[1] as! [String])
                    self.idDato.append(self.idXdato[1] as! [Int])
                }
                if self.datoXfecha[2].count != 0 {
                    self.datoFecha.append(self.datoXfecha[2] as! [String])
                    self.idDato.append(self.idXdato[2] as! [Int])
                }
                if self.datoXfecha[3].count != 0 {
                    self.datoFecha.append(self.datoXfecha[3] as! [String])
                    self.idDato.append(self.idXdato[3] as! [Int])
                }
                if self.datoXfecha[4].count != 0 {
                    self.datoFecha.append(self.datoXfecha[4] as! [String])
                    self.idDato.append(self.idXdato[4] as! [Int])
                }
                if self.datoXfecha[5].count != 0 {
                    self.datoFecha.append(self.datoXfecha[5] as! [String])
                    self.idDato.append(self.idXdato[5] as! [Int])
                }
            
            //self.tableView.reloadData()
            
            }
            
            // llenar las rows
            
            var expositoresOrdText = [String]()
            var expositoresOrdId = [Int]()
            
            for letra in self.abcArray {
                for result in self.expositores {
                    
                    if (result["nombre"] as! String).uppercased().hasPrefix( letra) {
                        expositoresOrdText.append( result["nombre"] as! String )
                        expositoresOrdId.append( result["id"] as! Int )
                    }
                }
                
                self.idExpositores.append(expositoresOrdId)
                self.expositoresArray.append(expositoresOrdText)
                expositoresOrdId.removeAll()
                expositoresOrdText.removeAll()
            }
            
        } else {
            
            alphabet = false
            
            if self.seccion == 5 { // patrocinadores
                self.datos = CoreDataHelper.fetchData(entityName: "Patrocinadores", keyName: "patrocinador")!
                self.imgs = CoreDataHelper.fetchItem(entityName: "Patrocinadores", keyName: "imgPatrocinador")!
                self.expositores = self.datos.sorted(by: { (a,b) in (a["stand"] as! String) < (b["stand"] as! String) })
            }else if self.seccion == 6{
                self.datos = CoreDataHelper.fetchData(entityName: "MisExpositores", keyName: "misExpositores")!
                self.imgs = CoreDataHelper.fetchItem(entityName: "MisExpositores", keyName: "imgMisExpositores")!
                self.expositores = self.datos.sorted(by: { (a,b) in (a["stand"] as! String) < (b["stand"] as! String) })
                
            } else {
                self.datos = CoreDataHelper.fetchData(entityName: "Expositores", keyName: "expositor")!
                self.imgs = CoreDataHelper.fetchItem(entityName: "Expositores", keyName: "imgExpositor")!
                self.expositores = self.datos.sorted(by: { (a,b) in (a["stand"] as! String) < (b["stand"] as! String) })
            }
            
            // llenar las secciones
            for letra in self.expositores {
                
                if !self.abcArray.contains( letra["stand"] as! String ) {
                    self.abcArray.append( letra["stand"] as! String )
                }
            }
            
            // llenar las rows
            
            var expositoresOrdText = [String]()
            var expositoresOrdId = [Int]()
            
            for letra in self.abcArray {
                for result in self.expositores {
                    
                    if (result["stand"] as! String) ==  letra {
                        expositoresOrdText.append( result["nombre"] as! String )
                        expositoresOrdId.append( result["id"] as! Int )
                    }
                }
                
                self.idExpositores.append(expositoresOrdId)
                self.expositoresArray.append(expositoresOrdText)
                expositoresOrdId.removeAll()
                expositoresOrdText.removeAll()
            }
            
            for index in 0 ... self.abcArray.count-1 {
                self.abcArray[index] = "Stand " + self.abcArray[index]
            }
        }
        
        self.tableView.reloadData()
        
    }

    func swipeKeyBoard(sender:AnyObject) {
        //Baja el textField
        self.view.endEditing(true)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - SearchBar
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        searchActive = true;
    }
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        searchActive = false;
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchActive = false;
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchActive = false;
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        
        //junta los arrays[String] de adentro a uno solo
        if self.seccion == 1 {
            
            filtered = datoFecha.joined().filter { (text) in
                //print(text)
                return text.lowercased().contains(searchText.lowercased())
            }
            
            filteredProvisional = datoFecha.joined().filter({ (text) in
                return true
            })
            
            filteredID = idDato.joined().filter({ (Int) in
                return true
            })
            
            if(filtered.count == 0){
                searchActive = false;
            } else {
                searchActive = true;
            }

        }else{
            filtered = expositoresArray.joined().filter { (text) in
                //print(text)
                return text.lowercased().contains(searchText.lowercased())
            }
            
            filteredProvisional = expositoresArray.joined().filter({ (text) in
                return true
            })
            
            filteredID = idExpositores.joined().filter({ (Int) in
                return true
            })
            
            if(filtered.count == 0){
                searchActive = false;
            } else {
                searchActive = true;
            }
        }
        
        self.tableView.reloadData()
    }
 
    // MARK: - TableView
 
    func numberOfSections(in tableView: UITableView) -> Int {
        if(searchActive) {
            return 1
        }
        if self.seccion == 1{
            return fechas.count
        }
        
        return abcArray.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(searchActive) {
            return filtered.count
        }
        if self.seccion == 1{
             return self.datoFecha[section].count
        }
    
        return self.expositoresArray[section].count
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if(searchActive) {
            return nil
        }
        var diaMostrar = ""
        
        if self.seccion == 1 {
            switch fechas[section] {
            case "2017-06-05":
                diaMostrar = "Lunes 5 de Junio"
            case "2017-06-06":
                diaMostrar = "Martes 6 de Junio"
            case "2017-06-07":
                diaMostrar = "Miércoles 7 de Junio"
            case "2017-06-08":
                diaMostrar = "Jueves 8 de Junio"
            case "2017-06-09":
                diaMostrar = "Viernes 9 de Junio"
            case "2017-06-10":
                diaMostrar = "Sábado 10 de Junio"
            default:
                diaMostrar = ""
            }
         return diaMostrar
        }
    

        return abcArray[section]
    }
    
    func sectionIndexTitles(for tableView: UITableView) -> [String]? {
        if(searchActive) {
            return nil
        }
        return abcArray
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        
        if(searchActive){
            cell.textLabel?.text = filtered[indexPath.row]
        } else {
            if self.seccion == 1 {
            cell.textLabel?.text = self.datoFecha[indexPath.section][indexPath.row]

            }else{
            cell.textLabel?.text = self.expositoresArray[indexPath.section][indexPath.row]
            }
        }
        
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if(searchActive){
            let cellText = tableView.cellForRow(at: indexPath)?.textLabel?.text
            
            for (index, expositor) in self.datos.enumerated() {
                if expositor["id"] as! Int == filteredID[filteredProvisional.index(of: cellText!)!] {
                    expositorAmostrar = expositor
                    self.imgAmostrar = imgs[index]
                    self.performSegue(withIdentifier: "detalle", sender: nil)
                }
            }
            
        } else {
            if self.seccion != 1 {
            for (index, expositor) in self.datos.enumerated() {
                if expositor["id"] as! Int == idExpositores[indexPath.section][indexPath.row] {
                    expositorAmostrar = expositor
                    self.imgAmostrar = imgs[index]
                    self.performSegue(withIdentifier: "detalle", sender: nil)
                }
            }
            }else{
                for (index, dato) in self.datos.enumerated() {
                    if dato["id"] as! Int == idDato[indexPath.section][indexPath.row] {
                        self.expositorAmostrar = dato
                        
                        self.imgAmostrar = imgs[index]
                        
                        self.performSegue(withIdentifier: "detalle", sender: nil)
                    }
                }

            }
        }
    }
    
    @IBAction func menu(_ sender: Any) {
        let vc = storyboard!.instantiateViewController(withIdentifier: "MenuPrincipal")
        self.present( vc , animated: true, completion: nil)
    }

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "detalle" {
            (segue.destination as! DetalleViewController).seccion = self.seccion
            (segue.destination as! DetalleViewController).detalle = self.expositorAmostrar
            (segue.destination as! DetalleViewController).imgData = self.imgAmostrar
        }
    }

}
