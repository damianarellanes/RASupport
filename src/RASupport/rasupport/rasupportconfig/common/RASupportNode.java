package RASupport.rasupport.rasupportconfig.common;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeCategories;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeCategories.*;
import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;

/**
 * RASupportConfig: representation of a node with static and dynamic attributes
 * This node has a dynamic attributes monitor
 * @author Damian Arellanes
 */
public class RASupportNode {

    private RASupportMap useRestrictions;
    private RASupportMap staticAttributes;
    private RASupportMap dynamicAttributes;    
    
    // Reference to a node of the topology selected. RAToolkit is using Myconet
    // Reference to a topology node to acces to super-peers, protocols, etc
    private RASupportTopologyNode topologyNode;
    private String alias = "";
    
    public RASupportNode(RASupportTopologyNode topologyNode) {
        
        this.topologyNode = topologyNode;
        
        useRestrictions = new RASupportMap();
        staticAttributes = new RASupportMap();
        dynamicAttributes = new RASupportMap();       
        
        // Only for testing, we set the alias equals to the alias of the topology node
        // In real scenarios, we set the alias specified in the configuration
        alias = topologyNode.getAlias();
    }
    
    public void printAttributes(AttributeCategories category) {
        if(category == DYNAMIC_ATTRIBUTE) {
            dynamicAttributes.printMap("Dynamic attributes from peer " + topologyNode.getIdNode() +": ");
        } else if(category == STATIC_ATTRIBUTE) {
            staticAttributes.printMap("Static attributes from peer " + topologyNode +": ");
        } 
    }
    
    public void updateAttribute(Object key, Object update) {
        
        if(this.dynamicAttributes.containsKey(key)) {
            this.dynamicAttributes.put(key, update);
        }
        else if(this.staticAttributes.containsKey(key)) {
            this.staticAttributes.put(key, update);
        }
    }
    
    /**
     * @return the useRestrictions
     */
    public RASupportMap getUseRestrictions() {
        return useRestrictions;
    }

    /**
     * @param useRestrictions the useRestrictions to set
     */
    public void setUseRestrictions(RASupportMap useRestrictions) {
        this.useRestrictions = useRestrictions;
    }

    /**
     * @return the staticAttributes
     */
    public RASupportMap getStaticAttributes() {
        return staticAttributes;
    }

    /**
     * @param staticAttributes the staticAttributes to set
     */
    public void setStaticAttributes(RASupportMap staticAttributes) {
        this.staticAttributes = staticAttributes;
    }

    /**
     * @return the dynamicAttributes
     */
    public RASupportMap getDynamicAttributes() {
        return dynamicAttributes;
    }

    /**
     * @param dynamicAttributes the dynamicAttributes to set
     */
    public void setDynamicAttributes(RASupportMap dynamicAttributes) {
        this.dynamicAttributes = dynamicAttributes;
    }

    /**
     * @return the topologyNode
     */
    public RASupportTopologyNode getTopologyNode() {
        return topologyNode;
    }

    /**
     * @param topologyNode the topologyNode to set
     */
    public void setTopologyNode(RASupportTopologyNode topologyNode) {
        this.topologyNode = topologyNode;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
}
