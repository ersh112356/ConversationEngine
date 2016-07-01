/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.model.datamodel.impl;

import engine.model.datamodel.Model;
import engine.model.state.ConversationState;
import engine.model.state.Keyword;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import utils.Callback;

/**
 *
 * @author Eran
 */
public class DataModel extends HashMap<String,ConversationState> implements Model{
    
    /** Holds the default invalid messages. */
    private List<String> invalidMessages = new ArrayList();
    /** That's used to cycle between default invalid messages. */
    private int invalidMessageIndex = 0;
    /** Holds the default functions that are common to all reply request. */
    private List<Keyword> defaultKeywords = new ArrayList();
    /** Holds the exit functions that are signaling that the user is ready to move on. */
    private List<Keyword> exitKeywords = new ArrayList();
    
    /**
     * The constructor.
     * 
     */
    public DataModel(){
       
        super();
    }
    
    /**
     * Set a new invalid messages (default messages when no such state was found) here.
     * 
     * @param invalidMessages- the new invalid messages.
     */
    @Override
    public void setInvalidMessages(List<String> invalidMessages){
        
        this.invalidMessages = invalidMessages;
    }
    
    /**
     * Returns the default functions (the large scale functions).
     * 
     * @return the default functions (the large scale functions).
     */
    @Override
    public List<Keyword> getDefaultKeywords(){
        
        return defaultKeywords;
    }
    
    /**
     * Set a new default functions that are called on each request for reply.
     * 
     * @param defaultFunctions- the new default functions.
     */
    @Override
    public void setDefaultKeywords(List<Keyword> defaultFunctions){
        
        this.defaultKeywords = defaultFunctions;
    }
    
    /**
     * Returns the exit functions (the ones that signal we're ready to move on to the next stage).
     * 
     * @return the exit functions (the ones that signal we're ready to move on to the next stage).
     */
    @Override
    public List<Keyword> getExitKeywords(){
        
        return exitKeywords;
    }
    
    /**
     * Set a new exit functions that are signaling that the user is ready to move on.
     * 
     * @param exitFunctions- the new exit functions.
     */
    @Override
    public void setExitKeywords(List<Keyword> exitFunctions){
        
        this.exitKeywords = exitFunctions;
    }
    
    /**
     * Get state object by id.
     * 
     * @param id- the id of the state.
     * 
     * @return the state.
     */
    @Override
    public ConversationState getState(String id){
        
        return get(id);
    }

    /**
     * create a new state.
     * 
     * @param state- the new state.
     */
    @Override
    public void addState(ConversationState state){
        
        put(state.getId(), state);
    }

    /**
     * Returns one of the invalid messages and move the index to the next message.
     * 
     * @return a default message.
     */
    @Override
    public String getInvalidAnswer(){

        // Get current answer.
        String answer = invalidMessages.get(invalidMessageIndex);

        // Increase the index, if it is end of messages, reset the index to 0.
        invalidMessageIndex++;
        
        if(invalidMessageIndex >= invalidMessages.size()) 
        {
            invalidMessageIndex = 0;
        }
        
        return answer;
    }
    
    /**
     * Return the number of states.
     * 
     * @return the number of states.
     */
    public int getStateCounter(){
        
        return size();
    }
    
    /**
     * Return the ConversationState Objects, one by one.
     * 
     * @param callback- the callback to invoke on a new entry.
     */
    @Override
    public void forEach(Callback<ConversationState> callback){
        
        super.forEach((k,v) ->{
            
            callback.run(v);
        });
    }
}