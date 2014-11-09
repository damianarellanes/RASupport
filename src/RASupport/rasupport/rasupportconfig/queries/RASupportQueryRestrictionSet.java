package RASupport.rasupport.rasupportconfig.queries;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logWarning;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RASupport: class that represents a query restriction (e.g., a restriction between nodes)
 * Currently, RASupport only supports restrictions between nodes and restrictions between groupds
 * @author Damian Arellanes
 */
public class RASupportQueryRestrictionSet  {
    
    // Name of groups or name of nodes involved in the restriction evaluation
    // An example: group1, group3 means that the restrictions are going to be applicable to them
    private List<String> restrictedEntities = null;
    private String entitiesContent = "";
    
    // A concurrent map of restriction to be applicable
    private RASupportMap<RASupportQueryRestrictions, RASupportQueryRequirement> 
            restrictions = null;        
    
    public RASupportQueryRestrictionSet () {
        
        this.restrictedEntities = new ArrayList<>();
        this.restrictions = new RASupportMap<>();        
    }
    
    private void joinEntitiesContent() {
        
        // Deletes commas in entities list        
        // Add commas in a correct way
        StringBuilder sb = new StringBuilder();
        for(int i = 0, size = restrictedEntities.size(); i < size; i++) {
            
            if(i!= size-1) {
                sb.append(restrictedEntities.get(i).replace(RASupportCommon.querySeparator, "") + RASupportCommon.querySeparator);
            }            
            else {
                sb.append(restrictedEntities.get(i).replace(RASupportCommon.querySeparator, ""));
            }
        }
        entitiesContent = sb.toString();
    }
    
    public String getEntitiesContent() {
        return entitiesContent;
    }
    
    public void addEntity(String entity) {
        getRestrictedEntities().add(entity);
    }
    
    public void addEntities(List<String> entities) {
        restrictedEntities = entities;
        joinEntitiesContent();
    }
    
    // Facade to insert a numerical restriction
    public void addNumericalRestriction(RASupportQueryRestrictions restriction,
        Number min_val, Number min_ideal, Number max_ideal, Number max_val, double penalty) {
                
        // Tries to put a restriction in the restrictions set
        addRestriction(
            restriction, min_val, min_ideal, max_ideal, max_val, null, penalty, 
                "NUMERICAL"
        );        
    }
    
    // Facade to insert a string restriction
    public void addStringRestriction(RASupportQueryRestrictions restriction,
            String value, double penalty) {
                
        // Tries to put a restriction in the restrictions set
        addRestriction(
            restriction, null, null, null, null, value, penalty, "STRING"
        );        
    }
    
    private void addRestriction(RASupportQueryRestrictions restriction,
        Number min_val, Number min_ideal, Number max_ideal, Number max_val, 
        String value,
        double penalty,
        String type) {
                   
        // Valids the type of the specified attribute
        if(type.equals("NUMERICAL") && 
                !restriction.isNumerical()) {            
            logWarning(restriction + " is not a numerical restriction, please specify a valid numerical restriction",this.getClass());
            return;
        }
        else if(type.equals("STRING") && 
                !restriction.isString()) {
            logWarning(restriction + " is not a string restriction, please specify a valid string restriction",this.getClass());
            return;
        }
                
        // You can't specify a same restriction in the same set
        if(restrictions.containsKey(restriction)) {
            logWarning("the restriction " + restriction + " already exists in the restriction set", this.getClass());
            return;
        }
        
        // Creates a query requirement and puts in the respective map
        restrictions.put(restriction, 
            new RASupportQueryRequirement
                (restriction, min_val, min_ideal, max_ideal, max_val, value, penalty)
        );        
    }
    
    public boolean isValid() {
        
        /*// The entities list must have >= entities
        if(restrictedEntities.size() < 2) {
            logWarning("the entities list must have >= 2 entities" ,this.getClass());
            return false;
        }*/
        
        // Restriction set must have at least one restriction
        if(restrictions.size() == 0) {
            logWarning("the restriction set must have at least one restriction" ,this.getClass());
            return false;
        }
        
        return true;
    }

    /**
     * @return the restrictedEntities
     */
    public List<String> getRestrictedEntities() {
        return restrictedEntities;
    }

    /**
     * @return the restrictions
     */
    public Collection<RASupportQueryRequirement> getRestrictions() {
        return restrictions.values();
    }
    
    public int getSize() {
        return restrictions.size();
    }
    
    @Override
    public String toString() {
        return "\n------------------------------\n" +                
                "Entities: "+ restrictedEntities + "\n" + 
                "------------------------------\n" +
                "Restrictions: " + restrictions + "\n";
    }

}
