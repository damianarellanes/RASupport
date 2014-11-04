package RASupport.rasupport.rasupportconfig.resourcesmodel;

/**
 * RASupportConfig: allowed resource models in the support
 * Add new resource models to the support
 * @author damianarellanes
 */
public enum RASupportResources {    
    
    PROCESSING ("processing"),
    STORAGE ("storage"),
    DISPLAY ("display"),
    NETWORK ("network");
    
    private final String alias;    
    RASupportResources(String alias) {
        this.alias = alias;                 
    }
    
    public String getResourceAlias() { return alias; }       
}