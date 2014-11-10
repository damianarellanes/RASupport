package RASupport.rasupport.ratoolkit.apismanagement;

import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import RASupport.rasupport.ratoolkit.advertisementapi.*;
import RASupport.rasupport.ratoolkit.common.*;
import static RASupport.rasupport.ratoolkit.common.Common.*;
import RASupport.rasupport.ratoolkit.common.Common.AdvertisementAPIS;
import static RASupport.rasupport.ratoolkit.common.Common.AdvertisementAPIS.*;
import RASupport.rasupport.ratoolkit.common.Common.SelectionAPIS;
import static RASupport.rasupport.ratoolkit.common.Common.SelectionAPIS.*;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.selectionapi.SelectionAPI;

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
    
    public static RAToolkitSelectionAPI createSelectionAPI(SelectionAPIS api, RASupportNode peer, DatabaseManager dbMan) {
        
        if(api.equals(SELECTION_API_DEFAULT)) {
            return new SelectionAPI(peer, dbMan);
        }
        
        return createSelectionAPI(defaultSelectionAPI, peer, dbMan); // returns default selection API
    }
}
