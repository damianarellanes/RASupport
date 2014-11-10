package RASupport.tests;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import RASupport.rasupport.rasupportconfig.queries.RASupportGroupRestrictions;
import RASupport.rasupport.rasupportconfig.queries.RASupportNodeRestrictions;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryGroup;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryOptions;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryRestrictionSet;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes.*;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes.*;
import RASupport.rasupport.rasupportconfig.xml.XMLQueryReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
 
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**     
 *
 * @author damianarellanes
 */
public class QueriesTest {
    
    private static String file1 = "RASupportSchemas/query.xml";
    private static String schemaFile = "RASupportSchemas/Query.xsd";
    
    public static void validate() throws XMLStreamException, FileNotFoundException, IOException {
        
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(schemaFile));
                        
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(file1));
            Validator validator = schema.newValidator();
            validator.validate(new StAXSource(xmlStreamReader));
                
            /*Validator validator = schema.newValidator();
            XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
            XMLEventReader reader = xmlFactory.createXMLEventReader(new FileInputStream(file1));
            validator.validate(new StAXSource(reader));*/
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws XMLStreamException, IOException {
        
        /*// Validaciones en las restricciones entre nodos para el grupo "Processing"
        RASupportQueryGroup processing = new RASupportQueryGroup("Processing", 8);         
        RASupportQueryRestrictionSet nodeRestrictions = new RASupportQueryRestrictionSet();
        nodeRestrictions.addEntities(Arrays.asList("Peer1", "Peer2")); // Regla 1                      
        processing.addNodeRestrictions(nodeRestrictions);*/
        
        /*// Validaciones para el grupo de "Processing" (reglas 2, 3 y 4)
        RASupportQueryGroup processing = new RASupportQueryGroup("Processing", 8);        
        processing.addNumericalAttribute(free_mem, 512.0, 1024.0, 4096.0, 8192.0, 0.05);
        processing.addNumericalAttribute(os_name, 1024.0, 2500.0, 4096.0, 4096.0, 0.2); // regla 2       
        processing.addStringAttribute(cpu_speed, "Regla 2", 0.0); // regla 3
        processing.addNumericalAttribute(free_mem, 1000.0, 2048.0, 4096.0, 8192.0, 1.2); // regla 4*/
        
        /*// Validaciones para el grupo "Processing" (regla 5)
        RASupportQueryGroup processing = new RASupportQueryGroup("Processing", 8);         
        RASupportQueryRestrictionSet nodeRestrictions = new RASupportQueryRestrictionSet();        
        processing.addNodeRestrictions(nodeRestrictions);*/
        
        /*// Validaciones para una consulta de recursos (reglas 6, 7 y 8)       
        RASupportQuery query = new RASupportQuery(); 
        query.addOption(RASupportQueryOptions.PERFORMANCE);
        query.addTTL(3);
        query.addOption(RASupportQueryOptions.FIND_RESOURCES); // Regla 6
        query.addTTL(10); // Regla 6        
        RASupportQueryGroup processing = new RASupportQueryGroup(); 
        query.addGroup(processing); // Regla 7
        RASupportQueryGroup group = new RASupportQueryGroup("Regla 8", 8); 
        RASupportQueryGroup duplicatedGroup = new RASupportQueryGroup("Regla 8", 10); 
        query.addGroup(group);          
        query.addGroup(duplicatedGroup); // Regla 8*/
        
        /*// Validaciones para una consulta de recursos (reglas 9, 10 y 11)       
        RASupportQuery query = new RASupportQuery(); 
        RASupportQueryRestrictionSet groupRestrictions = new RASupportQueryRestrictionSet();
        query.addGroupRestrictions(groupRestrictions); // Regla 9        
        query.saveXMLQuery(); // Regla 10
        File queryFile = query.getXMLQuery(); // Regla 11*/
        
        // Creamos la consulta
        RASupportQuery query = new RASupportQuery(RASupportQueryOptions.FIND_RESOURCES, 4); 
        
        // Creamos un grupo llamado "Processing" con 6 atributos
        RASupportQueryGroup processing = new RASupportQueryGroup("Processing", 8);        
        processing.addNumericalAttribute(cpu_speed, 1024.0, 2500.0, 4096.0, 4096.0, 0.2);        
        processing.addNumericalAttribute(free_mem, 512.0, 1024.0, 4096.0, 8192.0, 0.05);
        processing.addNumericalAttribute(busy_cpu, 0, 0, 30, 75, 0.05);
        processing.addNumericalAttribute(cores, 1, 2, 4, 4, 0.03);
        processing.addNumericalAttribute(free_hdisk, 50.0, 60.5, 92.5, 100.0, 0.05);
        processing.addStringAttribute(os_name, "Linux", 0.0);  
                
        // Creamos 2 restricciones entre nodos para el grupo "Processing" (opcional)
        RASupportQueryRestrictionSet nodeRestrictions = new RASupportQueryRestrictionSet();
        nodeRestrictions.addNumericalRestriction(RASupportNodeRestrictions.latency, 10.2, 22.1, 30.0, 50.0, 0.02);
        nodeRestrictions.addNumericalRestriction(RASupportNodeRestrictions.bandwidth, 10.2, 22.1, 30.0, 50.0, 0.02);
        processing.addNodeRestrictions(nodeRestrictions);
        
        // Creamos un grupo llamado "Display" (opcional) con 4 atributos 
        RASupportQueryGroup display = new RASupportQueryGroup("Display", 1);        
        display.addNumericalAttribute(screen_width, 800, 1024, 1366, 2560, 0.02);        
        display.addNumericalAttribute(screen_height, 600, 1024, 2048, 1700, 0.02);    
        display.addNumericalAttribute(bit_depth, 8, 16, 48, 48, 0.0);        
        display.addNumericalAttribute(refresh_rate, 40, 60, 120, 120, 0.05);        
        
        // Creamos 2 restricciones entre los grupos "Processing" y "Display" (opcional)
        RASupportQueryRestrictionSet groupRestrictions = new RASupportQueryRestrictionSet();
        groupRestrictions.addEntities(Arrays.asList(processing.getName(), display.getName()));
        groupRestrictions.addNumericalRestriction(RASupportNodeRestrictions.latency, 
                0.0, 0.0, 50.0, 100.0, 0.2);
        
        // Agregamos los grupos y restricciones entre grupos a la consulta
        query.addGroup(processing);
        query.addGroup(display);
        query.addGroupRestrictions(groupRestrictions);      
        
        // Guardamos la consulta (i.e., le notificamos al objeto que hemos concluido nuestra consulta)
        // Esta línea genera un documento XML, con la finalidad de no generarlo varias veces
        query.saveXMLQuery("queryTest");        
        
        // Esta línea imprime un mensaje en caso de haber obtenido una consulta válida
        File f = query.getXMLQuery();
                
        XMLQueryReader qw = new XMLQueryReader("query.xml", RASupportCommon.RASupportQueryReader.INSIDE);
        RASupportQuery query2 = qw.getQuery(); 
        query2.saveXMLQuery("readQuery");   
        
        if(query2.isConsistent()) {
            System.out.println(query2);
        }           
        
        //QueriesTest.validate();
    }

}
