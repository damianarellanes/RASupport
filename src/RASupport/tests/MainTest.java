package RASupport.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import RASupport.myconet.MycoNode;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportUseRestrictions;
import RASupport.rasupport.rasupportmain.RASupportMain;
import RASupport.rasupport.ratoolkit.advertisementapi.AdvertisementAPI;
import static RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseErrors.NO_PEERID;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.databasesmanagement.sqlite.SQLiteManager;

/**
 *
 * @author damianarellanes
 */
public class MainTest {

    public static void insertIndex(String peerAlias, String rsPath, DatabaseManager dbMan) {

        int peerId = dbMan.getPeerId(peerAlias);
        if (peerId == NO_PEERID) {
            logError("error inserting a RS for " + peerAlias);
            exit(0);
        }

        String insertAttribute = "INSERT INTO Attributes(idPeer, nameAttribute, valueAttribute) "
                + "VALUES";
        String insertUR = "INSERT INTO UseRestrictions(idPeer, nameUseRestriction, valueUseRestriction) "
                + "VALUES";

        XMLInputFactory factory = XMLInputFactory.newInstance();
        boolean useRestriction = false, attribute = false;
        String foundTag = "";

        try {
            InputStream inStream = new FileInputStream(rsPath);

            XMLStreamReader reader = factory.createXMLStreamReader(inStream);

            int event;
            while (reader.hasNext()) {
                event = reader.next();

                switch (event) {

                    case XMLStreamConstants.START_ELEMENT:
                        try {
                            String c = reader.getLocalName();
                            if (RASupportDynamicAttributes.valueOf(reader.getLocalName()) != null) {
                                //System.out.println("Found dynamic = " + reader.getLocalName());
                                attribute = true;
                                foundTag = reader.getLocalName();
                            }

                        } catch (IllegalArgumentException ex) {

                            try {
                                if (RASupportStaticAttributes.valueOf(reader.getLocalName()) != null) {
                                    //System.out.println("Found static = " + reader.getLocalName());
                                    attribute = true;
                                    foundTag = reader.getLocalName();
                                }
                            } catch (IllegalArgumentException ex2) {

                                try {

                                    if (RASupportUseRestrictions.valueOf(reader.getLocalName()) != null) {
                                        //System.out.println("Found use = " + reader.getLocalName());
                                        useRestriction = true;
                                        foundTag = reader.getLocalName();
                                    }

                                } catch (IllegalArgumentException ex3) {
                                }
                            }
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        if (attribute) {

                            //System.out.println("Value = " + reader.getText().trim());
                            String insert = insertAttribute + "(" + peerId + ",'" + foundTag + "','"
                                    + reader.getText().trim() + "')";

                            //dbMan.executeSentenceImpr(peerId, foundTag, reader.getText().trim());
                            dbMan.executeSentence(insert);
                        } else if (useRestriction) {

                            //System.out.println("Value = " + reader.getText().trim());
                            String insert = insertUR + "(" + peerId + ",'" + foundTag + "','"
                                    + reader.getText().trim() + "')";

                            dbMan.executeSentence(insert);
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        useRestriction = attribute = false;
                        break;

                    /*case XMLStreamConstants.START_DOCUMENT:                    
                     break;*/
                }
            }

            inStream.close();
           

        } catch (FileNotFoundException | XMLStreamException ex) {
            logError(ex);
            exit(0);
        } catch (IOException ex) {
            logError(ex);
            exit(0);
        }
    }

    public static <E> E isAttribute(String alias, Class<E> list) {
        /*for(E a : list.getEnumConstants()) {
         if(RASupportAttributes.getAttributeAlias(a).equals(alias)){
         return a;
         }
         }*/

        return null;
    }

    public static RASupportUseRestrictions isUseRestriction(String alias) {
        for (RASupportUseRestrictions ur : RASupportUseRestrictions.values()) {
            if (ur.getUseRestrictionName().equals(alias)) {
                return ur;
            }
        } 

        return null;
    }    

    // TODO merge all the modules of RASupportMain
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        //RASupport support = new RASupportMain(new MycoNode(""));        

        //&new SQLiteManager(10);
        /*for(int i = 0; i< 1000000; i++) {
            DatabaseManager dbMan = new SQLiteManager("ratoolkit_0.db");

            dbMan.insertPeer("peer"+i);
            MainTest.insertIndex("peer"+i, "test.xml", dbMan);
            dbMan.closeConnection();
        }*/
        
        
                
    }

}
