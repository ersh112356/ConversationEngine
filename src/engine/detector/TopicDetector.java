/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.detector;

import engine.model.state.Keyword;
import engine.utils.Response;
import engine.functions.Function;
import engine.functions.util.FunctionsStorage;
import java.util.List;
import java.util.Map;
import logging.utils.Verbose;

/**
 *
 * @author Eran
 */
public class TopicDetector implements Verbose{
    
    /** Holds the Keyword objects that belong to the given ConversationState. */
    protected List<Keyword> stateKeywords;
    /** Holds the general purpose Keyword objects. */
    protected List<Keyword> generalKeywords;
    /** Holds the storage on the Function Objects. */
    protected FunctionsStorage functions;
    /** Store all regular expression matches. */
    protected Map<String,String> dictionary;
    
    /**
     * The constructor.
     * 
     * @param dictionary- holds all regular expression matches.
     */
    public TopicDetector(Map<String,String> dictionary){
        
        this.dictionary = dictionary;
    }
    
    /**
     * introduces a new Function storage here.
     * 
     * @param functions- a storage of read to be used of Function Objects.
     */
    public void setFunctionStorage(FunctionsStorage functions){
        
        this.functions = functions;
    }
    
    /**
     * Introduces new Keyword Objects that belong to a given ConversationState.
     * 
     * @param keywords- Keyword Objects that belong to a given ConversationState.
     */
    public void setConversationFunctions(List<Keyword> keywords){
        
        this.stateKeywords = keywords;
    }
    
    /**
     * Introduces new Keyword Objects that should be invoked regardless a given ConversationState.
     * 
     * @param keywords- Keyword Objects that belong to a given ConversationState. 
     */
    public void setLargeScaleKeyword(List<Keyword> keywords){
        
        this.generalKeywords = keywords;
    }
    
    /**
     * Try to match a proper response to the given text.
     * 
     * @param text- the text to analyze.
     * 
     * @return a proper response, on null if no such was found.
     */
    public Response getResponse(String text){
        
        int bestMatch = -1;
        Keyword match = null;
        String responseText = null;
                
        // 1. look at the ones that belong to ConversationState.
        int len = stateKeywords.size();
        
        for(int i=0;i<len;i++)
        {
            Keyword keyword = stateKeywords.get(i);
            Function function = fetchFunctionObject(text,keyword);
            
            if(function==null)
            {   // A bad function, just move on.
                continue;
            }
            
            int score = function.getScore();

            if(score>-1 && score>bestMatch) 
            {   // If this match is better than best match, replace it.
                match = keyword;
                bestMatch = score;
                responseText = function.getResponse();
                
                verbose("found a match for text("+text+"): "+match);
            }
        }
        
        verbose("message: "+text+", state functions- match:"+match);
        
        if(generalKeywords!=null)
        {   // 2. look at the ones that belong to the general Keyword Objects.
            len = generalKeywords.size();

            for(int i=0;i<len;i++)
            {
                Keyword keyword = generalKeywords.get(i);
                Function function = fetchFunctionObject(text,keyword);
                
                if(function==null)
                {
                    continue;
                }
                
                int score = function.getScore();

                if(score>-1 && score>bestMatch) 
                {   // If this match is better than best match, replace it.
                    match = keyword;
                    bestMatch = score;
                    responseText = function.getResponse();
                }
            }
        }
        
        verbose("message: "+text+", default functions- match:"+match);
        
        if(match==null)
        {
            return null;
        }
        
        Response response = new Response(bestMatch,match.getNext(),match.get("nextHappyID"),match.get("nextUnhappyID"),responseText);
        
        return response;
    }
        
    
    /**
     * Return a Function Object ready to be used.
     * 
     * @param text- the text to match.
     * @param keyword- the keyword to use.
     * 
     * @return a Function Object ready to be used.
     */
    protected Function fetchFunctionObject(String text, Keyword keyword){
        
        String functionname = keyword.getClassOfFunction();
        Function function = functions.getOrCreate(functionname);
        
        if(function==null)
        {
            return null;
        }
        
        Map<String,String> parameters = keyword.getParameters();
        function.setParameters(parameters);
        function.setDictionary(dictionary);
        function.setText(text);
        
        return function;
    }
}