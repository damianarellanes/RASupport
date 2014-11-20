package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportActions;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryGroup;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.ratoolkit.common.Agent;
import RASupport.rasupport.ratoolkit.common.RAToolkitConfigParser;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.selectionapi.protocols.ProtocolContext;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages;
import static RASupport.rasupport.ratoolkit.transportlayer.RAToolkitMessages.*;
import RASupport.rasupport.ratoolkit.transportlayer.RAToolkitSender;
import java.util.Map;
import myconet.MycoNode;

/**
 * SelectionAPI: representation of a query agent
 * Finish its task when the ttl expires (ttl=0) or when it can't reach a neighbor from the visited super-peer
 * @author Damian Arellanes
 */
public class QueryAgent implements Agent {
    
    private String agentId =  "";
    private int ttl;
    private ProtocolContext protocolContext = null;
    private QueryAgentsWaiter waiter = null;
    
    private RASupportQuery query = null;
    private MycoNode spInitiator = null;
    private String spInitiatorAlias = "";
    private boolean hasFinished ;
    private String currentVisited = "";
    
    private RASupportMap<MycoNode, MycoNode> exclusionList = null; // Exclusion list to avoid query duplicates in super-peers
    private RASupportMap<MycoNode, QueryAgentResult> resultSet = null;
    private Map<RASupportQueryGroup, String> sqlQueries = null;
    
    public QueryAgent(RASupportQuery query, MycoNode peerOwner, int ttl, String agentId, ProtocolContext protocolContext, 
            QueryAgentsWaiter waiter) {
        
        this.agentId = agentId;
        this.protocolContext = protocolContext;
        
        this.query = query;
        this.spInitiator = peerOwner;
        this.spInitiatorAlias = peerOwner.getAlias();
        this.exclusionList = new RASupportMap<>();
        this.resultSet = new RASupportMap<>();
        this.sqlQueries = QueryEvaluator.buildSqlQueriesPerGroup(query);
        
        this.ttl = ttl;
        
        this.waiter = waiter;
        this.hasFinished = false;
    }
    
    public QueryAgent(RASupportQuery query, MycoNode peerOwner, int ttl, String agentId, ProtocolContext protocolContext, 
            QueryAgentsWaiter waiter, RASupportMap<MycoNode, MycoNode> exclusionList) {
        
        this.agentId = agentId;
        this.protocolContext = protocolContext;
        
        this.query = query;
        this.spInitiator = peerOwner;
        this.spInitiatorAlias = peerOwner.getAlias();
        this.exclusionList = new RASupportMap<>();
        this.resultSet = new RASupportMap<>();
        this.sqlQueries = QueryEvaluator.buildSqlQueriesPerGroup(query);
        
        this.ttl = ttl;
        
        this.waiter = waiter;
        this.hasFinished = false;
        
        this.exclusionList = exclusionList;
    }
    
    // Constructor for prototypes
    public QueryAgent(QueryAgent queryAgentPrototype) {
        
        this.agentId = queryAgentPrototype.getAgentId();
        this.protocolContext = queryAgentPrototype.getProtocolContext();
        
        this.query = queryAgentPrototype.getQuery();
        this.spInitiator = queryAgentPrototype.getSpInitiator();
        this.spInitiatorAlias = spInitiator.getAlias();
        this.exclusionList = queryAgentPrototype.getExclusionList();
        this.resultSet = queryAgentPrototype.getResultSet();
        this.sqlQueries = queryAgentPrototype.getSqlQueries();
        
        this.ttl = queryAgentPrototype.getTtl();        
        this.currentVisited = queryAgentPrototype.getCurrentVisited();
        
        this.waiter = queryAgentPrototype.getWaiter();
        this.hasFinished = queryAgentPrototype.hasFinished();
    }
    
    public void behaveIn(MycoNode spVisited, DatabaseManager dbMan) {
        
        // Selects resources in spVisited
        QueryAgentResult result = QueryEvaluator.evaluateSuperpeer(query, dbMan, spVisited, sqlQueries);        
        
        // Add result iff result != emptySet (i.e., iff result contains something)
        if(!result.isEmpty()) {
            resultSet.put(spVisited, result);
        }
        
        // If ttl expires, so the query agent returns to super-peer initiator
        if(--ttl == 0) {
            
            System.err.println(agentId + " has expired in " + spVisited.getAlias());
                        
            returnToSpInitiator();
        }
        else {
            
            // Adds spVisited to the exclusion list
            getExclusionList().put(spVisited, spVisited);

            // Performs selected protocol (flooding or iRandomWalks)
            getProtocolContext().executeProtocol(this, spVisited);
        }              
    }
    
