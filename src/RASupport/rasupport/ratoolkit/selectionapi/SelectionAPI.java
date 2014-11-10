package RASupport.rasupport.ratoolkit.selectionapi;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.xml.XMLQueryReader;
import RASupport.rasupport.ratoolkit.apismanagement.RAToolkitSelectionAPI;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.selectionapi.agents.SelectionManager;
import java.io.File;
import myconet.MycoNode;

/**
 * RAToolkit: facade of the selection API
 * @author Damian Arellanes
 */
public class SelectionAPI implements RAToolkitSelectionAPI {
    
    private DatabaseManager databaseManager = null;
    private RASupportNode node = null;
    private MycoNode myconetPeer = null;
    private String myconetAlias = "";
    
    private SelectionManager selectionManager = null;
    
    public SelectionAPI(RASupportNode node, DatabaseManager dbMan) {
        
        
        // Sets the node
        this.node = node;
        this.myconetPeer = (MycoNode) node.getTopologyNode();
        this.myconetAlias = myconetPeer.getAlias();
        
        // Sets the database manager
        this.databaseManager = dbMan;
        
        // Creates the manager of query agents
        this.selectionManager = new SelectionManager(myconetPeer, dbMan);
    }
    
    @Override
    public File createXMLQuery() {
        return null;
    }
    
    @Override
    public void selectResources(RASupportQuery query) {
        
        if(query.isConsistent()) {
            // Starts selection phase
            logMessage(myconetAlias + " has requested resources");
        }
    }

    @Override
    public void selectResources(File queryFile) {
        
        // Creates a query object taking into account the specified query file
        XMLQueryReader qw = new XMLQueryReader(queryFile.getAbsolutePath(), RASupportCommon.RASupportQueryReader.INSIDE);
        RASupportQuery query = qw.getQuery();
        
        if(query.isConsistent()) {
            // Starts selection phase
            logMessage(myconetAlias + " has requested resources");
        }
    }

    @Override
    public void testQuery(long idQuery, MycoNode sender) {
        
    }
    
}
