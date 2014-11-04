package RASupport.rasupport.rasupportconfig.resourcesmodel;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.concurrencyLevel;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.initialCapacity;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.loadFactor;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import static RASupport.rasupport.rasupportconfig.random.RASupportRandomManager.*;

/**
 * RASupportConfig: representation of an attributes map
 * It can be use for maps of dynamic and static resources
 * Maintains concurrence between threads 
 * @author damianarellanes
 * @param <A> Attribute supported in the RASupport
 * @param <V> Value of the attribute
 */
public class RASupportMap<A, V> extends ConcurrentHashMap<A,V> {
    
    //private A attribute;
    //private V value;

    public RASupportMap() {
        
        // Best practive to increase performance in concurrent hash maps
        // Initialize initial capacity, load factor, and concurrency level 
        super(initialCapacity, loadFactor, concurrencyLevel);
        
    }   
        
    public void printMap(String tag) {
        logMessage(tag + " " + this.toString());
    }
    
    public Entry getRandomEntry(int min, int max) {          
        return (Entry) this.entrySet().toArray()[getRandomInt(min, max)];
    }
    
    public Entry getEntry(Object keyToSearch) {
        
        for(Entry e: this.entrySet()) {
            if(e.getKey().equals(keyToSearch)) {
                return e;
            }
        }
        
        return null;
    }
}
