package RASupport.rasupport.rasupportconfig.modules.transportlayer;

import java.io.File;

/**
 * RASupport: receiver of XML and messages
 * Must to be implemented by the sender of each transport layer in each resource aggregation module (e.g., RAToolkit)
 * @author damianarellanes
 */
public interface RASupportReceiver {
    
    // For any scenario
    public void receiveObject(Object receivedObject, RASupportActions action);
    
    // For real scenarios    
    public void receiveXML();
    
    // For simulated scenarios
    public void simulateReceiveXML(File rsFile, String aliasSender, RASupportActions action);
    
    // For any scenario (real or simulation)
    public void receiveMessage(String message, String aliasSender);
    
    // For simulated scenarios
    public void simulateReceiveMessages(String aliasSender, RASupportActions action, String...messages);
    
}
