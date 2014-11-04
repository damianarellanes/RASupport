package RASupport.rasupport.ratoolkit.advertisementapi;

import myconet.MycoNode;
import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.ratoolkit.advertisementapi.agents.*;
import RASupport.rasupport.ratoolkit.advertisementapi.rs.RSpec;
import static RASupport.rasupport.ratoolkit.common.Common.Agents.*;
import RASupport.rasupport.ratoolkit.common.RAToolkitAdvertisementAPI;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import java.io.File;

/**
 * RAToolkit: facade of the resource advertisement API
 * @author Damian Arellanes
 */
public class AdvertisementAPI implements RAToolkitAdvertisementAPI {
    
    private DatabaseManager databaseManager = null;    
    private RSpec rspec = null;
    
    // Keeping only a reference two both advertisement agents, in order to send copies 
    // and do not create an object for each advertisement agent sending
    private AdvertisementAgentsFactory agentsFactory = null;
    private AdvertisementAgentInitial initialAgent = null;    
    private AdvertisementAgentUpdating updatingAgent = null;    
    
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
        
        // Creates advertisement agents
        agentsFactory = new AdvertisementAgentsFactory();
        
        initialAgent = (AdvertisementAgentInitial) 
                agentsFactory.create(AGENT_ADVERTISEMENT_INITIAL, rspec, 
                        this.myconetAlias, this.databaseManager);
        
        updatingAgent = (AdvertisementAgentUpdating) 
                agentsFactory.create(AGENT_ADVERTISEMENT_UPDATING, rspec, 
                        this.myconetAlias, this.databaseManager);
        
        // Inserts the own lindex in the database (peer and RS)
        insertOwnIndex();
                
    } 
    
    private void insertOwnIndex() {
        databaseManager.insertPeer(myconetAlias);
        initialAgent.computeRS(rspec.get().getAbsolutePath(), myconetAlias);
    }

    @Override
    public void advertiseRSTo(MycoNode superpeer) {       
       
        if(superpeer != null) {
            initialAgent.send(myconetPeer, superpeer);
        }        
        else {
            logError("impossible to advertise resources from normal peer " + myconetPeer.getID() + " because it is not connected to a super-peer");
        }
    }

    @Override
    public void advertiseUpdatingTo(String attribute, String newValue) {
        
        // The updating agent updates the RS common between advertisement agents
        // The agent updates the own database and the own RS (only resources block)
        updatingAgent.updateRS(attribute, newValue);
        
        // Sends the updating agent only if a super-peer available exists
        RASupportTopologyNode sp = myconetPeer.getSuperpeer();
        if(sp != null) { 
            
            // Sends an updating advertisement agent to the corresponding super-peer
            //logMessage("NORMAL-PEER " + myconetPeer + " updates " + attribute + "=" + newValue + " in " + sp);
            updatingAgent.send(myconetPeer, (MycoNode) sp, attribute, newValue);
        }
        
    }
    
    @Override
    public void receiveUpdating(String attribute, String newValue, String aliasSender) {
        
        logMessage("SUPER-PEER " + myconetAlias + " has received an UPDATING AGENT from " +
                aliasSender + ": " + attribute + "=" + newValue);
        databaseManager.updateAttribute(attribute, newValue, aliasSender);
    }

    @Override
    public void receiveRS(File rsFile, String aliasSender) {
        
        /*if(mycoNode.isBiomass()) {
            return;
        }*/
        
        logMessage("SUPER-PEER " + myconetPeer + 
                " has received a INITIAL AGENT from " + aliasSender);
        
        // Inserts the sender peer into the database        
        databaseManager.insertPeer(aliasSender);
        
        // Parses the RS and inserts the attributes, and use restrictions        
        initialAgent.computeRS(rsFile.getAbsolutePath(), aliasSender);        

    }       
    
}
