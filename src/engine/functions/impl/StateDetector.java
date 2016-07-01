/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.functions.impl;

import engine.functions.Function;
import engine.model.datamodel.impl.DataModelConsts;
import engine.service.LuceneService;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Eran
 */
public class StateDetector implements Function<Map<String,Object>>{
    
    /** Holds the text to search for. */
    protected String text;
    /** Holds the domain to filter by. */
    protected String domain;
    /** Holds the LuceneService Object. */
    protected LuceneService luceneService;
    
    /**
     * The constructor.
     * 
     */
    public StateDetector(){
        
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
     *
     * 
     * @param parameters- a Map holding a list of tags, a lists of IDs, a list of domains and the current domain..
     */
    @Override
    public void setParameters(Map<String,Object> parameters){
        
        if(luceneService==null)
        {   // This method is called on each time using this Funcion. No need to reinstall Lucene.
            luceneService = new LuceneService();
            
            List<String> mtags = (List<String>)parameters.get("tags");
            List<String> mids = (List<String>)parameters.get("ids");
            List<String> mdomains = (List<String>)parameters.get("domains");
            
            int len = mtags.size();
            
            for(int i=0;i<len;i++)
            {
                String tags = mtags.get(i);
                String id = mids.get(i);
                String stateDomain = mdomains.get(i);
                
                luceneService.trainEntry(tags,id,stateDomain);
            }
        }
        
        domain = (String)parameters.get("domain");
    }
    
    /**
     * Introduces a new dictionary that might hold dynamic parameters that were gathered in to process. 
     * Ignore.
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

        String sid = luceneService.findCandidate(text,domain);
        int id = sid==null ? DataModelConsts.STATE_DEFAULT_ID : Integer.valueOf(sid);

        return id;
    }
    
    /**
     * Fetches the response from the Keyword Object.
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
