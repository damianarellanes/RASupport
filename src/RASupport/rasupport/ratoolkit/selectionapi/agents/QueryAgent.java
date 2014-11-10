package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.config.RASupportConfigParser;
import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.ratoolkit.common.Agent;
import RASupport.rasupport.ratoolkit.common.RAToolkitConfigParser;
import myconet.MycoNode;

/**
 * SelectionAPI: representation of a query agent
 * @author Damian Arellanes
 */
public class QueryAgent implements Agent {
    
    RASupportQuery query = null;
    String peerOwner = "";
    QueryAgentsBehaviour selectedProtocol;
    RASupportMap exclusionList = null; // Exclusion list to avoid query duplicates in super-peers
    
    public QueryAgent(RASupportQuery query, String peerOwner, QueryAgentsBehaviour protocol, RASupportMap exclusionList) {
        this.query = query;
        this.peerOwner = peerOwner;
        this.selectedProtocol = protocol;
        this.exclusionList = exclusionList;
    }

    @Override
    public String getSender() {
        return peerOwner;
    }

    @Override
    public void sendTo(RASupportTopologyNode receiver) {        
    }        
    
    // Algorithm 4 from selection phase
    private void testSuperpeer(MycoNode sp) {
        
        if(exclusionList.size() <= RAToolkitConfigParser.raToolkit_maxExclusion) {
            
            // If sp doesn't exists in the exclusion list
            if(!exclusionList.containsKey(sp)) {
                sendTestMessageTo(sp);
            }
        }
        else {
            
            // If the exclusion list is bigger than the limit, don't search on it and only send a test message
            sendTestMessageTo(sp);            
        }
    }
    
    // Algorithm 5 from selection phase
    private void sendTestMessageTo(MycoNode sp) {
        
        //sp.getRASupport().getReceiver().receiveMessage(query.getId(), peerOwner);
        // Waits acknowledge
        boolean ack = true;
        
        if(ack) {
            // Visits the target superpeer
        }
    }

}
