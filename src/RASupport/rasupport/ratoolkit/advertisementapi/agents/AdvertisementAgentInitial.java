package RASupport.rasupport.ratoolkit.advertisementapi.agents;

import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportUseRestrictions;
import myconet.MycoNode;
import RASupport.rasupport.ratoolkit.advertisementapi.rs.RSpec;
import static RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseErrors.NO_PEERID;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.CREATE_RS;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitSender;
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
 * RAToolkit:: Advertisement phase: representation of an initial agent
 * Initial advertisement agents are sent from normal peers to super-peers when:
 * - The normal peer joins at first time to a super-peer
 * - The normal peer change of super-peer; in this case, the agent is sent to the new super-peer
 * @author damianarellanes
 */
public class AdvertisementAgentInitial implements AdvertisementAgent {
    
    private RSpec rspec = null;
    
    private String peerAlias = "";
    private DatabaseManager dbMan = null;
    
    public AdvertisementAgentInitial(RSpec rspec, String peerAlias, DatabaseManager dbMan) {
        this.rspec = rspec;
        this.peerAlias = peerAlias;
        this.dbMan = dbMan;
    }      
    
    @Override
    public void send(MycoNode sender, MycoNode receiver) {
        
        /*logMessage("sending initial advertisement agent from " + sender.getID() + 
                " to " + receiver.getID());*/
        
        // Sends a initial RS (initialRSPath) from normal peer (sender) to its super-peer (receiver)                                        
        RAToolkitSender.sendXML(rspec.get(), sender, receiver, CREATE_RS);                
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
