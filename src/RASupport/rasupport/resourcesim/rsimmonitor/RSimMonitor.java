package RASupport.rasupport.resourcesim.rsimmonitor;

import static java.lang.System.exit;
import java.util.List;
import java.util.Map.Entry;
import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceAggregation;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceMonitor;
import static RASupport.rasupport.rasupportconfig.random.RASupportRandomManager.getRandomInt;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import static RASupport.rasupport.resourcesim.common.RSimCommon.*;

/**
 * RSim: Update monitor of dynamic attributes
 * Updates a random attribute from dynamic attributes map
 * This a module of RSim that can be used externally
 * @author Damian Arellanes
 */
public class RSimMonitor implements RASupportResourceMonitor {   
            
    RASupportNode node = null;
    private RASupportResourceAggregation resourceAggregation = null;
    
    private RASupportMap dynamicAttributes = null;
    private RASupportMap staticAttributes = null; // Only for the relationship list
    protected Thread thread;    
    
    // The monitor selects a value within the range to update a random dynamic attribute
    // Monitor waits between 2 and 10 ms (default configuration)
    private int MIN_UPDATING = 2;
    private int MAX_UPDATING = 10;

    public RSimMonitor(RASupportNode n, RASupportResourceAggregation resourceAggregation) {
        
        this.node = n;                
        this.resourceAggregation = resourceAggregation;
        
        this.dynamicAttributes = n.getDynamicAttributes();
        this.staticAttributes = n.getStaticAttributes();
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(getRandomInt(
                        MIN_UPDATING, 
                        MAX_UPDATING) 
                        * 1000);                
                updateRandomAttribute();
            } catch (InterruptedException ex) {}
        } 
    }
    
    @Override
    public void startMonitor() {
       
        if (thread == null) {
            thread = new Thread (this);
            thread.start ();
        } 
    }
    
    private void updateRandomAttribute() {
        
        // Updating a random attribute with index from 0 to the number of attributes in dynamicAttributes
        int min = 0;
        int max = dynamicAttributes.size()-1;
        Entry randomEntry = 
               dynamicAttributes.getRandomEntry(0, dynamicAttributes.size()-1);        
        
        RASupportDynamicAttributes attribute = (RASupportDynamicAttributes)randomEntry.getKey();        
        Number minValue = RASupportAttributes.getAllowedMinValue(attribute);
        Number maxValue = (Number) getRelationshipValue(attribute);
        
        Object name = randomEntry.getKey();
        Object oldValue = randomEntry.getValue();
        Object newValue = generateRandomValueFor(attribute, minValue, maxValue);
               
        logMessage(node.getAlias() + " updated: " + name + " " + oldValue + " -> " + newValue);
       
        randomEntry.setValue(newValue);
        
        // Sends a resource updating  to the indexer node
        // Resource aggregation module knows how to manage and updating
        resourceAggregation.updateDynamicResource(name.toString(), newValue.toString());
        
        // Only for testing
        /*Testing.monitorOracle(attribute, (Number) newValue, 
                dynamicAttributes, staticAttributes);*/
    }  
    
    // Gets the max value to generate a random value for attribute based on the relationship with the next element in the list
    private Object getRelationshipValue(RASupportDynamicAttributes attribute) {
            
        List<Object> selected = null;
        int index = -1;
        for(List<Object> list: attributesRelation) {
            
            index = list.indexOf(attribute);
            if(index != -1) {
                selected = list;                                
            }
        }
        
        if(index == -1) {
            return RASupportAttributes.getAllowedMaxValue(attribute);
        }
        else {
            if(index + 1 < selected.size()) {
                
                // Gets the reference of the succesor of attribute in the relationship list
                Object next = selected.get(index+1);
                Number nextValue = null;
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
                                
                return nextValue;
            }
            else {
                return RASupportAttributes.getAllowedMaxValue(selected.get(index));
            }
        }
        
    }

    @Override
    public void setUpdatingInterval(int min, int max) {
        this.MIN_UPDATING = min;
        this.MAX_UPDATING = max;
    }
}
