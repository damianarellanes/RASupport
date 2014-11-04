package RASupport.rasupport.rasupportconfig.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * RAsupport: interface to manage xml documents in RASupport
 * @author Damian Arellanes
 */
public interface XMLManager {
    
    public static File saveXML(String xmlBody, String xmlName) {
        
        // Variables to save the XML document in a temporal file
        File xmlFile = null;        
        BufferedWriter writer = null;
        
        // Saves the XML document in a temporal file: /tmp
        try {            
            
            xmlFile = File.createTempFile(xmlName, ".xml");
            writer = new BufferedWriter(new FileWriter(xmlFile));
            writer.write(xmlBody);
            
            // To delete the xml when the program terminates
            xmlFile.deleteOnExit();
            
        } catch (UnsupportedEncodingException e) { 
            System.err.println("Error creating a initial RS: " + e.getMessage());
            e.printStackTrace(System.out);
        } catch (IOException e) {               
            System.err.println("Error creating a initial RS: " + e.getMessage());
            e.printStackTrace(System.out);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                
            }
        }
        
        return xmlFile;
    }

}