    public void returnToSpInitiator() {
        
        hasFinished = true;        
        RAToolkitSender.sendObject(this, spInitiator, QUERY_AGENT_FINISHED); 
    }
    

    @Override
    public String getSender() {
        return spInitiatorAlias;
    }

    @Override
    public boolean sendTo(RASupportTopologyNode receiver) {  
        
        // Only test if the query agent still active
        if(!hasFinished) {
                        
            return testSuperpeer((MycoNode) receiver);
        }
        return false;
    }
    
    public void notifyArrival() {
        
        waiter.notifyQueryAgentArrival(this);
    }
    
    // Algorithm 4 from selection phase
    // Returns true on sending success
    private boolean testSuperpeer(MycoNode sp) {
        
        if(getExclusionList().size() <= RAToolkitConfigParser.raToolkit_maxExclusion) {
            
            // If sp doesn't exists in the exclusion list
            if(!exclusionList.containsKey(sp)) {
                return sendTestMessageTo(sp);
            }
            else {
                System.err.println("IMPOSSIBLE TO SEND TO SUPER-PEER " + sp.getAlias() + " -> exclusion list");
                return false;
            }
        }
        else {
            
            // If the exclusion list is bigger than the limit, don't search on it and only send a test message
            return sendTestMessageTo(sp);            
        }
    }
    
    // Algorithm 5 from the selection phase
    // Returns true on sending success
    private boolean sendTestMessageTo(MycoNode sp) {
                
        RASupportActions ack = RAToolkitSender.sendSyncMessage(String.valueOf(getQuery().getQueryId()), getSpInitiator(), sp, TEST_QUERY);

        if(ack.equals(RAToolkitMessages.NO_RECEIVED_QUERY)) {

            RAToolkitSender.sendObject(this, sp, VISIT_SP); 
            return true;
        }
        else {
            System.err.println("SUPER-PEER " + sp.getAlias() + " REJECTS(received in the past) " + agentId);
            return false;
        }                        
    }

    /**
     * @return the query
     */
    public RASupportQuery getQuery() {
        return query;
    }

    /**
     * @return the agentId
     */
    public String getAgentId() {
        return agentId;
    }

    /**
     * @return the ttl
     */
    public int getTtl() {
        return ttl;
    }

    /**
     * @return the spInitiator
     */
    public MycoNode getSpInitiator() {
        return spInitiator;
    }

    /**
     * @return the protocolContext
     */
    public ProtocolContext getProtocolContext() {
        return protocolContext;
    }

    /**
     * @return the exclusionList
     */
    public RASupportMap<MycoNode, MycoNode> getExclusionList() {
        return exclusionList;
    }

    /**
     * @return the waiter
     */
    public QueryAgentsWaiter getWaiter() {
        return waiter;
    }

    /**
     * @return the hasFinished
     */
    public boolean hasFinished() {
        return hasFinished;
    }

    /**
     * @return the resultSet
     */
    public RASupportMap<MycoNode, QueryAgentResult> getResultSet() {
        return resultSet;
    }

    /**
     * @return the sqlQueries
     */
    public Map<RASupportQueryGroup, String> getSqlQueries() {
        return sqlQueries;
    }

    /**
     * @param exclusionList the exclusionList to set
     */
    public void setExclusionList(RASupportMap<MycoNode, MycoNode> exclusionList) {
        this.exclusionList = exclusionList;
    }
    
    // Union of exclusion lists
    public void unionExclusionListWith(RASupportMap<MycoNode, MycoNode> exclusionList) {
        this.exclusionList.putAll(exclusionList);
    }

    /**
     * @param currentVisited the currentVisited to set
     */
    public void setCurrentVisited(String currentVisited) {
        this.currentVisited = currentVisited;
    }

    /**
     * @return the currentVisited
     */
    public String getCurrentVisited() {
        return currentVisited;
    }

}
