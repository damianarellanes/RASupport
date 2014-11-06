package RASupport.rasupport.rasupportconfig.common;

import java.util.Date;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;

/**
 * RASupportConfig: common variables used by the subsystems of the support
 * @author damianarellanes
 */
public interface RASupportCommon {
    
    // Initial capacity of concurrent hash maps
    public final int initialCapacity = 16;
    // Load factor in concurrent hash maps
    public final float loadFactor = 0.9f;
    // Concurrency level in concurrent hash maps
    public final int concurrencyLevel = 2;
    
    // Types of attributes (integer, float, or string)
    public enum AttributeTypes { INT_ATTRIBUTE, FLOAT_ATTRIBUTE, STRING_ATTRIBUTE };
    
    // Category of attributes (static or dynamic)
    public enum AttributeCategories { STATIC_ATTRIBUTE, DYNAMIC_ATTRIBUTE };    
    
    // Random attributes or real attributes
    // The mode is read from the configuration file
    public enum RASupportMode { RANDOM_MODE, REAL_MODE, NO_MODE };
    
    // ResourceSim generates all attributes o a random number of attributes
    // The rule is read from the configuration file
    public enum RASupportRules { GENERATE_ALL_ATTRIBUTES, GENERATE_RANDOM_ATTRIBUTES, NO_RULE };
    
    // Default values in use restrictions of the configuration file
    public final Date NO_CONFIG_AVAILABILITY_START = null;
    public final Date NO_CONFIG_AVAILABILITY_END = null;
    public final int NO_CONFIG_CPU_UTILIZED = -3;
    //public final boolean NO_CONFIG_ONLY_FOR_FRIENDS = false;
    public final RASupportMap NO_CONFIG_ONLY_FOR_FRIENDS = null;
    
}
