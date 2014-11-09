package RASupport.rasupport.rasupportconfig.resourcesmodel;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeCategories;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeCategories.*;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeTypes;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeTypes.*;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportResources.*;
import java.util.List;

/**
 * RASupportConfig: allowed dynamic attributes in the support
 * Add new dynamic attributes to the support
 * @author damianarellanes
 */
public enum RASupportDynamicAttributes implements RASupportAttributesInterface {
    
    // PROCESSING ATTRIBUTES    
    free_mem (FLOAT_ATTRIBUTE, PROCESSING, 10f, 16000f),
    busy_cpu (FLOAT_ATTRIBUTE, PROCESSING, 0f, 100f),
    
    // STORAGE ATTRIBUTES
    free_hdisk (FLOAT_ATTRIBUTE, STORAGE, 0f, 1000000f),    
    
    // DISPLAY ATTRIBUTES     
    
    // NETWORK ATTRIBUTES
    bandwidth (FLOAT_ATTRIBUTE, NETWORK, 100f, 1000f),
    latency (FLOAT_ATTRIBUTE, NETWORK, 100f, 1000f);
    
    // INTERNAL VARIABLES OF THE ATTRIBUTE    
    private final RASupportCommon.AttributeTypes type;    
    private final RASupportResources resourceModel;
    
    // Variables used by ResourceSim to generate random attributes with values within the range
    private final Number allowedMinValue;
    private final Number allowedMaxValue;
    private final List<String> allowedValues;
    
    // For numerical (int or float) attributes
    RASupportDynamicAttributes(RASupportCommon.AttributeTypes type, RASupportResources resource,
            Number min, Number max) {        
        this.type = type;        
        this.resourceModel = resource;
        
        // Initializing variables used by ResourceSim for numerical attributes
        this.allowedMinValue = min;
        this.allowedMaxValue = max;
        this.allowedValues = null;
    }
    
    // For string attributes
    RASupportDynamicAttributes(RASupportCommon.AttributeTypes type, RASupportResources resource,
            List<String> allowedValues) {               
        this.type = type;        
        this.resourceModel = resource;
        
        // Initializing variables used by ResourceSim for string attributes
        this.allowedMinValue = 0;
        this.allowedMaxValue = 0;
        this.allowedValues = allowedValues;
    }
    
    // METHODS TO GET THE CHARACTERISTICS OF THE ATTRIBUTE
    @Override
    public String getAttributeName() { return this.name(); }
    @Override
    public AttributeTypes getAttributeType() { return type; }    
    @Override
    public RASupportResources getResourceModel() { return resourceModel; }  
    
    @Override
    public Number getAllowedMinValue() { return allowedMinValue; }
    @Override
    public Number getAllowedMaxValue() { return allowedMaxValue; }
    @Override
    public List<String> getAllowedValues() { return allowedValues; }

    @Override
    public boolean isNumerical() {
        return (type.equals(FLOAT_ATTRIBUTE) || type.equals(INT_ATTRIBUTE));
    }
    @Override
    public boolean isString() {
        return type.equals(STRING_ATTRIBUTE);
    }
    @Override
    public AttributeCategories getCategory() {
        return DYNAMIC_ATTRIBUTE;
    }
    
    @Override
    public boolean isFloat() {
        return type.equals(FLOAT_ATTRIBUTE);
    }

    @Override
    public boolean isInteger() {
        return type.equals(INT_ATTRIBUTE);
    }
}
