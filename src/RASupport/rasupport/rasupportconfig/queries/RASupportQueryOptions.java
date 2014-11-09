package RASupport.rasupport.rasupportconfig.queries;

import RASupport.rasupport.rasupportconfig.xml.TagsConfiguration;

/**
 * RASupport: options supported in XML queries
 * 
 * FIND_RESOURCES  = flooding protocol
 * PERFORMANCE = iRandomWalk protocol
 * 
 * @author Damian Arellanes
 */
public enum RASupportQueryOptions {
    
    FIND_RESOURCES("requires:find_resources"),    
    PERFORMANCE("requires:performance");
    
    String contentTag;
    
    RASupportQueryOptions(String contentTag) {
        this.contentTag = contentTag;
    }
    
    public String getName() {
        return contentTag;
    }
    
    public String getTag() {
        return TagsConfiguration.openQueryOptionTag + 
                contentTag + 
                TagsConfiguration.closeQueryOptionTag;
    }
}
