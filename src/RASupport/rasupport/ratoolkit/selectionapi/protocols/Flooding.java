package RASupport.rasupport.ratoolkit.selectionapi.protocols;

import RASupport.rasupport.rasupportconfig.queries.*;
import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryAgent;
import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryAgentsWaiter;
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
    
    public Flooding() {}

    @Override
    public void execute(QueryAgent queryAgent, MycoNode spVisited) {
                
        HyphaLink link = spVisited.getHyphaLink();
        int neighborCount = link.getHyphae().size();
        
        // If there are neighbors
        // Floods the neighborhood of spVisited with query agents
        if(neighborCount > 0) {
                  
            int countNoSending = 0;
            for(MycoNode neighbor: link.getHyphae()) {
                
                System.out.println("SP " + spVisited.getAlias() + " tries to send " + queryAgent.getAgentId()  +" to " + neighbor.getAlias());
                
               // Sends a clone to a specific neighbor using the query agent prototype
               // Don't use clone because is slow
               if(!new QueryAgent(queryAgent).sendTo(neighbor)) {
                   countNoSending++;
               }
            }
            
            // If the agent wasn't able to send any query agent
            if(countNoSending == neighborCount) {
                queryAgent.returnToSpInitiator();
            }
        }
        else {
            // If there aren't anymore neighbors to visit, finish the task of the query agent
            queryAgent.returnToSpInitiator();
        }        
    }
}
