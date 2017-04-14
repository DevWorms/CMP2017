//
//  EncuentaTableViewCell.swift
//  CMP 2017
//
//  Created by mac on 13/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class EncuestaTableViewCell: UITableViewCell {
    
    @IBOutlet weak var btnCalificar: UIButton!
    @IBOutlet weak var imgEncuesta: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}
