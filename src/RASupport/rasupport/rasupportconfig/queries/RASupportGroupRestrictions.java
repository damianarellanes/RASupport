package RASupport.rasupport.rasupportconfig.queries;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeCategories;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeCategories.*;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeTypes.*;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportResources;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportResources.*;
import java.util.List;

/**
 * RASupport: allowed restrictions between groups in RASupport
 * @author damianarellanes
 */
public enum RASupportGroupRestrictions implements RASupportQueryRestrictions  {

    latency (DYNAMIC_ATTRIBUTE, FLOAT_ATTRIBUTE, NETWORK, 100f, 1000f),
    bandwidth (DYNAMIC_ATTRIBUTE, FLOAT_ATTRIBUTE, NETWORK, 100f, 1000f);
    
    
    // INTERNAL VARIABLES OF THE RESTRICTION
    private final AttributeCategories category;
    private final RASupportCommon.AttributeTypes type;    
    private final RASupportResources resourceModel;    
    
    // Variables used by ResourceSim to generate random restriction values within the range
    private final Number allowedMinValue;
    private final Number allowedMaxValue;
    private final List<String> allowedValues;

    // For numerical (int or float) restrictions
    RASupportGroupRestrictions(AttributeCategories category,
            RASupportCommon.AttributeTypes type, RASupportResources resource,
            Number min, Number max) {        
        
        this.category = category;
        this.type = type;        
        this.resourceModel = resource;
        
        // Initializing variables used by ResourceSim for numerical restrictions
        this.allowedMinValue = min;
        this.allowedMaxValue = max;
        this.allowedValues = null;
    }
    
    // For string restrictions
    RASupportGroupRestrictions(AttributeCategories category,
            RASupportCommon.AttributeTypes type, RASupportResources resource,
            List<String> allowedValues) {               
        
        this.category = category;
        this.type = type;        
        this.resourceModel = resource;
        
        // Initializing variables used by ResourceSim for string restrictions
        this.allowedMinValue = 0;
        this.allowedMaxValue = 0;
        this.allowedValues = allowedValues;
    }
    
    @Override
    public void evaluate() {
        
    }

    // METHODS TO GET THE CHARACTERISTICS OF THE RESTRICTION
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
        return category;
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
