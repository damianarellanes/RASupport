package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.xml.XMLQueryReader;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import java.io.File;
import myconet.MycoNode;

/**
 * SelectionAPI: manager of query agents
 * @author Damian Arellanes
 */
public class SelectionManager {
    
    QueryAgentsBehaviour agentBehaviour;
    RASupportQuery query = null; // Only in the case that the collaborative system use one query in the whole system
    MycoNode peerOwner = null;
    DatabaseManager dbMan = null;
    
    public SelectionManager(MycoNode peerOwner, DatabaseManager dbMan) {
        
        this.peerOwner = peerOwner;
        this.dbMan = dbMan;
    }
    
    /*public SelectionManager(MycoNode peerOwner, RASupportQuery query) {
        
        this.peerOwner = peerOwner;
        this.query = query;
        this.query.setQueryId(computeQueryCode());
    }*/
    
    // Method to calculate the code from a specific query
    // In particular, RAToolkit use: query_hash_code + peerAlias_hash_code + queryAgent_hash_code
    private long computeQueryCode() {
        
        return query.hashCode() + peerOwner.hashCode() + this.hashCode();
    }
    
    // Sends queries in collaborative P2P systems that only request one query
    public void sendQueryAgent() {
        
        if(this.query != null) {
            // Send Agent
        }
    }
        
    // SelectionAPI always provides a consistent query
    public void sendQueryAgent(RASupportQuery query) {
        
        this.query.setQueryId(computeQueryCode());
        // Send agent
    }
}
