package RASupport.rasupport.rasupportconfig.xml;

import RASupport.rasupport.rasupportconfig.common.RASupportCommon;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logWarning;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryGroup;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryOptions;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryRequirement;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryRestrictionSet;
import static RASupport.rasupport.rasupportconfig.xml.TagsConfiguration.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
/**
 * SelectionAPI: writes a new XML query
 * @author Damian Arellanes
 */
public class XMLQueryWriter {
    
    private File queryFile;        
    private BufferedWriter writer;
    private char[] openQueryOption;    
    
    public XMLQueryWriter(String xmlName) {
        
        try {            
            
            queryFile = new File(RASupportCommon.queriesDirectory + "/" + xmlName + ".xml");
            writer = new BufferedWriter(new FileWriter(queryFile));
            writer.write(XML_HEADER + openQueryTag);
            
        } catch (UnsupportedEncodingException  e) { 
            logError("error creating a XML Query: " + e.getMessage());            
        } catch (IOException e) {               
            logError("error creating a XML Query: " + e.getMessage());            
        }
    }
    
    public void createOption(RASupportQueryOptions option) {
        
        try {                        
                writer.write(option.getTag());                        
                
        } catch (IOException e) {
            logError("error creating an option tag in XML query: " + e.getMessage());
        }
        
    }
    
    public void createTTL(int ttl) {
        
        try {                        
                writer.write(openQueryTTLTag + ttl + closeQueryTTLTag);                        
         
        } catch (IOException e) {
            logError("Error creating a ttl tag in XML query: " + e.getMessage());
        }
    }     
    
    public void createGroup(RASupportQueryGroup group) {
        try {
            
            writer.write(openQueryGroupTag);
            
            // Creates the name of the group
            writer.write(openQueryGroupNameTag + group.getName() + closeQueryGroupNameTag);
            
            // Creates the number of required nodes for the group
            writer.write(openQueryGroupNodesTag + group.getNum_nodes() + closeQueryGroupNodesTag);
                        
            // Creates tags for the attributes
            for(RASupportQueryRequirement attribute: group.getAttributes().values()) {
                                                
                writer.write(createTag(attribute.getAttribute().getAttributeName(), 
                        attribute.getContent(), indentationLevelThree));
            }
            
            // Creates restrictions between nodes
            RASupportQueryRestrictionSet rbn = group.getNodeRestrictions();
            if(rbn != null) {
                
                writer.write(openqueryRestrictionNodesTag);
                
                // Don't write restricted entities beacuse restrictions between nodes don't have them
                /*if(rbn.getRestrictedEntities().size() > 0) {
                    writer.write(openqueryRestrictionNodesEntitiesTag + 
                            rbn.getEntitiesContent() +
                        closequeryRestrictionNodesEntitiesTag);
                }*/
                
                for(RASupportQueryRequirement restriction: rbn.getRestrictions()) {
                                        
                    writer.write(createTag(restriction.getAttribute().getAttributeName(), 
                        restriction.getContent(), indentationLevelFour));
                }
                writer.write(closequeryRestrictionNodesTag);
            }
            
            writer.write(closeQueryGroupTag);
            
        } catch (IOException e) {
            logError("error creating a group in XML query: " + e.getMessage());
        }
    }
    
    public void createRestrictionBetweenGroups(RASupportQueryRestrictionSet restrictions)  {
        
        try {
            
            if(restrictions.getRestrictedEntities().size() < 2) {
                logWarning("the entities list from restrictions between groups must have at least 2 entities" ,this.getClass());
                return;
            }
            
            if(restrictions.isValid()) {
                writer.write(openqueryRestrictionGroupsTag);
                        
                writer.write(openqueryRestrictionGroupsEntitiesTag + 
                                restrictions.getEntitiesContent() +
                            closequeryRestrictionGroupsEntitiesTag);

                // Creates restrictions between groups
                for(RASupportQueryRequirement restriction: restrictions.getRestrictions()) {

                        writer.write(createTag(restriction.getAttribute().getAttributeName(), 
                            restriction.getContent(), indentationLevelThree));
                    }
                writer.write(closequeryRestrictionGroupsTag);
            }
            
            
        } catch (IOException e) {
            logError("error creating a restriction between groups in XML query: " + e.getMessage());
        }
    }
    
    public void finishXMLQuery() {
        try {        
            writer.write(closeQueryTag);
            writer.close();
        } catch (IOException e) {
            System.err.println("error finishing a XML query: " + e.getMessage());
        }
    }
    
    public File getXMLQueryFile() {
        return queryFile;
    }       
    
}
