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
    // To notify a super-peer that it should be a query originator
    REQUEST_QUERY,
    
    // Message sent by query agents to visit a SP
    VISIT_SP,
    
    // To test if the current peer has received previously a specific query agent transporting a specific query
    TEST_QUERY,
    
    // Notifies that the sender has received the query
    RECEIVED_QUERY,
    
    // Notifies that the sender has not received the query
    NO_RECEIVED_QUERY,
    
    // This message is sent from query agents to their respective super-peer initiator when they finish their tasks
    QUERY_AGENT_FINISHED;
    
}
