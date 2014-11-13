package RASupport.rasupport.rasupportconfig.xml;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon.RASupportQueryReader;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logWarning;
import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.queries.RASupportGroupRestrictions;
import RASupport.rasupport.rasupportconfig.queries.RASupportNodeRestrictions;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryGroup;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryOptions;
import static RASupport.rasupport.rasupportconfig.queries.RASupportQueryOptions.FIND_RESOURCES;
import static RASupport.rasupport.rasupportconfig.queries.RASupportQueryOptions.PERFORMANCE;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryRestrictionSet;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryRestrictions;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributesInterface;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportUseRestrictions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * SelectionAPI: reads and validates an existing XML query
 * @author Damian Arellanes
 */
public class XMLQueryReader {
    
    private String xmlName = "";
    private XMLInputFactory factory;    
    private InputStream inStream;
    private XMLStreamReader reader;
    private RASupportQuery query = null;
    
    private boolean queryFlag = false;
    private boolean option = false;
    private boolean ttl = false;
    private boolean attributeFlag = false;
    private boolean groupFlag = false;
    private boolean groupNameFlag = false;
    private boolean groupNodesFlag = false;
    private boolean restrictionNodesFlag = false;   
    private boolean nodeRestrictionFlag = false;       
    private boolean restrictionGroupsFlag = false;
    private boolean groupEntitiesFlag = false;   
    private boolean groupRestrictionFlag = false;   
    
    RASupportAttributesInterface attribute = null;
    RASupportQueryGroup group = null;
    RASupportQueryRestrictionSet restrictionNodes = null;
    RASupportNodeRestrictions nodeRestriction = null;
    RASupportQueryRestrictionSet restrictionGroups = null;
    RASupportGroupRestrictions groupRestriction = null;
    
    public XMLQueryReader(String xmlPath, RASupportTopologyNode requestor) {
        
        File document = new File(xmlPath);
        String xmString = document.getName();
        
        
        // If the file is outside, we create a copy in te queries directory
        new File(RASupportCommon.queriesDirectory + "/" + xmlName);
        
        try {
            
            // Tries to isValidDocument the XML file
            if(!isValidDocument(document)) {
               return; 
            }
            
            factory = XMLInputFactory.newInstance();
            inStream = new FileInputStream(document);
            reader = factory.createXMLStreamReader(inStream);
            query = new RASupportQuery(requestor);
            
            readXMLQuery();
        } catch (FileNotFoundException | XMLStreamException e) {
            logError("impossible to read the XML query " + xmlName ,this.getClass());
            System.out.println(e);
        } catch (IOException ex) {
            logError("impossible to read the XML query " + xmlName ,this.getClass());
            System.out.println(ex);
        }
    }
    
    public boolean isValidDocument(File document) throws XMLStreamException, FileNotFoundException, IOException {
        
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(RASupportCommon.schemaFile);
                        
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(document));
            Validator validator = schema.newValidator();
            validator.validate(new StAXSource(xmlStreamReader));
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public RASupportQuery getQuery() {
        return query;
    }
    
    private void readXMLQuery() throws XMLStreamException, IOException {
        
        int event;        
        while (reader.hasNext()) {
            event = reader.next();

            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    parseStartTag(reader.getLocalName());
                break;

                case XMLStreamConstants.CHARACTERS:
                    parseContent(reader.getText());
                break;

                case XMLStreamConstants.END_ELEMENT:
                   parseEndTag(reader.getLocalName());
                break;

                /*case XMLStreamConstants.START_DOCUMENT:                    
                 break;*/
            }
        }

