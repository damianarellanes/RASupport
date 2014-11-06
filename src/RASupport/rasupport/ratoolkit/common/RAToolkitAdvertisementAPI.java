package RASupport.rasupport.ratoolkit.common;

import RASupport.rasupport.ratoolkit.advertisementapi.agents.AdvertisementAgentInitial;
import RASupport.rasupport.ratoolkit.advertisementapi.agents.AdvertisementAgentUpdating;
import myconet.MycoNode;

/**
 * RAToolkit: parent of advertisement APIs (facades)
 * @author Damian Arellanes
 */
public interface RAToolkitAdvertisementAPI extends RAToolkitAPI {
    
    public void advertiseRSTo(MycoNode superpeer);
    
    //public void receiveRS(File rsFile, String aliasSender);
    public void receiveInitialAgent(AdvertisementAgentInitial agent);
    
    public void advertiseUpdating(String attribute, String newValue);
    
    //public void receiveUpdating(String attribute, String newValue, String aliasSender);
    public void receiveUpdatingAgent(AdvertisementAgentUpdating agent);

}
