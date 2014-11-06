package RASupport.rasupport.ratoolkit.advertisementapi.rs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportResources;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportUseRestrictions;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportUseRestrictions.*;
import RASupport.rasupport.rasupportconfig.xml.TagsConfiguration;
import static RASupport.rasupport.rasupportconfig.xml.TagsConfiguration.indentationLevelThree;
import static RASupport.rasupport.rasupportconfig.xml.TagsConfiguration.indentationLevelTwo;
import RASupport.rasupport.rasupportconfig.xml.XMLManager;

/**
 * RAToolkit:: Advertisement phase: representation of a resource specification with initial attributes to update
 * This RS contains all the attributes from a specific peer
 * @author Damian Arellanes
 */
public class RSpec {
    
    private String peerAlias = "";
    private RASupportMap staticAttributes = null;
    private RASupportMap dynamicAttributes = null;    
    private RASupportMap useRestrictions = null;     
    
    // Variables to mantain the initial RS
    private String blockUseRestrictions = "";
    private String blockResources = "";
    
    public RSpec(String peerAlias,
            RASupportMap useRestrictions, RASupportMap dynamicAttributes, RASupportMap staticAttributes) {
        
        this.peerAlias = peerAlias;
        this.useRestrictions = useRestrictions;
        this.dynamicAttributes = dynamicAttributes;
        this.staticAttributes = staticAttributes;                
        
        blockUseRestrictions = createUseRestrictions(useRestrictions);
        blockResources = createResources(dynamicAttributes, staticAttributes);                
    }
    
    public File get() {                                
        StringBuilder rs = new StringBuilder();
        rs.append(TagsConfiguration.openRSInitialTag);
        rs.append(blockUseRestrictions);
        rs.append(blockResources);
        rs.append(TagsConfiguration.closeRSInitialTag);        
                
        return saveXML(rs.toString(), peerAlias);        
    }
    
    private static File saveXML(String xmlBody, String xmlName) {
        
        return XMLManager.saveXML(xmlBody, xmlName);
    }
    
    public void printRS(String rs) {
        
        logMessage(rs);        
    }    
        
    private String createUseRestrictions(RASupportMap useRestrictions) {
        
        StringBuilder block = new StringBuilder();
        block.append(TagsConfiguration.openUseRestrictionsTag);
      
        // Iterating over the use restrictions specified in the configuration of RASupport
        for(RASupportUseRestrictions ur: RASupportUseRestrictions.values()) {
            
            if(useRestrictions.containsKey(ur)) {              
                
                // Parsing hash maps
                if (ur.equals(only_for_friends)) {
                    RASupportMap map = (RASupportMap) useRestrictions.get(ur);                                        
                    String tag = TagsConfiguration.createTag(
                            ur.getUseRestrictionName(), map, indentationLevelThree
                    );                    
                    
                    block.append(tag);                    
                }
                /*// Parsing array lists
                else if(ur.equals(AVAILABILITY)) {
                    List list = (List) useRestrictions.get(ur);
                    
                    for(Object listElement: list) {
                        System.out.println(ur + ": " + listElement);
                    }                    
                }*/
                // Parsing integers, strings or floats
                else {
                    Object element = useRestrictions.get(ur);
                    String tag = TagsConfiguration.createTag(
                            ur.getUseRestrictionName(), String.valueOf(element), indentationLevelThree
                    );
                    
                    block.append(tag);
                }                
            }
        }
        
        block.append(TagsConfiguration.closeUseRestrictionsTag);
        
        return block.toString();
    }
    
    private String createResources(RASupportMap dynamicAttributes, 
            RASupportMap staticAttributes) {
        
        StringBuilder block = new StringBuilder();        
        
        // Open XML tags of each configurated resource and creates a string builder for each one
        Map tmpR = new HashMap();
        for(RASupportResources r: RASupportResources.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(TagsConfiguration.openTagEndLine(
                    r.getResourceAlias(), indentationLevelTwo
            ));
            
            tmpR.put(r, sb);
        }
        
        // Insert XML tags of mapTarget (dynamic/static resources) into the corresponding builder (tmpR)
        computeResources(dynamicAttributes, tmpR);
        computeResources(staticAttributes, tmpR);                
        
        
        // Close XML tags for each configurated resource and inserts each string builder into the block of resources
        for(RASupportResources r: RASupportResources.values()) {
            StringBuilder sb = (StringBuilder) tmpR.get(r);
            
            sb.append(TagsConfiguration.closeTagEndLine(
                    r.getResourceAlias(), indentationLevelTwo
            ));
            
            block.append(sb);
        } 
        
        
        return  block.toString();
    }
    
    // TODO remove to increase performance
    // It is not recommendable to create this each time we create a RS initial
    // Maybe is better build a hash map of resources and parse this
    private void computeResources(RASupportMap mapTarget, Map builders) {
        for(Object e: mapTarget.entrySet()) {
            
            Object attribute = ((Entry)e).getKey();
            RASupportResources resource = RASupportAttributes.getResourceModel(attribute);
            String alias = RASupportAttributes.getAttributeName(attribute);
            Object value = ((Entry)e).getValue();
            
            ((StringBuilder)builders.get(resource)).append(TagsConfiguration.createTag(
                    alias, String.valueOf(value), indentationLevelThree
            ));
        }
    }

    /**
     * @return the blockResources
     */
    public String getBlockResources() {
        return blockResources;
    }

    /**
     * @param blockResources the blockResources to set
     */
    public void setBlockResources(String blockResources) {
        this.blockResources = blockResources;
    }

}
