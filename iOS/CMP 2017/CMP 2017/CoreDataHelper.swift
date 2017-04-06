//
//  CoreDataHelper.swift
//  CMP 2017
//
//  Created by Emmanuel Valentín Granados López on 05/04/17.
//  Copyright © 2017 devworms. All rights reserved.
//

import Foundation
import CoreData

class CoreDataHelper {
    
    // MARK: - Core Data
    
    class func saveData(data: [String:Any], entityName: String, keyName: String) {
        
        
        let entity = NSEntityDescription.entity(forEntityName: entityName, in: managedContext)
        let item = NSManagedObject(entity: entity!, insertInto: managedContext)
        
        item.setValue(data, forKey: keyName)
        
        do {
            try managedContext.save()
        } catch {
            fatalError("Failure to save context: \(entityName),\(keyName): \(error)")
        }
        
    }
    
    class func fetchData(entityName: String, keyName: String) -> [[String : Any]]? {
        
        let requestFetch = NSFetchRequest<NSFetchRequestResult>(entityName: entityName)
        //employeesFetch.returnsObjectsAsFaults = false
        
        do {
            let fetched = try managedContext.fetch(requestFetch)
            
            var results = [[String : Any]]()
            
            for ftd in fetched {
                
                //transformar Transformable(Core Data) a Dictionary
                let res = ftd as! NSManagedObject
                let keys = Array(res.entity.attributesByName.keys)
                let dict = res.dictionaryWithValues(forKeys: keys)
                
                if let result = dict[ keyName ] as? [String:Any] {
                    
                    results.append(result)
                }
            }
            
            print(fetched.count)
            
            return results
            
        } catch {
            fatalError("Failed to fetch: \(entityName),\(keyName): \(error)")
        }
        
    }
    
    class func deleteEntity(entityName: String) {
        let request = NSBatchDeleteRequest(fetchRequest: NSFetchRequest<NSFetchRequestResult>(entityName: entityName))
        
        do {
            try managedContext.execute(request)
        } catch {
            fatalError("Failed to delete \(entityName): \(error)")
        }
        
    }
}
