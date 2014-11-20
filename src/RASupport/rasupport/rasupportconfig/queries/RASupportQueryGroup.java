package RASupport.rasupport.rasupportconfig.queries;

import static RASupport.rasupport.rasupportconfig.log.LogManager.logWarning;
import RASupport.rasupport.rasupportconfig.resourcesmodel.*;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.rasupportconfig.xml.XMLQueryReader;

/**
 * RASupport: representation of a group in RASupport
 * @author Damian Arellanes
 */
public class RASupportQueryGroup  {
    
    // Information about the group
    private String name = "";
    private int num_nodes = 0;
        
    private RASupportMap<RASupportAttributesInterface, RASupportQueryRequirement>
            attributes = null;
    
    // Content of the group
    private String content = "";
    
    // Restrictions between nodes in the group
    // We can have more than one restriction between nodes in the group
    private RASupportQueryRestrictionSet nodeRestrictions = null;
    
    public RASupportQueryGroup() {        
        
        this.attributes =  new RASupportMap();
    }
    
    public RASupportQueryGroup(String name, int nodes) {
        this.name = name;
        this.num_nodes = nodes;
        
        this.attributes =  new RASupportMap();
    }
    
    // For prototypes
    public RASupportQueryGroup(RASupportQueryGroup prototype) {
        
        this.name = prototype.getName();
        this.num_nodes = prototype.getNum_nodes();
        this.attributes = prototype.getAttributes();
        this.nodeRestrictions = prototype.getNodeRestrictions();
    }
    
    public String getContent() {
        
        return XMLQueryReader.getGroupContent(this);
    }
        
    // Facade to insert a numerical requirement
    public void addNumericalAttribute(RASupportAttributesInterface attribute,
            Number min_val, Number min_ideal, Number max_ideal, Number max_val, double penalty) {
    
        addAttribute(
            attribute, min_val, min_ideal, max_ideal, max_val, null, penalty, "NUMERICAL"
        );
        
    }
    
    // Facade to insert a string requirement
    public void addStringAttribute(RASupportAttributesInterface attribute, String value, double penalty) {
    
        addAttribute(
            attribute, null, null, null, null, value, penalty, "STRING"
        );
        
    }
    
    public void addNodeRestrictions(RASupportQueryRestrictionSet restrictions) {
        
        // A restriction between nodes can't have entities, because you don't hav would be those entites
        if(restrictions.getRestrictedEntities().size() > 0) {
            logWarning("restrictions between nodes can't have node names, please don't specify them", 
                    this.getClass());
            return;
        }
        
        if(restrictions.isValid()) {
            nodeRestrictions = restrictions;
        }                
    }
            
    private void addAttribute(RASupportAttributesInterface requirement,
        Number min_val, Number min_ideal, Number max_ideal, Number max_val, 
        String value,
        double penalty,
        String type) {
                   
        // Valids the type of the specified attribute
        if(type.equals("NUMERICAL") && 
                !requirement.isNumerical()) {            
            logWarning(requirement + " is not a numeric attribute, please specify a valid numeric attribute",this.getClass());
            return;
        }
        else if(type.equals("STRING") && 
                !requirement.isString()) {
            logWarning(requirement + " is not a string attribute, please specify a valid string attribute",this.getClass());
            return;
        }
        
        /*String tag = "";
        RASupportMap selectedMap = null;
        
        // Validates the attribute
        AttributeCategories category = requirement.getCategory();        
        if(category.equals(DYNAMIC_ATTRIBUTE)) {
            selectedMap = dynamicAttributes;
        }
        else {
            selectedMap = staticAttributes;
        }*/        
        
        // You can't specify a same attribute in the same group
        if(attributes.containsKey(requirement)) {
            logWarning("the attribute " + requirement + " already exists in the group " + getName(), this.getClass());
            return;
        }
        
        // Creates the attribute and puts in the respective map
        attributes.put(requirement, 
            new RASupportQueryRequirement
                (requirement, min_val, min_ideal, max_ideal, max_val, value, penalty)
        );        
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the num_nodes
     */
    public int getNum_nodes() {
        return num_nodes;
    }

    /**
     * @return the attributes
     */
    public RASupportMap<RASupportAttributesInterface, RASupportQueryRequirement> getAttributes() {
        return attributes;
    }
    
    
    /**
     * @return the nodeRestrictions
     */
    public RASupportQueryRestrictionSet getNodeRestrictions() {
        return nodeRestrictions;
    }
    
    @Override
    public String toString() {
        return "\n******************************\n" +
                "Group: " + getName() + "\n" +
                "******************************\n" +
                "Nodes: " + getNum_nodes() + "\n" +
                "Attributes: " + attributes + "\n" +                
                "Node restrictions: " + getNodeRestrictions() + "\n";
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param num_nodes the num_nodes to set
     */
    public void setNum_nodes(int num_nodes) {
        this.num_nodes = num_nodes;
    }
    
    public int getNumAttributes() {
        return attributes.size();
    }
    
    public boolean containsAttribute(RASupportAttributesInterface attribute) {
        
        return attributes.containsKey(attribute);
    }
    
    public RASupportQueryRequirement getAttribute(RASupportAttributesInterface attribute) {
        
        return attributes.get(attribute);
    }
    
    // The hash code is determinated by the string representation
    @Override
    public int hashCode() {        
        return toString().hashCode();
    }
    
    // Two groups are equal if they have the same string representation
    @Override
    public boolean equals(Object o) {
        
        return o.toString().equals(toString());
    }

}
