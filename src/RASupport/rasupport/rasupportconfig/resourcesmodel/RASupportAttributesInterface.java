package RASupport.rasupport.rasupportconfig.resourcesmodel;

import java.util.List;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeTypes;

/**
 * RASupportConfig: interface for the new enums of allowed attributes
 * By the meantime, this interface is only implemented in Allowed dynamic and static resources
 * @author damianarellanes
 */
public interface RASupportAttributesInterface {
    
    public String getAttributeName();
    public AttributeTypes getAttributeType();    
    public RASupportResources getResourceModel();
    
    public Number getAllowedMinValue();
    public Number getAllowedMaxValue();
    public List<String> getAllowedValues();
    
}
