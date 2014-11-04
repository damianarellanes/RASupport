package RASupport.rasupport.ratoolkit.advertisementapi;

import RASupport.myconet.MycoNode;
import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.resourcesmodel.*;
import RASupport.rasupport.ratoolkit.advertisementapi.agents.*;
import RASupport.rasupport.ratoolkit.advertisementapi.rs.RSpec;
import static RASupport.rasupport.ratoolkit.common.Common.Agents.*;
import RASupport.rasupport.ratoolkit.common.RAToolkitAdvertisementAPI;
import static RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseErrors.*;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * RAToolkit: facade of the resource advertisement API
 * @author Damian Arellanes
 */
public class AdvertisementAPI extends RAToolkitAdvertisementAPI {
    
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
                agentsFactory.create(AGENT_ADVERTISEMENT_INITIAL, rspec);
        
        updatingAgent = (AdvertisementAgentUpdating) 
                agentsFactory.create(AGENT_ADVERTISEMENT_UPDATING, rspec, 
                        this.myconetAlias, this.databaseManager);
        
        // Inserts the own lindex in the database (peer and RS)
        insertOwnIndex();
                
    } 
    
    private void insertOwnIndex() {
        databaseManager.insertPeer(myconetAlias);
        this.computeRS(rspec.get().getAbsolutePath(), myconetAlias);
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
    public void SendAttributeUpdating(String attribute, String newValue) {
        
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
    public void UpdateAttributeInDatabase(String attribute, String newValue, String aliasSender) {
        
        logMessage("SUPER-PEER " + myconetAlias + " has received an UPDATING AGENT from " +
                aliasSender + ": " + attribute + "=" + newValue);
        databaseManager.updateAttribute(attribute, newValue, aliasSender);
    }

    @Override
    public void createRSInDatabase(File rsFile, String aliasSender) {
        
        /*if(mycoNode.isBiomass()) {
            return;
        }*/
        
        logMessage("SUPER-PEER " + myconetPeer + 
                " has received a INITIAL AGENT from " + aliasSender);
        
        // Inserts the sender peer into the database        
        databaseManager.insertPeer(aliasSender);
        
        // Parses the RS and inserts the attributes, and use restrictions        
        computeRS(rsFile.getAbsolutePath(), aliasSender);        

    }       
    
    private void computeRS(String rsPath, String peerAlias) {                
        
        // Retrieves the id of the new peer
        int peerId = databaseManager.getPeerId(peerAlias);
        if (peerId == NO_PEERID) {
            logError("error inserting a RS for " + peerAlias);
            exit(0);
        }
        
        // Parses the resource specification from rsPath
        XMLInputFactory factory = XMLInputFactory.newInstance();
        boolean useRestriction = false, attribute = false;
        String foundTag = "";

        try {
            InputStream inStream = new FileInputStream(rsPath);

            XMLStreamReader reader = factory.createXMLStreamReader(inStream);

            int event;
            while (reader.hasNext()) {
                event = reader.next();

                switch (event) {

                    case XMLStreamConstants.START_ELEMENT:
                        try {
                            String c = reader.getLocalName();
                            if (RASupportDynamicAttributes.valueOf(reader.getLocalName()) != null) {
                                //System.out.println("Found dynamic = " + reader.getLocalName());
                                attribute = true;
                                foundTag = reader.getLocalName();
                            }

                        } catch (IllegalArgumentException ex) {

                            try {
                                if (RASupportStaticAttributes.valueOf(reader.getLocalName()) != null) {
                                    //System.out.println("Found static = " + reader.getLocalName());
                                    attribute = true;
                                    foundTag = reader.getLocalName();
                                }
                            } catch (IllegalArgumentException ex2) {

                                try {

                                    if (RASupportUseRestrictions.valueOf(reader.getLocalName()) != null) {
                                        //System.out.println("Found use = " + reader.getLocalName());
                                        useRestriction = true;
                                        foundTag = reader.getLocalName();
                                    }

                                } catch (IllegalArgumentException ex3) {
                                }
                            }
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        if (attribute) {
                            
                            databaseManager.insertAttribute(
                                    peerId, foundTag, reader.getText().trim());
                        } else if (useRestriction) {

                            databaseManager.insertUseRestriction(
                                    peerId, foundTag, reader.getText().trim());
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        useRestriction = attribute = false;
                        break;

                    /*case XMLStreamConstants.START_DOCUMENT:                    
                     break;*/
                }
            }

            inStream.close();
           

        } catch (FileNotFoundException | XMLStreamException ex) {
            logError(ex);
            exit(0);
        } catch (IOException ex) {
            logError(ex);
            exit(0);
        }
    }


}
