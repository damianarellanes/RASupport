package RASupport.rasupport.ratoolkit.advertisementapi;

import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import RASupport.rasupport.ratoolkit.advertisementapi.agents.*;
import RASupport.rasupport.ratoolkit.advertisementapi.rs.RSpec;
import RASupport.rasupport.ratoolkit.common.RAToolkitAdvertisementAPI;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import myconet.MycoNode;
import simulation.AdvertisementObserver;// Only for experiments
import simulation.NetworkObserver;

/**
 * RAToolkit: facade of the resource advertisement API
 * @author Damian Arellanes
 */
public class AdvertisementAPI implements RAToolkitAdvertisementAPI {
    
    private DatabaseManager databaseManager = null;    
    private RSpec rspec = null;
    
    private InitialManager initialAgentsManager = null;
    private UpdatingManager updatingManager = null;
    
    private RASupportNode node = null;
    private MycoNode myconetPeer = null;
    private String myconetAlias = "";
    
    public AdvertisementAPI(RASupportNode node, DatabaseManager dbMan) {
        
        // Sets the node
        this.node = node;
        this.myconetPeer = (MycoNode) node.getTopologyNode();
        this.myconetAlias = myconetPeer.getAlias();
        
        // Sets the database manager
        this.databaseManager = dbMan;
        
        // Creates the initial RS
        // The initial RS is used by the advertisement agents, they act over the same RS
        rspec = new RSpec(myconetPeer.getAlias(), node.getUseRestrictions(), 
                node.getDynamicAttributes(), node.getStaticAttributes());
        
        // Create the factory of advertisement agents
        AdvertisementAgentsFactory factory = new AdvertisementAgentsFactory();
        
        // Creates advertisment agents managers
        updatingManager = new UpdatingManager(rspec, 
                this.myconetPeer, this.databaseManager, factory);
        
        initialAgentsManager = new InitialManager(rspec, 
                        this.myconetAlias, this.databaseManager, factory);
                
        // Inserts the own lindex in the database (peer and RS)
        insertOwnIndex();
                
    } 
    
    private void insertOwnIndex() {
        databaseManager.insertPeer(myconetAlias);
        initialAgentsManager.computeRS(rspec.get().getAbsolutePath(), myconetAlias);
    }

    @Override
    public void advertiseRSTo(MycoNode superpeer) {       
       
        initialAgentsManager.sendInitialAgent(superpeer);                
    }

    @Override
    public void advertiseUpdating(String attribute, String newValue) {
        
        updatingManager.sendUpdatingAgent(attribute, newValue);                
    }
    
    @Override
    public void receiveUpdatingAgent(AdvertisementAgentUpdating updatingAgent) {     
        
        logMessage("SUPER-PEER " + myconetAlias + " has received an UPDATING AGENT from " +
                updatingAgent.getSender() + ": " + 
                updatingAgent.getAttributeUpdating() + "=" + updatingAgent.getNewValueUpdating());
        
        // Only for experiments
        AdvertisementObserver.incUpdatingAgentsCount();
        NetworkObserver.incUpdatingAgentsCount();
        
        updatingAgent.behaveInSP(databaseManager);
    }

    @Override
    public void receiveInitialAgent(AdvertisementAgentInitial initialAgent) {
        
        logMessage("SUPER-PEER " + myconetPeer + 
                " has received a INITIAL AGENT from " + initialAgent.getSender());
        
        // Only for experiments
        AdvertisementObserver.incInitialAgentsCount();
        NetworkObserver.incInitialAgentsCount();
                
        initialAgent.behaveInSP(databaseManager, initialAgentsManager);
    }
    
}
