/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package RASupport.rasupport.rasupportconfig.modules.transportlayer;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;

/**
 * RASupport; entity (object) that is sent over the network
 * @author damianarellanes
 */
public interface RASupportNetworkTraveler {
    
    public String getSender();
    
    public boolean sendTo(RASupportTopologyNode receiver);
    
}
