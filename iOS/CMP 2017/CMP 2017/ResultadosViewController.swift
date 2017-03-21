//
//  ResultadosViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 17/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class ResultadosViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var tableView: UITableView!
    
    // 1 Programa
    var seccion = 0
    
    var diaPrograma = ""
    var tipoPrograma = ""
    
    var programas = [[String : Any]]()
    var fechas = [String]()
    var programaXfecha = [[], [], [], [], []]
    var idXprograma = [[], [], [], [], []]
    // variables finales sin basura
    var programaFecha = [[String]]()
    var idPrograma = [[Int]]()
    
    var programaAmostrar = [String : Any]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let apiKey = UserDefaults.standard.value(forKey: "api_key")
        let userID = UserDefaults.standard.value(forKey: "user_id")
        
        switch self.seccion {
        case 1:
            let parameterString = "user_id=\(userID!)&api_key=\(apiKey!)&categoria_id=\(tipoPrograma)&fecha=\(diaPrograma)"
            
            print(parameterString)
            
            let strUrl = "http://cmp.devworms.com/api/programa/search"
            
            if let httpBody = parameterString.data(using: String.Encoding.utf8) {
                var urlRequest = URLRequest(url: URL(string: strUrl)!)
                urlRequest.httpMethod = "POST"
                
                URLSession.shared.uploadTask(with: urlRequest, from: httpBody, completionHandler: parseJsonPrograma).resume()
            } else {
                print("Error de codificación de caracteres.")
            }
        default: break
        }
    }
    
    func parseJsonPrograma(data: Data?, urlResponse: URLResponse?, error: Error?) {
        if error != nil {
            print(error!)
        } else if urlResponse != nil {
            if (urlResponse as! HTTPURLResponse).statusCode == 200 {
                if let json = try? JSONSerialization.jsonObject(with: data!, options: []) {
                    //print(json)
                    
                    DispatchQueue.main.async {
                        
                        if self.programas.count > 0 {
                            self.programas.removeAll()
                        }
                        
                        if let jsonResult = json as? [String: Any] {
                            for programa in jsonResult["programas"] as! [[String:Any]] {
                                self.programas.append(programa)
                            }
                            
                            //para titulos
                            for date in self.programas {
                                if self.fechas.contains(date["fecha"] as! String) {
                                    
                                } else {
                                    self.fechas.append(date["fecha"] as! String)
                                }
                                
                                if date["fecha"] as! String == "2017-06-05" {
                                    self.programaXfecha[0].append(date["nombre"] as! String)
                                    self.idXprograma[0].append(date["id"] as! Int)
                                }else if date["fecha"] as! String == "2017-06-06" {
                                    self.programaXfecha[1].append(date["nombre"] as! String)
                                    self.idXprograma[1].append(date["id"] as! Int)
                                }else if date["fecha"] as! String == "2017-06-07" {
                                    self.programaXfecha[2].append(date["nombre"] as! String)
                                    self.idXprograma[2].append(date["id"] as! Int)
                                }else if date["fecha"] as! String == "2017-06-08" {
                                    self.programaXfecha[3].append(date["nombre"] as! String)
                                    self.idXprograma[3].append(date["id"] as! Int)
                                }else if date["fecha"] as! String == "2017-06-09" {
                                    self.programaXfecha[4].append(date["nombre"] as! String)
                                    self.idXprograma[4].append(date["id"] as! Int)
                                }
                            }
                            
                            // agregar los datos que no estan vacios
                            if self.programaXfecha[0].count != 0 {
                                self.programaFecha.append(self.programaXfecha[0] as! [String])
                                self.idPrograma.append(self.idXprograma[0] as! [Int])
                            }
                            if self.programaXfecha[1].count != 0 {
                                self.programaFecha.append(self.programaXfecha[1] as! [String])
                                self.idPrograma.append(self.idXprograma[1] as! [Int])
                            }
                            if self.programaXfecha[2].count != 0 {
                                self.programaFecha.append(self.programaXfecha[2] as! [String])
                                self.idPrograma.append(self.idXprograma[2] as! [Int])
                            }
                            if self.programaXfecha[3].count != 0 {
                                self.programaFecha.append(self.programaXfecha[3] as! [String])
                                self.idPrograma.append(self.idXprograma[3] as! [Int])
                            }
                            if self.programaXfecha[4].count != 0 {
                                self.programaFecha.append(self.programaXfecha[4] as! [String])
                                self.idPrograma.append(self.idXprograma[4] as! [Int])
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
    
    func numberOfSections(in tableView: UITableView) -> Int {
        switch self.seccion {
        case 1:
            return fechas.count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch self.seccion {
        case 1:
            return self.programaFecha[section].count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        
        var diaMostrar = ""
        
        switch self.seccion {
        case 1:
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
            default:
                diaMostrar = ""
            }
        default: break
        }
        
        return diaMostrar
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        
        switch self.seccion {
        case 1:
            cell.textLabel?.text = self.programaFecha[indexPath.section][indexPath.row]
        default: break
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        switch self.seccion {
        case 1:
            for program in self.programas {
                if program["id"] as! Int == idPrograma[indexPath.section][indexPath.row] {
                    programaAmostrar = program
                    self.performSegue(withIdentifier: "detalle", sender: nil)
                }
            }
        default: break
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
            (segue.destination as! DetalleViewController).detalle = self.programaAmostrar
        }
    }

}
