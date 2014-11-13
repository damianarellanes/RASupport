package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportActions;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.ratoolkit.common.Agent;
import RASupport.rasupport.ratoolkit.common.RAToolkitConfigParser;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.*;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitSender;
import myconet.HyphaLink;
import myconet.MycoNode;

/**
 * SelectionAPI: representation of a query agent
 * @author Damian Arellanes
 */
public class QueryAgent implements Agent {
    
    int ttl = 0;
    
    private RASupportQuery query = null;
    MycoNode spInitiator = null;
    String spInitiatorAlias = "";
    //QueryAgentsBehaviour selectedProtocol;
    RASupportMap<MycoNode, MycoNode> exclusionList = null; // Exclusion list to avoid query duplicates in super-peers
    
    
    public QueryAgent(RASupportQuery query, MycoNode peerOwner, int ttl) {
        
        this.query = query;
        this.spInitiator = peerOwner;
        this.spInitiatorAlias = peerOwner.getAlias();
        this.exclusionList = new RASupportMap<>();
        this.ttl = ttl;
        
        this.exclusionList.put(peerOwner, peerOwner); // The peerOwner has been visited because is SP_initiator        
    }
    
    public void performSelectionIn(MycoNode spVisited) {
        
        // Adds spVisited to the exclusion list
        exclusionList.put(spVisited, spVisited);
        
        performProtocol(spVisited);
    }
    

    @Override
    public String getSender() {
        return spInitiatorAlias;
    }

    @Override
    public void sendTo(RASupportTopologyNode receiver) {  
        
        testSuperpeer((MycoNode) receiver);
    }        
    
    public void performProtocol(MycoNode spVisited) {
        
        HyphaLink link = spVisited.getHyphaLink();
        int neighborCount = link.getHyphae().size();
        
        // If there are neighbors
        // Floods the neighborhood of spVisited with query agents
        if(neighborCount > 0) {
                        
            for(MycoNode neighbor: link.getHyphae()) {
                
                sendTo(neighbor);
            }            
        }
        else {
            // Finish its task
        }
    }
    
    private void returnToInitiator() {
        RAToolkitSender.sendObject(this, spInitiator, QUERY_AGENT_FINISHED);
    }
    
    // Algorithm 4 from selection phase
    private void testSuperpeer(MycoNode sp) {
        
        if(exclusionList.size() <= RAToolkitConfigParser.raToolkit_maxExclusion) {
            
            // If sp doesn't exists in the exclusion list
            if(!exclusionList.containsKey(sp)) {
                sendTestMessageTo(sp);
            }
            else {
                // return to SP_initiator
            }
        }
        else {
            
            // If the exclusion list is bigger than the limit, don't search on it and only send a test message
            sendTestMessageTo(sp);            
        }
    }
    
    // Algorithm 5 from selection phase
    private void sendTestMessageTo(MycoNode sp) {
        
        RASupportActions ack = RAToolkitSender.sendSyncMessage(String.valueOf(getQuery().getQueryId()), spInitiator, sp, TEST_QUERY);
        
        if(ack.equals(RAToolkitMessages.NO_RECEIVED_QUERY)) {
            
            // Visits SP
            // Query agents die when its ttl expires
            if(--ttl > 0) {
                RAToolkitSender.sendObject(this, sp, VISIT_SP); 
            }
        }
    }

    /**
     * @return the query
     */
    public RASupportQuery getQuery() {
        return query;
    }

}
