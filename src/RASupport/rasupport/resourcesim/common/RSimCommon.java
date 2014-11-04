package RASupport.rasupport.resourcesim.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeTypes.*;
import static RASupport.rasupport.rasupportconfig.common.RASupportErrors.*;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes.*;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes.*;
import RASupport.rasupport.rasupportconfig.random.RASupportRandomManager;

/**
 * RSimCommon configuration for the module ResourceSim
 * @author Damian Arellanes
 */
public class RSimCommon {
    
    public static final RASupportStaticAttributes[] STATIC_ATTRIBUTES = RASupportStaticAttributes.values();
    
    public static final RASupportDynamicAttributes[] DYNAMIC_ATTRIBUTES = RASupportDynamicAttributes.values();
    
    // RELATIONSHIP BETWEEN STATIC AND DYNAMIC RESOURCES
    // Minimum resource always has a lower or equal value than the maximum value
    // Only for numerical types
    // Structure: minimum (index 0), and maximum (next indexes from 1 to n)
    // Ordered from minimum to maximum
    
    public static final List<List<Object>> attributesRelation = new ArrayList<List<Object>>() {{
        add(Arrays.asList(free_hdisk, total_hdisk));    
    }};
        
    // Parameters: attribute, min value (optional), currentMax value (optional)
    public static <E extends Enum<E>> Object generateRandomValueFor(Enum<E> attribute, 
            Number...params) {
        
        Class<?> clazz = attribute.getClass();
        
        RASupportCommon.AttributeTypes type = RASupportAttributes.getAttributeType(attribute);
        
        // We can add 2 default parameters (min and currentMax)
        Number min,max;
        
        if(params.length == 2) {
           min = params[0];
           max = params[1];
        } else {
           min = RASupportAttributes.getAllowedMinValue(attribute);
           max = RASupportAttributes.getAllowedMaxValue(attribute);
        }
        
        if(type == STRING_ATTRIBUTE) {                            

        List<String> allowedValues = RASupportAttributes.getAllowedValues(attribute);
        min = 0;
        max = allowedValues.size();
        int randInt = RASupportRandomManager.getRandomInt(
                min.intValue(), max.intValue());
        return allowedValues.get(randInt);
        
        } else if(type == FLOAT_ATTRIBUTE) {                                              
            return RASupportRandomManager.getRandomFloat(
                    min.floatValue(), max.floatValue());                    
        }
        else if(type == INT_ATTRIBUTE) {
            return RASupportRandomManager.getRandomInt(
                    min.intValue(), max.intValue());
        }
        
        return ERROR_UNDEFINED_ATTRIBUTE_TYPE;
    }        
}
