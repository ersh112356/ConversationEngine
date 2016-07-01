/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

/**
 *
 * @author eran_sh
 */
public interface EngineConsts{
    
    /** Holds the delimiter inside of the keywords. */
    public static final String DELIMITER_KEYWORDS = ",";
    /** Holds the delimiter inside of the keywords (inner). */
    public static final String DELIMITER_INNER_KEYWORDS = " ";
    
    /** Holds the key to fetch the path to the English model. */
    public static final String KEY_ENGINE_MODEL_ENGLISH = "engine.en.path";
    /** Holds the key to fetch the path to the Hebrew model. */
    public static final String KEY_ENGINE_MODEL_HEBREW = "engine.he.path";
    
    /** Holds the key to fetch the English DataParser. */
    public static final String KEY_ENGLISH = "en";
    /** Holds the key to fetch the Hebrew DataParser. */
    public static final String KEY_HEBREW = "he";
    /** Holds the key to fetch the TTL value. */
    public static final String KEY_ENGINE_TTL = "engine.ttl";
    
    /** Holds the key to fetch the path to the Mood classifier. */
    public static final String KEY_MOOD_INDEX = "mood.index";
    
    
    // Used in conjunction with the Injector.
    
    /** Holds the key to fetch the class name of the Model. */
    public static final String KEY_CLASSNAME_CONVERSATION_MODEL = "engine.model.classname";
    /** Holds the key to fetch the class name of the DataModel creator. */
    public static final String KEY_CLASSNAME_DATA_MODEL_CREATOR = "engine.creator.classname";
    /** Holds the key to fetch the class name of the Planner. */
    public static final String KEY_CLASSNAME_CONVERSATION_PLANNER = "engine.planner.classname";
    /** Holds the key to fetch the class name of the mood detector. */
    public static final String KEY_CLASSNAME_MOOD_DETECTOR = "engine.mood.classname";
    /** Holds the key to fetch the class name of the goal fulfillment detector. */
    public static final String KEY_CLASSNAME_GOAL_DETECTOR = "engine.goal.classname";
    /** Holds the key to fetch the class name of the exhaust detector. */
    public static final String KEY_CLASSNAME_EXHAUST_DETECTOR = "engine.exhaust.classname";
}