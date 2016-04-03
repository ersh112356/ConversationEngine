# ConversationEngine

An overview:
The ConversationEngine is an highly sofisticated chat bot implementation that's designed to meet dynamically fashion and on the fly sunjects while talking with humans.

This one contains an implementation of a conversation engine (chat bot).

The conversation engine is mostly controlled by an XML file that defines states.
A state can holds keyword (AKA, sort of function that determine whether the message belongs to this function, or not). On top of this, a state contains a set of messages that are used to reply to the message.
Each keyword contains directions of navigation (AKA, what would be the next state).
Derections are divided into three classes, one if the message contains no positive sentiment (mood) nor negative one. One for positive sentiment, and one for negative sentiment.
The flow is ran be the ConversationPlanner.
To allow more fecsibility and avoiding exhauseted of the inner ready to use replies, dynamic fuctions can be provided.
For instance, News function can take the hot news topic to enrich the conversatio with an uptodate topic.


Instructions:
1. download ConversationEngine.jar and all dependencies.
2. create your own Java code like this:

// That's a must in order to run properly.
Preferences.init("../props/conf/properties.default");
LoggerManager.initiate();

// A new instance is created here.        
CreateDataModel creator = new CreateDataModel("../props/engine/data_en.xml");
DataModel dp = creator.getDataModel();
ConversationEngine engine = new ConversationEngine("0",dp,"en");

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
        
Where:
"../props/conf/properties.default" is the path to the properties file (can also be found here).
"../props/engine/data_en.xml" is the path to the XML file that controlls the flow (a toy one is provided).

You need to put inside the properties file the path to the index files (toy classification files are provided, please make sure to train your own for best results).

