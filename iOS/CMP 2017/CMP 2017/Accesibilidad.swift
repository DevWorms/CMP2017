//
//  Accesibilidad.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 24/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import Foundation
import SystemConfiguration

class Accesibilidad {
    
    class func isConnectedToNetwork() -> Bool {
        
        let scriptUrl = URL(string: "https://www.google.com.mx")
        let data = NSData(contentsOf: scriptUrl!)
        
        if ((data) != nil){
            return true
        } else {
            return false
        }
    }
}
