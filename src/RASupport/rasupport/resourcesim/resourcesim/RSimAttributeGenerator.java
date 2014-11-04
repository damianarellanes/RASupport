package RASupport.rasupport.resourcesim.resourcesim;

import java.util.Iterator;
import java.util.List;
import static RASupport.rasupport.resourcesim.common.RSimCommon.*;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeTypes.*;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon.*;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;

/**
 * ResourceSim : generator of random atributes of ResourceSim
 * @author Damian Arellanes
 */
public class RSimAttributeGenerator {
    
    RASupportRules behavior;
    private RASupportMap generatedDynamicAttributes;
    private RASupportMap generatedStaticAttributes;
    
    public RSimAttributeGenerator(RASupportRules behaviour) {
        
        this.behavior = behaviour;
        
        generatedStaticAttributes = new RASupportMap();
        generatedDynamicAttributes = new RASupportMap();
        
        /* THE ORDER OF THE FOLLOWING IS INMUTABLE */
        /*****************************************************************/
        // First of all, we create all the dynamic and static attributes
        createAttributes(DYNAMIC_ATTRIBUTES);
        createAttributes(STATIC_ATTRIBUTES);
        
        // Then, we compute the relationship between the attributes
        computeAttributeRelationship();
        /*****************************************************************/
    }
    
    private <E extends Enum<E>> void createAttributes(E[] category) {      
        
         String categoryName = category.getClass().getCanonicalName();
         String dynamicListName = DYNAMIC_ATTRIBUTES.getClass().getCanonicalName();
         RASupportMap selectedList;
         
         if(categoryName.equals(dynamicListName))
             selectedList = this.generatedDynamicAttributes;
         else 
             selectedList = this.generatedStaticAttributes;
         
        // Creating each attribute from AllowedCategoryAttributes with a random value
        for(int i = 0, size = category.length ; i < size; i++) {            
            
            Object value = generateRandomValueFor(category[i]);                                    
            selectedList.put(category[i], value);
        }
    }        
    
    private <E extends Enum<E>> void computeAttributeRelationship() {
        
        // For each list of relationship between attributes
        Iterator i = attributesRelation.iterator();
        while(i.hasNext()) {

            // Getting the list of the relationship as an array
            Object[] list = ((List<Object>) i.next()).toArray();

            Number currentMax = RASupportAttributes.getAllowedMaxValue((Enum<E>) list[list.length-1]);
            Object newValue;
            for(int index = list.length-1; index >= 0; index--) {

                // Setting the new value in the corresponding map
                if(generatedDynamicAttributes.containsKey(list[index])) {

                    // Getting the new value for the dynamic attribute in index position
                    newValue = generateNewRandomValue(
                            RASupportDynamicAttributes.class, 
                            (Enum<E>) list[index], 
                            currentMax);                    

                    generatedDynamicAttributes.put(list[index], newValue);
                }
                else {
                    
                    // Getting the new value for the static attribute in index position
                    newValue = generateNewRandomValue(
                            RASupportStaticAttributes.class, 
                            (Enum<E>) list[index], 
                            currentMax);
                    
                    generatedStaticAttributes.put(list[index], newValue);
                }
                
                currentMax = (Number) newValue;
            }
        }
    }
    
    public static <E extends Enum<E>> Object generateNewRandomValue( Class<?> clazz, Enum<E> attribute, 
            Number currentMax) {
        
        RASupportCommon.AttributeTypes type = RASupportAttributes.getAttributeType(attribute);
        Number min = null, max = null;
        
        if(type == FLOAT_ATTRIBUTE) {
            min = RASupportAttributes.getAllowedMinValue(attribute);
            max = Math.min(currentMax.floatValue(), 
                RASupportAttributes.getAllowedMaxValue(attribute).floatValue());

        }
        else if(type == INT_ATTRIBUTE) {
            min = RASupportAttributes.getAllowedMinValue(attribute);
            max = Math.min(currentMax.intValue(), 
                 RASupportAttributes.getAllowedMaxValue(attribute).intValue());
        }
        
        return generateRandomValueFor(attribute,min, max);
                                  
    }

    /**
     * @return the generatedDynamicAttributes
     */
    public RASupportMap getGeneratedDynamicAttributes() {
        return generatedDynamicAttributes;
    }

    /**
     * @param generatedDynamicAttributes the generatedDynamicAttributes to set
     */
    public void setGeneratedDynamicAttributes(RASupportMap generatedDynamicAttributes) {
        this.generatedDynamicAttributes = generatedDynamicAttributes;
    }

    /**
     * @return the generatedStaticAttributes
     */
    public RASupportMap getGeneratedStaticAttributes() {
        return generatedStaticAttributes;
    }

    /**
     * @param generatedStaticAttributes the generatedStaticAttributes to set
     */
    public void setGeneratedStaticAttributes(RASupportMap generatedStaticAttributes) {
        this.generatedStaticAttributes = generatedStaticAttributes;
    }
}
