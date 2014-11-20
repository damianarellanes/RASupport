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
    
    // Number of neighbors to send query agentes from Sp_initiator using iRandomWalks
    public static final String raToolkit_maxRW_tag = "ratoolkit.max_rw";
    public static int raToolkit_maxRW = 1; // One is the default value, in order to send to at least one neighbor
    
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
        
        /*------------------------------------------------------------------------------------------------*/
        // Maximum number of neighbors to send query agents from Sp_initiator using iRandomWalks: ratoolkit.initialN
        /*------------------------------------------------------------------------------------------------*/
        if(configuration.containsKey(raToolkit_maxRW_tag)) {
                     
            try {
                raToolkit_maxRW = Integer.valueOf(
                        configuration.getProperty(raToolkit_maxRW_tag));

                if(raToolkit_maxRW < 0 || raToolkit_maxRW > Integer.MAX_VALUE) {
                    logError("max exclusion value must be in the interval [0,MAX_VALUE=" + Integer.MAX_VALUE +"]");
                    
                    raToolkit_maxRW = 1;
                }
                else {
                    logMessage("ratoolkit.max_rw found: " + raToolkit_maxRW);                            
                }

            } catch (NumberFormatException | NullPointerException e) {            
                raToolkit_maxRW = 1;
            }
        }
        
    }

}
