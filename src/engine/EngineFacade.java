/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import engine.model.create.Creator;
import engine.model.datamodel.Model;
import inject.Injector;
import inject.InjectorImpl;
import java.util.HashMap;
import java.util.Map;
import logging.LoggerManager;
import utils.ExpiringMap;
import utils.Preferences;

/**
 *
 * @author Eran
 */
public class EngineFacade{
   
    /** Holds the data model of the ChatterBot. */
    private final static Map<String,Model> models = new HashMap(2);
    /** Holds a Map of used Bots. Each Bot will be held active for 30 minutes, or so. */
    private final static Map<String,ConversationEngine> engines;
    
    static
    {
        LoggerManager.logInformation("start to load engine model files.");
        
        String enpath = Preferences.get(EngineConsts.KEY_ENGINE_MODEL_ENGLISH,"../props/engine/data_en.xml");
        String hepath = Preferences.get(EngineConsts.KEY_ENGINE_MODEL_HEBREW,"../props/engine/data_he.xml");
        
        // Use your own Creator, if default doesn't meet your goals.
        String creatorName = Preferences.get(EngineConsts.KEY_CLASSNAME_DATA_MODEL_CREATOR,"engine.model.create.impl.CreateDataModelImpl");
        
        // We're going to use the Injector to inject the proper Creator into this static piece of code.
        Injector<Creator> injector = new InjectorImpl();
        
        //CreateDataModelImpl encreator = new CreateDataModelImpl();
        Creator encreator = injector.inject(creatorName);
        encreator.create(enpath);
        Model endm = encreator.getDataModel();
        
        //CreateDataModelImpl hecreator = new CreateDataModelImpl(hepath);
        Creator hecreator = injector.inject(creatorName);
        hecreator.create(hepath);
        Model hedm = hecreator.getDataModel();
        
        models.put(EngineConsts.KEY_ENGLISH,endm);
        models.put(EngineConsts.KEY_HEBREW,hedm);
        
        LoggerManager.logInformation("Engine model files are loaded.");
        
        int ttl = Integer.parseInt(Preferences.get(EngineConsts.KEY_ENGINE_TTL,"1800"));
        engines = new ExpiringMap(ttl);
    }
    
    /**
     * The constructor.
     * 
     */
    private EngineFacade(){
    }
    
    /**
     * Gets a valid Bot Object for the given combination of identification and language. 
     * 
     * @param cid- a unique identification.
     * @param lang- the given language.
     * 
     * @return a valid Bot Object.
     */
    public static ConversationEngine getOrCreate(String cid, String lang){
        
        String formated = format(cid,lang);
        ConversationEngine engine = engines.get(formated);
        
        if(engine==null)
        {
            Model dm = models.get(lang);
            engine = new ConversationEngine();
            engine.initEngine("0",dm,lang);
            engines.put(formated,engine);
        }
        
        return engine;
    }
    
    /**
     * Format a given cid and language into a valid String Object.
     * 
     * @param cid- a unique identification
     * @param lang- a given language.
     * 
     * @return the two parameters formated.
     */
    private static String format(String cid, String lang){
        
        StringBuilder buf = new StringBuilder();
        buf.append(cid);
        buf.append('_');
        buf.append(lang);
        
        return buf.toString();
    }
}
