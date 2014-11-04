package RASupport.rasupport.rasupportconfig.modules;

/**
 * RASuportConfig: modules for the RASupport 
 * @author damianarellanes
 */
public interface RASupportModulesConfiguration {
    
    // Allowed attribute managers in the support
    // ResourceSim (RSIM)
    public enum ResourceManagers { RSIM, NO_RESOURCE_MANAGER };
    
    // Allowed attribute monitors in the support
    // ResourceSimMonitor (RSIM_MONITOR)
    public enum ResourceMonitors { RSIM_MONITOR, NO_RESOURCE_MONITOR };
    
    // Allowed resource aggregation toolkits in the support
    // RAToolkit (RATOOLKIT)
    public enum ResourceAggregationToolkits { RATOOLKIT, NO_TOOLKIT };
    
}
