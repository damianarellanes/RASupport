package RASupport.rasupport.resourcesim.resourcesim;

import static RASupport.rasupport.rasupportconfig.config.RASupportConfigParser.raConfig_RSim_Behaviour;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceManager;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;

/**
 * ResourceSim: facade for the ResourceSim API
 * @author damianarellanes
 */
public class ResourceSim implements RASupportResourceManager {
    
    RSimAttributeGenerator generator;
    
    public ResourceSim() { 
                
        generator = new RSimAttributeGenerator(raConfig_RSim_Behaviour);
    }                

    @Override
    public RASupportMap obtainDynamicAttributes() {
        return generator.getGeneratedDynamicAttributes();
    }

    @Override
    public RASupportMap obtainStaticAttributes() {
        return generator.getGeneratedStaticAttributes();
    }
}