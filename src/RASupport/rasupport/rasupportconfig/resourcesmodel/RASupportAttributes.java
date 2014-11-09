package RASupport.rasupport.rasupportconfig.resourcesmodel;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeCategories;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeTypes;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeTypes.*;
import static RASupport.rasupport.rasupportconfig.common.RASupportErrors.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RASupportConfig: manages all the allowed attributes in the support 
 * (dynamic and static attributes supported)
 * @author Damian Arellanes
 */
public class RASupportAttributes {
    
    // Method names from the AllowedAttributesInterface
    // If a method name of such a interface changes, we have to change it MANUALLY
    static Method[] methods = RASupportAttributes.class.getDeclaredMethods();
    private static final String methodAlias = "getAttributeName";
    private static final String methodType = "getAttributeType";
    private static final String methodResources = "getResourceModel";
    
    private static final String methodMinVal = "getAllowedMinValue";
    private static final String methodMaxVal = "getAllowedMaxValue";
    private static final String methodAllowedValues = "getAllowedValues";
    
    private static final String classDynamicAttributes = 
            RASupportDynamicAttributes.class.getCanonicalName();
    private static final String classStaticAttributes = 
            RASupportStaticAttributes.class.getCanonicalName();
    
    private static final Class attributesInterface = RASupportAttributesInterface.class;
    
    public static String getAttributeName (Object attribute) {
        return (String) invokeMethod(methodAlias, attribute);
    }
    
    public static AttributeTypes getAttributeType (Object attribute) {
        return (AttributeTypes) invokeMethod(methodType, attribute);
    }
    
    public static RASupportResources getResourceModel(Object attribute) {
        return (RASupportResources) invokeMethod(methodResources, attribute);
    }
    
    public static Number getAllowedMinValue (Object attribute) {
        return (Number) invokeMethod(methodMinVal, attribute);
    }
    
    public static Number getAllowedMaxValue (Object attribute) {
        return (Number) invokeMethod(methodMaxVal, attribute);
    }
    
    public static List<String> getAllowedValues (Object attribute) {
        return (List<String>) invokeMethod(methodAllowedValues, attribute);
    }
    
    public static AttributeCategories getCategory(Object attribute) {
        
        if(attribute.getClass().getCanonicalName().equals(classDynamicAttributes)) {
            return AttributeCategories.DYNAMIC_ATTRIBUTE;
        }
        else if(attribute.getClass().getCanonicalName().equals(classStaticAttributes)) {
            return AttributeCategories.STATIC_ATTRIBUTE;
        }
        else {
            return null;
        }
    }
    
    public static boolean isNumerical(Object attribute) {
        
        if(!isAttribute(attribute)) {
            return false;
        }
        
        AttributeTypes realType = getAttributeType(attribute);
        return (realType.equals(INT_ATTRIBUTE) || realType.equals(FLOAT_ATTRIBUTE));
    }
    
    public static boolean isString(Object attribute) {
        
        if(!isAttribute(attribute)) {
            return false;
        }
        
        return getAttributeType(attribute).equals(STRING_ATTRIBUTE);
    }
    
    public static boolean isAttribute(Object obj) {
                
        return (attributesInterface.isAssignableFrom(obj.getClass()));
    }
    
    private static Object invokeMethod(
            String methodName,
            Object attribute) {
        
        try {
            
            Method method = attribute.getClass().getDeclaredMethod(methodName);
            return method.invoke(attribute);
            
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(RASupportAttributes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(RASupportAttributes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(RASupportAttributes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(RASupportAttributes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(RASupportAttributes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ERROR_NO_ATTRIBUTE_METHOD;
    }
    

}
