package RASupport.rasupport.ratoolkit.selectionapi.protocols;

import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import myconet.MycoNode;

/**
 * RAToolkit: intelligent random walks protocol
 * @author Damian Arellanes
 */
public class IRandomWalk implements SelectionProtocol {
    
    MycoNode peerOwner = null;
    String peerAlias = "";
    RASupportMap statisticList = null;
    
    public IRandomWalk(MycoNode peerOwner) {
        
        this.peerOwner = peerOwner;
        this.peerAlias = peerOwner.getAlias();
        
        statisticList = new RASupportMap();        
    }

    @Override
    public void execute(RASupportQuery query) {
        
    }

}
