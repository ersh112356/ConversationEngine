package engine.model.create.impl;

import engine.model.state.ConversationState;
import engine.EngineConsts;
import engine.model.state.Keyword;
import engine.model.create.Creator;
import engine.model.datamodel.Model;
import inject.Injector;
import inject.InjectorImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import logging.LoggerManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.Preferences;

/**
 * 
 */
public class CreateDataModelImpl implements Creator{

    /** Holds the DOM object. */
    private Document dom;
    /** Holds the states. */
    private Model model;
    /** Holds the default invalid messages. */
    private List<String> invalidMessages = new ArrayList();
    /** Holds the default functions. */
    private List<Keyword> defaultKeywords = new ArrayList();
    /** Holds the ready to exit functions. */
    private List<Keyword> exitKeywords = new ArrayList();
    /** That's used for learn in which we need to make room for new states, dynamically. */
    private int stateCounter = 100000;
    
    /**
     * The constructor.
     * Using a default path to the XML file (English).
     *
     */
    public CreateDataModelImpl(){
    }
    
    /**
     * The constructor.
     * 
     * @param path- the path to the XML file.
     * @deprecated 
     */
    public CreateDataModelImpl(String path){

        // Use your own Creator, if default doesn't meet your goals.
        String modelName = Preferences.get(EngineConsts.KEY_CLASSNAME_CONVERSATION_MODEL,"engine.model.datamodel.impl.DataModel");
        
        // We're going to use the Injector to inject the proper Model.
        Injector<Model> injector = new InjectorImpl();
        model = injector.inject(modelName);
        
        // Load the XML file and parse it
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try
        {   // Using factory to get an instance of document builder.
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Parse using builder to get a DOM representation of the XML file.
            dom = db.parse(path);

            // Load configuration and states from the XML file.
            loadInvalidMessages();
            loadStates();
            loadDefaultKeywords();
            loadExitKeywords();
        }
        catch(ParserConfigurationException | SAXException | IOException e)
        {
            LoggerManager.logError("failed to parse the Engine models. "+e);
        }
    }
    
    /**
     * Start to create a new DataModel using a default path to the XML file..
     * 
     */
    @Override
    public void create(){
        
        create("../props/engine/data_en.xml");
    }
    
    /**
     * Start to create a new DataModel.
     * 
     * @param path- the path to the XML file.
     */
    @Override
    public void create(String path){
        
        // Use your own Creator, if default doesn't meet your goals.
        String modelName = Preferences.get(EngineConsts.KEY_CLASSNAME_CONVERSATION_MODEL,"engine.model.datamodel.impl.DataModel");
        
        // We're going to use the Injector to inject the proper Model.
        Injector<Model> injector = new InjectorImpl();
        model = injector.inject(modelName);
        
        // Load the XML file and parse it
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try
        {   // Using factory to get an instance of document builder.
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Parse using builder to get a DOM representation of the XML file.
            dom = db.parse(path);

            // Load configuration and states from the XML file.
            loadInvalidMessages();
            loadStates();
            loadDefaultKeywords();
            loadExitKeywords();
        }
        catch(ParserConfigurationException | SAXException | IOException e)
        {
            LoggerManager.logError("failed to parse the Engine models. "+e);
        }
    }
    
    /**
     * Return the DataModel Object.
     * 
     * @return the DataModel Object.
     */
    @Override
    public Model getDataModel(){
        
        model.setInvalidMessages(invalidMessages);
        model.setDefaultKeywords(defaultKeywords);
        model.setExitKeywords(exitKeywords);
        
        return model;
    }

    /**
     * Load states from XML file.
     */
    private void loadStates(){

        // Get document element object.
        Element docEle = dom.getDocumentElement();

        // Get all ConversationState node names.
        NodeList nl = docEle.getElementsByTagName("State");
        int len = nl==null ? 0 : nl.getLength();
           
        for(int i=0; i<len;i++)
        {   // If node is not null and it has children.
            // Loop through all children.
            // Get state element.
            Element el = (Element)nl.item(i);

            // Get state id.
            String id = el.getAttribute("id");
            String label = el.getAttribute("label");
            String mvisited = el.getAttribute("maxvisited");
            String tags = el.getAttribute("tags");
            
            // Get all state messages.
            List messages = new ArrayList();
            NodeList messagesNodeList = el.getElementsByTagName("message");
            int innerlen = messagesNodeList==null ? 0 : messagesNodeList.getLength();

            for(int j=0;j<innerlen;j++)
            {   // If messages node is not null and has children.
                // Loop through all children.
                // Get current message element.
                Element elmsg = (Element)messagesNodeList.item(j);
                String message = elmsg.getFirstChild().getNodeValue();
                
                // Append message node value to the messages list.
                messages.add(message);
            }

            // Get keywords in the current state.
            List keywords = getKeywords(el);

            // Construct a new ConversationState object.
            ConversationState state = new ConversationState(id,label,messages,keywords);
            state.setTags(tags);
            
            if(mvisited!=null && !mvisited.isEmpty())
            {
                try
                {
                    int max = Integer.valueOf(mvisited);
                    state.setMaxVisit(max);
                }
                catch(Exception e)
                {
                }
            }

            stateCounter ++;

            // Add the state to the states hashmap.
            model.put(id,state);
        }
    }

