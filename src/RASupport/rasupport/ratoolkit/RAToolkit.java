package RASupport.rasupport.ratoolkit;

import RASupport.myconet.MycoNode;
import RASupport.rasupport.ratoolkit.apismanagement.APIFactory;
import static RASupport.rasupport.ratoolkit.common.Common.AdvertisementAPIS.*;
import static RASupport.rasupport.ratoolkit.common.Common.DatabaseManagers.*;
import RASupport.rasupport.ratoolkit.common.RAToolkitAPI;
import RASupport.rasupport.ratoolkit.common.RAToolkitAdvertisementAPI;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManagerFactory;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitReceiver;
import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceAggregation;
import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;

/**
 * RAToolkit: facade of the toolkit for resource aggregation
 * This toolkit is using myconet to control our P2P overlay network (based on super-peers)
 * We have to cast RASupportTopologyNode to MyconetNode (myconet is the topology manager for this toolkit)
 * @author Damian Arellanes
 */
public class RAToolkit implements RASupportResourceAggregation {
    
    // In our toolkit we have a reference to each API
    // We can change APIS and the database manager easily because they are modules
    private DatabaseManager databaseManager = null;
    private RAToolkitAdvertisementAPI advertisementAPI = null;
    private RAToolkitAPI selectionAPI;
    private RAToolkitAPI matchingAPI;
    private RAToolkitAPI bindingAPI;
    private RASupportNode peer = null;
    private MycoNode myconetPeer = null;
    
    private RAToolkitReceiver receiver = null;
    
    public RAToolkit(RASupportNode peer) {
        
        // Creates the database manager. We are selecting SQLite
        databaseManager = DatabaseManagerFactory.buildDatabaseManager(SQLITE, 
                peer.getTopologyNode().getIdNode());
        
        // Sets the reference to the peer/node
        this.peer = peer;                
        
        // Sets the reference to MYconet peer
        this.myconetPeer = (MycoNode) peer.getTopologyNode();
        
        // Creates APIs                
        advertisementAPI = APIFactory.createAdvertisementAPI(ADVERTISEMENT_API_DEFAULT, peer, databaseManager);                
        
        // Creates the receiver
        this.receiver = new RAToolkitReceiver(advertisementAPI);
    }
    
    @Override
    public void advertiseResourcesTo(RASupportTopologyNode targetNode) {
        advertisementAPI.advertiseRSTo((MycoNode) targetNode);
    }

    @Override
    public RAToolkitReceiver getReceiver() {
        return this.receiver;
    }
    
    @Override
    public void deleteIndexEntry(String normalPeer) {
        
        logMessage("SUPER-PEER " + peer.getTopologyNode().getAlias() + " DELETES " + normalPeer + " from its database");
        databaseManager.executeSentence("DELETE FROM Peers WHERE namePeer='" + normalPeer + "'");
    }

    @Override
    public void cleanIndexTable() {
        databaseManager.restartDatabase();
    }

    @Override
    public void updateDynamicResource(String attribute, String newValue) {
        advertisementAPI.SendAttributeUpdating(attribute, newValue);        
    }
}