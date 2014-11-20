package RASupport.rasupport.ratoolkit.selectionapi;

import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportActions;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.ratoolkit.apismanagement.RAToolkitSelectionAPI;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryAgent;
import RASupport.rasupport.ratoolkit.selectionapi.agents.SelectionManager;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.*;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitSender;
import java.io.File;
import myconet.MycoNode;

/**
 * RAToolkit: facade of the selection API
 * @author Damian Arellanes
 */
public class SelectionAPI implements RAToolkitSelectionAPI {
    
    private DatabaseManager databaseManager = null;
    private RASupportNode node = null;
    private MycoNode myconetPeer = null;
    private String myconetAlias = "";
    
    private SelectionManager selectionManager = null;
    
    public SelectionAPI(RASupportNode node, DatabaseManager dbMan) {
        
        
        // Sets the node
        this.node = node;
        this.myconetPeer = (MycoNode) node.getTopologyNode();
        this.myconetAlias = myconetPeer.getAlias();
        
        // Sets the database manager
        this.databaseManager = dbMan;        
        
        // Creates the manager of query agents
        this.selectionManager = new SelectionManager(myconetPeer, dbMan);
    }
    
    @Override
    public File createXMLQuery() {
        return null;
    }
    
    @Override
    public void selectResources(RASupportQuery query) {
        
        // Starts selection phase        
        logMessage(myconetAlias + "->" + query.getRequestor().getAlias() + " has requested resources");
        logMessage("queryID -> " + query.getQueryId());
        selectionManager.startSelection(query);
    }

    @Override
    public RASupportActions testQuery(long idQuery) {
        
        if(selectionManager.hasReceivedQueryAgent(idQuery)) {
            
            return RECEIVED_QUERY;
        }
        else {
            return NO_RECEIVED_QUERY;
        }
    }

    @Override
    public void receiveQueryAgent(QueryAgent queryAgent) {
        
        long receivedQuery = queryAgent.getQuery().getQueryId();
        
        logMessage("SUPER-PEER " + myconetAlias + " has received the query " + 
                receivedQuery);
        
        selectionManager.addQueryAgent(receivedQuery);
        queryAgent.behaveIn(myconetPeer, databaseManager);
        
    }
    
    @Override
    public void receiveQueryAgentResults(QueryAgent queryAgent) {
        
        logMessage("SUPER-PEER " + myconetAlias + " has received a result set for the query " + 
                queryAgent.getQuery().getQueryId());
        
        selectionManager.receiveQueryAgentResults(queryAgent);
    }
    
}
