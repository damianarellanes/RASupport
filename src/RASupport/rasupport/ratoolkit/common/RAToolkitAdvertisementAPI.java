package RASupport.rasupport.ratoolkit.common;

import java.io.File;
import myconet.MycoNode;

/**
 * RAToolkit: parent of advertisement APIs (facades)
 * @author Damian Arellanes
 */
public interface RAToolkitAdvertisementAPI extends RAToolkitAPI {
    
    public void advertiseRSTo(MycoNode superpeer);
    
    public void receiveRS(File rsFile, String aliasSender);
    
    public void advertiseUpdatingTo(String attribute, String newValue);
    
    public void receiveUpdating(String attribute, String newValue, 
            String aliasSender);

}
