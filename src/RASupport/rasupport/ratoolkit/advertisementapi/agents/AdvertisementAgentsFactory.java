package RASupport.rasupport.ratoolkit.advertisementapi.agents;

import RASupport.rasupport.ratoolkit.advertisementapi.rs.RSpec;
import RASupport.rasupport.ratoolkit.common.AgentsFactory;
import RASupport.rasupport.ratoolkit.common.Common.Agents;
import static RASupport.rasupport.ratoolkit.common.Common.Agents.*;
import static RASupport.rasupport.ratoolkit.common.ErrorsManager.*;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;

/**
 *
 * @author damianarellanes
 */
public class AdvertisementAgentsFactory implements AgentsFactory {

    @Override
    public AdvertisementAgent create(Agents type, Object...params) {
        
        if(type == AGENT_ADVERTISEMENT_INITIAL) {
            return new AdvertisementAgentInitial((RSpec) params[0]);
        }
        else if(type == AGENT_ADVERTISEMENT_UPDATING) {
            return new AdvertisementAgentUpdating((RSpec) params[0], 
                    (String) params[1], (DatabaseManager) params[2]);
        }
        else{
            return AGENT_ADVERTISEMENT_UNKNOWN;
        }        
    }

}
