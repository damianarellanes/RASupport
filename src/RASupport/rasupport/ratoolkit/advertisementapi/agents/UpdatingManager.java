package RASupport.rasupport.ratoolkit.advertisementapi.agents;

import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;
import RASupport.rasupport.rasupportconfig.xml.TagsConfiguration;
import static RASupport.rasupport.rasupportconfig.xml.TagsConfiguration.indentationLevelThree;
import RASupport.rasupport.ratoolkit.advertisementapi.rs.RSpec;
import static RASupport.rasupport.ratoolkit.common.Common.Agents.AGENT_ADVERTISEMENT_UPDATING;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import myconet.MycoNode;

/**
 * RAToolkit:: Advertisement phase: representation of an updating agent
 * Updating advertisement agents are sent when:
 * - The monitor detects a change in the value of a dynamic attribute; in this case, the agent is sent to the super-peer in charge of
 * @author damianarellanes
 */
public class UpdatingManager {
        
    private Map patterns = null;
    private RSpec rspec = null;
    
    private MycoNode peerOwner = null;
    private String peerAlias = "";
    private DatabaseManager dbMan = null;
    private AdvertisementAgentsFactory agentsFactory = null;
        
    public UpdatingManager(RSpec rspec, MycoNode peerOwner, DatabaseManager dbMan, 
            AdvertisementAgentsFactory agentsFactory) {
        
        this.rspec = rspec;
        
        this.peerOwner = peerOwner;
        this.peerAlias = peerOwner.getAlias();
        this.dbMan = dbMan;
        this.agentsFactory = agentsFactory;
        
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
    public void sendUpdatingAgent(String attribute, String value) {
        
        // The updating agent updates the RS common between advertisement agents
        // The agent updates the own database and the own RS (only resources block)
        updateRS(attribute, value);
        
        // Sends the updating agent only if a super-peer available exists
        MycoNode sp = peerOwner.getSuperpeer();
        if(sp != null) { 
            
            // Sends an updating advertisement agent to the corresponding super-peer
            //logMessage("NORMAL-PEER " + myconetPeer + " updates " + attribute + "=" + newValue + " in " + sp);
            //updatingAgent.sendUpdatingAgent(myconetPeer, (MycoNode) sp, attribute, newValue);
            //RAToolkitSender.sendMessages(np, sp, UPDATE_ATTRIBUTE, attribute, value);
            
            agentsFactory.create
                (AGENT_ADVERTISEMENT_UPDATING, peerAlias, attribute, value)
                    .sendTo(sp);
        }                
    }
}
