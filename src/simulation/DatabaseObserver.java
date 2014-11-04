package simulation;

import java.io.IOException;
import static java.lang.System.exit;
import myconet.MycoCast;
import myconet.MycoList;
import myconet.MycoNode;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.databasesmanagement.sqlite.SQLiteManager;

/**
 * Simulation: control to validate entries in the Peers table with the graph of myconet
 * @author Damian Arellanes
 */
public class DatabaseObserver implements Control {
    
    private static final String PAR_PROTO = "protocol";
    
    private final String name;
    private final int pid;
    
    public DatabaseObserver(String name) {
        this.name = name;
        this.pid = Configuration.getPid(name + "." + PAR_PROTO);    
    }
    
    private void validateSuperPeer(long superpeer, MycoList normalPeers) {
        
        DatabaseManager dbMan = new SQLiteManager("ratoolkit_" + superpeer + ".db");
        
        for(int i = 0; i < normalPeers.size(); i++) {
            
            if(normalPeers.get(i).isBiomass() && 
                    dbMan.countRows("Peers", "namePeer='" + normalPeers.get(i).getAlias() + "'") == 0) {
                
                dbMan.closeConnection();
                
                //MycoNode spx = normalPeers.get(i).getMycoCast().getAllHyphae().get(0);
                try {
                    System.in.read();
                    System.err.println("Superpeer " + superpeer + 
                        " does not has peer" + normalPeers.get(i) + " with sp = " + 
                            normalPeers.get(i).getHyphaData());
                    exit(0);
                } catch (IOException ex) {

                }
            }
        }
        
        dbMan.closeConnection();        
    }
    
    private void validateNormalPeer(long normalpeer) {
        DatabaseManager dbMan = new SQLiteManager("ratoolkit_" + normalpeer + ".db");
        
        if(dbMan.countRows("Peers", "") != 0) {
            
            dbMan.closeConnection();
            
            try {
                System.in.read();
                System.err.println("Normal peer " + normalpeer + 
                    " has entries in its Peers table");
                exit(0);
            } catch (IOException ex) {
                
            }
        }
            
        dbMan.closeConnection();
    }

    @Override
    public boolean execute() {
        
        MycoCast mycocast = (MycoCast) Network.get(0).getProtocol(pid);
        
        //trick();
        //testDatabase();        
        
        return false;
    }
    
    // Matches Peers tables from the database with the graph
    private void testDatabase() {
        
        MycoCast mycocast = (MycoCast) Network.get(0).getProtocol(pid);
        
        // Validates that Peers tables of each super-peer have their corresponding normal peers
        //System.err.println("TESTING DATABASES OF SUPER-PEERS...");
        MycoList superpeers = mycocast.getAllHyphae();     
        for(MycoNode superpeer: superpeers) {
            
            MycoList normalpeers = superpeer.getHyphaLink().getBiomass();
            validateSuperPeer(superpeer.getID(), normalpeers);           
        }
        System.out.println("DATABASE OF SUPER-PEERS OK!");
        
        /*// Validates that Peers tables of each normal peer are empty
        //System.err.println("TESTING DATABASES OF NORMAL PEERS...");
        MycoList normalpeers = mycocast.getAllBiomass();            
        for(MycoNode normalpeer: normalpeers) {
                        
            validateNormalPeer(normalpeer.getID());           
        }
        System.out.println("DATABASE OF NORMAL-PEERS OK!");    */
    }
    
    // Fills the database corresponding to the graph
    private void trick() {
        
        MycoCast mycocast = (MycoCast) Network.get(0).getProtocol(pid);
        
        // Fills with normal peers the table Peers from the database of each super-peer
        MycoList superpeers = mycocast.getAllHyphae();     
        for(MycoNode superpeer: superpeers) {
            
            DatabaseManager dbMan = new SQLiteManager("ratoolkit_" + superpeer.getID() + ".db");
            dbMan.restartDatabase();
            for(MycoNode normalpeer: superpeer.getHyphaLink().getBiomass()) {
                dbMan.insertPeer(normalpeer.getAlias());
            }  
            
            dbMan.closeConnection();
        }
        
        // Validates that Peers tables of each normal peer are empty
        //System.err.println("TESTING DATABASES OF NORMAL PEERS...");
        MycoList normalpeers = mycocast.getAllBiomass();            
        for(MycoNode normalpeer: normalpeers) {
                        
            normalpeer.getRASupport().cleanIndexTable();
        }
    }

}
