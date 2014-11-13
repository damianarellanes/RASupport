package RASupport.rasupport.rasupportconfig.resourcesmodel;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeCategories.*;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeTypes.*;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportResources.*;
import java.util.Arrays;
import java.util.List;


/**
 * RASupportConfig: allowed static attributes in the support
 * Add new static attributes to the support
 * @author damianarellanes
 */
public enum RASupportStaticAttributes implements RASupportAttributesInterface {
    
    // PROCESSING ATTRIBUTES    
    cpu_speed (FLOAT_ATTRIBUTE, PROCESSING, 500f, 5000f),           
    cores (INT_ATTRIBUTE, PROCESSING, 1, 8),
    //os_name (STRING_ATTRIBUTE, PROCESSING, Arrays.asList("Windows", "Linux", "HP UX")),    
    os_name (STRING_ATTRIBUTE, PROCESSING, Arrays.asList("Linux")),    
    
    // STORAGE ATTRIBUTES    
    total_hdisk (FLOAT_ATTRIBUTE, STORAGE, 1000f, 1000000f),
    
    // DISPLAY ATTRIBUTES 
    screen_width (INT_ATTRIBUTE, DISPLAY, 100, 1024),
    screen_height (INT_ATTRIBUTE, DISPLAY, 100, 1024),
    bit_depth (INT_ATTRIBUTE, DISPLAY, 1, 8),
    refresh_rate (INT_ATTRIBUTE, DISPLAY, 1, 1000),
    
    // NETWORK ATTRIBUTES
    ;
    
    // INTERNAL VARIABLES OF THE ATTRIBUTE    
    private final RASupportCommon.AttributeTypes type;    
    private final RASupportResources resourceModel;
    
    // Variables used by ResourceSim to generate random attributes with values within the range
    private final Number allowedMinValue;
    private final Number allowedMaxValue;
    private final List<String> allowedValues;
    
    // For numerical (int or float) attributes
    RASupportStaticAttributes(RASupportCommon.AttributeTypes type, RASupportResources resource,
            Number min, Number max) {                
        this.type = type;        
        this.resourceModel = resource;
        
        // Initializing variables used by ResourceSim for numerical attributes
        this.allowedMinValue = min;
        this.allowedMaxValue = max;
        this.allowedValues = null;
    }
    
    // For string attributes
    RASupportStaticAttributes(RASupportCommon.AttributeTypes type, RASupportResources resource,
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
    public RASupportCommon.AttributeTypes getAttributeType() { return type; }    
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
    public RASupportCommon.AttributeCategories getCategory() {
        return STATIC_ATTRIBUTE;
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