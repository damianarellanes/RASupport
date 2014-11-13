package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import myconet.HyphaLink;
import myconet.MycoNode;

/**
 * RAToolkit: class that represents a service for the query agents
 * It is a thread because SP_initiaor needs to dispatch many queries at the same time
 * Waits for the response of query agentes
 * Lives in SP_initiator
 * @author Damian Arellanes
 */
public class QueryAgentsWaiter extends Thread {
    
    private MycoNode peerOwner = null;
    private RASupportQuery query = null;
    
    private int queryAgentsToWait;
    private volatile int receivedAgents;
    
    public QueryAgentsWaiter(RASupportQuery query, MycoNode peerOwner) {
        
        this.query = query;
        this.peerOwner = peerOwner;
        
        this.queryAgentsToWait = 0;
        this.receivedAgents = 0;
    }
    
    @Override
    public void run() {
        
        HyphaLink link = peerOwner.getHyphaLink();
        int neighborCount = link.getHyphae().size();
        
        // If there are neighbors
        // Floods the neighborhood with query agents
        if(neighborCount > 0) {
            
            queryAgentsToWait = neighborCount;
            for(MycoNode neighbor: link.getHyphae()) {
            
                //System.out.println("Query sent to " + neighbor.getAlias());
                new QueryAgent(query, peerOwner, query.getTtl()).sendTo(neighbor);
            }
            
        }
    }
    
    public synchronized void receiveQueryAgent(QueryAgent queryAgent) {
        
        receivedAgents++;
        
        // Do something with the response
        
        // If SP_initiator has received response of all query agents 
        if(receivedAgents == queryAgentsToWait) {
            System.out.println("SP_initiator has received all query agents");
        }
    }

}
