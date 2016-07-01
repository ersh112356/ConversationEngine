/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.model.create;

import engine.model.datamodel.Model;

/**
 *
 * @author Eran
 */
public interface Creator{
    
    /**
     * Start to create a new Model using a default path to the XML file.
     * 
     */
    public void create();
    
    /**
     * Start to create a new DataModel.
     * 
     * @param path- the path to the XML file.
     */
    public void create(String path);
    
    /**
     * Return the DataModel Object.
     * 
     * @return the DataModel Object.
     */
    public Model getDataModel();
}
