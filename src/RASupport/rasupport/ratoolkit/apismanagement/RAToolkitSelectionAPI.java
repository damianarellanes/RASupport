package RASupport.rasupport.ratoolkit.apismanagement;

import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import java.io.File;
import myconet.MycoNode;

/**
 * RAToolkit: interface that selection APIs must to implement
 * @author damianarellanes
 */
public interface RAToolkitSelectionAPI {
    
    public File createXMLQuery();
    
    public void selectResources(RASupportQuery query);
    
    public void selectResources(File queryFile);
    
    public void testQuery(long idQuery,  MycoNode sender);
    
}
