package RASupport.rasupport.ratoolkit.databasesmanagement;

import RASupport.rasupport.ratoolkit.common.Common.DatabaseManagers;
import static RASupport.rasupport.ratoolkit.common.Common.DatabaseManagers.*;
import static RASupport.rasupport.ratoolkit.common.Common.defaultDatabaseManager;
import RASupport.rasupport.ratoolkit.databasesmanagement.sqlite.SQLiteManager;

/**
 * RAToolkit: factory of database managers
 * @author Damian Arellanes
 */
public class DatabaseManagerFactory {
    
    public static DatabaseManager buildDatabaseManager(DatabaseManagers selected, long idPeer) {
        
        if(selected.equals(SQLITE)) {
            return new SQLiteManager(idPeer);
        }
        
        return buildDatabaseManager(defaultDatabaseManager, idPeer); // returns the default database manager
    }

}
