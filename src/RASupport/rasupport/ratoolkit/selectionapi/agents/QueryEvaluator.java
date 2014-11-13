package RASupport.rasupport.ratoolkit.selectionapi.agents;

import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryGroup;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryRequirement;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * RAToolkit: evaluates a query in the super-peer visited
 * @author Damian Arellanes
 */
public class QueryEvaluator {
    
    // Performs selection locally
    public static void evaluateSPvisited(RASupportQuery query, DatabaseManager dbMan) {
        
        ArrayList<ArrayList> candidates = new ArrayList<>();
        
        String nameColumn = "nameAttribute";
        String valueColumn = "valueAttribute";
        String castNumerical = " AND CAST(valueAttribute AS DOUBLE) BETWEEN ";
        String castString = " AND valueAttribute=";
        String groupBy = "GROUP BY idPeer HAVING count(*)=";
        String selectPrefix = "SELECT idPeer, COUNT(*) as count FROM Attributes WHERE ";
        StringBuilder select;
                
        for(RASupportQueryGroup group: query.getGroups().values()) {                        
            
            try {
                int count = 0;
                int size = group.getAttributes().size();
                select = new StringBuilder(selectPrefix);
                for(RASupportQueryRequirement attribute: group.getAttributes().values()) {
                    
                    select.append("(" + nameColumn + "='" + attribute.getAttribute() + "'");
                    if(attribute.isNumerical()) {
                                            
                        select.append(castNumerical + attribute.getMin_val() + " AND " + attribute.getMax_val() + ")");
                    }
                    else {
                        select.append(castString + "'" + attribute.getValue() + "'" + ")");
                    }
                    select.append("\n");
                    
                    if(count != size -1) {
                        select.append(" OR ");
                    }
                    
                    count++;
                    
                }
                
                select.append(groupBy + count);                
                
                System.out.println("GROUP " + group.getName() +":");
                System.out.println(select.toString());
                
                ResultSet peers = dbMan.executeQuery(select.toString());
                
                ArrayList tmp = new ArrayList<>();
                while(peers.next()) {
                    tmp.add(peers.getString("idPeer"));
                }
                candidates.add(tmp);
            }
            catch (SQLException ex) {
                
            }
            
            System.out.println(candidates);
        }
    }
    
    private static void evaluateNormalPeer(RASupportQueryGroup group, ResultSet normalPeers, DatabaseManager dbMan) {
        
        double computedPenalty = 0.0; // Equation 1.9
        System.out.println("GROUP: " + group.getName());
        
        // Evaluates each attribute from the group for the specified normal peer (np)
        for(RASupportQueryRequirement attribute: group.getAttributes().values()) {

            String sentence = "SELECT * FROM Attributes WHERE nameAttribute='" + attribute.getAttribute() + "'";
            try {                    
                ResultSet dbAttributes = dbMan.executeQuery(sentence);
                while(dbAttributes.next()) {

                     int idPeer = dbAttributes.getInt("idPeer");                          
                     
                     while(normalPeers.next()) {
                
                        // Evaluates each normal peer for the current attribute
                        String np = normalPeers.getString("namePeer");
                        /*for(RASupportQueryGroup group: query.getGroups().values()) {

                        }*/
                    }

                     if(attribute.isNumerical()) {

                         double real_value = dbAttributes.getDouble("valueAttribute");;
                         computedPenalty += computePenalty(attribute.getMin_val().doubleValue(), attribute.getMin_ideal().doubleValue(), 
                                 attribute.getMax_ideal().doubleValue(), attribute.getMax_val().doubleValue(), 
                                 attribute.getPenalty() , real_value);
                     }
                     else {
                         computedPenalty += computePenalty(attribute.getValue(), dbAttributes.getString("valueAttribute"));                             
                     }                         

                }
            } catch (SQLException ex) {                    

            }
        }

        // The current peer don't satisfy the requirements fro the group iff penalty = inf
        if(computedPenalty == Double.POSITIVE_INFINITY) {
            System.out.println("Peer ");
        }
        else {

        }
        
    }    
    
    // Computes the penalty for numerical attributes (int or float)
    // Equations 1.6 and 1.7
    private static double computePenalty(double min_val, double min_ideal, double max_ideal, double max_val, double penalty, 
            double real_val) {
        
        double p = 0.0;
        double delta = 0.0;
        
        // min_val <= real_val < min_ideal
        if(real_val >= min_val && real_val < min_ideal) {
            delta = min_ideal - min_val;
        }
        // max_ideal < real_val < max_val
        else if(real_val > max_ideal && real_val <= max_val) {
            delta = max_val - max_ideal;
        }
        // min_ideal <= real_val <= max_ideal
        else if(real_val >= min_ideal && real_val <= max_ideal) {
            delta = 0;
        }
        else {
            delta = Double.POSITIVE_INFINITY;
        }
        
        return p * delta;
    }
    
    // Computes the penalty for string attributes
    // Equations 1.6 and 1.8
    private static double computePenalty(String val, String real_val) {
        
        return (val.equals(real_val)) ? 0 : Double.POSITIVE_INFINITY;
    }

}
