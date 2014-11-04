package RASupport.rasupport.rasupportconfig.resourcesmodel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon.*;
import static RASupport.rasupport.rasupportconfig.common.RASupportErrors.*;

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
