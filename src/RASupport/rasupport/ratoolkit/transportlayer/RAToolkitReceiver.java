package RASupport.rasupport.ratoolkit.transportlayer;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportActions;
import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportReceiver;
import RASupport.rasupport.ratoolkit.advertisementapi.agents.AdvertisementAgentInitial;
import RASupport.rasupport.ratoolkit.advertisementapi.agents.AdvertisementAgentUpdating;
import RASupport.rasupport.ratoolkit.apismanagement.RAToolkitAdvertisementAPI;
import RASupport.rasupport.ratoolkit.apismanagement.RAToolkitSelectionAPI;
import RASupport.rasupport.ratoolkit.common.*;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.*;
import java.io.File;
import myconet.MycoNode;

/**
 * RAToolkit: receiver from RAToolkit
 * @author Damian Arellanes
 */
public class RAToolkitReceiver implements RASupportReceiver {
    
    // The receiver needs a reference to each API in order to perform actions
    RAToolkitAdvertisementAPI advertisementAPI;
    RAToolkitSelectionAPI selectionAPI;
    
    public RAToolkitReceiver(RAToolkitAdvertisementAPI adv, RAToolkitSelectionAPI sel) {
        
        this.advertisementAPI = adv;
        this.selectionAPI = sel;        
    }
    
    @Override
    public void receiveObject(Object receivedObject, RASupportActions action) {
        
        if(action.equals(CREATE_RS)) {
            advertisementAPI.receiveInitialAgent(
                    (AdvertisementAgentInitial) receivedObject
            );
        }
        else if(action.equals(UPDATE_ATTRIBUTE)) {
            advertisementAPI.receiveUpdatingAgent(
                    (AdvertisementAgentUpdating) receivedObject
            );
        }
    }

    @Override
    public void receiveXML() {        
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public void simulateReceiveXML(File rsFile, String aliasSender, RASupportActions action) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void receiveMessage(String message, RASupportTopologyNode sender, RASupportActions action) { 
        
        // Test query since the received query id
        if(action.equals(TEST_QUERY)) {
            selectionAPI.testQuery(Long.parseLong(message), (MycoNode) sender);
        }
    }
    
    @Override
    public void simulateReceiveMessages(String aliasSender, RASupportActions action, String...messages) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
