package RASupport.rasupport.ratoolkit.common;

import static RASupport.rasupport.ratoolkit.common.Common.AdvertisementAPIS.*;
import static RASupport.rasupport.ratoolkit.common.Common.BindingAPIS.*;
import static RASupport.rasupport.ratoolkit.common.Common.DatabaseManagers.*;
import static RASupport.rasupport.ratoolkit.common.Common.MatchingAPIS.*;
import static RASupport.rasupport.ratoolkit.common.Common.SelectionAPIS.*;

/**
 * RAToolkit: common variables in the whole toolkit
 * Default enums are the default modules to be used
 * @author damianarellanes
 */
public interface Common {
    
    // Database managers
    DatabaseManagers defaultDatabaseManager = SQLITE;
    enum DatabaseManagers { SQLITE }
    
    // Agents involved
    enum Agents { AGENT_ADVERTISEMENT_INITIAL,  AGENT_ADVERTISEMENT_UPDATING, AGENT_QUERY }
    
    // Advertisement APIS
    AdvertisementAPIS defaultAdvertisementAPI = ADVERTISEMENT_API_DEFAULT;
    enum AdvertisementAPIS { ADVERTISEMENT_API_DEFAULT }
    
    // Selection APIS
    SelectionAPIS defaultSelectionAPI = SELECTION_API_DEFAULT;
    enum SelectionAPIS { SELECTION_API_DEFAULT }
    
    // Matching APIS
    MatchingAPIS defaultMatchingAPI = MATCHING_API_DEFAULT;
    enum MatchingAPIS { MATCHING_API_DEFAULT }
    
    // Binding APIS
    BindingAPIS defaultBindingAPI = BINDING_API_DEFAULT;
    enum BindingAPIS { BINDING_API_DEFAULT }
    
}
