package RASupport.rasupport.ratoolkit.advertisementapi.agents;

import myconet.MycoNode;
import RASupport.rasupport.ratoolkit.advertisementapi.rs.RSpec;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.CREATE_RS;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitSender;

/**
 * RAToolkit:: Advertisement phase: representation of an initial agent
 * Initial advertisement agents are sent from normal peers to super-peers when:
 * - The normal peer joins at first time to a super-peer
 * - The normal peer change of super-peer; in this case, the agent is sent to the new super-peer
 * @author damianarellanes
 */
public class AdvertisementAgentInitial implements AdvertisementAgent {
    
    private RSpec rspec = null;
    
    public AdvertisementAgentInitial(RSpec rspec) {
        this.rspec = rspec;
    }      
    
    @Override
    public void send(MycoNode sender, MycoNode receiver) {
        
        /*logMessage("sending initial advertisement agent from " + sender.getID() + 
                " to " + receiver.getID());*/
        
        // Sends a initial RS (initialRSPath) from normal peer (sender) to its super-peer (receiver)                                        
        RAToolkitSender.sendXML(rspec.get(), sender, receiver, CREATE_RS);                
    }
}
