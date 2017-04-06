//
//  MenuPrincipalViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 08/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class MenuPrincipalViewController: UIViewController {
    
    @IBOutlet weak var programaBtn: UIButton!
    @IBOutlet weak var acompañantesBtn: UIButton!
    @IBOutlet weak var patrocinadoresBtn: UIButton!
    @IBOutlet weak var transportacionBtn: UIButton!
    @IBOutlet weak var conoceBtn: UIButton!
    @IBOutlet weak var mapaBtn: UIButton!
    @IBOutlet weak var socialesBtn: UIButton!
    @IBOutlet weak var expositoresBtn: UIButton!
    @IBOutlet weak var banner: UIImageView!
    
    var datos = [[String : Any]]()
    var imgs = [String]()
    var imagenes = [UIImage]()
    var noImg = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let navBackgroundImage:UIImage! = UIImage(named: "10Pleca")
        
        let nav = self.navigationController?.navigationBar
        nav?.tintColor = UIColor.white
        nav!.setBackgroundImage(navBackgroundImage, for:.default)
        
        nav?.titleTextAttributes = [NSForegroundColorAttributeName: #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)]
        nav?.topItem?.title = UserDefaults.standard.value(forKey: "name") as! String?
        
        ///
        let invitado = UserDefaults.standard.value(forKey: "invitado") as! Bool
        
        if invitado {
            programaBtn.isEnabled = false
            acompañantesBtn.isEnabled = false
            transportacionBtn.isEnabled = false
            conoceBtn.isEnabled = false
            mapaBtn.isEnabled = false
            socialesBtn.isEnabled = false
            
            programaBtn.setImage(UIImage(named: "01Programa-1.png"), for: .normal)
            acompañantesBtn.setImage(UIImage(named: "03Eventos_Acompañantes-1.png"), for: .normal)
            transportacionBtn.setImage(UIImage(named: "07Transportacion-1.png"), for: .normal)
            conoceBtn.setImage(UIImage(named: "08Conoce_Puebla-1.png"), for: .normal)
            mapaBtn.setImage(UIImage(named: "06Mapas-1.png"), for: .normal)
            socialesBtn.setImage(UIImage(named: "04Eventos_Deportivos-1.png"), for: .normal)
        }
        
        cargarImg()
        
    }
    
    func cargarImg() {
        
        self.datos = CoreDataHelper.fetchData(entityName: "MisExpositores", keyName: "banner")!
        
        for img in self.datos {
            self.imgs.append(img["url"] as! String)
        }
        
        for img in imgs {
            // thread para que cargue en segundo plano la imagen
            DispatchQueue.global(qos: .userInitiated).async { // 1
                let data = try? Data(contentsOf: URL(string: img)!)
                
                DispatchQueue.main.async { // 2
                    self.imagenes.append( UIImage(data: data!)! )
                }
            }
        }
        
        Timer.scheduledTimer(timeInterval: 1.2, target: self, selector: #selector(self.update), userInfo: nil, repeats: true);
        
    }
    
    func update() {
        
        banner.image = imagenes[noImg]
        
        if noImg < imagenes.count - 1{
            noImg += 1
        } else {
            noImg = 0
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "acompañantes" {
            (segue.destination as! ResultadosViewController).seccion = 3
        } else if segue.identifier == "deportivos" {
            (segue.destination as! ResultadosViewController).seccion = 4
        } else if segue.identifier == "patrocinadores" {
            (segue.destination as! BuscadorViewController).seccion = 5
        }
    }

}
