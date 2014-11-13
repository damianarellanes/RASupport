package RASupport.rasupport.ratoolkit.selectionapi.protocols;

import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;

/**
 * RAToolkit: interface that must be implemented by selection protocols
 * @author Damian Arellanes
 */
public interface SelectionProtocol {
    
    public void execute(RASupportQuery query);

}
