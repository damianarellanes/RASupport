package RASupport.rasupport.rasupportconfig.queries;

import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributesInterface;

/**
 * RASupport: interface that retsrictions must to implement
 * @author Damian Arellanes
 */
public interface RASupportQueryRestrictions extends RASupportAttributesInterface {
    
    public void evaluate();
    
}
