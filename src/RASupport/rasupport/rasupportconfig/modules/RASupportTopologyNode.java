package RASupport.rasupport.rasupportconfig.modules;

import RASupport.rasupport.rasupportmain.RASupportMain;

/**
 * RASupportConfig: the nodes of a topology manager (e.g., Myconet) must to implement this interface
 * This interface represents a node in the topology selected
 * @author damianarellanes
 */
public interface RASupportTopologyNode {
    
    public void createRASupport();
    
    public long getIdNode();
    
    public String getAlias();
    
    public RASupportMain getRASupport();
    
    public boolean isSuperpeer();
    
}