    /**
     * Get all keywords in an ConversationState tag.
     * 
     * @param ele- the Element that holds the Keywords.
     * 
     * @return a List of Keyword Objects.
     */
    protected List getKeywords(Element ele){

        // Construct keywords arraylist.
        List keywords = new ArrayList();

        // get all nodes by keyword tag name
        NodeList nl = ele.getElementsByTagName("keyword");
        int len = nl==null ? 0 : nl.getLength();
            
        for(int i=0; i<len;i++)
        {   // If the tag is not null and has children.
            // Loop through all the children.
            // Get the keyword element.
            Element el = (Element)nl.item(i);

            // Find the keyword target, classname and argument attributes.
            String keywordTags = el.getFirstChild().getNodeValue();
            String nextID = el.getAttribute("nextId");
            String nextHappy = el.getAttribute("nextHappy");
            String nextUnHappy = el.getAttribute("nextUnHappy");
            String className = el.getAttribute("className");
            String arg = el.getAttribute("arg");
            String variable = el.getAttribute("variable");
            String sgroup = el.getAttribute("group");
            int group = 1;

            try
            {
                if(sgroup!=null && !sgroup.isEmpty())
                {   // That's an optional parameter.
                    group = Integer.valueOf(sgroup);
                }
            }
            catch(Exception e)
            {
            }

            int payload = 1;
            String spayload = el.getAttribute("payload");

            try
            {
                if(spayload!=null && !spayload.isEmpty())
                {
                    payload = Integer.valueOf(spayload);
                }
            }
            catch(Exception e)
            {
            }

            String learn = el.getAttribute("learn");

            // Split keywordss by ','.               
            String[] words = keywordTags.split(EngineConsts.DELIMITER_KEYWORDS);

            for(String word : words) 
            {   // Loop through all words.
                // Trim the word to remove spaces.
                word = word.trim();

                // Construct a new keyword.
                Keyword keyword = new Keyword();
                keyword.setMeans(word);
                keyword.setNext(nextID);
                keyword.put("nextHappy",nextHappy);
                keyword.put("nextUnHappy",nextUnHappy);
                keyword.setClassNameOfFunction(className);
                keyword.setArg(arg);
                keyword.setVariable(variable);
                keyword.setPayload(payload);
                keyword.setLearn(learn);
                keyword.setGroup(group);

                // Add the keyword to keywords array list.
                keywords.add(keyword);
            }
        }

        // Return all the keywords in the given node.
        return keywords;
    }

    /**
     * Load the invalid messages from data XML file.
     */
    private void loadInvalidMessages(){

        // Get document element.
        Element docEle = dom.getDocumentElement();

        // Get all node names for invalid messages.
        NodeList node = docEle.getElementsByTagName("InvalidMessages");

        // Get all message nodes inside invalid messages node.
        NodeList nl = ((Element)node.item(0)).getElementsByTagName("message");
        int len = nl==null ? 0 : nl.getLength();
         
        for(int i=0;i<len;i++)
        {   // If node is not null and has children.
            // Loop through all children.
            // Get message node.
            Element el = (Element)nl.item(i);

            // Get message and add it to invalid messages array.
            String message = el.getFirstChild().getNodeValue();
            invalidMessages.add(message);
        }
    }
    
    /**
     * Load into the model the default Keyword Objects.
     * 
     */
    private void loadDefaultKeywords(){
        
        // Get document element.
        Element docEle = dom.getDocumentElement();

        // Get all node names for invalid messages.
        NodeList nl = docEle.getElementsByTagName("defaultKeywords");
        int len = nl==null ? 0 : nl.getLength();
        
        for(int i=0;i<len;i++)
        {
            Element el = (Element)nl.item(i);
            defaultKeywords = getKeywords(el);
        }
    }
    
    /**
     * Load into the model the exit Keyword Objects.
     * 
     */
    private void loadExitKeywords(){
        
         // Get document element.
        Element docEle = dom.getDocumentElement();

        // Get all node names for invalid messages.
        NodeList nl = docEle.getElementsByTagName("exitKeywords");
        int len = nl==null ? 0 : nl.getLength();
        
        for(int i=0;i<len;i++)
        {
            Element el = (Element)nl.item(i);
            exitKeywords = getKeywords(el);
        }
    }
}