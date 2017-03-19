//
//  RegistroViewController.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 08/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class RegistroViewController: UIViewController, UITextFieldDelegate {
    @IBOutlet weak var name: UITextField!
    @IBOutlet weak var lastName: UITextField!
    @IBOutlet weak var mail: UITextField!
    @IBOutlet weak var password: UITextField!
    @IBOutlet weak var claveInscripcion: UITextField!

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = UIColor(patternImage: UIImage(named: "fondo.png")!)
        
        let swipeDown = UISwipeGestureRecognizer(target: self, action: #selector(self.swipeKeyBoard(sender:)))
        swipeDown.direction = UISwipeGestureRecognizerDirection.down
        self.view.addGestureRecognizer(swipeDown)
        
        name.delegate = self
        lastName.delegate = self
        mail.delegate = self
        password.delegate = self
        claveInscripcion.delegate = self
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - UITextFieldDelegate
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    func swipeKeyBoard(sender:AnyObject) {
        //Baja el textField
        self.view.endEditing(true)
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "registro" {
            UserDefaults.standard.set(false, forKey: "invitado")
        }
    }

}
