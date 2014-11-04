package RASupport.rasupport.ratoolkit.advertisementapi.agents;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import myconet.MycoNode;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;
import RASupport.rasupport.rasupportconfig.xml.TagsConfiguration;
import static RASupport.rasupport.rasupportconfig.xml.TagsConfiguration.indentationLevelThree;
import RASupport.rasupport.ratoolkit.advertisementapi.rs.RSpec;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.UPDATE_ATTRIBUTE;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitSender;

/**
 * RAToolkit:: Advertisement phase: representation of an updating agent
 * Updating advertisement agents are sent when:
 * - The monitor detects a change in the value of a dynamic attribute; in this case, the agent is sent to the super-peer in charge of
 * @author damianarellanes
 */
public class AdvertisementAgentUpdating implements AdvertisementAgent {
    
    private Map patterns = null;
    private RSpec rspec = null;
    
    private String peerAlias = "";
    private DatabaseManager dbMan = null;
    
    public AdvertisementAgentUpdating(RSpec rspec, String peerAlias, DatabaseManager dbMan) {
        
        this.rspec = rspec;
        
        this.peerAlias = peerAlias;
        this.dbMan = dbMan;
        
        patterns = new HashMap();
        computePatterns(RASupportDynamicAttributes.class);
        computePatterns(RASupportStaticAttributes.class);
    }
    
    private <E> void computePatterns(Class<E> list) {
        for(E e: list.getEnumConstants()) {
            Pattern p = Pattern.compile("<" + e + ">(.*)" + "</" + e + ">");
            patterns.put(e.toString(), p);
        }
    }
    
    public void updateRS(String tag, String value) {
        
        // Inserts the updating in the own database
        dbMan.updateAttribute(tag, value, peerAlias);
        
        // Updates the own RS (only resources block)
        String oldResources = rspec.getBlockResources();
        String newTag = TagsConfiguration.createTag(tag, value, indentationLevelThree);
        
        rspec.setBlockResources(
            ((Pattern)patterns.get(tag)).matcher(oldResources).replaceFirst(newTag)
        );        
    }
    
     
    // The updating one only sends a message with the attribute and the value, in order to reduce the bandwidth traffic and performance
    public void send(MycoNode np, MycoNode sp, String attribute, String value) {
        
        // Updates the RS of the current peer and sends the updating to the super-peer (sp)        
        RAToolkitSender.sendMessages(np, sp, UPDATE_ATTRIBUTE, attribute, value);
    }

    @Override
    public void send(MycoNode sender, MycoNode receiver) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

}
