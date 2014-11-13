package RASupport.tests;

import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryAgentsWaiter;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;

/**
 *
 * @author Damian Arellanes
 */
public class ThreadsMessageTest {
    
    public class Agent {
        
        QueryAgentsWaiter qaw = null;
        
        public Agent(QueryAgentsWaiter qaw) {
            this.qaw = qaw;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        
        RASupportMap<QueryAgentsWaiter, QueryAgentsWaiter> waiterQueriesMap = new RASupportMap<>();
        
        /*QueryAgentsWaiter qaw = new QueryAgentsWaiter();
        waiterQueriesMap.put(qaw, qaw);
        qaw.run();
        
        QueryAgentsWaiter qaw2 = new QueryAgentsWaiter();
        waiterQueriesMap.put(qaw2, qaw2);
        qaw2.run();
        
        Thread.sleep(2000);
        
        //Agent a = new Agent();
        QueryAgentsWaiter received = waiterQueriesMap.get(qaw);
        received.receiveQueryAgent(10);
        received.receiveQueryAgent(11);
        received.receiveQueryAgent(12);*/
    }

}
