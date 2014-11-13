package RASupport.rasupport.ratoolkit.selectionapi.agents;

import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.selectionapi.protocols.*;
import RASupport.rasupport.ratoolkit.selectionapi.protocols.QueryAgentsBehaviour;
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
    
    RASupportMap<Long, MycoNode> receivedQueries = null;// This could be maintained in the database (idQuery, peerSender) 
    
    ProtocolContext protocolContext;    
    
    RASupportMap<QueryAgentsWaiter, QueryAgentsWaiter> queriesSent = null;// Queries sent by the current peer (only used by super-peers)
    
    public SelectionManager(MycoNode peerOwner, DatabaseManager dbMan) {
        
        this.peerOwner = peerOwner;
        this.peerAlias = peerOwner.getAlias();
        this.dbMan = dbMan;
        
        this.receivedQueries = new RASupportMap<>();
        
        this.protocolContext = new ProtocolContext(peerOwner, dbMan);
        
        this.queriesSent = new RASupportMap<>();
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
            
            // In the protocols, we send query agents
            //protocolContext.executeProtocol(query);        
            QueryAgentsWaiter qaw = new QueryAgentsWaiter(query, peerOwner);
            queriesSent.put(qaw, qaw);
            query.setIsTraveling(true);
            
            qaw.run();
        }
        else {
            // Sends the query to the super-peer
            RAToolkitSender.sendObject(query, sp, REQUEST_QUERY);            
        }
                                
    }
    
    public boolean hasReceivedQuery(long idQuery) {
        return receivedQueries.containsKey(idQuery);
    }
    
}
