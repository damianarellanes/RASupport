package RASupport.rasupport.rasupportconfig.modules;

import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;

/**
 * RASupportConfig: this class must to be inhereted from the facades of the attribute managers (e.g., ResourceSim)
 * @author Damian Arellanes
 */
public abstract interface RASupportResourceManager extends RASupportModule {
    
    public abstract RASupportMap obtainDynamicAttributes();
    
    public abstract RASupportMap obtainStaticAttributes();

}
