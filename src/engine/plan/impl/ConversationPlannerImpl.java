/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.plan.impl;

import com.google.common.util.concurrent.AtomicLongMap;
import engine.model.datamodel.Model;
import engine.model.state.ConversationState;
import engine.EngineConsts;
import engine.model.state.Keyword;
import engine.language.impl.LanguageImpl;
import engine.utils.ReplyCleaner;
import engine.utils.Response;
import engine.detector.TopicDetector;
import engine.functions.Function;
import engine.functions.util.FunctionsStorage;
import engine.plan.Planner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logging.utils.Verbose;

/**
 *
 * @author Eran
 */
public class ConversationPlannerImpl extends LanguageImpl implements Verbose, Planner{
    
    /** Holds the DataMOdel (XML based) Object. */
    protected Model model;
    /** Holds the tags for each state. */
    protected List<String> mtags;
    /** Holds the id for each state. */
    protected List<String> mids;
     /** Holds the domain for each state. */
    protected List<String> mdomains;
    /** That stores all regular expression matches. */
    protected Map<String,String> dictionary;
    /** Holds the FunctionsStorage, to be used globally. */
    protected FunctionsStorage functions = new FunctionsStorage();
    /** Holds the TopicDetector Object. */
    protected TopicDetector topicDetector;
    /** Holds the general purpose Keyword objects. */
    protected List<Keyword> generalKeywords = null;
    /** Holds the number of a state was used. */
    protected AtomicLongMap counts = AtomicLongMap.create();
    /** Holds the reply cleaner. */
    protected ReplyCleaner cleaner;
    /** Holds the current level (the state ID). */
    protected String level;
    
    /**
     * The constructor.
     * 
     */
    public ConversationPlannerImpl(){
    }
    
    /**
     * The constructor.
     * Not in use anymore.
     * 
     * @param dictionary- holds all regular expression matches. 
     * 
     * @deprecated 
     */
    @Deprecated
    public ConversationPlannerImpl(Map<String,String> dictionary){
        
        this.dictionary = dictionary;
        cleaner = new ReplyCleaner(dictionary);
        topicDetector = new TopicDetector(dictionary);
        topicDetector.setFunctionStorage(functions);
    }
    
    /**
     * Presents a new dictionary that holds all tags that needs to be replaced.
     * 
     * @param dictionary- holds all regular expression matches. 
     */
    @Override
    public void setDictionary(Map<String,String> dictionary){
        
        this.dictionary = dictionary;
        cleaner = new ReplyCleaner(dictionary);
        topicDetector = new TopicDetector(dictionary);
        topicDetector.setFunctionStorage(functions);
    }
    
    /**
     * Introduces a new CreateDataModel object here.
     * 
     * @param model- the new CreateDataModel Object.
     */
    @Override
    public void setDataModel(Model model){
        
        this.model = model;
        generalKeywords = model.getDefaultKeywords();
        
        initModelTags();
    }
    
    /**
     * Start to collect tags from all states.
     * Those tags will be used in connection to StateDetector.
     */
    protected void initModelTags(){
        
        if(model!=null)
        {
            mtags = new ArrayList();
            mids = new ArrayList();
            mdomains = new ArrayList();
            
            model.forEach((ConversationState state) -> {
                
                String stags = state.getTags();
                String id = state.getId();
                String label = state.getLabel();
                mtags.add(stags);
                mids.add(id);
                mdomains.add(label);
            });
        }
    }
    
    /**
     * Report back the current ID of the selected ConversationState.
     * 
     * @return the current ID of the selected ConversationState.
     */
    @Override
    public String getCurrentLevel(){
        
        return level;
    }
    
