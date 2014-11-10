package RASupport.rasupport.rasupportconfig.modules;

import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportReceiver;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import java.io.File;

/**
 * RASupport: interface that represents a resource aggregation module in RASupport
 * It must be implemented in the facades of the resource aggregation modules (e.g., RAToolkit)
 * @author Damian Arellanes
 */
public interface RASupportResourceAggregation extends RASupportModule {
    
    public void deleteIndexEntry(String entry);
    
    public void cleanIndexTable(); 
    
    public void advertiseResourcesTo(RASupportTopologyNode targetNode);
    
    public void updateDynamicResource(String attribute, String newValue);
    
    public RASupportReceiver getReceiver();
    
    // This method performs the key phases of the resource aggregation
    public void executeQuery(RASupportQuery query);
    public void executeQuery(File query);

}
