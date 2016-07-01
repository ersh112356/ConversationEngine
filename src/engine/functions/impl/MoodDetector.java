/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.functions.impl;

import classifier.Classifier;
import classifier.ClassifierConsts;
import engine.EngineConsts;
import engine.functions.Function;
import java.io.IOException;
import java.util.Map;
import logging.LoggerManager;
import utils.Preferences;

/**
 *
 * @author Eran
 */
public class MoodDetector implements Function<String>{
    
    /** Holds the text to analyze. */
    protected String text;
    /** Holds the Classifier here. */
    protected static Classifier classifier;
    
    /**
     * The constructor.
     * 
     */
    public MoodDetector(){
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
     * @param lang- the language to use here.
     */
    @Override
    public void setParameters(String lang){
        
        String path = Preferences.get(EngineConsts.KEY_MOOD_INDEX,"../props/index/mood");
        classifier = new Classifier(path+'/'+lang);
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
        
        try 
        {
            Map result = classifier.classify(text);
            double confidence = (Double)result.get(ClassifierConsts.RESULT);
            String cat = (String)result.get(ClassifierConsts.FIELD_CATEGORY);
            
            switch(cat)
            {
                case ClassifierConsts.RESULT_POSITIVE:
                {   // 'pos'
                    return 1;
                }
                case ClassifierConsts.RESULT_NEUTRAL:
                {   // 'net'
                    return 0;
                }
                case ClassifierConsts.RESULT_NEGATIVE:
                {   // 'neg'
                    return -1;
                }
            }
        } 
        catch(IOException e) 
        {
            LoggerManager.logError("Got an error for "+text+"\r\n"+e);
        }
        
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
