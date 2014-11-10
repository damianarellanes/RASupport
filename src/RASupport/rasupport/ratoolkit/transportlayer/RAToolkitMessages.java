package RASupport.rasupport.ratoolkit.transportlayer;

import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportActions;

/**
 * RAToolkit: messages that are sent between senders and receivers
 * @author Damian Arellanes
 */
public enum RAToolkitMessages implements RASupportActions {
    
    /**************************************************************************/
    // ADVERTISEMENT MESSAGES
    /**************************************************************************/
    // To create a RS in the receiver with a XML file received
    CREATE_RS,
    
    // To update any attribute (dynamic or static) with the received value
    UPDATE_ATTRIBUTE,
    
    
    /**************************************************************************/
    // SELECTION MESSAGES
    /**************************************************************************/
    // To test if the current peer has received previously a specific query agent transporting a specific query
    TEST_QUERY;
    
}
