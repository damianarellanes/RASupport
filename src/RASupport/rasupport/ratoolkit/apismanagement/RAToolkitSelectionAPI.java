package RASupport.rasupport.ratoolkit.apismanagement;

import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportActions;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryAgent;
import java.io.File;
import myconet.MycoNode;

/**
 * RAToolkit: interface that selection APIs must to implement
 * @author damianarellanes
 */
public interface RAToolkitSelectionAPI {
    
    public File createXMLQuery();
    
    public void selectResources(RASupportQuery query);
    
    public RASupportActions testQuery(long idQuery); // Returns an acknowledge as reponse (synchronous method)
    
    public void receiveQueryAgent(QueryAgent queryAgent);
    
    public void receiveQueryAgentResults(QueryAgent queryAgent);
    
}
