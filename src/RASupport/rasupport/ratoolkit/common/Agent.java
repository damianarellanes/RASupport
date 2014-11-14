package RASupport.rasupport.ratoolkit.common;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportNetworkTraveler;

/**
 * RAToolkit: representation of an agent in the toolkit
 *
 * @author damianarellanes
 */
public interface Agent extends RASupportNetworkTraveler {

    @Override
    public String getSender();

    @Override
    public boolean sendTo(RASupportTopologyNode receiver);
}
