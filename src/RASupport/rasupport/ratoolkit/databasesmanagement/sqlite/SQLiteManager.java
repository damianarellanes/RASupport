package RASupport.rasupport.ratoolkit.databasesmanagement.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import static RASupport.rasupport.rasupportconfig.log.LogManager.*;
import static RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseErrors.NO_PEERID;
import static RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseErrors.NO_QUERY_RESULTS;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import static RASupport.rasupport.ratoolkit.databasesmanagement.sqlite.SQLiteConfiguration.*;


/**
 * RAToolkit: SQLite database manager
 * @author Damian Arellanes
 */
public class SQLiteManager  implements DatabaseManager {
    
    // Alias of the peer owner
    private String aliasPeerOwner = "";
    
    // Name of the database (this names changes depending on the database)        
    private String dbName = "";
    // URL of the database
    private String dbURL = "jdbc:sqlite:";
    
    
    private Connection dbConnection;    
    
    public SQLiteManager(long idPeer) {
        
        // Only for testing
        // We have to set the real peer alias
        this.aliasPeerOwner = "peer" + idPeer;
        
        setDatabaseName(idPeer);
        
        createDatabase();        
    }
    
    // Constructor only for testing
    public SQLiteManager(String databaseName) {
        
        try {
            Class.forName(sqliteClass);
            
            dbURL += databaseName;
            dbConnection = DriverManager.getConnection(dbURL);
            
            dbConnection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        } 
        
        //System.out.println("Database " + databaseName + " loaded");
    }
    
    @Override
    public void createDatabase() {                
        
        logMessage("creating database " + dbName);
        try {
                        
            Class.forName(sqliteClass);                                                                                
            
            // This is to improve performance, but in the practice this do not have to exist
            // Always re-create tables beacuse they can change
            // By the moment, if support config change, we need to remove the databases manually
            File db = new File(dbName);            
            if(db.exists()) {
                
                // Creates the connection to the existent database
                dbConnection = DriverManager.getConnection(dbURL);
                
                // Improves insertions
                // This command will cause SQLite to not wait on data to reach the disk surface, which will make write operations appear to be much faster. 
                // But if you lose power in the middle of a transaction, your database file might go corrupt.
                executeSentence("PRAGMA synchronous=OFF");
                
                // Enables ON UPDATE CASCADE ON DELETE CASCADE
                executeSentence(enableOnDeleteCascade);
                
                // Delete tables that change (e.g., AttributesPeers and UseRestrictionsPeers)
                executeSentence("DELETE FROM Peers");       
                
                logMessage("database " + dbName +" restarted");
            }
            else {                                
                
                // Creates the database and its connection
                dbConnection = DriverManager.getConnection(dbURL);
                
                // Enables ON UPDATE CASCADE ON DELETE CASCADE
                executeSentence(enableOnDeleteCascade);
                
                // Creates tables in the database
                executeSentence(Peers);                
                executeSentence(Attributes);
                executeSentence(UseRestrictions);
                /*executeSentence(types);
                executeSentence(resources);
                executeSentence(attributes);
                executeSentence(peers);
                executeSentence(AttributesPeers);
                executeSentence(useRestrictions);
                executeSentence(useRestrictionsPeers);*/

                // Creates indexes in created tables
                createIndexes();

                // Populates tables that does not change
                /*populate(AttributeTypes.class, "Types", "(idType, nameType)");
                populate(RASupportResources.class, "Resources", "(idResource, nameResource)");
                populate(RASupportUseRestrictions.class, "UseRestrictions", 
                        "(idUseRestriction, nameUseRestriction)");
                int index = populateAttributes(RASupportDynamicAttributes.class, 0);
                populateAttributes(RASupportStaticAttributes.class, index);*/
                
                logMessage("database " + dbName +" created");
                                        
        }                         
        } catch ( ClassNotFoundException | SQLException e ) {
            //e.printStackTrace();
            logError("impossible to create the database in the peer");            
            System.exit(0);
        }        
    }
    
    private void createIndexes() {
        
        // Index on nameType from Types        
        //executeSentence("CREATE INDEX nameType_index ON Types (nameType)");
        
        // Index on nameResource from Resources
        //executeSentence("CREATE INDEX nameResources_index ON Resources (nameResource)");
        
        // Index on aliasPeer from Peers
        executeSentence("CREATE INDEX peers_index ON Peers (namePeer)");
        
        // Index on idPeer, nameAttribute, and valueAttribute from Attributes
        executeSentence("CREATE INDEX attributes_index ON Attributes (idPeer, nameAttribute, valueAttribute)");
        
        // Index on idPeer, nameUseRestriction, and valueUseRestriction from UseRestrictions
        executeSentence("CREATE INDEX useRestrictions_index ON UseRestrictions (idPeer, nameUseRestriction, valueUseRestriction)");
    }
    
