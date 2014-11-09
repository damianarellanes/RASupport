package RASupport.rasupport.rasupportconfig.common;

import java.io.File;

/**
 * RASupport: initializer of RASupport
 * Sets the configuration  required for the normal operation of RASupport
 * @author Damian Arellanes
 */
public class RASupportInitializer {
    
    // Creates the directory where queries will be stored
    {
        new File(RASupportCommon.queriesDirectory).mkdir();        
    }

    // Creates the directory of XML schemas
    {
        new File(RASupportCommon.schemasDirectory).mkdir();        
    }
}
