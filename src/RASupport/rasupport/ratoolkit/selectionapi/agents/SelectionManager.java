package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.selectionapi.protocols.*;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.*;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitSender;
import myconet.MycoNode;

/**
 * SelectionAPI: manager of query agents
 * @author Damian Arellanes
 */
public class SelectionManager {
    
    QueryAgentsBehaviour agentBehaviour;
    //RASupportQuery query = null; // Only in the case that the collaborative system use one query in the whole system
    MycoNode peerOwner = null;
    String peerAlias = "";
    DatabaseManager dbMan = null;
    
    RASupportMap<Long, Long> receivedQueryAgents = null;// This could be maintained in the database (idQuery, peerSender)     
    ProtocolContext protocolContext = null;    
    
    public SelectionManager(MycoNode peerOwner, DatabaseManager dbMan) {
        
        this.peerOwner = peerOwner;
        this.peerAlias = peerOwner.getAlias();
        this.dbMan = dbMan;
        
        this.receivedQueryAgents = new RASupportMap<>();
        
        this.protocolContext = new ProtocolContext(peerOwner);
        
        //this.queriesSent = new RASupportMap<>();
    }        
    
    /*// Sends queries in collaborative P2P systems that only request one query
    public void startSelection() {
        
        if(this.query != null) {
            // Send Agent
        }
    }*/
        
    // SelectionAPI always provides a consistent query
    // This method starts the selection phase (in the requestor that could be a normal-peer or a super-peer)
    public void startSelection(RASupportQuery query) {
        
        MycoNode sp = peerOwner.getSuperpeer();
        
        // If the current peer is a super-peer and the query has not been sent yet
        if(sp == null && !query.isTraveling()) {
                        
            query.setIsTraveling(true);
            
            // Creates a query agents waiter (1 per request)
            new QueryAgentsWaiter(query, peerOwner, protocolContext, dbMan).start();
            
        }
        else {
            
            // Sends the query to the super-peer until such a query arrives to a super-peer initiator
            RAToolkitSender.sendObject(query, sp, REQUEST_QUERY);            
        }
                                
    }
    
    public void addQueryAgent(long idQuery) {
        receivedQueryAgents.put(idQuery, idQuery);
    }
    
    public boolean hasReceivedQueryAgent(long idQuery) {
        return receivedQueryAgents.containsKey(idQuery);
    }
    
    public void receiveQueryAgentResults(QueryAgent queryAgent) {
        
        // Processes the results from the query agent
        
        // Notifies the arrival of a query agent to the correct query agents waiter
        queryAgent.notifyArrival();
    }
    
}
