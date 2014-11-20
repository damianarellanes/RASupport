package RASupport.tests;

import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryGroup;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes.free_mem;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes.cpu_speed;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportUseRestrictions;
import RASupport.rasupport.rasupportmain.RASupportMain;
import RASupport.rasupport.ratoolkit.advertisementapi.AdvertisementAPI;
import static RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseErrors.NO_PEERID;
import RASupport.rasupport.ratoolkit.databasesmanagement.DatabaseManager;
import RASupport.rasupport.ratoolkit.databasesmanagement.sqlite.SQLiteManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import myconet.MycoNode;

/**
 *
 * @author damianarellanes
 */
public class MainTest implements 
        Runnable {
    
    public Thread t;
    private boolean stop;
    private String name = "";

    public MainTest(String n) {
        this.name = n;        
        stop = false;
    }

    public void start ()
    {
       System.out.println("Starting " +  name );
       if (t == null)
       {
          t = new Thread (this, name);
          t.start ();
       }
    }

    public void stopThread() {
        this.stop = true;       
        t = null;
    }

    @Override
    public void run() {
        while(!stop) {
            
        }
        System.out.println("Thread " + name + " stopped!");
    }

    @Override
    public void finalize() {
        System.out.println("Thread " + name + " KILLED!");
    }

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
    public static void main(String[] args) throws FileNotFoundException, InterruptedException, IOException {
        //RASupport support = new RASupportMain(new MycoNode(""));        

        //&new SQLiteManager(10);
        /*for(int i = 0; i< 1000000; i++) {
            DatabaseManager dbMan = new SQLiteManager("ratoolkit_0.db");

            dbMan.insertPeer("peer"+i);
            MainTest.insertIndex("peer"+i, "test.xml", dbMan);
            dbMan.closeConnection();
        }*/
        
        /*MainTest t1 = new MainTest("1");
        t1.start();
        MainTest t2 = new MainTest("2");
        MainTest t3 = new MainTest("3");
        
        //Thread.sleep(1000);
        
        t1.stopThread();
        t1 = null;
        Thread.sleep(60000);*/
        
        /*t2.stopThread();
        t3.stopThread();
        */
        //Runtime.runFinalizersOnExit(true);
        //System.gc();

        //String path = "/home/damianarellanes/Documentos/CINVESTAV/Tesis/Soporte\\ P2P\\ para\\ la\\ colaboración\\ de\\ recursos/Tesis/Disertación/Figures/Resultados/Anunciamiento";
        /*String path = "/home/damianarellanes/Documentos/CINVESTAV/Tesis/Soporte P2P para la colaboración de recursos/Tesis/Disertación/Figures/Resultados/Anunciamiento/cycles.gnu";
        File file = new File(path);
        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        
        String result = "0 \t\t 0 \n 1 \t\t 1";
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.append("\nOK");
        bw.close();
        fw.close();*/
        
        /*List<String> listA = new ArrayList<String>();
        listA.add("A");

        List<String> listB = new ArrayList<String>();
        listB.add("B");
        listB.add("A");

        List<String> listFinal = new ArrayList<String>();
        listFinal.addAll(listA);
        listFinal.addAll(listB);

        System.out.println("listA : " + listA);
        System.out.println("listB : " + listB);
        System.out.println("listFinal : " + listFinal);*/
        
        /*RASupportMap<String, String> m1 = new RASupportMap<>();
        m1.put("A", "A");
        RASupportMap<String, String> m2 = new RASupportMap<>();
        m2.put("B", "B");
        m2.put("A", "ABC");
        RASupportMap<String, String> m3 = new RASupportMap<>();
        m3.putAll(m1);
        m3.putAll(m2);
        
        System.out.println("listA : " + m1);
        System.out.println("listB : " + m2);
        System.out.println("listFinal : " + m3);*/
        
        String s1 = "Str";
        String s2 = "Str";
        System.out.println(s1.hashCode() == s2.hashCode());
        
        RASupportMap<RASupportQueryGroup, RASupportQueryGroup> map = new RASupportMap<>();
        
        RASupportQueryGroup g1 = new RASupportQueryGroup("Similar group", 8);
        g1.addNumericalAttribute(cpu_speed,500.0,2500.0,4096.0,5000.0,0.2);
        g1.addNumericalAttribute(free_mem, 10.0,1024.0,4096.0,16000.0,0.05);        
        map.put(g1, g1);
        
        RASupportQueryGroup g2 = new RASupportQueryGroup("Similar group", 8);
        g2.addNumericalAttribute(cpu_speed,500.0,2500.0,4096.0,5000.0,0.2);
        g2.addNumericalAttribute(free_mem, 10.0,1024.0,4096.0,16000.0,0.05);        
        //map.put(g2, g2);
                
        //System.err.println(g1 == g2);
        //System.err.println(g1.equals(g2));
        System.err.println(map.containsKey(g2));
        
        System.out.println(g1.hashCode());
        System.out.println(g2.hashCode());
        System.out.println(g1.hashCode() == g2.hashCode());
    }

}
