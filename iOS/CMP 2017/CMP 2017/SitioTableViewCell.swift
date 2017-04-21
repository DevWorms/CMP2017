//
//  SitioTableViewCell.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 22/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class SitioTableViewCell: UITableViewCell {

    @IBOutlet weak var foto: UIImageView!
    @IBOutlet weak var titulo: UILabel!
    @IBOutlet weak var descripcion: UILabel!
    @IBOutlet weak var link: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
     


}