        inStream.close();        
    }
    
    private boolean isAttribute(String tag) {
        try {                
                RASupportDynamicAttributes dynamicAttribute = RASupportDynamicAttributes.valueOf(tag);                
                if(dynamicAttribute != null) {
                    attributeFlag = true;
                    attribute = dynamicAttribute;
                    return true;
                }
               
        } catch (IllegalArgumentException ex) {
            try {
                RASupportStaticAttributes staticAttribute = RASupportStaticAttributes.valueOf(tag);
                if (staticAttribute != null) {                    
                    attributeFlag = true;
                    attribute = staticAttribute;
                    return true;
                }
            } catch (IllegalArgumentException ex2) {
                logWarning(tag + " is a non-supported attribute", this.getClass());
                return false;
            }
        }
        
        return false;
    }
    
    // Is restriction between groups or restriction between nodes?
    // NODES = restrictions between nodes, GROUPS = restrictions between groups
    private boolean isRestriction(String tag, String container) {
        
        try {
                if(container.equals("NODES")) {
                    RASupportNodeRestrictions nr = RASupportNodeRestrictions.valueOf(tag);
                    if(nr != null) {                
                        nodeRestrictionFlag = true;
                        nodeRestriction = (RASupportNodeRestrictions) nr;
                        return true;
                    }
                }
                else if(container.equals("GROUPS")) {
                    RASupportGroupRestrictions gr = RASupportGroupRestrictions.valueOf(tag);
                    if(gr != null) {
                        groupRestrictionFlag = true;
                        groupRestriction = (RASupportGroupRestrictions) gr;
                        return true;
                    }
                }
        } catch (IllegalArgumentException ex) {
            logWarning(tag + " is a non-supported restriction", this.getClass());            
            return false;
        }
        
        
        
        return false;
    }
    
    private void parseStartTag(String tag) {
        
        switch(tag) {     
                            
                case TagsConfiguration.queryGroupTag:
                    group = new RASupportQueryGroup();
                    groupFlag = true;
                    return;

                case TagsConfiguration.queryGroupNameTag:                            
                    groupNameFlag = true;
                    return;

                case TagsConfiguration.queryGroupNodesTag:                            
                    groupNodesFlag = true;
                    return;

                case TagsConfiguration.queryRestrictionNodesTag:                            
                    restrictionNodes = new RASupportQueryRestrictionSet();
                    restrictionNodesFlag = true;
                    return;

                case TagsConfiguration.queryRestrictionGroupsTag:                            
                    restrictionGroups = new RASupportQueryRestrictionSet();
                    restrictionGroupsFlag = true;
                    return;
                    
                case TagsConfiguration.queryRestrictionGroupsEntitiesTag:                     
                    groupEntitiesFlag = true;
                    return;

                case TagsConfiguration.queryOptionTag:
                    option = true;
                    return;

                case TagsConfiguration.queryTTLTag:
                    ttl = true;
                    return;
                    
                case TagsConfiguration.queryRestrictionNodesEntitiesTag:
                    return;
                    
                case TagsConfiguration.queryTag:
                    if(!queryFlag) {
                        queryFlag = true;
                    }                    
                    else {
                        logWarning("only one query per XML document is allowed", this.getClass());
                    }
                    return;
        }
        
        if(groupFlag && !restrictionNodesFlag && isAttribute(tag)) {
            return;
        }
        
        if(restrictionNodesFlag  && isRestriction(tag, "NODES")) {
            return;
        }
        
        if(restrictionGroupsFlag && isRestriction(tag, "GROUPS")) {
            return;
        }
                       
    }        
    
    private void parseContent(String content) {
        
        // Only parse content if a query tag exists
        if(!queryFlag) {
            return;
        }
        
        // Parse group
        if(groupFlag) {
            // Parse attribute
            if(attributeFlag) {

                String[] arguments = content.split(RASupportCommon.querySeparator);

                if(attribute.isNumerical()) {
                    addNumericalRequirement(arguments, "GROUP");
                }
                else if(attribute.isString()) {
                    addStringRequirement(arguments, "GROUP");
                }

                attributeFlag = false;
            }
            if(groupNameFlag) {
                group.setName(content);
            }
            else if(groupNodesFlag) {
                group.setNum_nodes(Integer.parseInt(content));
            }
            else if(restrictionNodesFlag) {               

               if(nodeRestrictionFlag) {
                   String[] arguments = content.split(RASupportCommon.querySeparator);
                   if(nodeRestriction.isNumerical()) {
                        addNumericalRequirement(arguments, "NODE_RESTRICTIONS");
                    }
                    else if(nodeRestriction.isString()) {
                        addStringRequirement(arguments, "NODE_RESTRICTIONS");
                    }
                   nodeRestriction = null;
                   nodeRestrictionFlag = false;
               }     
               
            }
        }
        // Parse restrictions between groups
        else if(restrictionGroupsFlag) {
            
            String[] arguments = content.split(RASupportCommon.querySeparator);
            if(groupRestrictionFlag) {
                // Parse restrictions between groups                
                if(groupRestriction.isNumerical()) {
                     addNumericalRequirement(arguments, "GROUP_RESTRICTIONS");
                 }
                 else if(groupRestriction.isString()) {
                     addStringRequirement(arguments, "GROUP_RESTRICTIONS");
                 }
                groupRestriction = null;
                groupRestrictionFlag = false;
            }
            // Parse groups under restriction
            else if(groupEntitiesFlag) {
                restrictionGroups.addEntities(Arrays.asList(arguments));
            }            
        }
        // Parse option
        else if(option) {                        
            if(content.equals(FIND_RESOURCES.getName())) {
                query.addOption(FIND_RESOURCES);
            }
            else if(content.equals(PERFORMANCE.getName())) {
                query.addOption(PERFORMANCE);
            }                        
        }
        // Parse TTL
        else if(ttl) {
            query.addTTL(Integer.parseInt(content));                        
        }
    }
    
    private void parseEndTag(String tag) {
        
        switch(tag) {
            
            case TagsConfiguration.queryGroupTag:
                query.addGroup(group);
                group = null;
                groupFlag = false;
                restrictionNodesFlag = false;
                nodeRestrictionFlag = false;
            break;
                
            case TagsConfiguration.queryGroupNameTag:                            
                groupNameFlag = false;
            break;
                
            case TagsConfiguration.queryGroupNodesTag:                            
                groupNodesFlag = false;
            break;
                
            case TagsConfiguration.queryRestrictionNodesTag: 
                group.addNodeRestrictions(restrictionNodes);
                restrictionNodes = null;
                restrictionNodesFlag = false;
                nodeRestrictionFlag = false;
            break;
                
            case TagsConfiguration.queryRestrictionGroupsTag:                            
                query.addGroupRestrictions(restrictionGroups);
                restrictionGroups = null;
                restrictionGroupsFlag = false;
                groupRestrictionFlag = false;
            break;
                
            case TagsConfiguration.queryRestrictionGroupsEntitiesTag:                     
                groupEntitiesFlag = false;
            break;
                
            case TagsConfiguration.queryOptionTag:
                option = false;
            break;
                
            case TagsConfiguration.queryTTLTag:
                ttl = false;
            break;
                
            case TagsConfiguration.queryTag:
                queryFlag = false;
            break;
                
        }
    }
    
    // Tries to add a numerical attribute in a container (GROUP, GROUP_RESTRICTIONS, or NODE_RESTRICTIONS)
    private void addNumericalRequirement(String[] arguments, String container) {
        
        if(arguments.length == RASupportCommon.numericalArguments) {
                    
            String token = "";
            Number min_val = null, min_ideal = null, max_ideal = null, max_val = null;
            Double penalty = null;                    
            try {

                token = "min_val"; min_val = getNumber(arguments[0]);
                token = "min_ideal"; min_ideal = getNumber(arguments[1]);
                token = "max_ideal"; max_ideal = getNumber(arguments[2]);
                token = "max_val"; max_val = getNumber(arguments[3]);
                token = "penalty"; penalty = Double.parseDouble(arguments[4]);
                
                if(container.equals("GROUP")) {
                    group.addNumericalAttribute(attribute, min_val, min_ideal, max_ideal, max_val, penalty);
                }   
                else if(container.equals("NODE_RESTRICTIONS")) {
                    restrictionNodes.addNumericalRestriction(nodeRestriction, min_val, min_ideal, max_ideal, max_val, penalty);
                }
                else if(container.equals("GROUP_RESTRICTIONS")) {                    
                    restrictionGroups.addNumericalRestriction(groupRestriction, min_val, min_ideal, max_ideal, max_val, penalty);
                }

            } catch(NumberFormatException e3) {
                logError("invalid " + token +" in the numerical attribute" + attribute +" from the XML query " + xmlName, this.getClass());
            }                                                                                
        }
        else {
            logError("invalid attribute" + attribute +" in the XML query " + xmlName, this.getClass());
        }
    }
    
    // Tries to add a string attribute in a container (GROUP, GROUP_RESTRICTIONS, or NODE_RESTRICTIONS)
    private void addStringRequirement(String[] arguments, String container) {
        
        if(arguments.length == RASupportCommon.stringArguments) {
                                            
            Double penalty = null;                    
            try {
                
                penalty = Double.parseDouble(arguments[1]);
                
                if(container.equals("GROUP")) {
                    group.addStringAttribute(attribute, arguments[0], penalty);
                }   
                else if(container.equals("NODE_RESTRICTIONS")) {
                    restrictionNodes.addStringRestriction(nodeRestriction, arguments[0], penalty);
                }
                else if(container.equals("GROUP_RESTRICTIONS")) {                    
                    restrictionGroups.addStringRestriction(groupRestriction, arguments[0], penalty);
                }                
                
            } catch(NumberFormatException e3) {
                logError("invalid penalty in the string attribute" + attribute +" from the XML query " + xmlName, this.getClass());
            }                                                                                
        }
        else {
            logError("invalid attribute" + attribute +" in the XML query " + xmlName, this.getClass());
        }
    }
    
    // Tries to transform a String into Number    
    private Number getNumber(String str) {
        Number number = null;
        try {
            number = Float.parseFloat(str);            
        } catch(NumberFormatException e) {
            
            try {
                number = Double.parseDouble(str);
            } catch(NumberFormatException e1) {
                
                try {
                    number = Integer.parseInt(str);                    
                } catch(NumberFormatException e2) {
                    
                    try {
                        number = Long.parseLong(str);                        
                    } catch(NumberFormatException e3) {                        
                        throw e3;
                    }       
                }       
            }       
        }
        return number;
    }

}
