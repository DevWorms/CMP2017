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
    // 2 Expositores
    // 3 acompañantes
    // 4 deportivos
    var seccion = 0
    
    var diaPrograma = ""
    var tipoPrograma = ""
    
    var datosGlobal = [[String : Any]]()
    
    var datos = [[String : Any]]()
    var imgs = [Any?]()
    var fechas = [String]()
    var datoXfecha = [[], [], [], [], [], []]
    var idXdato = [[], [], [], [], [], []]
    // variables finales sin basura
    var datoFecha = [[String]]()
    var idDato = [[Int]]()
    
    var datoAmostrar = [String : Any]()
    var imgAmostrar: Any?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
  
        
            switch self.seccion {
            case 1:
                
                self.datosGlobal = CoreDataHelper.fetchData(entityName: "Programas", keyName: "programa")!
                self.imgs = CoreDataHelper.fetchItem(entityName: "Programas", keyName: "imgPrograma")!
                
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
                
                
            case 3:
                self.datosGlobal = CoreDataHelper.fetchData(entityName: "Acompanantes", keyName: "acompanante")!
                self.datos = CoreDataHelper.fetchData(entityName: "Acompanantes", keyName: "acompanante")!
                self.imgs = CoreDataHelper.fetchItem(entityName: "Acompanantes", keyName: "imgAcompanante")!
                
            case 4:
                self.datosGlobal = CoreDataHelper.fetchData(entityName: "Deportivos", keyName: "evento")!
                self.datos = CoreDataHelper.fetchData(entityName: "Deportivos", keyName: "evento")!
                self.imgs = CoreDataHelper.fetchItem(entityName: "Deportivos", keyName: "imgDeportivo")!
                
            default: break
            }
            
            //para titulos
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

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        switch self.seccion {
        case 1,3,4:
            return fechas.count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch self.seccion {
        case 1,3,4:
            return self.datoFecha[section].count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        
        var diaMostrar = ""
        
        switch self.seccion {
        case 1,3,4:
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
        default: break
        }
        
        return diaMostrar
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        
        switch self.seccion {
        case 1,3,4:
            cell.textLabel?.text = self.datoFecha[indexPath.section][indexPath.row]
        default: break
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        switch self.seccion {
        case 1,3,4:
            for (index, dato) in self.datosGlobal.enumerated() {
                if dato["id"] as! Int == idDato[indexPath.section][indexPath.row] {
                    self.datoAmostrar = dato
                    
                    self.imgAmostrar = imgs[index]
                    
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
            (segue.destination as! DetalleViewController).detalle = self.datoAmostrar
            (segue.destination as! DetalleViewController).imgData = self.imgAmostrar
        }
    }

}
