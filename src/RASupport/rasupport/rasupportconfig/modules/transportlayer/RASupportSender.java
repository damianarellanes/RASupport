package RASupport.rasupport.rasupportconfig.modules.transportlayer;

import java.io.File;
import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;

/**
 * RASupport: sender of XML and messages
 * Must to be implemented by the sender of each transport layer in each resource aggregation module (e.g., RAToolkit)
 * @author Damian Arellanes
 */
public interface RASupportSender {
    
    public void sendObject(Object object, RASupportTopologyNode sender, RASupportTopologyNode receiver);
    
    public void sendXML(File rsFile, RASupportTopologyNode sender, RASupportTopologyNode receiver);

    public void sendMessage(String message, RASupportTopologyNode sender, RASupportTopologyNode receiver);
}
