package RASupport.rasupport.ratoolkit.advertisementapi.agents;

import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportUseRestrictions;
import RASupport.rasupport.ratoolkit.advertisementapi.rs.RSpec;
import static RASupport.rasupport.ratoolkit.common.Common.Agents.AGENT_ADVERTISEMENT_INITIAL;
import static RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseErrors.NO_PEERID;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import myconet.MycoNode;

/**
 * RAToolkit: manager of initial advertisment agents
 * @author Damian Arellanes
 */
public class InitialManager {
    
    private RSpec rspec = null;
    
    private String peerAlias = "";
    private DatabaseManager dbMan = null;
    private AdvertisementAgentsFactory  agentsFactory = null;
    
    public InitialManager(RSpec rspec, String peerAlias, DatabaseManager dbMan, 
            AdvertisementAgentsFactory factory) {
        this.rspec = rspec;
        this.peerAlias = peerAlias;
        this.dbMan = dbMan;
        this.agentsFactory = factory;
    }   
    
    public void sendInitialAgent(MycoNode superpeer){
        if(superpeer != null) {
            agentsFactory.create
                (AGENT_ADVERTISEMENT_INITIAL, rspec.get().getAbsolutePath(), peerAlias)
                    .sendTo(superpeer);
        }        
        else {
            logError("impossible to advertise resources from normal peer " + peerAlias + " because it is not connected to a super-peer");
        }
    }
    
    public void computeRS(String rsPath, String peerAlias) {                
        
        // Retrieves the id of the new peer
        int peerId = dbMan.getPeerId(peerAlias);
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
                            
                            dbMan.insertAttribute(
                                    peerId, foundTag, reader.getText().trim());
                        } else if (useRestriction) {

                            dbMan.insertUseRestriction(
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
