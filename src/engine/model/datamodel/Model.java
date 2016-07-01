/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.model.datamodel;

import engine.model.state.ConversationState;
import engine.model.state.Keyword;
import java.util.List;
import utils.Callback;

/**
 *
 * @author Eran
 */
public interface Model{
    
    /**
     * Set a new invalid messages (default messages when no such state was found) here.
     * 
     * @param invalidMessages- the new invalid messages.
     */
    public void setInvalidMessages(List<String> invalidMessages);
    
    /**
     * Returns the default functions (the large scale functions).
     * 
     * @return the default functions (the large scale functions).
     */
    public List<Keyword> getDefaultKeywords();
    
    /**
     * Set a new default functions that are called on each request for reply.
     * 
     * @param defaultFunctions- the new default functions.
     */
    public void setDefaultKeywords(List<Keyword> defaultFunctions);
    
    /**
     * Returns the exit functions (the ones that signal we're ready to move on to the next stage).
     * 
     * @return the exit functions (the ones that signal we're ready to move on to the next stage).
     */
    public List<Keyword> getExitKeywords();
    
    /**
     * Set a new exit functions that are signaling that the user is ready to move on.
     * 
     * @param exitFunctions- the new exit functions.
     */
    public void setExitKeywords(List<Keyword> exitFunctions);
    
    /**
     * Get state object by id.
     * 
     * @param id- the id of the state.
     * 
     * @return the state.
     */
    public ConversationState getState(String id);
    
    /**
     * create a new state.
     * 
     * @param state- the new state.
     */
    public void addState(ConversationState state);
    
    /**
     * Returns one of the invalid messages and move the index to the next message.
     * 
     * @return a default message.
     */
    public String getInvalidAnswer();
    
    /**
     * Return the ConversationState Objects, one by one.
     * 
     * @param callback- the callback to invoke on a new entry.
     */
    public void forEach(Callback<ConversationState> callback);
    
    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is replaced.
     * 
     * @param key- key with which the specified value is to be associated 
     * @param value- value to be associated with the specified key 
     * 
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    public ConversationState put(String key, ConversationState value);
}
