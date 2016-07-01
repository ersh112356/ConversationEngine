package engine;

import engine.model.state.ConversationState;
import engine.language.impl.LanguageImpl;
import engine.utils.ReplyCleaner;
import engine.model.datamodel.Model;
import engine.plan.Planner;
import inject.Injector;
import java.util.HashMap;
import java.util.Map;
import utils.Preferences;

/**
 * 
 */
public class ConversationEngine extends LanguageImpl implements Injector<Planner>{
    
    /** Default state to start the engine with. */
    protected String level = "0";
    /** The XML model of states. */
    protected Model model;
    /** Holds the conversation planner. */
    protected Planner planner;
    /** Holds the reply cleaner. */
    protected ReplyCleaner cleaner;

    /**
     * The constructor.
     */
    public ConversationEngine(){
    }
    
    /**
     * The constructor.
     * Starts with default English engine.
     * Deprecated.
     *
     * @param level- the start point level to start with.
     * @param model- the model of states to use.
     * 
     * @deprecated 
     */
    @Deprecated
    public ConversationEngine(String level, Model model){
        
        this(level,model,"en");
    }
    
    /**
     * The constructor.
     * Deprecated.
     *
     * @param level- the start point level to start with.
     * @param model- the model of states to use.
     * @param lang- the language in which this engine addresses.
     * 
     * @deprecated 
     */
    @Deprecated
    public ConversationEngine(String level, Model model, String lang){
        
        this.level = level;
        this.model = model;
        setLanguage(lang);
        
        // Store here all tags (which are regular expression matches).
        Map<String,String> dictionary = new HashMap();
        cleaner = new ReplyCleaner(dictionary);
        
        // Use your own Planner, if default doesn't meet your goals. 
        String pname = Preferences.get(EngineConsts.KEY_CLASSNAME_CONVERSATION_PLANNER,"engine.plan.impl.ConversationPlannerImpl");
        
        // We're going to use the Injector to inject the proper Planner into this object.
        planner = inject(pname);
        planner.setDictionary(dictionary);
        planner.setLanguage(lang);
        planner.setDataModel(model);
    }
    
    /**
     * Initiate the engine.
     *
     * @param level- the start point level to start with.
     * @param model- the model of states to use.
     * @param lang- the language in which this engine addresses.
     */
    public void initEngine(String level, Model model, String lang){
        
        initEngine(level,model,lang,null);
    }
    
    /**
     * Initiate the engine.
     *
     * @param level- the start point level to start with.
     * @param model- the model of states to use.
     * @param lang- the language in which this engine addresses.
     * @param dictionary- store here all tags (which are regular expression matches).
     */
    public void initEngine(String level, Model model, String lang, Map<String,String> dictionary){
        
        this.level = level;
        this.model = model;
        setLanguage(lang);
        
        if(dictionary==null)
        {   // Start fresh.
            dictionary = new HashMap();
        }
        
        cleaner = new ReplyCleaner(dictionary);
        
        // Use your own Planner, if default doesn't meet your goals. 
        String pname = Preferences.get(EngineConsts.KEY_CLASSNAME_CONVERSATION_PLANNER,"engine.plan.impl.ConversationPlannerImpl");
        
        // We're going to use the Injector to inject the proper Planner into this object.
        planner = inject(pname);
        planner.setDictionary(dictionary);
        planner.setLanguage(lang);
        planner.setDataModel(model);
    }
    
    /**
     * Introduces a new model.
     * 
     * @param model- the model of states to use.
     */
    public void setModel(Model model){
        
        this.model = model;
        planner.setDataModel(model);
    }

    /**
     * Return the current state message.
     * Used at the beginning to fetch the welcome message from ConversationState 0.
     * 
     * @return the current state message.
     */
    public String getEmbeddedReply(){
        
        ConversationState state = model.getState(level);
        
        return cleaner.replaceMatches(state.getReply()).trim();
    }

    /**
     * Send user message to the bot and get the response using the default level.
     * The default level is updated as the conversation moves on.
     * 
     * @param message- the text to reply to.
     * 
     * @return a reply.
     */
    public String getReply(String message){
        
        return getReply(message,level);
    }
    
    /**
     * Send user message to the bot and get the response.
     * 
     * @param message- the text to reply to.
     * @param level- use this entry level to start searching for a reply. If this is taken from the above, it's the last ConversationState ID.
     * 
     * @return a reply.
     */
    public String getReply(String message, String level){
 
        ConversationState state = model.getState(level);
        
        // 1. Fetches the reply.
        String response = planner.startToPlanReply(message,state);
        
        // 2. Fetches the new state ID.
        //    That's used to control the flow when no level parameter is provided (which is the default here).
        this.level = planner.getCurrentLevel();
        
        return response;
    }
}