package RASupport.rasupport.rasupportconfig.modules;


/**
 * RASupportConfig: this interface must to be implemented by the attribute monitors (e.g., RSimMonitor)
 * @author Damian Arellanes
 */
public interface RASupportResourceMonitor extends RASupportModule, Runnable {            
    
    public void startMonitor();
    
    // Interval to perform a random updating (in ms)
    public void setUpdatingInterval(int min, int max);
        
}
