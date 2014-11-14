package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.queries.RASupportQueryGroup;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import java.util.ArrayList;
import java.util.Map;
import myconet.MycoNode;

/**
 * RAToolkit: this class represents the result obtained by a query agent in a specific SPv
 * This class is the equivalent of CGv used in the Master's thesis
 * Candidate groups (group, array of peer identifiers)
 * @author Damian Arellanes
 */
public class QueryAgentResult extends RASupportMap<RASupportQueryGroup, ArrayList<Long>> {
    
    private MycoNode spVisited = null;    
    
    public QueryAgentResult(MycoNode spVisited) {
        
        this.spVisited = spVisited;
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<RASupportQueryGroup, ArrayList<Long>> entry: this.entrySet()) {
            
            sb.append("Group: " + entry.getKey().getName() + " -> " + entry.getValue());
        }
        
        return sb.toString();
    }

}
