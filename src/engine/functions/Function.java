/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.functions;

import java.util.Map;

/**
 * @param <T> - the type to use here.
 * @author Eran
 */
public interface Function<T>{
    
    /**
     * Introduces a text to process.
     * 
     * @param text- the text to process.
     */
    public void setText(String text);
    
    /**
     * Introduces a new set of parameters here. 
     * 
     * @param parameters- the parameters to use here.
     */
    public void setParameters(T parameters);
    
    /**
     * Introduces a new dictionary that might hold dynamic parameters that were gathered in to process. 
     * 
     * @param dictionary- a new dictionary that might hold dynamic parameters that were gathered in to process. 
     */
    public void setDictionary(Map<String,String> dictionary);
    
    /**
     * Gets the outcome level of this Function Object as an Integer.
     * 
     * @return the outcome level of this Function Object as an Integer.
     */
    public int getScore();
    
    /**
     * Fetches the response from the Keyword Object.
     * 
     * value- a parameter.
     * 
     * @return the response.
     */
    public String getResponse();
}
