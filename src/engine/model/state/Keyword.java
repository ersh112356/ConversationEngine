package engine.model.state;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Keyword extends HashMap<String,String>{

    /**
     * Default constructor.
     * constructs a Keyword object.
     * 
     */
    public Keyword(){
        
        super();
    }

    /**
     * @return the means (keyword).
     */
    public String getMeans(){
        
        return get("means");
    }

    /**
     * Set a new keyword here.
     * 
     * @param means- the means (keyword) to set in here.
     */
    public void setMeans(String means){
        
        put("means",means);
    }

    /**
     * Return the nextId (next ConversationState) of this Keyword.
     * 
     * @return the nextId.
     */
    public String getNext(){
        
        return get("nextId");
    }

    /**
     * Set a new target (the next ConversationState).
     * 
     * @param nextId the target to set.
     */
    public void setNext(String nextId){
        
        put("nextId",nextId);
    }

    /**
     * Return the fully qualified class name.
     * 
     * @return the fully qualified class name.
     */
    public String getClassOfFunction(){
        
        return get("className");
    }

    /**
     * Set a new className. That is used to create a new Function.
     * 
     * @param className the className (a fully qualified name of the Function to invoke) to set.
     */
    public void setClassNameOfFunction(String className){
        
        put("className",className);
    }

    /**
     * Return the arg (the tag to look for in relation to Dynamic Function).
     * 
     * @return the arg.
     */
    public String getArg(){
        
        return get("arg");
    }

    /**
     * Set a new arg.
     * 
     * @param arg the arg (the tag to look for in relation to Dynamic Function) to set.
     */
    public void setArg(String arg){
        
        put("arg",arg);
    }

    /**
     * Set a new variable here.
     * 
     * @return the variable (the label of the tag in relation to Function out put).
     */
    public String getVariable(){
        
        return get("variable");
    }

    /**
     * Set a new variable.
     * 
     * @param variable the variable (the label of the tag in relation to Function out put) to set.
     */
    public void setVariable(String variable){
        
        put("variable",variable);
    }

    /**
     * Return the learn.
     * 
     * @return the learn (in relation to extending dynamically the knowledge base).
     */
    public String getLearn(){
        
        return get("learn");
    }

    /**
     * Set a new learn.
     * 
     * @param learn the learn (in relation to extending dynamically the knowledge base) to set.
     */
    public void setLearn(String learn){
        
        put("learn",learn);
    }

    /**
     * Return the point (the payload).
     * 
     * @return the points.
     */
    public int getPayload(){
        
        String p = get("payload");
        
        return p==null || p.isEmpty() ? 1 : Integer.valueOf(p);
    }

    /**
     * Set a new points (payload) value.
     * 
     * @param payload the points to set.
     */
    public void setPayload(int payload){
        
        put("payload",""+payload);
    }

    /**
     * Return the group index (the index of the group to fetch in relation to RegEx).
     * 
     * @return the group.
     */
    public int getGroup(){
        
        String g = get("group");
        
        return g==null || g.isEmpty() ? 1 : Integer.valueOf(g);
    }

    /**
     * Set a new group index.
     * 
     * @param group the group to set
     */
    public void setGroup(int group){
        
        put("group",""+group);
    }
    
    /**
     * Returns the embedded parameters here.
     * 
     * @return the embedded parameters here.
     */
    public Map<String,String> getParameters(){
        
        return this;
    }
    
    /**
     * Return a human representation of this Object.
     * 
     * @return a human representation of this Object. 
     */
    @Override
    public String toString(){
        
        return "means: "+get("means")+", nextId: "+get("nextId");
    }
}