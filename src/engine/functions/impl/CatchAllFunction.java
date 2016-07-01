/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.functions.impl;

import engine.functions.Function;
import java.util.Map;

/**
 *
 * @author Eran
 */
public class CatchAllFunction implements Function<Object>{
    
    /**
     * The constructor.
     * 
     */
    public CatchAllFunction(){   
    }
    
    /**
     * Introduces a text to process.
     * 
     * @param text- the text to process.
     */
    @Override
    public void setText(String text){
    }
    
    /**
     * Introduces a new set of parameters here.
     * Must be called in order this to work.
     * 
     * @param params- ignored.
     */
    @Override
    public void setParameters(Object params){
    }
    
    /**
     * Introduces a new dictionary that might hold dynamic parameters that were gathered in to process.
     * Ignored.
     * 
     * @param dictionary- a new dictionary that might hold dynamic parameters that were gathered in to process. 
     */
    @Override
    public void setDictionary(Map<String,String> dictionary){
    }
    
    /**
     * Gets the mood level for the given text. 0, for neutral, 1 for positive, and -1 for negative.
     * 
     * @return the mood level for the given text. 0, for neutral, 1 for positive, and -1 for negative.
     */
    @Override
    public int getScore(){
       
        // Return 0 match when keyword is *.
        return 0;
    }
    
    /**
     * Fetches the response from the Keyword Object.
     * Returns null.
     * 
     * value- a parameter.
     * 
     * @return the response.
     */
    @Override
    public String getResponse(){
        
        return null;
    }
}
