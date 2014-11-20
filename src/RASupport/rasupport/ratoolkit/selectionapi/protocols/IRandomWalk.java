package RASupport.rasupport.ratoolkit.selectionapi.protocols;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryGroup;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryRequirement;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributesInterface;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes.free_mem;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes.cpu_speed;
import RASupport.rasupport.rasupportmain.RASupportMain;
import RASupport.rasupport.ratoolkit.common.RAToolkitConfigParser;
import static RASupport.rasupport.ratoolkit.common.RAToolkitConfigParser.raToolkit_maxRW;
import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryAgent;
import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryAgentResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import myconet.HyphaLink;
import myconet.MycoList;
import myconet.MycoNode;

/**
 * RAToolkit: intelligent random walks protocol
 * @author Damian Arellanes
 */
public class IRandomWalk implements SelectionProtocol {
    
    private MycoNode peerOwner = null;
    private String peerAlias = "";
    
    private volatile RASupportMap<RASupportQueryGroup, RASupportMap<MycoNode, MycoNode>> statisticList = null;
    
    
    public IRandomWalk(MycoNode peerOwner) {
        
        this.peerOwner = peerOwner;
        this.peerAlias = peerOwner.getAlias();
        
        statisticList = new RASupportMap<>();        
        //applyTest();// Only for testing
    }    
    
    private void applyTest() {
        
        RASupportQueryGroup g1 = new RASupportQueryGroup("Similar group", 8);
        g1.addNumericalAttribute(cpu_speed,500.0,2500.0,4096.0,5000.0,0.2);
        g1.addNumericalAttribute(free_mem, 10.0,1024.0,4096.0,16000.0,0.05);
        
        RASupportMap<MycoNode, MycoNode> m1 = new RASupportMap<>();
        m1.put(peerOwner, peerOwner);
        
        System.err.println("Statistic list cointains: " + peerOwner.getAlias());
        
        statisticList.put(g1, m1);
    }

    @Override
    public void execute(QueryAgent queryAgent, MycoNode spVisited) {
        
        // If it is not the super-peer initiator
        if(!spVisited.equals(queryAgent.getSpInitiator())) {
            performTraditionalRandomWalks(queryAgent, spVisited);
        }
        else {
            performIntelligentRandomWalks(queryAgent);
        }
        
    }
    
    // This method is executed by the super-peer initiator
    private void performIntelligentRandomWalks(QueryAgent queryAgent) {
        
        System.out.println("iRandomWalk executed!");
        
        RASupportMap<MycoNode, MycoNode> intelligentList = new RASupportMap<>();
        RASupportMap<MycoNode, MycoNode> exclusionList = new RASupportMap<>();
        
        RASupportQuery query = queryAgent.getQuery();
        for(RASupportQueryGroup group: query.getGroups().values()) {

            for(Map.Entry<RASupportQueryGroup, RASupportMap<MycoNode, MycoNode>> entry: statisticList.entrySet()) {
                
                /*System.out.println("Comparing with group " + entry.getKey().getName() + " from " + entry.getValue());                
                System.out.println("Similarity = " + getSimilarity(entry.getKey(), group));*/
                
                if(areSimilar(entry.getKey(), group)) {
                    
                    System.out.println("Similar with " + entry.getValue());  
                    
                    intelligentList.putAll(entry.getValue());
                    exclusionList.putAll(entry.getValue());
                }
            }

        }
        
        // Performs an union operation in the query agent exclusion list with the above exclusion list
        //queryAgent.unionExclusionListWith(exclusionList);        
        exclusionList.put(peerOwner, peerOwner);
        
        // Walks into the super-peers that would answer the query
        for(MycoNode sp: intelligentList.keySet()) {
            
            if(sp.isSuperpeer() && !sp.getAlias().equals(peerOwner.getAlias())) {
                
                // Copy all exclusion list except the receiver, because we need to send a query and then add the sp visited to the exclusion list        
                RASupportMap<MycoNode, MycoNode> copy = new RASupportMap<>();
                copy.putAll(exclusionList);
                copy.remove(sp);
                //queryAgent.unionExclusionListWith(copy);
                queryAgent.setExclusionList(copy);
                
                System.out.println("Intelligent agent sent to " + sp.getAlias());   
                //queryAgent.setCurrentVisited(sp.getAlias());
                new QueryAgent(queryAgent).sendTo(sp);
            }            
        }
        
        // Performs an union operation in the query agent exclusion list with the above exclusion list        
        queryAgent.setExclusionList(exclusionList);        
        
        // Finally, performs traditional random walks using maxRW
        HyphaLink spLink = peerOwner.getHyphaLink();
        if(raToolkit_maxRW >= spLink.getHyphae().size()) {
            sendAgentsToAllNeighbors(queryAgent, peerOwner);
        }
        else {
            sendAgentsToRandomNeighbors(queryAgent, peerOwner, raToolkit_maxRW);
        }
        
        //performTraditionalRandomWalks(queryAgent, peerOwner);
    }
    
