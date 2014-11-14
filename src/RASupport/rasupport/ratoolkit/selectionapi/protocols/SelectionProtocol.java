package RASupport.rasupport.ratoolkit.selectionapi.protocols;

import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryAgent;
import myconet.MycoNode;

/**
 * RAToolkit: interface that must be implemented by selection protocols
 * @author Damian Arellanes
 */
public interface SelectionProtocol {
    
    public void execute(QueryAgent queryAgent, MycoNode spVisited);

}
