package RASupport.rasupport.rasupportconfig.common;

import RASupport.rasupport.rasupportconfig.modules.RASupportResourceAggregation;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceManager;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceMonitor;

/**
 * RASupportConfig: common error messages in the support
 * @author damianarellanes
 */
public interface RASupportErrors {
    
    // Errors in modules
    public final RASupportResourceManager ERROR_NO_RESOURCE_MANAGER = null;
    public final RASupportResourceMonitor ERROR_NO_RESOURCE_MONITOR = null;
    public final RASupportResourceAggregation ERROR_NO_TOOLKIT = null;
    
    // Errors in attributes
    public final int ERROR_NO_ATTRIBUTE_METHOD = -1;    
    public final int ERROR_UNDEFINED_ATTRIBUTE_TYPE = -2;

}
