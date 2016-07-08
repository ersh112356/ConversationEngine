/*
*
*
*/
package engine.model.state;

import java.util.List;
import java.util.Random;
/**
 * 
 */
public class ConversationState{

    /** Holds the ID of this state object. */
    protected String id = "";
    /** Holds the optional action that might be tied to this state object. */
    protected String action = null;
    /** Holds the embeddedReplies that are tied to this state object. */
    protected List<String> embeddedReplies;
    /** Holds the keywords that are tied to this state. */
    protected List<Keyword> keywords;
    /** Holds the argument here. */
    //private String argument = "";
    /** Holds the Object to generate a random number. */
    protected Random generator = new Random();
    /** Holds the label of this state. */
    protected String label;
    /** Holds the tags that fully describes this state. */
    protected String tags;
     /** Holds the number of times the conversation hits this state. */
    protected int counter = 0;
    /** Holds the max visit times. */
    protected int maxVisit = Integer.MAX_VALUE;

    /**
     * Default constructor, constructs a State object.
     * 
     * @param id- the ID of this ConvesationState. E.g. "0".
     * @param embeddedReplies- the tied messages.
     * @param keywords- the tied Keyword objects.
     */
    public ConversationState(String id, List<String> embeddedReplies, List<Keyword> keywords){
        
        this(id,"",embeddedReplies,keywords);
    }
    
    /**
     * Default constructor, constructs a State object.
     * 
     * @param id- the ID of this ConvesationState. E.g. "0".
     * @param label- the label of this ConversationState. E.g. 'Elicitation'.
     * @param embeddedReplies- the tied messages.
     * @param keywords- the tied Keyword objects.
     */
    public ConversationState(String id, String label, List<String> embeddedReplies, List<Keyword> keywords){
        
        this.id = id;
        this.label = label;
        this.embeddedReplies = embeddedReplies;
        this.keywords = keywords;
    }
    
    /**
     * Returns the tied label.
     * 
     * @return the tied label.
     */
    public String getLabel(){
        
        return label;
    }
    
    /**
     * Set a new label here.
     * 
     * @param label- the new label.
     */
    public void setLabel(String label){
        
        this.label = label;
    }
    
    /**
     * Return the maxVisit value.
     * 
     * @return the maxVisit value.
     */
    public int getMaxVisit(){
        
        return maxVisit;
    }
    
    /**
     * Set s new MaxVisit value.
     * 
     * @param maxVisit- the new value.
     */
    public void setMaxVisit(int maxVisit){
        
        this.maxVisit = maxVisit;
    }
    
    /**
     * Return the embedded tags.
     * 
     * @return the embedded tags.
     */
    public String getTags(){
        
        return tags;
    }
    
    /**
     * Introduces a new set of tags here.
     * 
     * @param tags- the new set of tags.
     */
    public void setTags(String tags){
        
        this.tags = tags;
    }

    /** 
     * Get id of the state
     * 
     * @return the id of the state.
     */
    public String getId(){
        
        return id;
    }
    
    /**
     * Introduces a new action here. 
     * 
     * @param action- the new action.
     */
    public void setAction(String action){
        
        this.action = action;
    }
    
    /**
     * Get the optional action that might be tied to this state object.
     * 
     * @return the optional action that might be tied to this state object. Null if not present.
     */
    public String getAction(){
        
        return action;
    }

    /**
     * Get random state embeddedReplies.
     * 
     * @return a random message from the list of embeddedReplies.
     */
    public String getReply(){
        
        int len = embeddedReplies.size();
        
        if(len==0)
        {   // TODO
            return "";
        }
        
        int candidate = generator.nextInt(len);
        
        return embeddedReplies.get(candidate);
    }

    /**
     * Set the argument from regex matcher.
     *//*
    public void setRegex(String argument){
        
        this.argument = argument;
    }*/

    /**
     * Get state keywords.
     * 
     * @return the Keyword Objects.
     */
    public List<Keyword> getKeywords(){
        
        return keywords;
    }
    
    /**
     * Determine whether this ConversationState Object should be considered as an end point, or not.
     * 
     * @return whether this ConversationState Object should be considered as an end point, or not.
     */
    public boolean isEndPoint(){
        
        return getKeywords().isEmpty();
    }
    
    /**
     * Fetches the internal counter of visits.
     * 
     * @return the internal counter of visits.
     */
    public int getCounter(){
        
        return counter;
    }
    
    /**
     * Add 1 to the present count.
     * 
     */
    public void increaseCounter(){
        
        counter++;
    }
    
    /**
     * Return a human representation of this Object.
     * 
     * @return a human representation of this Object.
     */
    @Override
    public String toString(){
        
        return id;
    }
}