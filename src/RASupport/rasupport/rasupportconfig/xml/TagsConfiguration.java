package RASupport.rasupport.rasupportconfig.xml;

import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;

/**
 * RASupportConfig: configuration to create XML tags in XML documents
 * @author damianarellanes
 */
public interface TagsConfiguration {
    
    // Strings for XML tags
    public final String openStartingXMLTag = "<";
    public final String openEndingXMLTag = "</";
    public final String closeXMLTag = ">";    
    
    // Indentation
    // Indentation for the following tags: use and categories
    public final String indentationLevelTwo = "\t";
    // Indentation for use restrictions or attributes
    public final String indentationLevelThree = "\t\t";
    
    // RS initial tags
    public final String rsInitialTag = "rs_initial";
    public final String openRSInitialTag = openTag(rsInitialTag, "") + "\n";            
    public final String closeRSInitialTag = closeTag(rsInitialTag, "");
    
    // Use restrictions tags
    public final String useRestrictionsTag = "use";
    public final String openUseRestrictionsTag = 
            openTag(useRestrictionsTag, indentationLevelTwo) + "\n";            
    public final String closeUseRestrictionsTag = 
            closeTag(useRestrictionsTag, indentationLevelTwo) + "\n";
    
    // Methods to open and close a tag (alias) in a specific level (indentationLevel)
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
            builder.append(e + ",");            
        }
        
        String tag = builder.toString();
        return tag.substring(0, tag.length() - 1) + closeTag(alias, "") + "\n";
        
    }
    
    /*// Method to create a tag (alias) in a specific level (indentationLevel) from a list of values (if so)
    public static String createTag(String alias, String[] values, String indentationLevel) {
        
        return "";
    }*/
}
