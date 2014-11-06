package RASupport.rasupport.ratoolkit.transportlayer;

import java.io.File;
import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportActions;

/**
 * RAToolkit: this is the proxy to inovke remote objects (remote receivers)
 * For our simulations, we are invoking the method directly, but is neccesary to create sockets
 * @author Damian Arellanes
 */
public class RAToolkitSender {
    
    // Sends an object/agent over the network
    public static void sendObject(Object object, RASupportTopologyNode receiver,
            RASupportActions action) {
        
        receiver.getRASupport().getReceiver().receiveObject(object, action);
        
    }
    
    // Sends a xml message (from xmlPath) from sender to receiver.
    // The receiver performs the specified action
    public static void sendXML(File xmlPath, RASupportTopologyNode sender, RASupportTopologyNode receiver, 
            RASupportActions action) {
        
        // For simulations, we use simulateReceiveXML() instead of receiveXML()
        receiver.getRASupport().getReceiver().simulateReceiveXML(xmlPath, sender.getAlias(), action);

    }

    public static void sendMessage(String message, RASupportTopologyNode sender, RASupportTopologyNode receiver) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    public static void sendMessages(RASupportTopologyNode sender, RASupportTopologyNode receiver,
            RASupportActions action, String...messages) {
        
        // For simulations
        receiver.getRASupport().getReceiver().simulateReceiveMessages(sender.getAlias(), action, messages);
        
    }
    
}
