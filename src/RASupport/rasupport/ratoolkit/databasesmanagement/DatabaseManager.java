package RASupport.rasupport.ratoolkit.databasesmanagement;

import java.sql.ResultSet;

/**
 * RAToolkit: this interface must to be implemented by database managers (e.g., SQLite)
 * The classes that implement this interface are singletons
 * @author damianarellanes
 */
public interface DatabaseManager {
    
    // Name of the database: ratoolkit_[peerID].db
    public final String databasePrefix = "ratoolkit_"; 
   
    // Sets the name of the database
    public void setDatabaseName(long peerId);
    
    // Creates the database
    public void createDatabase();
    
    // Drops peers table and its use restrictions and attributes
    public void restartDatabase();
    
    // Closes the connection to the database
    public void closeConnection();
    
    // Creates a RS for a specific peer in the database
    //public void createRS(Path rsPath, String peerAlias);
    
    // Executes a sentence
    public void executeSentence(String sentence);
    
    // Executes a query
    public ResultSet executeQuery(String query);
    
    // Counts rows in table with a specified whereClause
    public int countRows(String table, String whereClause);
    
    /**********************************************************************/
    // Mapping of the database
    /**********************************************************************/
    
    // Peers table
    public void insertPeer(String alias);
    public int getPeerId(String alias);    
    
    // Attributes table    
    public void insertAttribute(int peerId, String name, String value);    
    public void updateAttribute(String attribute, String value, String peerAlias);
    
    // Use restrictions table
    public void insertUseRestriction(int peerId, String name, String value);
    
}
