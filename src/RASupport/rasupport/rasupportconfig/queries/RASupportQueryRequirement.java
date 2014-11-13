package RASupport.rasupport.rasupportconfig.queries;

import static RASupport.rasupport.rasupportconfig.log.LogManager.*;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributesInterface;
import RASupport.rasupport.rasupportconfig.xml.TagsConfiguration;

/**
 * RASupport: this is the parent class of the query attributes, node restrictions and group restrictions
 * @author Damian Arellanes
 */
public class RASupportQueryRequirement {
    
    private RASupportAttributesInterface attribute;
    private boolean isNumerical; // To increase speedv
    private boolean isString; // To increase speed
    private Number min_val;
    private Number max_val;
    private Number min_ideal;
    private Number max_ideal;
    private double penalty;
    
    private String value = ""; // Only for string attributes
    
    public RASupportQueryRequirement(RASupportAttributesInterface attribute, Number min_val, Number min_ideal, 
            Number max_ideal, Number max_val, String value, double penalty) {
                
        if(attribute.isNumerical()) {
            
            this.value = null;                        
            this.isNumerical = true;
            this.isString = false;
            
            if(attribute.isFloat()) {
                this.min_val = min_val.doubleValue();
                this.min_ideal = min_ideal.doubleValue();
                this.max_ideal = max_ideal.doubleValue();
                this.max_val = max_val.doubleValue();
            }
            else if(attribute.isInteger()) {
                this.min_val = min_val.intValue();
                this.min_ideal = min_ideal.intValue();
                this.max_ideal = max_ideal.intValue();
                this.max_val = max_val.intValue();
            }
        }     
        else if(attribute.isString()) {
            
            this.min_val = null;
            this.min_ideal = null;
            this.max_ideal = null;
            this.max_val = null;
            this.value = value;
            
            this.isNumerical = false;
            this.isString = true;
        }
        else {
            logError("invalid query requirement, please match the constructor", this.getClass());
            return;
        }
        
        // Common variables between numeric and string attributes
        this.attribute = attribute;        
        this.penalty = penalty;        
    }
    
    // The order of the content is the configuration of XML tags
    public String getContent() {
        
        if(isNumerical) {
            return TagsConfiguration.orderQueryNumericalRequirement(min_val, min_ideal, max_ideal, max_val, penalty);
        }
        else {
            return TagsConfiguration.orderQueryStringRequirement(getValue(), penalty);
        }        
    }
    
    public boolean isNumerical() {
        return isNumerical;
    }
    
    public boolean isString() {
        return isString;
    }
    
    public boolean isInteger() {
        return attribute.isInteger();
    }
    
    public boolean isFloat() {
        return attribute.isFloat();
    }

    /**
     * @return the attribute
     */
    public RASupportAttributesInterface getAttribute() {
        return attribute;
    }

    /**
     * @param attribute the attribute to set
     */
    public void setAttribute(RASupportAttributesInterface attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the min_val
     */
    public Number getMin_val() {
        return min_val;
    }

    /**
     * @param min_val the min_val to set
     */
    public void setMin_val(Number min_val) {
        this.min_val = min_val;
    }

    /**
     * @return the max_val
     */
    public Number getMax_val() {
        return max_val;
    }

    /**
     * @param max_val the max_val to set
     */
    public void setMax_val(Number max_val) {
        this.max_val = max_val;
    }

    /**
     * @return the min_ideal
     */
    public Number getMin_ideal() {
        return min_ideal;
    }

    /**
     * @param min_ideal the min_ideal to set
     */
    public void setMin_ideal(Number min_ideal) {
        this.min_ideal = min_ideal;
    }

    /**
     * @return the max_ideal
     */
    public Number getMax_ideal() {
        return max_ideal;
    }

    /**
     * @param max_ideal the max_ideal to set
     */
    public void setMax_ideal(Number max_ideal) {
        this.max_ideal = max_ideal;
    }

    /**
     * @return the penalty
     */
    public double getPenalty() {
        return penalty;
    }

    /**
     * @param penalty the penalty to set
     */
    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }
    
    @Override
    public String toString() {
        return getContent();
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

}
