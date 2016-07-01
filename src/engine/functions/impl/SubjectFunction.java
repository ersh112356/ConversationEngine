/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.functions.impl;

/**
 *
 * @author Eran
 */
public class SubjectFunction extends RegExFunction{
    
    /**
     * The constructor.
     * 
     */
    public SubjectFunction(){
    }
    
    /**
     * Return the label of the tag to replace.
     * 
     * @return the label of the tag to replace.
     */
    @Override
    protected String getKey(){
        
        return "subject";
    }
}
