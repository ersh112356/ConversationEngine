/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.functions.impl;

import engine.model.state.Keyword;
import engine.functions.Function;
import engine.functions.util.FunctionsStorage;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Eran
 */
public class GoalFulfillmentDetector implements Function<List<Keyword>>{
    
    /** Holds the text to analyze. */
    protected String text;
    /** Holds a List of Keywords to signal the exit. */
    protected List<Keyword> exitKeywords;
     /** Holds the FunctionsStorage, to be used here locally, only. */
    protected FunctionsStorage functions = new FunctionsStorage();
    
    /**
     * The constructor.
     * 
     */
    public GoalFulfillmentDetector(){
    }
    
    /**
     * Introduces a text to process.
     * 
     * @param text- the text to process.
     */
    @Override
    public void setText(String text){
        
        this.text = text;
    }
    
    /**
     * Introduces a new set of parameters here.
     * Must be called in order this to work.
     * 
     * @param exitKeywords- the functions to use here.
     */
    @Override
    public void setParameters(List<Keyword> exitKeywords){
        
        this.exitKeywords = exitKeywords;
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
        
        int max = Integer.MIN_VALUE;
        
        for(Keyword keyword: exitKeywords)
        {
            String className = keyword.getClassOfFunction();
            Function function = functions.getOrCreate(className);
            
            if(function==null)
            {
                continue;
            }
            
            Map<String,String> parameters = keyword.getParameters();
            function.setText(text);
            function.setParameters(parameters);
            int score = function.getScore();

            if(score>max)
            {
                max = score;
            }
        }
        
        return max;
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
