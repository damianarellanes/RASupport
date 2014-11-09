package RASupport.rasupport.ratoolkit.common;

import java.io.File;

/**
 * RAToolkit: interface that selection APIs must to implement
 * @author damianarellanes
 */
public interface RAToolkitSelectionAPI {
    
    public File createXMLQuery();
    
    public void selectResources();
    
}
