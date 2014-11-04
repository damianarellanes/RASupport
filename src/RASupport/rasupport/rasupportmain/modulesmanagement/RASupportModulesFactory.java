package RASupport.rasupport.rasupportmain.modulesmanagement;

import RASupport.rasupport.ratoolkit.RAToolkit;
import RASupport.rasupport.resourcesim.resourcesim.ResourceSim;
import RASupport.rasupport.resourcesim.rsimmonitor.RSimMonitor;
import static RASupport.rasupport.rasupportconfig.common.RASupportErrors.*;
import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import RASupport.rasupport.rasupportconfig.modules.RASupportModulesConfiguration.ResourceAggregationToolkits;
import static RASupport.rasupport.rasupportconfig.modules.RASupportModulesConfiguration.ResourceAggregationToolkits.RATOOLKIT;
import RASupport.rasupport.rasupportconfig.modules.RASupportModulesConfiguration.ResourceManagers;
import static RASupport.rasupport.rasupportconfig.modules.RASupportModulesConfiguration.ResourceManagers.*;
import RASupport.rasupport.rasupportconfig.modules.RASupportModulesConfiguration.ResourceMonitors;
import static RASupport.rasupport.rasupportconfig.modules.RASupportModulesConfiguration.ResourceMonitors.*;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceAggregation;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceManager;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceMonitor;

/**
 * RASupport: factory to create new modules (e.g., an attribute manager like ResourceSIm)
 * Here we can add support for more modules adding new methods
 * @author Damian Arellanes
 */
public class RASupportModulesFactory {
    
    public static RASupportResourceManager createResourceManager(ResourceManagers am) {
        
        if(am == RSIM) {
            return new ResourceSim();
        }
        else {
            return ERROR_NO_RESOURCE_MANAGER;
        }
    }
    
    public static RASupportResourceMonitor createResourceMonitor(ResourceMonitors am, 
            RASupportNode n, RASupportResourceAggregation ra) {
        if(am == RSIM_MONITOR) {
            return new RSimMonitor(n, ra);
        }
        else {
            return ERROR_NO_RESOURCE_MONITOR;
        }
    }
    
    public static RASupportResourceAggregation createResourceAggregationModule(ResourceAggregationToolkits t,
            RASupportNode node) {
        if(t == RATOOLKIT) {
            return new RAToolkit(node);
        }
        else {
            return ERROR_NO_TOOLKIT;
        }
    }
}
