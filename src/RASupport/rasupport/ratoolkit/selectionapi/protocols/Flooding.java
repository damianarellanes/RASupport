package RASupport.rasupport.ratoolkit.selectionapi.protocols;

import RASupport.rasupport.rasupportconfig.queries.*;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryEvaluator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import myconet.HyphaLink;
import myconet.MycoNode;

/**
 * RAToolkit: protocol of flooding with query agents
 * This protocol floods neighbors with query agents
 * Stops when there aren't anymore available neighbors or TTL has been expired
 * Improvement: test super-peers before send them query agents
 * @author Damian Arellanes
 */
public class Flooding implements SelectionProtocol {
    
    MycoNode peerOwner = null;
    String peerAlias = "";
    
    DatabaseManager dbMan = null;
    
    public Flooding(MycoNode peerOwner, DatabaseManager dbMan) {
        
        this.peerOwner = peerOwner;
        this.peerAlias = peerOwner.getAlias();
        
        this.dbMan = dbMan;
    }

    @Override
    public void execute(RASupportQuery query) {
                
        HyphaLink link = peerOwner.getHyphaLink();
        
        // If there are neighbors
        // Floods the neighborhood with query agents
        if(link.neighborCount() > 0) {
            for(MycoNode neighbor: link.getNeighbors()) {
            
                //new QueryAgent().sendTo(neighbor);
            }
            
        }        
    }
    
    public void executeInInitiator(RASupportQuery query) {
        
        HyphaLink link = peerOwner.getHyphaLink();
        
        // If there are neighbors
        // Floods the neighborhood with query agents
        if(link.neighborCount() > 0) {
            for(MycoNode neighbor: link.getNeighbors()) {
            
                //new QueryAgent().sendTo(neighbor);
            }
            
        }        
        
        QueryEvaluator.evaluateSPvisited(query, dbMan);
    }
    

}
