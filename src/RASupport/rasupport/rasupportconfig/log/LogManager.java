package RASupport.rasupport.rasupportconfig.log;

/**
 * RASupportConfig: manager of log messages in the support to output important issues
 * TODO implement this functionality
 * @author Damian Arellanes
 */
public class LogManager {       
    
    private static final String PREFIX_MSG = "RASupport: ";
    private static final String PREFIX_ERROR = "RASupport ERROR: ";
    private static final String PREFIX_WARNING = "RASupport WARNING";
    //private static final File logFile =  new File("RASupport.log");
    //private static boolean show_messages = true;
    
    public static void logMessage(String message) {
        
            System.out.println(PREFIX_MSG + message);
    }
    
    public static void logMessage(String message, Class name) {
        
            System.out.println(PREFIX_MSG +"::" + name.getCanonicalName() + ": " + message);
    }
    
    public static void logError(String message) {
        
            System.err.println(PREFIX_ERROR + message);
    }
    
    public static void logError(Throwable message) {
       
            System.err.println(PREFIX_ERROR + message);
    }
    
    public static void logError(String message, Class name) {
       
            System.err.println(PREFIX_ERROR +"::" + name.getCanonicalName() + ":" + message);
    }
    
    public static void logWarning(String message, Class name) {
       
            System.err.println(PREFIX_WARNING +"::" + name.getCanonicalName() + ": " + message);
    }
}
