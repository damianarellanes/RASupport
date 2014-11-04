package RASupport.rasupport.ratoolkit.common;

import java.io.File;
import myconet.MycoNode;

/**
 * RAToolkit: parent of advertisement APIs (facades)
 * @author Damian Arellanes
 */
public abstract class RAToolkitAdvertisementAPI implements RAToolkitAPI {
    
    public abstract void advertiseRSTo(MycoNode superpeer);
    
    public abstract void createRSInDatabase(File rsFile, String aliasSender);
    
    public abstract void SendAttributeUpdating(String attribute, String newValue);
    
    public abstract void UpdateAttributeInDatabase(String attribute, String newValue, 
            String aliasSender);

}
