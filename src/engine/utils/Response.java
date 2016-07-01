/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.utils;

/**
 *
 * @author Eran
 */
public class Response{
    
    /** Holds the score. */
    protected int score = -1;
    /** Holds the id of the default next ConversationState Object. */
    protected String nextID = null;
    /** Holds the id of the happy next ConversationState Object. */
    protected String nextHappyID = null;
    /** Holds the id of the unhappy next ConversationState Object. */
    protected String nextUnhappyID = null;
    /** Holds the optional response. */
    protected String response = null;
    
    /**
     * The constructor.
     * 
     */
    public Response(){    
    }
    
    /**
     * The constructor.
     * 
     * @param score- the score here.
     * @param nextID- the next default id.
     * @param nextHappyID- the next happy id.
     * @param nextUnhappyID- the next unhappy id.
     * @param response- the optional response.
     */
    public Response(int score, String nextID, String nextHappyID, String nextUnhappyID, String response){
        
        this.score = score;
        this.nextID = nextID;
        this.nextHappyID = nextHappyID;
        this.nextUnhappyID = nextUnhappyID;
        this.response = response;
    }

    /**
     * Return the score.
     * 
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set a new score.
     * 
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the nextID
     */
    public String getNextID(){
        
        return nextID;
    }

    /**
     * Set a new nextID.
     * 
     * @param nextID the nextID to set
     */
    public void setNextID(String nextID){
        
        this.nextID = nextID;
    }

    /**
     * Returns the nextHappyID.
     * 
     * @return the nextHappyID
     */
    public String getNextHappyID(){
        
        return nextHappyID;
    }

    /**
     * Set a new nextHappyID.
     * 
     * @param nextHappyID the nextHappyID to set
     */
    public void setNextHappyID(String nextHappyID){
        
        this.nextHappyID = nextHappyID;
    }

    /**
     * Returns the nextUnhappyID.
     * 
     * @return the nextUnhappyID
     */
    public String getNextUnhappyID(){
        
        return nextUnhappyID;
    }

    /**
     * Set a new nextUnhappyID the nextUnhappyID to set.
     * 
     * @param nextUnhappyID- the next unhappy state.
     */
    public void setNextUnhappyID(String nextUnhappyID){
        
        this.nextUnhappyID = nextUnhappyID;
    }
    
    /**
     * Return the optional response.
     * 
     * @return the optional response.
     */
    public String getResponse(){
        
        return response;
    }
    
    /**
     * Set a new optional response text.
     * 
     * @param response- the optional response text.
     */
    public void setResponse(String response){
        
        this.response = response;
    }
    
    /**
     * Return a human representation of this Object.
     * 
     * @return a human representation of this Object.
     */
    @Override
    public String toString(){
        
        return "nextID: "+nextID+", nextHappyID: "+nextHappyID+", nextUnHappyID: "+nextUnhappyID+", response: "+response+", score: "+score;
    }
}
