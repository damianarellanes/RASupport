package RASupport.rasupport.ratoolkit.common;

import static RASupport.rasupport.rasupportconfig.config.RASupportConfigParser.configuration;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;

/**
 * RAtoolkit: this class parses the configuration for RAToolkit specified by the client in the .properties file from RASupport
 * @author Damian Arellanes
 */
public class RAToolkitConfigParser {
    
    // Maximum number of elements that exclusion list from query agents must have
    public static final String raToolkit_maxExclusion_tag = "ratoolkit.max_exclusion";
    public static int raToolkit_maxExclusion = 0; // Zero is the default value
    
    static {
        loadRAToolkitConfiguration();
    }
    
    private static void loadRAToolkitConfiguration() {
        
        /*------------------------------------------------------------------------------------------------*/
        // Maximum number of elements in the exclusion list from the query agents: ratoolkit.max_exclusion
        /*------------------------------------------------------------------------------------------------*/
        if(configuration.containsKey(raToolkit_maxExclusion_tag)) {
                     
            try {
                raToolkit_maxExclusion = Integer.valueOf(
                        configuration.getProperty(raToolkit_maxExclusion_tag));

                if(raToolkit_maxExclusion < 0 || raToolkit_maxExclusion > Integer.MAX_VALUE) {
                    logError("max exclusion value must be in the interval [0,MAX_VALUE=" + Integer.MAX_VALUE +"]");
                    
                    raToolkit_maxExclusion = 0;
                }
                else {
                    logMessage("ratoolkit.max_exclusion found: " + raToolkit_maxExclusion);                            
                }

            } catch (NumberFormatException | NullPointerException e) {            
                raToolkit_maxExclusion = 0;
            }
        } 
        
    }

}