    /*private <E extends Enum<E>> void populate(Class<E> list, String table, String params) {
        
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO " + table + params +" VALUES ");
        
        int index = 0;
        for(E element: list.getEnumConstants()) {            
            
            insert.append("(" + index + ",\"" + element.name() + "\"),");                        
            
            index++;
        }        
        
        executeSentence(insert.substring(0, insert.length()-1));
    }
    
    private <E extends Enum<E>> int populateAttributes(Class<E> list, int index) {
        
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO Attributes(idAttribute, nameAttribute, idType, idResource) VALUES ");
        
        for(E element: list.getEnumConstants()) {            
                       
            String idType = "(SELECT idType FROM Types WHERE nameType='"+ 
                    RASupportAttributes.getAttributeType(element) +"')";
            String idResource = "(SELECT idResource FROM Resources WHERE nameResource='"+ 
                    RASupportAttributes.getResourceModel(element) +"')";
            
            insert.append("(" + index + ",\"" + element.name() + "\"," + idType + "," + idResource +"),");
            
            index++;
        } 
        
        executeSentence(insert.substring(0, insert.length()-1));
        return index;
    }*/
    
    /**
     * @return the dbConnection
     */
    public Connection getDbConnection() {
        return dbConnection;
    } 

    @Override
    public void setDatabaseName(long peerId) {
        dbName = databasePrefix + peerId + ".db";
        dbURL += dbName;
    }

    @Override
    public void restartDatabase() {
        
        // We have the restriction On delete Cascade On update Cascade
        // We don't have to delete the own index
        executeSentence("DELETE FROM Peers WHERE namePeer != " + aliasPeerOwner);       
    }
    
    @Override
    public void closeConnection() {
        try {
            this.dbConnection.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
        
    @Override
    public void executeSentence(String sentence) {
        
        try {
            Statement stmt = dbConnection.createStatement();
            stmt.executeUpdate(sentence);
            stmt.close();
            //connection.close();
        } catch (SQLException ex) {            
            logError(ex);
        }
    }    
    
    @Override
    public ResultSet executeQuery(String query) {
        
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);            
            //stmt.close();
            
            return rs;
        } catch (SQLException ex) {
            System.err.println(ex);
            logError(ex);
        }
        
        return NO_QUERY_RESULTS;
    }
    
    @Override
    public int countRows(String table, String whereClause) {
        
        String where = "";
        
        if(!whereClause.isEmpty()) {
            where = " WHERE " + whereClause;
        }
        
        int count = 0;
        
        try {
            Statement stmt = dbConnection.createStatement();
            ResultSet r = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM " + table +
                    where);
            r.next();
            
            count = r.getInt("rowcount");
            
            r.close();
            stmt.close();
            
            return count;
        } catch (SQLException ex) {            
        }
        
        return count;
    }
    
    private void update(String updateStr) {
        try {
            
            Statement stmt = dbConnection.createStatement();
            stmt.executeUpdate(updateStr);
            dbConnection.commit();
            
        } catch (SQLException ex) {
            
        }
    }    

    /*****************************************************************/
    // Peers table
    /*****************************************************************/  
    
    @Override
    public void insertPeer(String alias) {
        
        // Checks if the peer is already in the table Peers       
        if(countRows("Peers", "namePeer='" + alias + "'") == 0) {                        
            executeSentence(insertPeer + "(\""+ alias +"\")");
        }
    }
    
    @Override
    public int getPeerId(String alias) {
        
        int peerId = NO_PEERID;
        try {
            String query = "SELECT idPeer FROM Peers WHERE namePeer=\'" + alias + "\'";
            ResultSet res = this.executeQuery(query);            
            
            if(res != NO_QUERY_RESULTS) {
                
                while ( res.next() ) {
                    peerId = res.getInt("idPeer"); 
                }
            }
            
            res.close();
        } catch (SQLException ex) {
            logError(ex);
        }
        return peerId;
    }
    
    
    /*****************************************************************/
    // Attributes table
    /*****************************************************************/  
    
    @Override
    public void insertAttribute(int peerId, String name, String value) {
        executeSentence(insertAttribute + "("+ peerId + ",'" + name + "', '" + value + "')");
    }
    
    @Override
    public void updateAttribute(String attribute, String value, String peerAlias) {
        
        int idPeer = this.getPeerId(peerAlias);
        String where = " WHERE nameAttribute='" + attribute + "' AND idPeer=" + idPeer;
        update(updateAttribute + value + where);
    }
    
    
    /*****************************************************************/
    // Use restrictions table
    /*****************************************************************/

    @Override
    public void insertUseRestriction(int peerId, String name, String value) {
        executeSentence(insertUseRestriction + "("+ peerId + ",'" + name + "', '" + value + "')");
    }    
    
    
}
