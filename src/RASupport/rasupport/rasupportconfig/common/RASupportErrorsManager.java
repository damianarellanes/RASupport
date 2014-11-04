package RASupport.rasupport.rasupportconfig.common;

import static RASupport.rasupport.rasupportconfig.common.RASupportErrors.*;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceManager;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceMonitor;
import static RASupport.rasupport.rasupportconfig.log.LogManager.*;

/**
 * RASupport: manager of main errors in the support
 * @author Damian Arellanes
 */
public class RASupportErrorsManager {
    
    public static boolean existsAttributeManager(RASupportResourceManager am) {
        
        if(am != ERROR_NO_RESOURCE_MANAGER) {
            return true;
        } else {
            logError("there is not an attribute manager for the MODE that you specified");
            return false;
        }        
    }
    
    public static boolean existsAttributeMonitor(RASupportResourceMonitor am) {
        
        if(am != ERROR_NO_RESOURCE_MONITOR) {
            return true;
        } else {
            logError("there is not an attribute monitor for the MODE that you specified");
            return false;
        }        
    }

}