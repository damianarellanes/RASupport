package RASupport.rasupport.ratoolkit.advertisementapi.agents;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.ratoolkit.common.Agent;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.UPDATE_ATTRIBUTE;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitSender;

/**
 *
 * @author Damian Arellanes
 */
public class AdvertisementAgentUpdating implements Agent {
    
    private String attributeUpdating = "";
    private String newValueUpdating = "";       
    
    private String peerOwner = "";
    
    
    public AdvertisementAgentUpdating(String peerOwner, String attribute, String value) {
        
        this.peerOwner = peerOwner; 
        
        this.attributeUpdating = attribute;
        this.newValueUpdating = value;
    }
    
    // Updates the database of the visited super-peer
    public void behaveInSP(DatabaseManager dbMan) {
        
        dbMan.updateAttribute(attributeUpdating, newValueUpdating, peerOwner);
    }

    @Override
    public String getSender() {
        return peerOwner;
    }

    @Override
    public boolean sendTo(RASupportTopologyNode receiver) {
        RAToolkitSender.sendObject(this, receiver, UPDATE_ATTRIBUTE); 
        
        return true;
    }

    /**
     * @return the attributeUpdating
     */
    public String getAttributeUpdating() {
        return attributeUpdating;
    }

    /**
     * @return the newValueUpdating
     */
    public String getNewValueUpdating() {
        return newValueUpdating;
    }
}
