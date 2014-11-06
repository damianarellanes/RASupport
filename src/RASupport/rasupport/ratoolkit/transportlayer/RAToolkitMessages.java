package RASupport.rasupport.ratoolkit.transportlayer;

import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportActions;

/**
 * RAToolkit: messages that are sent between senders and receivers
 * @author Damian Arellanes
 */
public enum RAToolkitMessages implements RASupportActions {
    
    // To create a RS in the receiver with a XML file received
// To create a RS in the receiver with a XML file received
    CREATE_RS,
    
    // To update any attribute (dynamic or static) with the received value
    UPDATE_ATTRIBUTE;
    
}
