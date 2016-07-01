/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.plan;

import engine.model.state.ConversationState;
import engine.language.Language;
import engine.model.datamodel.Model;
import java.util.Map;

/**
 *
 * @author Eran
 */
public interface Planner extends Language{
    
    /**
     * Presents a new dictionary that holds all tags that needs to be replaced.
     * 
     * @param dictionary- holds all regular expression matches. 
     */
    public void setDictionary(Map<String,String> dictionary);
    
    /**
     * Introduces a new CreateDataModel object here.
     * 
     * @param model- the new CreateDataModel Object.
     */
    public void setDataModel(Model model);
    
    /**
     * Report back the current ID of the selected ConversationState.
     * 
     * @return the current ID of the selected ConversationState.
     */
    public String getCurrentLevel();
    
    /**
     * Starts the process of fetching a reply for the given text.
     * 
     * @param message- the given text to match to.
     * @param state- the current ConversationState Object.
     * 
     * @return a text response to the message.
     */
    public String startToPlanReply(String message, ConversationState state);
}
