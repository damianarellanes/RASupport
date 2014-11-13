package RASupport.tests;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.RASupportQueryReader.INSIDE;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.xml.XMLQueryReader;
import RASupport.rasupport.rasupportconfig.xml.XMLQueryWriter;

/**
 *
 * @author damianarellanes
 */
public class HashBenchmark {
    
    public static long javaHash(String str) {
        return str.hashCode();
    }
    
    // This hash function is one of the most efficient hash functions ever published
    // Created by Professor Daniel J. Bernstein
    public static long djb2(String str) {
        
        long hash = 5381;
        
        for(int i = 0, size = str.length(); i < size; i++ ) {
            hash = ((hash << 5) + hash) + str.charAt(i); // hash * 33 + c
        }
        
        return (int) Math.abs(hash % Long.MAX_VALUE);
    }
    
    public static void testJavaHash(String str, int iterations) {
        
        for(int i = 0; i < iterations; i++) {
            javaHash(str);
        }
    }
    
    public static void testDjb2(String str, int iterations) {
        
        for(int i = 0; i < iterations; i++) {
            djb2(str);
        }
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int iterations = 900000000;
        String test = "Hello hash world!";
        String test2 = "Damian Arellanes";
        XMLQueryReader qw = new XMLQueryReader("query.xml", null);
        RASupportQuery query = qw.getQuery(); 
        //query.saveXMLQuery("OKK");
        
        /*System.out.println("Java hash: " + HashBenchmark.javaHash(test));        
        System.out.println("DJB2 hash: " + HashBenchmark.djb2(test));*/        
        
        //testJavaHash(query.toString(), iterations);
        testDjb2(query.toString(), iterations);
    }

}
