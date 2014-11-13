package RASupport.rasupport.rasupportconfig.modules.transportlayer;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import java.io.File;

/**
 * RASupport: receiver of XML and messages
 * Must to be implemented by the sender of each transport layer in each resource aggregation module (e.g., RAToolkit)
 * @author damianarellanes
 */
public interface RASupportReceiver {
    
    // For any scenario
    public void receiveObject(Object receivedObject, RASupportActions action);
    
    // For any scenario
    public void receiveObject(Object receivedObject, RASupportTopologyNode sender, RASupportActions action);
    
    // For real scenarios    
    public void receiveXML();
    
    // For simulated scenarios
    public void simulateReceiveXML(File rsFile, String aliasSender, RASupportActions action);
    
    // For any scenario (sync message). Used commonly for acknowledges
    public RASupportActions receiveSyncMessage(String message, RASupportTopologyNode sender, RASupportActions action);
    
    // For any scenario (real or simulation)
    public void receiveMessage(String message, RASupportTopologyNode sender, RASupportActions action);
    
    // For simulated scenarios
    public void simulateReceiveMessages(String aliasSender, RASupportActions action, String...messages);
    
}