    /**
     * Starts the process of fetching a reply for the given text.
     * 
     * @param message- the given text to match to.
     * @param state- the current ConversationState Object.
     * 
     * @return a text response to the message.
     */
    @Override
    public String startToPlanReply(String message, ConversationState state){
        
        List<Keyword> stateKeywords = state.getKeywords();
        topicDetector.setConversationFunctions(stateKeywords);
        topicDetector.setLargeScaleKeyword(generalKeywords);
        Response response = topicDetector.getResponse(message);
        
        verbose("response object: "+response);
        
        ConversationState next = null;
        
        if(response==null)
        {   // Found nothing, try to match via the StateDetector (a probability matching).
            response = callStateDetector(state,message);
            String nid = nextIDByMood(response,0);
            next = model.getState(nid);
            
            verbose("StateDetector- original message: "+message+", next: "+next);
        }
        else
        {   // Inject the MoodDetector via the FunctionsStorage.
            Function moodFunction = functions.getOrCreate(EngineConsts.KEY_CLASSNAME_MOOD_DETECTOR,"engine.functions.impl.MoodDetector");
            moodFunction.setText(message);
            moodFunction.setParameters(lang);
            int mood = moodFunction.getScore();
            
            verbose("message: "+message+", mood: "+mood);
            
            // Inject the GoalFulfillmentDetector via the FunctionsStorage.
            Function goalFulfillmentDetector = functions.getOrCreate(EngineConsts.KEY_CLASSNAME_GOAL_DETECTOR,"engine.functions.impl.GoalFulfillmentDetector");
            goalFulfillmentDetector.setText(message);
            
            List<Keyword> exitKeywords = model.getExitKeywords();
            goalFulfillmentDetector.setParameters(exitKeywords);
            int readyToMove = goalFulfillmentDetector.getScore();
            
            verbose("message: "+message+", ready to move: "+readyToMove);
            
            int recommend = Math.max(readyToMove,mood);
            String nid = nextIDByMood(response,recommend);

            next = model.getState(nid);
            int max = next.getMaxVisit();
            int visited = (int)counts.incrementAndGet(nid);

            // Inject the GoalFulfillmentDetector via the FunctionsStorage.
            Function exhausted = functions.getOrCreate(EngineConsts.KEY_CLASSNAME_EXHAUST_DETECTOR,"engine.functions.impl.ExhaustedDetector");
            exhausted.setParameters(new int[]{visited,max});
            int result = exhausted.getScore();
            
            verbose("message: "+message+", nid: "+nid+", exhausted: "+result);
            
            if(result==1)
            {   // The state has exhausted, try to find a candidate using probabilities.
                response = callStateDetector(state,message);
                nid = response.getNextID();
                counts.incrementAndGet(nid);
                next = model.getState(nid);
            }
        }
        
        level = next.getId();
        
        // Makes room for dynamic Functions.
        String textResponse = response.getResponse();
        
        if(textResponse==null)
        {   // No dynamic Functions, get one from the messages.
            textResponse = next.getReply();
        }
        
        textResponse = cleaner.replaceMatches(textResponse);
        
        verbose("message: "+message+", reply: "+textResponse);
        
        return textResponse;
    }
    
    /**
     * Try to let the probability to take charge.
     * 
     * @param state- the current ConversationState.
     * @param text- the text to use.
     * 
     * @return a Response Object that meet the next probability as per the given text.
     */
    protected Response callStateDetector(ConversationState state, String text){
        
        String domain = state.getLabel();
        Function stateDetector = initiateStateDetector(domain,text);

        int id = stateDetector.getScore();
        Response response = new Response(0,""+id,null,null,null);
        
        return response;
    }
    
    /**
     * Makes the StateDetector operational.
     * 
     * @param domain- the current domain in which the ConversationState belongs to. Null for a wild card.
     * @param text- the text to use.
     * 
     * @return the StateDetector Object.
     */
    protected Function initiateStateDetector(String domain, String text){
        
        final String classname = "engine.functions.impl.StateDetector";
        boolean exists = functions.contains(classname);
        Function stateDetector = functions.getOrCreate(classname);
        stateDetector.setText(text);

        Map<String,Object> parameters = new HashMap();
        parameters.put("domain",domain);

        if(!exists)
        {   // Pack the Function with the model. Goes only once.
            parameters.put("tags",mtags);
            parameters.put("ids",mids);
            parameters.put("domains",mdomains);

            stateDetector.setParameters(parameters);
        }
        
        return stateDetector;
    }
    
    /**
     * Determine the next ID following the mood.
     * 
     * @param response- the response from other Function Objects (i.e. Detectors).
     * @param mood- the given mood. 1 for happy, 0 for neutral, and -1 for unhappy.
     * 
     * @return the next ID.
     */
    protected String nextIDByMood(Response response, int mood){
        
        String id = response.getNextID();
        String uhid = response.getNextUnhappyID();
        String hid = response.getNextHappyID();
        String nid;
        
        switch(mood)
        {
            case -1:
            {
                nid = uhid==null ? id : uhid;
                
                break;
            }
            case 0:
            {
                nid = id;
                
                break;
            }
            case 1:
            {
                nid = hid==null ? id : hid;
                
                break;
            }
            default:
            {
                nid = id;
            }
        }
        
        if(nid.equals("-1"))
        {   // Go to the default greeting state.
            nid = "1";
        }
        
        return nid;
    }
}