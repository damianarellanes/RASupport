package RASupport.rasupport.ratoolkit.advertisementapi.agents;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.ratoolkit.common.Agent;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.CREATE_RS;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitSender;

/**
 * RAToolkit:: Advertisement phase: representation of an initial agent
 * Initial advertisement agents are sent from normal peers to super-peers when:
 * - The normal peer joins at first time to a super-peer
 * - The normal peer change of super-peer; in this case, the agent is sent to the new super-peer
 * @author damianarellanes
 */
public class AdvertisementAgentInitial implements Agent {
    
    //private File rspec = null;    // For real scenarios
    private String rspecPath = null;  // For simulations
    private String ownerAlias = "";
    
    public AdvertisementAgentInitial(String rspecPath, String peerAlias) {
        this.rspecPath = rspecPath;
        this.ownerAlias = peerAlias;
    }     
    
    public void behaveInSP(DatabaseManager spDatabase, InitialManager spInitialAgentsManager) {        
        
        // Inserts the sender peer into the database        
        spDatabase.insertPeer(ownerAlias);
        
        // Insert the transporting rspec into the visited super-peer
        spInitialAgentsManager.computeRS(rspecPath, ownerAlias);
    }
            
    @Override
    public String getSender() {
        return this.ownerAlias;
    }

    @Override
    public void sendTo(RASupportTopologyNode receiver) {
        /*logMessage("sending initial advertisement agent from " + sender.getID() + 
                " to " + receiver.getID());*/
        
        // Sends a initial RS (initialRSPath) from normal peer (sender) to its super-peer (receiver)                
        RAToolkitSender.sendObject(this, receiver, CREATE_RS);         
    }
}
