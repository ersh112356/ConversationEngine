/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.functions.impl;

import engine.EngineConsts;
import engine.model.state.Keyword;
import engine.functions.Function;
import java.util.Map;

/**
 *
 * @author Eran
 */
public class ContainsFunction implements Function<Keyword>{
    
    /** Holds the text to analyze. */
    protected String text;
    /** Holds the means that need to be fitted to the text. */
    protected String means;
    /** Holds the payload. */
    protected int payload = 1;
    
    /**
     * The constructor.
     * 
     */
    public ContainsFunction(){
    }
    
    /**
     * Introduces a text to process here.
     * 
     * @param text- the text to process.
     */
    @Override
    public void setText(String text){
        
        this.text = text;
    }
    
    /**
     * Introduces a new set of parameters here.
     * Must be called in order for this to work.
     * 
     * @param keyword- the the Keyword Object that needs to be fitted to the text.
     */
    @Override
    public void setParameters(Keyword keyword){
        
        means = keyword.getMeans();
        payload = keyword.getPayload();
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
     * Gets the compatibility level for the given text. -1, for the expression was found (as a bag of words measured) , >0 for the length of the expression in terms of words.
     * 
     * @return the compatibility level for the given text. -1, for the expression was found (as a bag of words measured) , >0 for the length of the expression in terms of words.
     */
    @Override
    public int getScore(){
        
        String[] words = means.split(EngineConsts.DELIMITER_INNER_KEYWORDS);
        String temp = text.toLowerCase();
        
        // No match by default.
        int result = -1;
        
        for(String word : words) 
        {   // Loop through list of the means.

            if(temp.contains(word.toLowerCase())) 
            {    // If current keyword is in the text, add payload.
                result += payload;
            }
            else 
            {
                return -1;
            }
        }
        
        return result;
    }
    
    /**
     * Fetches the response from the Keyword Object.
     * Returns null.
     * 
     * @return the null/.
     */
    @Override
    public String getResponse(){
        
        return null;
    }
}