package RASupport.rasupport.ratoolkit.databasesmanagement;

import java.sql.ResultSet;

/**
 * Interface that represents the errors tha occurs in the database
 * @author Damian Arellanes
 */
public interface DatabaseErrors {
    
    // No results in an executed query
    public final ResultSet NO_QUERY_RESULTS = null;
    
    // No peer id
    public final int NO_PEERID = -1;

}
