/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.functions.util;

import engine.functions.Function;
import inject.Injector;
import java.util.HashMap;
import java.util.Map;
import utils.Preferences;

/**
 *
 * @author Eran
 */
public class FunctionsStorage implements Injector<Function>{
    
    /** Holds ready to use Function Objects. */
    protected Map<String,Function> functions = new HashMap();
    
    /**
     * The constructor.
     * 
     */
    public FunctionsStorage(){   
    }
    
    /**
     * Check whether a specified function exists here, or not.
     * 
     * @param functionname- a fully qualified name of the Function.
     * 
     * @return true if the Function exists, or false otherwise.
     */
    public boolean contains(String functionname){
        
        return functions.containsKey(functionname);
    }
    
    /**
     * Try to create a new Function Object.
     *
     * @param key- a key for fetch the fully qualified name of the Object.
     * @param defaultFunctionName- a fully qualified name of the desired Object. Used if calling to key fails.
     *
     * @return a new Function Object. 
     */
    public Function getOrCreate(String key, String defaultFunctionName){
        
        String cname = Preferences.get(key,defaultFunctionName);
        
        return getOrCreate(cname);
    }
    
    /**
     * Try to create a new Function Object.
     *
     * @param functionname- a fully qualified name of the desired application.
     *
     * @return a new Function Object. 
     */
    public Function getOrCreate(String functionname){
        
        Function function = functions.get(functionname);
        
        if(function!=null)
        {
            return function;
        }
        
        return inject(functionname);
    }
}