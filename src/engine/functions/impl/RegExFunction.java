/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.functions.impl;

import engine.model.state.Keyword;
import engine.functions.Function;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Eran
 */
public abstract class RegExFunction implements Function<Keyword>{
    
    /** Holds the text to analyze. */
    protected String text;
    /** Holds a dictionary to put in the new name. */
    protected Map<String,String> dictionary;
    /** Holds the Keyword. */
    protected Keyword keyword;
    
    /**
     * Try to match a specified pattern.
     * 
     * @param pattern- the pattern to match.
     * @param text- the text to search into.
     * 
     * @return the outcome, or null if none was found.
     */
    public String match(String pattern, String text){
        
        return  match(pattern,1,text);
    }
    
    /**
     * Try to match a specified pattern.
     * 
     * @param pattern- the pattern to match.
     * @param group- the index of the group to fetch back.
     * @param text- the text to search into.
     * 
     * @return the outcome, or null if none was found.
     */
    public String match(String pattern, int group, String text){
        
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        
        if(m.find()) 
        {
            return m.group(group);
        }
        
        return null;
    }
    
    /**
     * Try to match a specified pattern.
     * 
     * @param pattern- the pattern to match.
     * @param groups- the index of the groups to fetch back.
     * @param text- the text to search into.
     * 
     * @return the outcome, or null if none was found.
     */
    public List<String> match(String pattern, int[] groups, String text){
        
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        
        if(m.find()) 
        {
            List<String> results = new ArrayList();
            
            int len = groups.length;
            
            for(int i=0;i<len;i++)
            {
                int group = groups[i];
                String r = m.group(group);
                results.add(r);
            }
            
            return results;
        }
        
        return null;
    }
    
    /**
     * Return the name of the tag to replace.
     * 
     * @return the name of the tag to replace.
     */
    protected abstract String getKey();
    
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
     * @param keyword- the the Keyword Object that contains information about need to be fitted to the text.
     */
    @Override
    public void setParameters(Keyword keyword){
        
        this.keyword = keyword;
    }
    
    /**
     * Introduces a new dictionary that might hold dynamic parameters that were gathered in to process.
     * Ignored.
     * 
     * @param dictionary- a new dictionary that might hold dynamic parameters that were gathered in to process. 
     */
    @Override
    public void setDictionary(Map<String,String> dictionary){
        
        this.dictionary = dictionary;
    }
    
    /**
     * Gets the mood level for the given text. 0, for neutral, 1 for positive, and -1 for negative.
     * 
     * @return the mood level for the given text. 0, for neutral, 1 for positive, and -1 for negative.
     */
    @Override
    public int getScore(){
        
        int group = keyword.getGroup();
        String match = match(keyword.getMeans(),group,text);
            
        if(match!=null && match.length()>0)
        {
            if(dictionary!=null)
            {
                String key = getKey();
                dictionary.put(key,match);
            }
            
            keyword.put("variable",""+match);

            return keyword.getPayload();
        }
        
        return -1;
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
