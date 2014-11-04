package RASupport.rasupport.rasupportconfig.resourcesmodel;

/**
 * RASupport: use restrictions for the RS in the support
 * Add new use restrictions to the support
 * @author damianarellanes
 */
public enum RASupportUseRestrictions {
    
    availability_start (),
    availability_end (),
    max_cpu_utilization (),
    only_for_friends ();
        
    RASupportUseRestrictions() {        
    }
    
    public String getUseRestrictionName() { return this.name(); }     
}

