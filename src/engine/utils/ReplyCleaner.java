/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.utils;

import java.util.Map;

/**
 *
 * @author Eran
 */
public class ReplyCleaner{
    
    /** Holds a dictionary of tags. */
    protected Map<String,String> dictionary;
    
    /**
     * The constructor.
     * 
     * @param dictionary- holds a map of tags.
     */
    public ReplyCleaner(Map<String,String> dictionary){
        
        this.dictionary = dictionary;
    }
    
    /**
     * Replace given text with variables in the dictionary.
     * So 'Hi [name]' will become now 'Hi Ana'.
     * Optionally, [name|Tony] will become Hi Tony if not found inside the dictionary.
     *
     * @param text- the text to handle by inserting tag values.
     * 
     * @return a human representation version of the text.
     */
    public String replaceMatches(String text){
        
        if(text==null)
        {
            return null;
        }
        
        final char delim = '|';
        final char startDelim = '[';
        final char endDelim = ']';
        
        int start = 0;
        int end;
        
        while((start=text.indexOf(startDelim,start))>-1)
        {
            start++;
            end = text.indexOf(endDelim,start);
            
            if(end>start+1)
            {
                String tag = text.substring(start,end);
                String replacedSection;
                
                int pos = tag.indexOf(delim);
                
                if(pos>-1)
                {   // Default value was detected.
                    String subtag = tag.substring(0,pos);
                    replacedSection = dictionary.get(subtag);
                    
                    if(replacedSection==null)
                    {   // Use the default value.
                        replacedSection = tag.substring(pos+1);
                    }
                }
                else
                {   // No default value.
                    replacedSection = dictionary.get(tag);
                }
               
                if(replacedSection!=null)
                {
                    String beginSection = text.substring(0,start-1);
                    String endSection = text.substring(end+1);
                    text = beginSection + replacedSection + endSection;
                }
            }
        }

        // Remove empty variables tags.
        text = clean(text);
        
        return text;
    }
    
    /**
     * Remove any empty tags.
     * Two types of tags are supported, simple tags in the format of [your_tag].
     * And tags with a default value in the format of [your_tag|your_default_value]
     * 
     * @param text- The text to clean.
     * 
     * @return the text after being cleaned from empty tags.
     */
    public String clean(String text){
        
        // 1. Clean tasgs with a default value.
        // 2. Then clean the rest of simple tags.
        return text.replaceAll("\\[.*\\|(.*)\\]","$1").replaceAll("\\[.*\\]","");
    }
}