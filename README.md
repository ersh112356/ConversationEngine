ConversationEngine

An overview:
The ConversationEngine is a highly sophisticated chat bot which is fully implemented in Java.
The Conversation Engine is designed to follow a human conversation by providing dynamically responses to human texts. The Conversation Engine is fully customized to allow many conversation scripts.

Q-Learning style solution:
The conversation engine is partially implementing a Q-Learning technique. For more details on Q-Learning, please see https://en.wikipedia.org/wiki/Q-learning.
The basic model follows ground rules where the Agent is represented by the engine itself, the policy is the Planner, and the environment is a combination of the human and the XML data (the conversation scripts).  
XML provides the states in which the conversation jump into.
The human interacts with text replies. And the sentiment of his replies is used as a reward function.

So how it all connect:

It all starts with the Conversation Engine. 
1. The Conversation Engine is responsible for uploading needed modules, the Planner and the Dictionary.
2. The Conversation Engine is used as an access point to fetch a response to the human text.

The Dictionary is holding information that was gathered while the human is having a conversation.

The internal flow of finding a response is ran by the Planner. The Planner is used to navigate to the next state of the conversation. Each state has a role in the conversation, for instance, greeting, closure and more.

States of the conversation are held by an XML file. In the real-life scenario, the number of states might go very big and those states are difficult to be held by the XML. By implementing a customized Model (the one that holds the states) and using DI (dependency injection) to load the Model via the Creator, it’s easy to have the states stored inside a storage. For instance, MongoDB will fit in nicely. 

In general, 
1. The Planner is running thru Functions to find the best state match.
2. The Planner, as per state, is collecting information about the conversation, such as the name, the points of interest, interesting information the human wishes to talk about and so on, so those can be used to enrich the conversation down the road.
3. The human text is analyzed via sentiment analysis to figure out whether the human is fed up with this line of the conversation.
4. The sentiment of the human text is used to indicate whether the course of the conversation is going well. Where negative sentiment will lead to alternative path, and positive sentiment will continue the same path. 
5. Finally, when the new next state is selected, one of the predefined responses, which are embedded inside the state, is selected randomly.
6. If the response contains tags, it can now be replaced with the proper information that was gathered during the conversation.
7. The content of the dictionary can be held inside a java.util.HashMap. In this case it won’t be persistence for the next time. It can also get Hazelcast Map and keep persistence.

General guidelines:
1. To allow as much as possible of a leverage of customization, a simple DI (dependency injection) is used via a properties file. Object to be injected are written inside the file.
2. If no entry is found inside the properties file, the default is taken. This way the engine can run in a default mode without any farther configuration.
3. of course, you can make your own Planner to implement your own flow and business logic.



Future efforts:
1. To allow more feasibility and avoiding exhaustion of the inner ready to use replies, dynamic functions can be provided. For instance, News function can take a hot news topic to enrich the conversation with an up-to-date topic.
2. Yet to be implemented, a news feed that will provide a fresh and updated view of what’s going on at the human world domain to the conversation.
3. Implemented. Adding an action that can, optionally be invoked in parallel to responding to human text. For instance, "I want to buy a car". Action: invoke a Goole search and push options to bay a car.

Minimum requirement:
- Java8 and above (mandatory)
- Lucene 5.4 and above (for using the default Planner, ConversationPlannerImpl, and StateDetector)
- Guava 16.0 and above.


Instructions of usage:
1. download ConversationEngine.jar and all its dependencies (Logger.jar, Utils.jar, Classifier.jar, Injector.jar, lucene-analyzer-common-5.4.0.jar, lucene-classification-5.4.0.jar, lucene-code-5.4.0.jar, lucene-misc-5.4.0.jar, lucene-queries-5.4.0.jar, lucene-queryparser-5.4.0.jar, quava-16.0.1.jar).
2. create your own engine code like this:

public static void main(String[] args){

// That's a must in order to run properly. Use your own path.
Preferences.init("../props/conf/properties.default");
LoggerManager.initiate();

// A new instance of the default DataModel is created here.        
CreateDaytaModel creator = new CreateDataModel("../props/engine/data_en.xml");
Model dp = creator.getDataModel();
ConversationEngine engine = new ConversationEngine();
engine.initEngine("0",dp,"en");

// Run in a loop, until 'exit' is typed.
String message;

while((message=JOptionPane.showInputDialog(null,"Enter some text:"))!=null)
{
    if(message.equals("exit"))
    {
        break;
    }
                
    System.out.println(message);
    String result = engine.getReply(message);
    System.out.println(result);
    
    System.out.println("Enter some text: ");
}
}
        
Where:
"../props/conf/properties.default" is the path to the properties file (can also be found here).
"../props/engine/data_en.xml" is the path to the XML file that controls the flow (a toy one is provided).

If you’re using the default injection, you need to put inside the properties file the path to the Lucene index files (toy classification files are provided, please make sure to train your own for best results).