    private boolean sendAgentsToAllNeighbors(QueryAgent queryAgent, MycoNode spVisited) {
        
        System.out.println("SP " + spVisited.getAlias() + " sends to all neighbors");
        
        MycoList neighbors = spVisited.getHyphaLink().getHyphae();
        int neighborsCount = neighbors.size();
        int countSending = 0;
        for(int i = 0; i < neighborsCount; i++ ) {
            
            System.out.println("SP " + spVisited.getAlias() + " tries to " + neighbors.get(i).getAlias());
            if(new QueryAgent(queryAgent).sendTo(neighbors.get(i))) {
                                
                countSending++;
            }
        }
        
        return countSending == neighborsCount;
    }
    
    private boolean sendAgentsToRandomNeighbors(QueryAgent queryAgent, MycoNode spVisited, int nRandom) {
        
        System.out.println("SP " + spVisited.getAlias() + " sends to " + nRandom + " neighbors");
        
        MycoList neighbors = spVisited.getHyphaLink().getHyphae();
        int countSending = 0;
        
        for(int i = 0, size = neighbors.size(); i < size; i++) {
            
            System.out.println("SP " + spVisited.getAlias() + " tries to " + neighbors.get(i).getAlias());
            if(new QueryAgent(queryAgent).sendTo(neighbors.get(i))) {
                
                countSending++;
            }
            
            if(countSending == nRandom) {
                return true;
            }
        }
        
        return false;
        
    }
    
    private void performTraditionalRandomWalks(QueryAgent queryAgent, MycoNode spVisited) {
        
        // Tries to send the query agent to one random neighbor
        if(!sendAgentsToRandomNeighbors(queryAgent, spVisited, 1)) {
            
            System.out.println("SP " + spVisited.getAlias() + " returns a query agent ");
            queryAgent.returnToSpInitiator();
        }        
    }
    
    private double getSimilarityAttributes(RASupportQueryRequirement requeriment1, RASupportQueryRequirement requeriment2) {
        
        double similarity = 0.0;        
        
        if(requeriment2.isNumerical() && requeriment1.isNumerical()) {
            
            int count = 0;
            if(requeriment2.getMin_val().doubleValue() == requeriment1.getMin_val().doubleValue()) count++;
            if(requeriment2.getMin_ideal().doubleValue() == requeriment1.getMin_ideal().doubleValue()) count++;
            if(requeriment2.getMax_ideal().doubleValue() == requeriment1.getMax_ideal().doubleValue()) count++;
            if(requeriment2.getMax_val().doubleValue() == requeriment1.getMax_val().doubleValue()) count++;
            
            similarity = (100 * count) / 4;
        }   
        else if(requeriment2.isString() && requeriment1.isString()) {
            
            if(requeriment2.getValue().equals(requeriment1.getValue())) {
                similarity = 100.0;
            } else {
                similarity = 0.0;
            }
        }
        
        
        return similarity;
    }
        
    private double getSimilarity(RASupportQueryGroup existentGroup, RASupportQueryGroup targetGroup) {
        
        double similarity = 0.0;        
        
        for(RASupportQueryRequirement requirement: targetGroup.getAttributes().values()) {
            
            RASupportAttributesInterface attribute = requirement.getAttribute();
            if(existentGroup.containsAttribute(attribute)) {
                
                similarity += getSimilarityAttributes(existentGroup.getAttribute(attribute), requirement);
            }
        }
        
        
        return similarity / Math.max(existentGroup.getNumAttributes(), targetGroup.getNumAttributes());
    }
    
    private boolean areSimilar(RASupportQueryGroup group1, RASupportQueryGroup group2) {
        
        // Group1 and group2 are similar if they have a similarity greater than 75%
        return getSimilarity(group1, group2) > 75.0;
    }
    
    /*public double getSimilarity(String groupToCompare, int[] comparisonOccurrences) {
        
        double similarity = 0.0;
        int i = 0;
        int sizeSymbols = symbolsArray.size();
        int groupToCompareLength = groupToCompare.length();
        
        for( ; i < sizeSymbols; i++) {
            
            int groupOcurrences = groupToCompareLength - groupToCompare.replace(symbolsArray.get(i), "").length();
            
            if(groupOcurrences > comparisonOccurrences[i]) {
                similarity += comparisonOccurrences[i] / groupOcurrences;
            }
            else {
                similarity += groupOcurrences / comparisonOccurrences[i];
            }
        }
        
        return (similarity * 100) / i;
    }*/

    public synchronized void updateStatisticList(RASupportMap<MycoNode, QueryAgentResult> results) {
        
        
        for(Map.Entry<MycoNode, QueryAgentResult> entry: results.entrySet()) {
            
            MycoNode candidate = entry.getKey();
            
            // For each group in the result set
            for(RASupportQueryGroup group: entry.getValue().keySet()) {
                
                if(!statisticList.containsKey(group)) {
                    
                    statisticList.put(new RASupportQueryGroup(group), new RASupportMap<>());                    
                }
                
                statisticList.get(group).put(candidate, candidate);
            }            
        }
    }
}
