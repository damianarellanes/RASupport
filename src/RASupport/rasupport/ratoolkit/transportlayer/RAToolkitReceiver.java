package RASupport.rasupport.ratoolkit.transportlayer;

import java.io.File;
import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportActions;
import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportReceiver;
import RASupport.rasupport.ratoolkit.common.RAToolkitAdvertisementAPI;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.*;

/**
 * RAToolkit: receiver from RAToolkit
 * @author Damian Arellanes
 */
public class RAToolkitReceiver implements RASupportReceiver {
    
    RAToolkitAdvertisementAPI advertisementAPI;
    
    public RAToolkitReceiver(RAToolkitAdvertisementAPI adv) {
        this.advertisementAPI = adv;
    }

    @Override
    public void receiveXML() {
        // TODO implement
        // Write the received XML in a file
        // Path rsPath = getPath of the XML created
        // this.resourceAggregation.updateRSindex(rsPath);
        
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public void simulateReceiveXML(File rsFile, String aliasSender, RASupportActions action) {
       
        if(action.equals(CREATE_RS)) {
            advertisementAPI.createRSInDatabase(rsFile, aliasSender);
        }
    }

    @Override
    public void receiveMessage(String message, String aliasSender) {        
    }
    
    @Override
    public void simulateReceiveMessages(String aliasSender, RASupportActions action, String...messages) {
        
        if(action.equals(UPDATE_ATTRIBUTE)) {
            advertisementAPI.UpdateAttributeInDatabase(messages[0], messages[1], aliasSender);
        }
    }

}
