package RASupport.rasupport.rasupportmain;

import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.AttributeCategories.*;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.RASupportMode.*;
import static RASupport.rasupport.rasupportconfig.common.RASupportErrorsManager.*;
import RASupport.rasupport.rasupportconfig.common.RASupportNode;
import static RASupport.rasupport.rasupportconfig.config.RASupportConfigParser.*;
import static RASupport.rasupport.rasupportconfig.modules.RASupportModulesConfiguration.ResourceManagers.*;
import static RASupport.rasupport.rasupportconfig.modules.RASupportModulesConfiguration.ResourceMonitors.*;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceAggregation;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceManager;
import RASupport.rasupport.rasupportconfig.modules.RASupportResourceMonitor;
import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.modules.transportlayer.RASupportReceiver;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportmain.modulesmanagement.RASupportModulesFactory;
import java.io.File;
import static java.lang.System.exit;

/**
 * RASupportMain: facade for RASupport
 * @author damianarellanes
 */
public class RASupportMain {
    
    // Representation of a node in the support
    private RASupportNode node = null;
    // Attribute manager used by the support    
    private RASupportResourceManager resourceManager = null;
    // Dynamic resources monitor used by the support
    private RASupportResourceMonitor resourceMonitor = null;
    // Resource aggregation toolkit used by the support
    private RASupportResourceAggregation resourceAggregation = null;

    public RASupportMain(RASupportTopologyNode mycoNode) {   
        
        node = new RASupportNode(mycoNode);        
        
        // Creates the resource manager
        selectResourceManagerModule();                    
        
        // We need imperatively a resource manager
        if(existsAttributeManager(resourceManager)) {
                        
            // Obtains and sets attributes and use restrictions in the node
            obtainNodeCharacteristics();
            
            // Creating the toolkit for resource aggregation selected by the user
            // In the configuration parser we checked if the user selected a valid toolkit
            resourceAggregation = 
                RASupportModulesFactory.createResourceAggregationModule(raConfig_Toolkit, node);                
        
            // Creates the dynamic resources monitor
            selectResourceMonitorModule(); 
            
            // We need imperatively a resource monitor
            if(existsAttributeMonitor(resourceMonitor)) {
                // Starts the dynamic resources monitor            
                //resourceMonitor.startMonitor();            
            }
            else {
                exit(0);
            }
        }   
        
        else {
            exit(0);
        }                               
    }
        
    // For simulations we will use ResourceSim
    private void selectResourceManagerModule() {
        if(raConfig_Mode == RANDOM_MODE) {
            
            resourceManager = 
                RASupportModulesFactory.createResourceManager(RSIM);            
            
            // TODO create the dynamic attributes monitor
            // Maybe is better to have the monitor in the attribute manager
        }
        else {
            // TODO use API to get real attributes
        }
    }
    
    // For simulations we will use RSimMonitor
    private void selectResourceMonitorModule() {
        if(raConfig_Mode == RANDOM_MODE) {
            
            resourceMonitor = 
                RASupportModulesFactory.createResourceMonitor(RSIM_MONITOR, 
                        node, resourceAggregation);            
            
            // TODO create the dynamic attributes monitor
            // Maybe is better to have the monitor in the attribute manager
        }
        else {
            // TODO use API to monitor real attributes
        }
    }
    
    // Obtains dynamic and static attributes, and the use restrictions of the node
    private void obtainNodeCharacteristics() {
                
        node.setDynamicAttributes(
            resourceManager.obtainDynamicAttributes());
        node.setStaticAttributes(
                resourceManager.obtainStaticAttributes());
        
        // Sets use restrictions specified in configuration file in the node
        node.setUseRestrictions(raConfig_UseRestrictions);

        node.printAttributes(DYNAMIC_ATTRIBUTE);
        node.printAttributes(STATIC_ATTRIBUTE);        
    }
    
    /**************************************************************************    
    * ADVERTISEMENT METHODS     
    **************************************************************************/
    
    public void cleanIndexTable() {
        resourceAggregation.cleanIndexTable();
    }
    
    public void deleteIndexEntry(String entry) {
        resourceAggregation.deleteIndexEntry(entry);
    }
    
    public void advertiseResourcesTo(RASupportTopologyNode targetNode) {
        
        resourceAggregation.advertiseResourcesTo(targetNode);
    }
    
    public RASupportReceiver getReceiver() {
        return resourceAggregation.getReceiver();
    }
    
    /**************************************************************************    
    * SELECTION METHODS     
    **************************************************************************/
    
    // For this method, the client must to create a query object
    public void executeQuery(RASupportQuery query) {
        resourceAggregation.executeQuery(query);
    }
    
    // For this method, the resource aggregation module parses the XML file
    public void executeQuery(File queryFile) {
        
        
        resourceAggregation.executeQuery(queryFile);
    }
}
