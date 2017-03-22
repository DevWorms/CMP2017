//
//  BuscadorViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 13/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class BuscadorViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var searchBar: UISearchBar!
    //var abcArray = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","Ñ","O","P","Q","R","S","T","U","V","W","X","Y","Z", "#"]
    //http://shrikar.com/swift-ios-tutorial-uisearchbar-and-uisearchbardelegate/
    
    var abcArray = [String]()
    
    var expositores = [[String : Any]]()
    var expositoresArraySucio = [[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[]]
    var idExpositorSucio = [[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[]]
    // variables finales sin basura
    var expositoresArray = [[String]]()
    var idExpositores = [[Int]]()
    var expositorAmostrar = [String : Any]()
    
    var searchActive : Bool = false
    var filtered:[String] = []
    var filteredProvisional:[String] = []
    var filteredID:[Int] = []

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        let nav = self.navigationController?.navigationBar
        nav?.tintColor = UIColor.white
        nav!.setBackgroundImage(navBackgroundImage, for:.default)
        
        let swipeDown = UISwipeGestureRecognizer(target: self, action: #selector(self.swipeKeyBoard(sender:)))
        swipeDown.direction = UISwipeGestureRecognizerDirection.down
        self.view.addGestureRecognizer(swipeDown)
        
        searchBar.delegate = self
        
        let apiKey = UserDefaults.standard.value(forKey: "api_key")
        let userID = UserDefaults.standard.value(forKey: "user_id")
        
        let strUrl = "http://cmp.devworms.com/api/expositor/order/name/\(userID!)/\(apiKey!)"
        print(strUrl)
        
        URLSession.shared.dataTask(with: URL(string: strUrl)!, completionHandler: parseJsonExpositores).resume()
     
    }
    
    func parseJsonExpositores(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    DispatchQueue.main.async {
                        
                        if self.expositores.count > 0 {
                            self.expositores.removeAll()
                        }
                        
                        if let jsonResult = json as? [String: Any] {
                            for programa in jsonResult["expositores"] as! [[String:Any]] {
                                self.expositores.append(programa)
                            }
                            
                            //para titulos
                            for letra in self.expositores {
                                
                                if (letra["nombre"] as! String).hasPrefix("A") { // true
                                    if !self.abcArray.contains("A") {
                                        self.abcArray.append("A")
                                    }
                                    self.expositoresArraySucio[0].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[0].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("B") { // true
                                    if !self.abcArray.contains("B") {
                                        self.abcArray.append("B")
                                    }
                                    self.expositoresArraySucio[1].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[1].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("C") { // true
                                    if !self.abcArray.contains("C") {
                                        self.abcArray.append("C")
                                    }
                                    self.expositoresArraySucio[2].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[2].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("D") { // true
                                    if !self.abcArray.contains("D") {
                                        self.abcArray.append("D")
                                    }
                                    self.expositoresArraySucio[3].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[3].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("E") { // true
                                    if !self.abcArray.contains("E") {
                                        self.abcArray.append("E")
                                    }
                                    self.expositoresArraySucio[4].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[4].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("F") { // true
                                    if !self.abcArray.contains("F") {
                                        self.abcArray.append("F")
                                    }
                                    self.expositoresArraySucio[5].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[5].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("G") { // true
                                    if !self.abcArray.contains("G") {
                                        self.abcArray.append("G")
                                    }
                                    self.expositoresArraySucio[6].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[6].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("H") { // true
                                    if !self.abcArray.contains("H") {
                                        self.abcArray.append("H")
                                    }
                                    self.expositoresArraySucio[7].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[7].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("I") { // true
                                    if !self.abcArray.contains("I") {
                                        self.abcArray.append("I")
                                    }
                                    self.expositoresArraySucio[8].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[8].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("J") { // true
                                    if !self.abcArray.contains("J") {
                                        self.abcArray.append("J")
                                    }
                                    self.expositoresArraySucio[9].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[9].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("K") { // true
                                    if !self.abcArray.contains("K") {
                                        self.abcArray.append("K")
                                    }
                                    self.expositoresArraySucio[10].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[10].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("L") { // true
                                    if !self.abcArray.contains("L") {
                                        self.abcArray.append("L")
                                    }
                                    self.expositoresArraySucio[11].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[11].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("M") { // true
                                    if !self.abcArray.contains("M") {
                                        self.abcArray.append("M")
                                    }
                                    self.expositoresArraySucio[12].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[12].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("N") { // true
                                    if !self.abcArray.contains("N") {
                                        self.abcArray.append("N")
                                    }
                                    self.expositoresArraySucio[13].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[13].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("Ñ") { // true
                                    if !self.abcArray.contains("Ñ") {
                                        self.abcArray.append("Ñ")
                                    }
                                    self.expositoresArraySucio[14].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[14].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("O") { // true
                                    if !self.abcArray.contains("O") {
                                        self.abcArray.append("O")
                                    }
                                    self.expositoresArraySucio[15].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[15].append(letra["id"] as! Int)
                                } else if (letra["nombre"] as! String).hasPrefix("P") { // true
                                    if !self.abcArray.contains("P") {
                                        self.abcArray.append("P")
                                    }
                                    self.expositoresArraySucio[16].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[16].append(letra["id"] as! Int) 
                                } else if (letra["nombre"] as! String).hasPrefix("Q") { // true
                                    if !self.abcArray.contains("Q") {
                                        self.abcArray.append("Q")
                                    }
                                    self.expositoresArraySucio[17].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[17].append(letra["id"] as! Int) 
                                } else if (letra["nombre"] as! String).hasPrefix("R") { // true
                                    if !self.abcArray.contains("R") {
                                        self.abcArray.append("R")
                                    }
                                    self.expositoresArraySucio[18].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[18].append(letra["id"] as! Int) 
                                } else if (letra["nombre"] as! String).hasPrefix("S") { // true
                                    if !self.abcArray.contains("S") {
                                        self.abcArray.append("S")
                                    }
                                    self.expositoresArraySucio[19].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[19].append(letra["id"] as! Int) 
                                } else if (letra["nombre"] as! String).hasPrefix("T") { // true
                                    if !self.abcArray.contains("T") {
                                        self.abcArray.append("T")
                                    }
                                    self.expositoresArraySucio[20].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[20].append(letra["id"] as! Int) 
                                } else if (letra["nombre"] as! String).hasPrefix("U") { // true
                                    if !self.abcArray.contains("U") {
                                        self.abcArray.append("U")
                                    }
                                    self.expositoresArraySucio[21].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[21].append(letra["id"] as! Int) 
                                } else if (letra["nombre"] as! String).hasPrefix("V") { // true
                                    if !self.abcArray.contains("V") {
                                        self.abcArray.append("V")
                                    }
                                    self.expositoresArraySucio[22].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[22].append(letra["id"] as! Int) 
                                } else if (letra["nombre"] as! String).hasPrefix("W") { // true
                                    if !self.abcArray.contains("W") {
                                        self.abcArray.append("W")
                                    }
                                    self.expositoresArraySucio[23].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[23].append(letra["id"] as! Int) 
                                } else if (letra["nombre"] as! String).hasPrefix("X") { // true
                                    if !self.abcArray.contains("X") {
                                        self.abcArray.append("X")
                                    }
                                    self.expositoresArraySucio[24].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[24].append(letra["id"] as! Int) 
                                } else if (letra["nombre"] as! String).hasPrefix("Y") { // true
                                    if !self.abcArray.contains("Y") {
                                        self.abcArray.append("Y")
                                    }
                                    self.expositoresArraySucio[25].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[25].append(letra["id"] as! Int) 
                                } else if (letra["nombre"] as! String).hasPrefix("Z") { // true
                                    if !self.abcArray.contains("Z") {
                                        self.abcArray.append("Z")
                                    }
                                    self.expositoresArraySucio[26].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[26].append(letra["id"] as! Int) 
                                } else { // true
                                    if !self.abcArray.contains("#") {
                                        self.abcArray.append("#")
                                    }
                                    self.expositoresArraySucio[27].append(letra["nombre"] as! String)
                                    self.idExpositorSucio[27].append(letra["id"] as! Int) 
                                }

                            }
                            
                            self.llenarArraysLimpios()
                            
                            self.abcArray = self.abcArray.sorted { $0.localizedCaseInsensitiveCompare($1) == ComparisonResult.orderedDescending }
                            
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

    func llenarArraysLimpios() {
        if self.expositoresArraySucio[0].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[0] as! [String])
            self.idExpositores.append(self.idExpositorSucio[0] as! [Int])
        }
        if self.expositoresArraySucio[1].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[1] as! [String])
            self.idExpositores.append(self.idExpositorSucio[1] as! [Int])
        }
        if self.expositoresArraySucio[2].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[2] as! [String])
            self.idExpositores.append(self.idExpositorSucio[2] as! [Int])
        }
        if self.expositoresArraySucio[3].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[3] as! [String])
            self.idExpositores.append(self.idExpositorSucio[3] as! [Int])
        }
        if self.expositoresArraySucio[4].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[4] as! [String])
            self.idExpositores.append(self.idExpositorSucio[4] as! [Int])
        }
        if self.expositoresArraySucio[5].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[5] as! [String])
            self.idExpositores.append(self.idExpositorSucio[5] as! [Int])
        }
        if self.expositoresArraySucio[6].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[6] as! [String])
            self.idExpositores.append(self.idExpositorSucio[6] as! [Int])
        }
        if self.expositoresArraySucio[7].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[7] as! [String])
            self.idExpositores.append(self.idExpositorSucio[7] as! [Int])
        }
        if self.expositoresArraySucio[8].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[8] as! [String])
            self.idExpositores.append(self.idExpositorSucio[8] as! [Int])
        }
        if self.expositoresArraySucio[9].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[9] as! [String])
            self.idExpositores.append(self.idExpositorSucio[9] as! [Int])
        }
        if self.expositoresArraySucio[10].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[10] as! [String])
            self.idExpositores.append(self.idExpositorSucio[10] as! [Int])
        }
        if self.expositoresArraySucio[11].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[11] as! [String])
            self.idExpositores.append(self.idExpositorSucio[11] as! [Int])
        }
        if self.expositoresArraySucio[12].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[12] as! [String])
            self.idExpositores.append(self.idExpositorSucio[12] as! [Int])
        }
        if self.expositoresArraySucio[13].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[13] as! [String])
            self.idExpositores.append(self.idExpositorSucio[13] as! [Int])
        }
        if self.expositoresArraySucio[14].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[14] as! [String])
            self.idExpositores.append(self.idExpositorSucio[14] as! [Int])
        }
        if self.expositoresArraySucio[15].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[15] as! [String])
            self.idExpositores.append(self.idExpositorSucio[15] as! [Int])
        }
        if self.expositoresArraySucio[16].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[16] as! [String])
            self.idExpositores.append(self.idExpositorSucio[16] as! [Int])
        }
        if self.expositoresArraySucio[17].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[17] as! [String])
            self.idExpositores.append(self.idExpositorSucio[17] as! [Int])
        }
        if self.expositoresArraySucio[18].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[18] as! [String])
            self.idExpositores.append(self.idExpositorSucio[18] as! [Int])
        }
        if self.expositoresArraySucio[19].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[19] as! [String])
            self.idExpositores.append(self.idExpositorSucio[19] as! [Int])
        }
        if self.expositoresArraySucio[20].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[20] as! [String])
            self.idExpositores.append(self.idExpositorSucio[20] as! [Int])
        }
        if self.expositoresArraySucio[21].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[21] as! [String])
            self.idExpositores.append(self.idExpositorSucio[21] as! [Int])
        }
        if self.expositoresArraySucio[22].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[22] as! [String])
            self.idExpositores.append(self.idExpositorSucio[22] as! [Int])
        }
        if self.expositoresArraySucio[23].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[23] as! [String])
            self.idExpositores.append(self.idExpositorSucio[23] as! [Int])
        }
        if self.expositoresArraySucio[24].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[24] as! [String])
            self.idExpositores.append(self.idExpositorSucio[24] as! [Int])
        }
        if self.expositoresArraySucio[25].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[25] as! [String])
            self.idExpositores.append(self.idExpositorSucio[25] as! [Int])
        }
        if self.expositoresArraySucio[26].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[26] as! [String])
            self.idExpositores.append(self.idExpositorSucio[26] as! [Int])
        }
        if self.expositoresArraySucio[27].count != 0 {
            self.expositoresArray.append(self.expositoresArraySucio[27] as! [String])
            self.idExpositores.append(self.idExpositorSucio[27] as! [Int])
        }
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
        
        self.tableView.reloadData()
    }
 
    // MARK: - TableView
 
    func numberOfSections(in tableView: UITableView) -> Int {
        if(searchActive) {
            return 1
        }
        return abcArray.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(searchActive) {
            return filtered.count
        }
        return self.expositoresArray[section].count
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if(searchActive) {
            return nil
        }
        return abcArray[section]
    }
    
    func sectionIndexTitles(for tableView: UITableView) -> [String]? {
        return abcArray
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        
        if(searchActive){
            cell.textLabel?.text = filtered[indexPath.row]
        } else {
            cell.textLabel?.text = self.expositoresArray[indexPath.section][indexPath.row]
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if(searchActive){
            let cellText = tableView.cellForRow(at: indexPath)?.textLabel?.text
            
            for expositor in self.expositores {
                if expositor["id"] as! Int == filteredID[filteredProvisional.index(of: cellText!)!] {
                    expositorAmostrar = expositor
                    self.performSegue(withIdentifier: "detalle", sender: nil)
                }
            }
            
        } else {
            for expositor in self.expositores {
                if expositor["id"] as! Int == idExpositores[indexPath.section][indexPath.row] {
                    expositorAmostrar = expositor
                    self.performSegue(withIdentifier: "detalle", sender: nil)
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
            (segue.destination as! DetalleViewController).seccion = 2
            (segue.destination as! DetalleViewController).detalle = self.expositorAmostrar
        }
    }

}
