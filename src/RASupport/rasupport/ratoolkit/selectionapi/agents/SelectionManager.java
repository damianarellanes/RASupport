package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import java.io.File;
import myconet.MycoNode;

/**
 * SelectionAPI: manager of query agents
 * @author Damian Arellanes
 */
public class SelectionManager {
    
    RASupportMap statisticList;
    QueryAgentsBehaviour selectedProtocol;
    
    public SelectionManager() {
        
        statisticList = new RASupportMap();
    }
    
    public void sendQueryAgent(MycoNode target) {
        
    }
    
    public void parseQuery(File xmlQuery) {
        
    }
}
