//
//  ProgramaViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 12/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class ProgramaViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {

    @IBOutlet weak var dia: UIPickerView!
    @IBOutlet weak var tipoEvento: UIPickerView!
    
    var pickerTipo = ["Todos", "Sesiones Técnicas", "Comidas / Conferencias", "E-Póster", "Otros"]
    var pickerDia = ["Todos", "Lunes 5 de Junio", "Martes 6 de Junio", "Miércoles 7 de Junio", "Jueves 8 de Junio", "Viernes 9 de Junio"]
    
    var selectedTipo = "Todos"
    var selectedDia = "Todos"
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        tipoEvento.delegate = self
        tipoEvento.dataSource = self
        dia.delegate = self
        dia.dataSource = self
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - UIPickerViewDelegate
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if pickerView == self.dia {
            return self.pickerDia.count
        } else  {
            return self.pickerTipo.count
        }
    }
    
    // MARK: - UIPickerViewDataSource
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView == self.dia {
            return self.pickerDia[row]
        } else  {
            return self.pickerTipo[row]
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if pickerView == self.dia {
            selectedDia = self.pickerDia[row]
        } else  {
            selectedTipo = self.pickerTipo[row]
        }
    }
    
    @IBAction func menu(_ sender: Any) {
        let vc = storyboard!.instantiateViewController(withIdentifier: "MenuPrincipal")
        self.present( vc , animated: true, completion: nil)
    }

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "programa" {
            
            var diaEnviar = ""
            
            switch selectedDia {
            case "Todos":
                diaEnviar = ""
            case "Lunes 5 de Junio":
                diaEnviar = "2017-06-05"
            case "Martes 6 de Junio":
                diaEnviar = "2017-06-06"
            case "Miércoles 7 de Junio":
                diaEnviar = "2017-06-07"
            case "Jueves 8 de Junio":
                diaEnviar = "2017-06-08"
            case "Viernes 9 de Junio":
                diaEnviar = "2017-06-09"
            default:
                diaEnviar = ""
            }
            
            var tipoEnviar = ""
            
            switch selectedTipo {
            case "Todos":
                tipoEnviar = ""
            case "Sesiones Técnicas":
                tipoEnviar = "1"
            case "Comidas / Conferencias":
                tipoEnviar = "3"
            case "E-Póster":
                tipoEnviar = "4"
            case "Otros":
                tipoEnviar = "0"
            default:
                tipoEnviar = ""
            }
            
            (segue.destination as! ResultadosViewController).seccion = 1
            (segue.destination as! ResultadosViewController).diaPrograma = diaEnviar
            (segue.destination as! ResultadosViewController).tipoPrograma = tipoEnviar
        }
    }

}
