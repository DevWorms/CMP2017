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
    
    class func saveData(data: Any, entityName: String, keyName: String) {
        let entity = NSEntityDescription.entity(forEntityName: entityName, in: managedContext)
        let item = NSManagedObject(entity: entity!, insertInto: managedContext)
        
        item.setValue(data, forKey: keyName)
        
        do {
            try managedContext.save()
        } catch {
            fatalError("Failure to save context: \(entityName),\(keyName): \(error)")
        }
        
    }
    
    class func updateData(index: Int, data: Any, entityName: String, keyName: String) {
        
        let requestFetch = NSFetchRequest<NSFetchRequestResult>(entityName: entityName)
        
        do {
            let fetched = try managedContext.fetch(requestFetch) as! [NSManagedObject]
            
            //for ftd in fetched {
                
                fetched[index].setValue(data, forKey: keyName)
                
                do {
                    try managedContext.save()
                } catch {
                    fatalError("Failure to update context: \(entityName),\(keyName): \(error)")
                }
            //}
            
        } catch {
            fatalError("Failed to update: \(entityName),\(keyName): \(error)")
        }
    }
    
    class func fetchData(entityName: String, keyName: String) -> [[String : Any]]? {
        
        let requestFetch = NSFetchRequest<NSFetchRequestResult>(entityName: entityName)
        //requestFetch.resultType = NSFetchRequestResultType.dictionaryResultType
        //requestFetch.propertiesToFetch = ["banner"]
        //requestFetch.returnsObjectsAsFaults = false
        
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
            
            print("Count fetchData: \(results.count)")
            
            return results
            
        } catch {
            fatalError("Failed to fetchData: \(entityName),\(keyName): \(error)")
        }
        
    }
    
    class func fetchItem(entityName: String, keyName: String) -> [Any]? {
        
        let requestFetch = NSFetchRequest<NSFetchRequestResult>(entityName: entityName)
        //requestFetch.predicate = NSPredicate.init(format: String, args: CVarArg...)
        
        do {
            let fetched = try managedContext.fetch(requestFetch) as! [NSManagedObject]
            
            var results = [Any?]()
            
            for ftd in fetched {
                
                //if let dato = (ftd as? AnyObject)?.value(forKey: keyName)  {
                
                let dato = (ftd as AnyObject).value(forKey: keyName)
                    
                results.append(dato)
                //}
            }
            
            print("Count fetchItem: \(results.count)")
            
            return results
            
        } catch {
            fatalError("Failed to fetchItem: \(entityName),\(keyName): \(error)")
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
