package RASupport.rasupport.ratoolkit.selectionapi.protocols;

import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryOptions;
import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryAgent;
import RASupport.rasupport.ratoolkit.selectionapi.agents.QueryAgentsWaiter;
import static RASupport.rasupport.ratoolkit.selectionapi.protocols.QueryAgentsBehaviour.*;
import myconet.MycoNode;

/**
 * RAToolkit: context of protocols in order to execute a specific protocol
 * @author Damian Arellanes
 */
public class ProtocolContext {
    
    // To reduce the creation of ojects, we mantain only one reference to our protocols
    private Flooding floodingProtocol = null; 
    private IRandomWalk iRandomWalkProtocol = null;
    
    public ProtocolContext(MycoNode peerOwner) {
        floodingProtocol  = new Flooding();
        iRandomWalkProtocol = new IRandomWalk(peerOwner);
    }
    
    public void executeProtocol(QueryAgent queryAgent, MycoNode spVisited) {
        
        // Performs a strategy depending on the selected protocol
        switch(getSelectedProtocol(queryAgent.getQuery().getOption())) {
            case FLOODING:
                floodingProtocol.execute(queryAgent, spVisited);
                break;
            case IRANDOMWALK:
                //iRandomWalkProtocol.execute(query);
                break;    
            default:
                //iRandomWalkProtocol.execute(query);
                break;
        }
    }
    
    
    private QueryAgentsBehaviour getSelectedProtocol(RASupportQueryOptions option) {
        
        return (option.equals(RASupportQueryOptions.FIND_RESOURCES)) ? FLOODING : IRANDOMWALK;
    }
}
