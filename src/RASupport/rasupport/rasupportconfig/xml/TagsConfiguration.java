package RASupport.rasupport.rasupportconfig.xml;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;

/**
 * RASupportConfig: configuration to create XML tags in XML documents
 * TODO: transform into enumerate
 * @author Damian Arellanes
 */
public interface TagsConfiguration {
    
    /********************************************************************************/
    // Strings for XML tags
    /********************************************************************************/
    
    public final String openStartingXMLTag = "<";
    public final String openEndingXMLTag = "</";
    public final String closeXMLTag = ">";    
    
    /********************************************************************************/
    // XML header
    /********************************************************************************/
    
    public static final String XML_VERSION = "1.0";    
    public static final String XML_HEADER = openStartingXMLTag + "?xml version=\"" + 
            XML_VERSION + "\"?" + closeXMLTag + "\n";
    
    /********************************************************************************/
    // Indentation
    /********************************************************************************/
    
    // Indentation for the following tags: use and categories
    public final String indentationLevelTwo = "\t";
    
    // Indentation for use restrictions or attributes
    public final String indentationLevelThree = "\t\t";
    
    // Indentation for restrictions between nodes
    public final String indentationLevelFour = "\t\t\t";
    
    /********************************************************************************/
    // Query tags
    /********************************************************************************/
    
    public final String queryTag = "query";
    public final String openQueryTag = openTagEndLine(queryTag, "");
    public final String closeQueryTag = closeTag(queryTag, "");
    
    // Option
    public final String queryOptionTag = "option";
    public final String openQueryOptionTag = openTag(queryOptionTag, indentationLevelTwo);
    public final String closeQueryOptionTag = closeTagEndLine(queryOptionTag);
    
    // TTL
    public final String queryTTLTag = "ttl";
    public final String openQueryTTLTag = openTag(queryTTLTag, indentationLevelTwo);
    public final String closeQueryTTLTag = closeTagEndLine(queryTTLTag);
    
    // Group 
    public final String queryGroupTag = "group";
    public final String openQueryGroupTag = openTagEndLine(queryGroupTag, indentationLevelTwo);
    public final String closeQueryGroupTag = closeTagEndLine(queryGroupTag, indentationLevelTwo);
    
    // Name of the group 
    public final String queryGroupNameTag = "name";
    public final String openQueryGroupNameTag = openTag(queryGroupNameTag, indentationLevelThree);
    public final String closeQueryGroupNameTag = closeTagEndLine(queryGroupNameTag);
    
    // Number of nodes for the group 
    public final String queryGroupNodesTag = "num_nodes";
    public final String openQueryGroupNodesTag = openTag(queryGroupNodesTag, indentationLevelThree);
    public final String closeQueryGroupNodesTag = closeTagEndLine(queryGroupNodesTag);
    
    // Restriction between nodes
    public final String queryRestrictionNodesTag = "rest_betw_nodes";
    public final String openqueryRestrictionNodesTag = openTagEndLine(queryRestrictionNodesTag, indentationLevelThree);
    public final String closequeryRestrictionNodesTag = closeTagEndLine(queryRestrictionNodesTag, indentationLevelThree);
    public final String queryRestrictionNodesEntitiesTag = "node_names";
    public final String openqueryRestrictionNodesEntitiesTag = openTag(queryRestrictionNodesEntitiesTag, indentationLevelFour);
    public final String closequeryRestrictionNodesEntitiesTag = closeTagEndLine(queryRestrictionNodesEntitiesTag);
    
    // Restriction between groups
    public final String queryRestrictionGroupsTag = "rest_betw_groups";
    public final String openqueryRestrictionGroupsTag = openTagEndLine(queryRestrictionGroupsTag, indentationLevelTwo);
    public final String closequeryRestrictionGroupsTag = closeTagEndLine(queryRestrictionGroupsTag, indentationLevelTwo);
    public final String queryRestrictionGroupsEntitiesTag = "group_names";
    public final String openqueryRestrictionGroupsEntitiesTag = openTag(queryRestrictionGroupsEntitiesTag, indentationLevelThree);
    public final String closequeryRestrictionGroupsEntitiesTag = closeTagEndLine(queryRestrictionGroupsEntitiesTag);
    
    // Order of numerical requirements: min_val, min_ideal, max_ideal, max_val, penalty
    public static String orderQueryNumericalRequirement(Number min_val, Number min_ideal, 
            Number max_ideal, Number max_val, Number penalty) {
        return min_val + RASupportCommon.querySeparator + min_ideal + RASupportCommon.querySeparator + max_ideal + 
                RASupportCommon.querySeparator + max_val + RASupportCommon.querySeparator + penalty;
    }
    
    // Order of string requirements: min_val, min_ideal, max_ideal, max_val, penalty
    public static String orderQueryStringRequirement(String value, Number penalty) {
        return value + RASupportCommon.querySeparator + penalty;
    }
    
    /********************************************************************************/
    // RS initial tags
    /********************************************************************************/
    
    public final String rsInitialTag = "rs_initial";
    public final String openRSInitialTag = openTag(rsInitialTag, "") + "\n";            
    public final String closeRSInitialTag = closeTag(rsInitialTag, "");
    
    /********************************************************************************/
    // Use restrictions tags
    /********************************************************************************/
    
    public final String useRestrictionsTag = "use";
    public final String openUseRestrictionsTag = 
            openTag(useRestrictionsTag, indentationLevelTwo) + "\n";            
    public final String closeUseRestrictionsTag = 
            closeTag(useRestrictionsTag, indentationLevelTwo) + "\n";
    
    /********************************************************************************/
    // Methods to open and close a tag (alias) in a specific level (indentationLevel)
    /********************************************************************************/
    
    // Without end of line
    public static String openTag(String alias, String indentationLevel) {
        return indentationLevel + openStartingXMLTag + alias + closeXMLTag;
    }    
    public static String closeTag(String alias, String indentationLevel) {
        return indentationLevel + openEndingXMLTag + alias + closeXMLTag;
    }
    
    // Methods to open and close a tag (alias) in a specific level (indentationLevel)
    // With end of line
    public static String openTagEndLine(String alias, String indentationLevel) {
        return openTag(alias, indentationLevel) + "\n";
    }    
    public static String closeTagEndLine(String alias, String indentationLevel) {
        return closeTag(alias, indentationLevel) + "\n";
    }
    public static String closeTagEndLine(String alias) {
        return closeTag(alias, "") + "\n";
    }
    
    // Method to create a tag (alias) in a specific level (indentationLevel) with a specific value (if so)
    public static String createTag(String alias, String value, String indentationLevel) {
        return indentationLevel + openStartingXMLTag + alias + closeXMLTag + 
                value + 
                openEndingXMLTag + alias + closeXMLTag + "\n";
    }
    
    // Method to create a tag (alias) in a specific level (indentationLevel) from a hashmap of values (if so)
    public static String createTag(String alias, RASupportMap values, String indentationLevel) {
                
        StringBuilder builder = new StringBuilder();
        builder.append(openTag(alias, indentationLevel));
        
        for(Object e: values.keySet()) {
            builder.append(e + RASupportCommon.querySeparator);            
        }
        
        String tag = builder.toString();
        return tag.substring(0, tag.length() - 1) + closeTag(alias, "") + "\n";
        
    }
}
