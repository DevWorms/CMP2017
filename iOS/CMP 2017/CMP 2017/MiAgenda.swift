//
//  MiAgenda.swift
//  CMP 2017
//
//  Created by mac on 21/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit


class MiAgenda: UITableViewController {
    @IBOutlet weak var menu: UIBarButtonItem!
    
 
    
    @IBOutlet weak var scrollview: UIScrollView!
    
    var datos = [[String : Any]]()
    var imgs = [Any?]()
    var eventos = [[String : Any]]()
    
    // variables finales sin basura
    var eventosArray = [String]()
    var eventosHorarios = [String]()
    var eventoContador = [Int]()
    var eventosImagenes = [Any?] ()
    var idExpositores = [[Int]]()
    var evetontosAmostrar = [Any?] ()
    var horariosArray = [String]()
    var diaSelect = "2017-06-05"
    var idEventos = [Int]()
    var colorCell = 0
    
    var expositorAmostrar = [String : Any]()
    var imgAmostrar: Any?
   
    @IBOutlet weak var SegmentControlDias: UISegmentedControl!
    
    override func viewWillLayoutSubviews(){
        super.viewWillLayoutSubviews()
        
        self.scrollview.contentSize = CGSize(width: SegmentControlDias.bounds.width + 60, height:SegmentControlDias.bounds.height)
        }
      
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        let nav = self.navigationController?.navigationBar
        nav?.tintColor = UIColor.white
        nav?.setBackgroundImage(navBackgroundImage, for:.default)
        
        nav?.titleTextAttributes = [NSForegroundColorAttributeName: #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)]
        nav?.topItem?.title = UserDefaults.standard.value(forKey: "name") as! String?
        self.Buscardatos()
       

        
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
       
            return self.eventosArray.count
       
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! MiAgendaTableViewCell
        
      
        
            cell.nombreEvento.text = self.eventosArray[indexPath.row] as String
            cell.horarioEvento.text = self.eventosHorarios[indexPath.row] as String
        
        for (indexF, evento) in self.datos.enumerated() {
            if evento["id"] as! Int == idEventos[indexPath.row]{
                if let img = imgs[indexF] as? Data {
                    cell.imageViewFoto.image = UIImage(data:img)
                    
                    cell.imageViewFoto.layer.cornerRadius = cell.imageViewFoto.frame.height/2
                    cell.imageViewFoto.clipsToBounds = true
                    
                }else{
                cell.imageViewFoto.image = nil
                    
                }
                
            }
        }

        if colorCell == 0{
            cell.backgroundColor = UIColor(red:0.85, green:0.93, blue:1.00, alpha:1.0)
            self.colorCell = self.colorCell + 1
        }else if colorCell == 1{
              cell.backgroundColor = UIColor(red:0.85, green:1.00, blue:1.00, alpha:1.0)
            self.colorCell = self.colorCell + 1
        }else if colorCell == 2{
             cell.backgroundColor = UIColor(red:0.93, green:1.00, blue:0.85, alpha:1.0)
            self.colorCell = 0
        }
    
      
        
        
        return cell
    }
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
  
        
        for (indexF, evento) in self.datos.enumerated() {
            if evento["id"] as! Int == idEventos[indexPath.row]{
                self.imgAmostrar = imgs[indexF]
                self.expositorAmostrar = evento
                self.performSegue(withIdentifier: "detalle", sender: nil)
                
            }

       
        }
    }

  
    @IBAction func indexChanged(_ sender: UISegmentedControl) {
        
        switch SegmentControlDias.selectedSegmentIndex
        {
        case 0:
            self.diaSelect = "2017-06-05"
            self.colorCell = 0
            self.Buscardatos()
        case 1:
            self.diaSelect = "2017-06-06"
            self.colorCell = 0
            self.Buscardatos()
        case 2:
            self.diaSelect = "2017-06-07"
            self.colorCell = 0
            self.Buscardatos()
        case 3:
            self.diaSelect = "2017-06-08"
            self.colorCell = 0
            self.Buscardatos()
        case 4:
            self.diaSelect = "2017-06-09"
            self.colorCell = 0
            self.Buscardatos()
        case 5:
            self.diaSelect = "2017-06-10"
            self.colorCell = 0
            self.Buscardatos()
        default:
            break;
        }

    }
    
    func Buscardatos() {
        self.datos = CoreDataHelper.fetchData(entityName: "MiAgendaT", keyName: "miAgenda")!
        if datos.count>0 {
            
            eventos.removeAll()
            imgs.removeAll()
            eventosArray.removeAll()
            eventosHorarios.removeAll()
            idEventos.removeAll()
            
            self.imgs = CoreDataHelper.fetchItem(entityName: "MiAgendaT", keyName: "imgMiAgenda")!
            
            self.eventos = self.datos.sorted(by: { (a,b) in (a["hora_inicio"] as! String) < (b["hora_inicio"] as! String) || (a["hora_fin"] as! String) < (b["hora_fin"] as! String)})
            
            
            
        
            for  result in self.eventos {
                if result["fecha"] as! String == diaSelect {
                    
                    self.eventosArray.append( result["nombre"] as! String )
                    self.eventosHorarios.append("\(result["hora_inicio"]!) - \(result["hora_fin"]!)" )
                   
                    self.idEventos.append(result["id"] as! Int)
                  
                    
                    
                }
                
            }
        
        
     
       

            self.tableView.reloadData()
        }
       
    }
    
   
    @IBAction func menu(_ sender: Any) {
        let vc = storyboard!.instantiateViewController(withIdentifier: "MenuPrincipal")
        self.present( vc , animated: true, completion: nil)
    }
        
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
            if segue.identifier == "detalle" {
                (segue.destination as! DetalleViewController).seccion = 1
                (segue.destination as! DetalleViewController).detalle = self.expositorAmostrar
                (segue.destination as! DetalleViewController).imgData = self.imgAmostrar
            }
        }
    
    // para cuadrar las imagenes
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        
        return 133//pantallaSizeHeight(row: indexPath.row);//Choose your custom row height
    }
    /*
    func pantallaSizeHeight(row: Int)->CGFloat!
    {
        var strPantalla = 143 //iphone 5
        
        if(row == 0){
            strPantalla = 163
        }
        
        if (UIDevice.current.userInterfaceIdiom == .pad)
        {
            if(row == 0){
                strPantalla = 446
            }
            else{
                strPantalla = 350
            }
            
        }
        else
        {
            
            if UIScreen.main.bounds.size.width > 320 {
                if UIScreen.main.scale == 3 { //iphone 6 plus
                    
                    strPantalla = 143
                }
                else{
                    strPantalla = 143 //iphone 6
                }
            }
        }
        return CGFloat(strPantalla)
    }
    */

    
}
