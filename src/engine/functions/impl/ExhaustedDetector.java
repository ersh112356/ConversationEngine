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
public class ExhaustedDetector implements Function<int[]>{
    
    /** Holds the values of the counter and max visit. */
    protected int[] values;
    
    /**
     * The constructor.
     * 
     */
    public ExhaustedDetector(){
    }
    
    /**
     * Introduces a text to process.
     * Ignored.
     * 
     * @param text- the text to process.
     */
    @Override
    public void setText(String text){ 
    }
    
    /**
     * Introduces the present ConversationState. 
     * 
     * @param values- values of the counter and max visit.
     */
    @Override
    public void setParameters(int[] values){
        
        this.values = values;
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
     * Gets the outcome level of this Function Object as an Integer.
     * 
     * @return the outcome level of this Function Object as an Integer.
     */
    @Override
    public int getScore(){
        
        int counter = values[0];
        int max = values[1];
        
        return counter<max ? -1 : 1;
    }
    
    /**
     * Fetches the response from the Keyword Object.
     * Return null.
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
