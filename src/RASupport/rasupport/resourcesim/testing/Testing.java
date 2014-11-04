package RASupport.rasupport.resourcesim.testing;

import static java.lang.System.exit;
import java.util.Iterator;
import java.util.List;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import static RASupport.rasupport.resourcesim.common.RSimCommon.attributesRelation;

/**
 * ResourceSim: interface that contains all the oracles to perform testing on the module ResourceSim
 * @author damianarellanes
 */
public interface Testing {
    
    public static void monitorOracle(Object attribute, Number attrValue, 
            RASupportMap dynamicAttributes, RASupportMap staticAttributes) {
        Iterator i = attributesRelation.iterator();
        while(i.hasNext()) {
            // Getting the list of the relationship as an array
            List<Object> list = (List<Object>) i.next();
            
            int index = list.indexOf(attribute);            
            if(index != -1 && index != list.size()-1) {
                
                Object next = list.get(index+1);
                Number nextValue = null;
                
                // If the next attribute from the relationship is in dynamic or static attributes
                if(dynamicAttributes.containsKey(next)) {
                    nextValue = (Number) dynamicAttributes.get(next);
                }
                else if(staticAttributes.containsKey(next)) {
                    nextValue = (Number) staticAttributes.get(next);
                }
                else{
                    System.err.println(next + " is not a static or dynamic attribute from the node");
                    exit(0);
                }
                                
                // Compares the new value of the attribute its sucessor in the relationship list
                // In the relationship there is always numeric values
                if(attrValue.floatValue() > nextValue.floatValue()) {
                    System.err.println(attribute + "=" + attrValue + 
                            " greater than " + list.get(index+1) + "=" + nextValue);
                    exit(0);
                }
            }
        }
        
        System.out.println("TEST SUCCESSFUL!");
    }
    
}
