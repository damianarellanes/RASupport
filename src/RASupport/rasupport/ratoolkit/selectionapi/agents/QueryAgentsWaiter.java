package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.selectionapi.protocols.ProtocolContext;
import java.util.Map;
import myconet.MycoNode;

/**
 * RAToolkit: class that represents a service for the query agents
 * It is a thread because SP_initiaor needs to dispatch many queries at the same time
 * Waits for the response of query agentes
 * Lives in SP_initiator
 * @author Damian Arellanes
 */
public class QueryAgentsWaiter implements Runnable {
    
    protected Thread thread; 
    
    private MycoNode spInitiator = null;
    private RASupportQuery query = null;
    private ProtocolContext protocolContext = null;
    private DatabaseManager dbMan = null;
    
    /*private int queryAgentsToWait;
    private volatile int receivedAgents;*/
    
    private RASupportMap<MycoNode, QueryAgentResult> queryResult = null; // (superPeer, queryResult)
    
    public QueryAgentsWaiter(RASupportQuery query, MycoNode spInitiator,  ProtocolContext protocolContext, 
            DatabaseManager dbMan) {
        
        this.query = query;
        this.spInitiator = spInitiator;

        /*this.queryAgentsToWait = 0;
        this.receivedAgents = 0;*/
        
        this.protocolContext = protocolContext;        
        this.dbMan = dbMan;
        
        this.queryResult = new RASupportMap<>();
    }
    
    public void start() {
        
        if (thread == null) {
            thread = new Thread (this);
            thread.start ();
        } 
    }
    
    @Override
    public void run() {
        
        QueryAgent queryAgentPrototype = new QueryAgent(
                query, spInitiator, query.getTtl(), "AgentXXX", protocolContext, this
        );
        
        queryAgentPrototype.behaveIn(spInitiator, dbMan);
        
        // waitResults();
        
        // Is better to create a heuristic called timeout, in order to stop the process (only for flooding)
        System.out.println("SP_initiator has received all query agents");
        
        for(Map.Entry<MycoNode, QueryAgentResult> entry: queryResult.entrySet()) {
            
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("SP_visited = " + entry.getKey().getAlias());
            System.out.println("Candidate peers = " + entry.getValue());
            System.out.println("-----------------------------------------------------------------------------");
        }
        
    }
    
    public synchronized void notifyQueryAgentArrival(QueryAgent queryAgent) {
        
        // Adds result
        System.out.println("Adding result from query agent received...");
        
        // Iterates though the result set
	for (Map.Entry<MycoNode, QueryAgentResult> entry : queryAgent.getResultSet().entrySet()) {
            
            queryResult.putIfAbsent(entry.getKey(), entry.getValue());
            //System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
	}
        
        protocolContext.processResults(queryAgent);
    }

}
