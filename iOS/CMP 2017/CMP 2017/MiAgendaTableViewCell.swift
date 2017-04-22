//
//  MiAgendaTableViewCell.swift
//  CMP 2017
//
//  Created by mac on 21/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//

//
//  NotificacionTableViewCell.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 22/03/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class MiAgendaTableViewCell: UITableViewCell {
    @IBOutlet weak var horarioEvento: UILabel!
  
    @IBOutlet weak var nombreEvento: UILabel!
    @IBOutlet weak var imageViewFoto: UIImageView!
    
    override func awakeFromNib() {
        
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}
