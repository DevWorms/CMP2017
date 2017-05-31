//
//  DetalleViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 12/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class DetalleViewController: UIViewController {
    
    @IBOutlet weak var foto: UIImageView!
    @IBOutlet weak var titulo: UILabel!
    @IBOutlet weak var lugar: UILabel!
    @IBOutlet weak var recomendaciones: UILabel!
    @IBOutlet weak var horario: UILabel!
    
    @IBOutlet weak var lbl1: UILabel!
    @IBOutlet weak var lbl2: UILabel!
    @IBOutlet weak var lbl3: UILabel!
    
    @IBOutlet weak var btn1: UIButton!
    @IBOutlet weak var btn2: UIButton!
    @IBOutlet weak var btn3: UIButton!
    
    @IBOutlet weak var scrollView: UIScrollView!
    
    // 1 Programa
    // 2 Expositores
    // 3 acompañantes
    // 4 deportivo
    // 5 patrocinadores
    // 6 mis expositores
    var seccion = 0
    var detalle = [String: Any]()
    var imgData: Any?
    var misExpositores = [String: Any]()
    var miAgendaEventos = [String: Any]()
    var encontro = 0
    var estaMiagenda = 0
    var idMapa = 0
    var urlMapa = ""
    var tipoMapa = 0

    override func viewWillLayoutSubviews(){
        super.viewWillLayoutSubviews()
        self.scrollView.contentSize = CGSize(width: view.bounds.width, height: view.bounds.height - foto.bounds.height)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        if let img = self.imgData as? Data {
            self.foto.image = UIImage(data: img)
        }
        
        self.titulo.text = detalle["nombre"] as! String?
        
        switch self.seccion {
        case 1,3,4:
            btn3.isHidden = true
            
            self.lugar.text = detalle["lugar"] as! String?
            self.recomendaciones.text = detalle["recomendaciones"] as! String?
            self.horario.text = "\(detalle["hora_inicio"]!) - \(detalle["hora_fin"]!)"
            let miAgendaEventos = CoreDataHelper.fetchData(entityName: "MiAgendaT", keyName: "miAgenda")!
            if self.seccion == 1 {
             self.lbl3.text = "Más información:"
            }
            for ftdA in miAgendaEventos {
                print("ftdA: \(ftdA["id"] as! Int16)")
                print("detalle: \(detalle["id"] as! Int16)")
                
                
                if ftdA["id"] as! Int == detalle["id"] as! Int{
                    
                    estaMiagenda = 1
                    print("entro en mi agenda \(estaMiagenda)" )
                }
                if estaMiagenda > 0 {
                    btn2.imageView?.image = #imageLiteral(resourceName: "05Boton_Eliminar_Agenda")
                }else{
                    btn2.imageView?.image = #imageLiteral(resourceName: "05Boton_Agregar_Agenda")
                }
            }
            
        case 2,6:
            let misExpositores = CoreDataHelper.fetchData(entityName: "MisExpositores", keyName: "misExpositores")!
           
            
            for ftd in misExpositores {


                
                if ftd["id"] as! Int == detalle["id"] as! Int{
                    
                    encontro = 1
                   
                }
            }
            
            if encontro > 0 {
                btn2.imageView?.image = #imageLiteral(resourceName: "btneliminarexpo")
            }else{
                 btn2.imageView?.image = #imageLiteral(resourceName: "04Agregar_a_mis_expositores")
            }
        

            self.lbl1.isHidden = true
            self.horario.isHidden = true
            
            self.lbl2.text = "Contacto"
            self.lugar.text = (detalle["url"] as! String) + "\n" + (detalle["telefono"] as! String) + "\n" + (detalle["email"] as! String)
            self.lbl3.text = "Acerca de:"
            self.recomendaciones.text = detalle["acerca"] as! String?
            
        case 5:
            btn2.imageView?.image = #imageLiteral(resourceName: "05Boton_Presentacion_de_la_empresa")
            btn3.isHidden = true
            self.lbl1.isHidden = true
             self.horario.isHidden = true
            self.lbl2.text = "Contacto"
            self.lugar.text = (detalle["url"] as! String) + "\n" + (detalle["telefono"] as! String) + "\n" + (detalle["email"] as! String)
            self.lbl3.text = "Acerca de:"
            self.recomendaciones.text = detalle["acerca"] as! String?

        default:
            break
        }
        
        self.titulo.sizeToFit()
        
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func btnUno(_ sender: Any) {
        if  self.seccion == 3 || self.seccion == 4 {
            self.tipoMapa = 7
            self.urlMapa = detalle["maps_url"] as! String
            self.performSegue(withIdentifier: "webView", sender: nil)
        
        } else{
            
            self.idMapa = detalle["id"] as! Int
            if self.idMapa != nil {
                self.tipoMapa = 6
                self.urlMapa = "http://congreso.digital/public-map.php#\(self.idMapa)"
                self.performSegue(withIdentifier: "webView", sender: nil)
            }else{
            
            }
         
        
        }
        
        
        
    }
    
    @IBAction func btnDos(_ sender: Any) {
        if  self.seccion == 2 || self.seccion == 6  {
             let misExpositores = CoreDataHelper.fetchData(entityName: "MisExpositores", keyName: "misExpositores")!
            
            print("MisExpositores boton:  \(misExpositores.count)")
            
            if encontro > 0 {
                        print("Eliminar")
             CoreDataHelper.deleteObject(entityName: "MisExpositores", keyName: "misExpositores", id: detalle["id"] as! Int16)
                encontro = 0
                btn2.setImage(UIImage(named: "04Agregar_a_mis_expositores"), for: UIControlState.normal)
        

            } else {
                print("Agregar")
                 CoreDataHelper.saveData(entityName: "MisExpositores", data: detalle ,keyName: "misExpositores",dataImg: imgData, keyNameImg: "imgMisExpositores" )
                 encontro = 1
                 btn2.setImage(UIImage(named: "btneliminarexpo"), for: UIControlState.normal)
            }

           
            
        } else if self.seccion == 1 || self.seccion == 3 || self.seccion == 4 {
             print("Agregar a agenda")
            if estaMiagenda > 0 {
                print("Eliminar agenda")
                CoreDataHelper.deleteObject(entityName: "MiAgendaT", keyName: "miAgenda", id: detalle["id"] as! Int16)
                estaMiagenda = 0
                btn2.setImage(UIImage(named: "05Boton_Agregar_Agenda"), for: UIControlState.normal)
                let vc_alert = UIAlertController(title: "", message: "Este evento fue eliminado de tu agenda", preferredStyle: .alert)
                vc_alert.addAction(UIAlertAction(title: "OK",
                                                 style: UIAlertActionStyle.default,
                                                 handler: nil))
                self.present(vc_alert, animated: true, completion: nil)

                


            }else{
             CoreDataHelper.saveData(entityName: "MiAgendaT", data: detalle ,keyName: "miAgenda",dataImg: imgData, keyNameImg: "imgMiAgenda" )
                  print("Se agrego a la base")
                 estaMiagenda = 1
                let vc_alert = UIAlertController(title: "", message: "Este evento se agrego a tu agenda", preferredStyle: .alert)
                vc_alert.addAction(UIAlertAction(title: "OK",
                                                 style: UIAlertActionStyle.default,
                                                 handler: nil))
                self.present(vc_alert, animated: true, completion: nil)
                btn2.setImage(UIImage(named: "05Boton_Eliminar_Agenda"), for: UIControlState.normal)


            }
        } else if self.seccion == 5{
         
            self.idMapa = detalle["id"] as! Int
            self.tipoMapa = 6
            self.urlMapa = "n"
            if let a = detalle["pdf"] as? [String:Any] {
                
                self.urlMapa = a["url"] as! String
                print ( "url a mandar" + self.urlMapa )
                
            }
            if  self.urlMapa != "n"  {
                self.performSegue(withIdentifier: "webView", sender: nil)
            } else {
                print("no hay nada")
            }
        }
   
        
    }
    
    
    
    @IBAction func btnTres(_ sender: Any) {
        self.idMapa = detalle["id"] as! Int
        self.tipoMapa = 6
       self.urlMapa = "n"
        if let a = detalle["pdf"] as? [String:Any] {
            
               self.urlMapa = a["url"] as! String
            print ( "url a mandar" + self.urlMapa )
            
        }
        if  self.urlMapa != "n"  {
            self.performSegue(withIdentifier: "webView", sender: nil)
        } else {
            print("no hay nada")
        }
      
       
        
        
    }
    
    @IBAction func menu(_ sender: Any) {
        let vc = storyboard!.instantiateViewController(withIdentifier: "MenuPrincipal")
        self.present( vc , animated: true, completion: nil)
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
         if segue.identifier == "webView" {
            (segue.destination as! MapaSimpleViewController).idMapa = self.idMapa
            (segue.destination as! MapaSimpleViewController).urlWeb = self.urlMapa
            (segue.destination as! MapaSimpleViewController).tipoMapa = self.tipoMapa
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
