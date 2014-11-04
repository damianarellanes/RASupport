package RASupport.rasupport.ratoolkit.apismanagement;

import RASupport.rasupport.ratoolkit.advertisementapi.AdvertisementAPI;
import RASupport.rasupport.ratoolkit.common.Common.AdvertisementAPIS;
import static RASupport.rasupport.ratoolkit.common.Common.AdvertisementAPIS.*;
import static RASupport.rasupport.ratoolkit.common.Common.*;
import RASupport.rasupport.ratoolkit.common.RAToolkitAdvertisementAPI;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.rasupportconfig.common.RASupportNode;

/**
 * RAToolkit: creates APIs modularly
 * Always returns a valid API, in last instance returns the default API
 * @author Damian Arellanes
 */
public class APIFactory {

    public static RAToolkitAdvertisementAPI createAdvertisementAPI(AdvertisementAPIS api, RASupportNode peer, DatabaseManager dbMan) {
        
        if(api.equals(ADVERTISEMENT_API_DEFAULT)) {
            return new AdvertisementAPI(peer, dbMan);
        }
        
        return createAdvertisementAPI(defaultAdvertisementAPI, peer, dbMan); // returns default advertisement API
    }
}
