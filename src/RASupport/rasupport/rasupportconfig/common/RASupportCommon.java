package RASupport.rasupport.rasupportconfig.common;

import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import java.io.File;
import java.util.Date;

/**
 * RASupportConfig: common variables used by the subsystems of the support
 * @author damianarellanes
 */
public interface RASupportCommon {
    
    /********************************************************************************/
    // Initializer of RASupport
    /********************************************************************************/
    public final RASupportInitializer initializer = new RASupportInitializer();
    
    /********************************************************************************/
    // Concurrency of RASupportMap
    /********************************************************************************/
    // Initial capacity of concurrent hash maps
    public final int initialCapacity = 16;
    // Load factor in concurrent hash maps
    public final float loadFactor = 0.9f;
    // Concurrency level in concurrent hash maps
    public final int concurrencyLevel = 2;
    
    
    /********************************************************************************/
    // Attributes configuration
    /********************************************************************************/
    // Types of attributes (integer, float, or string)
    public enum AttributeTypes { INT_ATTRIBUTE, FLOAT_ATTRIBUTE, STRING_ATTRIBUTE };
    
    // Category of attributes (static or dynamic)
    public enum AttributeCategories { STATIC_ATTRIBUTE, DYNAMIC_ATTRIBUTE };    
    
    
    /********************************************************************************/
    // RASupport common configuration of .properties
    /********************************************************************************/
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
 
    /********************************************************************************/
    // RASupport common configuration of queries
    /********************************************************************************/
    public final String queriesDirectory = "RASupportQueries";
    public final String schemasDirectory = "RASupportSchemas";
    public final String schemaFilePath = "RASupportSchemas/Query.xsd";
    public final File schemaFile = new File(schemaFilePath);
    public enum RASupportQueryReader { INSIDE, OUTSIDE }
    public final int numericalArguments = 5; // Numerical attributes has 5 arguments (min_val, min_ideal, max_ideal, max_val, and penalty)
    public final int stringArguments = 2; // String attributes has 2 arguments (value and penalty)
    public final String querySeparator = ",";
    
}
