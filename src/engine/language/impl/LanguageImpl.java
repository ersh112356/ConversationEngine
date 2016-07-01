/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.language.impl;

/**
 *
 * @author Eran
 */
public abstract class LanguageImpl{
    
    /** Holds the language in which this engine addresses. */
    protected String lang = "en";
    
    /**
     * 
     * @param lang- the language in which an engine object addresses. 
     */
    public void setLanguage(String lang){
        
        this.lang = lang;
    }
}