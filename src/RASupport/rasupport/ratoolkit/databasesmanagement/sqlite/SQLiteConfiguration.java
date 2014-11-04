package RASupport.rasupport.ratoolkit.databasesmanagement.sqlite;

import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportUseRestrictions;

/**
 * RAToolkit: configuration of the database created by RAToolkit
 * @author damianarellanes
 * @param <E> can be any enum (e.g., AttributeTypes)
 */
public interface SQLiteConfiguration <E extends Enum<E>> {
    
    // Name of the sqliteClass
    public final String sqliteClass = "org.sqlite.JDBC";        
    
    // Enables ON DELETE CASCADE
    public final String enableOnDeleteCascade = "PRAGMA foreign_keys = ON";
    
    // Max chars allowed in types (STRING_ATTRIBUTE has 16 chars)
    //public final int maxCharsTypeAlias = findMaxAttribute(AttributeTypes.class); 
    
    // Max chars allowed in the resources (e.g., STRING_ATTRIBUTE is the current max because it has 16 chars)
    //public final int maxCharsResourceAlias = findMaxAttribute(RASupportResources.class); 
    
    // Max chars allowed in attributes
    public final int maxCharsDynamicName = 
            findMaxAttribute(RASupportDynamicAttributes.class); 
    public final int maxCharsStaticName = 
            findMaxAttribute(RASupportStaticAttributes.class); 
    public final int maxCharsAttributeName = (maxCharsDynamicName >= maxCharsStaticName) ? maxCharsDynamicName: maxCharsStaticName; 
    
    // Max chars allowed in use restrictions
    public final int maxCharsUseRestrictions = findMaxUseRestriction(); 
        
    // Max chars allowed in peer alias  
    public final int maxCharsPeerAlias = 100; 
    
    // Determinates max chars in dynamic and static attributes    
    public static <E> int findMaxAttribute(Class<E> list) {
                
        int max = 0;
        for(E element: list.getEnumConstants()) {            
            int currentLenght = RASupportAttributes.getAttributeName(element).length();   
            if(currentLenght > max) {
                max = currentLenght;
            }
        }
        return max;
    }
    
    // Determinates max chars from the enum "list"    
    public static int findMaxUseRestriction() {
                
        int max = 0;
        for(RASupportUseRestrictions ur: RASupportUseRestrictions.values()) {            
            int currentLenght = ur.name().length();
            if(currentLenght > max) {
                max = currentLenght;
            }
        }
        return max;
    }
    
    
    /*****************************************************************/
    // Strings to create tables
    /*****************************************************************/        
    
    // Peers table
    public final String Peers = "CREATE TABLE Peers " +
        "(idPeer INTEGER PRIMARY KEY AUTOINCREMENT," +
        "namePeer CHAR(" + maxCharsPeerAlias + ") NOT NULL)";
    
    // Attributes table
    public final String Attributes = "CREATE TABLE Attributes " +
        "(idAttribute INTEGER PRIMARY KEY AUTOINCREMENT," +
        "idPeer INTEGER NOT NULL," +
        "nameAttribute CHAR(" + maxCharsAttributeName + ") NOT NULL, " + 
        "valueAttribute CHAR(200) NOT NULL," +
        "FOREIGN KEY(idPeer) REFERENCES Peers(idPeer) " +
        "ON UPDATE CASCADE ON DELETE CASCADE)"; 
    
    // Use restrictions table
    public final String UseRestrictions = "CREATE TABLE UseRestrictions " +
        "(idUseRestriction INTEGER PRIMARY KEY AUTOINCREMENT," +
        "idPeer INTEGER NOT NULL," +
        "nameUseRestriction CHAR(" + maxCharsUseRestrictions + ") NOT NULL, " + 
        "valueUseRestriction CHAR(200) NOT NULL," +
        "FOREIGN KEY(idPeer) REFERENCES Peers(idPeer) " +
        "ON UPDATE CASCADE ON DELETE CASCADE)"; 
    
    /*
    // Types table
    public final String types = "CREATE TABLE Types " +
                     "(idType SMALLINT PRIMARY KEY NOT NULL," +
                     " nameType CHAR(" + maxCharsTypeAlias + ") NOT NULL)"; 
    
    // Resources table
    public final String resources = "CREATE TABLE Resources " +
                     "(idResource SMALLINT PRIMARY KEY NOT NULL," +
                     " nameResource CHAR(" + maxCharsResourceAlias + ") NOT NULL)"; 
    
    // Attributes table
    public final String attributes = "CREATE TABLE Attributes " +
                     "(idAttribute INTEGER PRIMARY KEY NOT NULL," +
                     " nameAttribute CHAR(" + maxCharsAttributeName + ") NOT NULL, " + 
                     " idType SMALLINT NOT NULL, " + 
                     " idResource SMALLINT NOT NULL," + 
                     " FOREIGN KEY(idType) REFERENCES Types(idType), " + 
                     " FOREIGN KEY(idResource) REFERENCES Resources(idResource))"; 
    
    // UseRestrictions table
    public final String useRestrictions = "CREATE TABLE UseRestrictions " +
                     "(idUseRestriction INTEGER PRIMARY KEY NOT NULL," +
                     " nameUseRestriction CHAR(" + maxCharsUseRestrictions + ") NOT NULL)"; 
    
    // Peers table
    public final String peers = "CREATE TABLE Peers " +
            "(idPeer INTEGER PRIMARY KEY AUTOINCREMENT," +
            " aliasPeer CHAR(100) NOT NULL)";
    
    // AttributesPeers table
    public final String AttributesPeers = "CREATE TABLE AttributesPeers " +
                     "(idPeer INTEGER NOT NULL," +
                     " idAttribute INTEGER NOT NULL, " +                        
                     " valueAttributesPeers CHAR(100) NOT NULL, " +
                     " PRIMARY KEY(idPeer, idAttribute), " +
                     " FOREIGN KEY(idPeer) REFERENCES Peers(idPeer)" + 
                     " FOREIGN KEY(idAttribute) REFERENCES Attributes(idAttribute))"; 
    
    // UseRestrictions table
    public final String useRestrictionsPeers = "CREATE TABLE UseRestrictionsPeers " +
            "(idPeer INTEGER NOT NULL," +
            " idUseRestriction INTEGER NOT NULL," +
            " valueUseRestrictionsPeers CHAR(100) NOT NULL," +
            " PRIMARY KEY(idPeer, idUseRestriction), " +
            " FOREIGN KEY(idPeer) REFERENCES Peers(idPeer)" + 
            " FOREIGN KEY(idUseRestriction) REFERENCES UseRestrictions(idUseRestriction))"; 
    */
    
    /*****************************************************************/
    // Strings to insert into tables
    /*****************************************************************/  
    
    public final String insertPeer = "INSERT INTO Peers(namePeer) VALUES";
    
    public final String insertAttribute = "INSERT INTO Attributes"
                + "(idPeer, nameAttribute, valueAttribute) VALUES";
    
    public final String insertUseRestriction = "INSERT INTO UseRestrictions"
                + "(idPeer, nameUseRestriction, valueUseRestriction) VALUES";
    
    
    /*****************************************************************/
    // Strings to perform updatings in tables
    /*****************************************************************/  
    
    public final String updateAttribute = "UPDATE Attributes set valueAttribute=";
    
}
