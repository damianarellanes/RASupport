package RASupport.tests;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author damianarellanes
 */
public class CloneBenchmark {
    
    private Agent agentRef = null;    
    private String owner = "";
    
    public CloneBenchmark(String owner) {
        this.agentRef = new Agent(owner);        
        this.owner = owner;
    }
    
    // Shallow copy
    public void sendAgentCloneable(int iterations) throws CloneNotSupportedException {
        
        Agent toSend;
        for(int i = 0; i < iterations; i++) {
                //Agent toSend = (Agent) agentRef.clone();
                //toSend.setTransport(i);
                agentRef.clone();            
            }
    }
    
    // Deep copy
    public void sendAgentCreating(int iterations) {
        
        for(int i = 0; i < iterations; i++) {
            Agent toSend = new Agent(owner, i);        
            //toSend.printTransport();
        }
        
    }
    
    // RMI*
    public void sendAgent(String a, String b, int iterations) {
        for(int i = 0; i < iterations; i++) {
            
            
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CloneNotSupportedException, Throwable {
        // TODO code application logic here
        
        {
            Agent a = new Agent("");
            //a=null;
        }
        for(int i = 0; i < 100000; i++);
        
        //a.killAgent(a);
        
        //a.finalize();
        
        /*for(int i = 0; i < 100000; i++) {
            System.gc();
        }
        
        Thread.sleep(1200000);*/
        
        System.gc();
        
        /*long time_start, time_end;
        int iterations = 2000000000;
        CloneBenchmark cb = new CloneBenchmark("Damian");
               
        time_start = System.currentTimeMillis();
        cb.sendAgent("AAAAA","AAA",iterations);
        time_end = System.currentTimeMillis();            
        System.out.println("RMI = " + (time_end-time_start));
        
        time_start = System.currentTimeMillis();
        cb.sendAgentCreating(iterations);
        time_end = System.currentTimeMillis();            
        System.out.println("Non-cloneable = " + (time_end-time_start));
        
        
        time_start = System.currentTimeMillis();
        cb.sendAgentCloneable(iterations);        
        time_end = System.currentTimeMillis();            
        System.out.println("Cloneable = " + (time_end-time_start));
        
        cb.finalize();*/
    }

}
