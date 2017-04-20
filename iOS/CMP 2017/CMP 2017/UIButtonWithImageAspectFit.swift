//
//  UIButtonWithImageAspectFit.swift
//  CMP 2017
//
//  Created by Sergio Ivan Lopez Monzon on 18/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import UIKit

class UIButtonWithImageAspectFit:UIButton{

    override func awakeFromNib() {
        self.imageView?.contentMode = UIViewContentMode.scaleAspectFit
    }
}
