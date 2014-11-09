package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.ratoolkit.common.Agent;

/**
 * SelectionAPI: representation of a query agent
 * @author Damian Arellanes
 */
public class QueryAgent implements Agent {
    
    String peerOwner = "";
    QueryAgentsBehaviour selectedProtocol;
    
    public QueryAgent(String peerOwner, QueryAgentsBehaviour protocol) {
        this.peerOwner = peerOwner;
        this.selectedProtocol = protocol;
    }

    @Override
    public String getSender() {
        return peerOwner;
    }

    @Override
    public void sendTo(RASupportTopologyNode receiver) {        
    }

}
