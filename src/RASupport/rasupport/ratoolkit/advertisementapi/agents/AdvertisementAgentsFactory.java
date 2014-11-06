package RASupport.rasupport.ratoolkit.advertisementapi.agents;

import RASupport.rasupport.ratoolkit.common.Agent;
import RASupport.rasupport.ratoolkit.common.AgentsFactory;
import RASupport.rasupport.ratoolkit.common.Common.Agents;
import static RASupport.rasupport.ratoolkit.common.Common.Agents.*;
import static RASupport.rasupport.ratoolkit.common.ErrorsManager.UNKNOWN_AGENT;

/**
 *
 * @author damianarellanes
 */
public class AdvertisementAgentsFactory implements AgentsFactory {

    @Override
    public Agent create(Agents type, Object...params) {
        
        if(type == AGENT_ADVERTISEMENT_INITIAL) {            
            return new AdvertisementAgentInitial((String) params[0],            
                (String) params[1]);
        }
        else if(type == AGENT_ADVERTISEMENT_UPDATING) {
            return new AdvertisementAgentUpdating((String) params[0], 
                    (String) params[1], (String) params[2]);
        }
        else{
            return UNKNOWN_AGENT;
        }
        
    }

}
