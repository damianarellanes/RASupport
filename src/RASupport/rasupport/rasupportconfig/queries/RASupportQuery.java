package RASupport.rasupport.rasupportconfig.queries;

import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logWarning;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import RASupport.rasupport.rasupportconfig.xml.XMLQueryWriter;
import java.io.File;

/**
 * RASupport: representation of a query in RASupport
 * @author Damian Arellanes
 */
public class RASupportQuery {
    
    File queryFile = null;
    
    private RASupportQueryOptions option = null;
    private int ttl = 0;    
    private boolean hasOption;
    private boolean hasTTL;
    private long queryId = 0; // Identifier of the query (it must be unique and is defined in the selection phase)
    
    // Groups specified in the query
    private RASupportMap<String, RASupportQueryGroup> groups = null;
    // Restrictions between groups in the query
    private RASupportQueryRestrictionSet groupRestrictions = null;
    
    public RASupportQuery() {
                
        this.groups = new RASupportMap<>();
        this.groupRestrictions = new RASupportQueryRestrictionSet();
        
        hasOption = false;
        hasTTL = false;
    }
    
    public RASupportQuery(RASupportQueryOptions option, int ttl) {
        this.option = option;
        this.ttl = ttl;
        
        this.groups = new RASupportMap<>();
        this.groupRestrictions = new RASupportQueryRestrictionSet();
        
        hasOption = true;
        hasTTL = true;
    }
    
    public void addOption(RASupportQueryOptions option) {
        
        if(!hasOption) {
            this.setOption(option);
            
            hasOption = true;
        }
        else {
            logWarning("XML queries can only have one option", this.getClass());
        }        
    }
    
    public void addTTL(int ttl) {
        
        if(!hasTTL) {
            this.setTtl(ttl);
            
            hasTTL = true;
        }
        else {
            logWarning("XML queries can only have one TTL", this.getClass());
        }        
    }
    
    public void addGroup(RASupportQueryGroup group) {
        
        String name = group.getName();
        int nodes = group.getNum_nodes();
        
        
        // You can't specify a same group in the same query
        if(getGroups().containsKey(name)) {
            logWarning("the group " + name + " already exists in the XML query", this.getClass());
            return;
        }
        
        // You can't specify a group without name or without number of nodes
        if(name.isEmpty() || nodes == 0) {
            logWarning("groups have to specify a name and the number of required nodes", this.getClass());
            return;
        }
        
        getGroups().put(name, group);
    }
    
    public void addGroupRestrictions(RASupportQueryRestrictionSet restrictions) {
        
        // The restriction set must have >= 2 groups
        if(restrictions.getRestrictedEntities().size() < 2) {
            logWarning("the restrictions between groups must specify at least two groups" ,this.getClass());
            return;
        }
        
        if(restrictions.isValid()) {
            setGroupRestrictions(restrictions);        
        }                
    }            
    
    public boolean isConsistent() {
        
        boolean consistent = true;
        if(!hasOption || !hasTTL) {
            logWarning("XML queries must have one option and one TTL", 
                    this.getClass());            
            consistent = false;
        }
        
        if(groups.size() == 0) {
            logWarning("XML queries must have at least one group", 
                    this.getClass());            
            
            consistent = false;
        }
        
        return consistent;
    }
    
    public void saveXMLQuery(String name) {
        
        if(!isConsistent()) {
            return;
        }
        
        XMLQueryWriter qw = new XMLQueryWriter(name);
        
        qw.createOption(getOption());
        qw.createTTL(getTtl());
        
        // Parse groups
        for(RASupportQueryGroup group: getGroups().values()) {
            qw.createGroup(group);
        }
        
        qw.createRestrictionBetweenGroups(getGroupRestrictions());
        
        qw.finishXMLQuery();
        
        queryFile = qw.getXMLQueryFile();
                
    }
    
    public File getXMLQuery() {
        
        if(queryFile != null) {
            logMessage("the XML query " + queryFile.getName() + " has been successfully generated", this.getClass());
            return queryFile;
        }
        
        logWarning("the XML query needs to be saved before and it must to be consistent", this.getClass());
        return null;        
    }
    
    @Override
    public String toString() {
        return "Option: " + getOption() + "\nTTL:" + getTtl() + "\n" +
                "Groups: " + getGroups() + "\n" + 
                "Group restrictions: " + getGroupRestrictions() +"\n";
    } 

    /**
     * @return the option
     */
    public RASupportQueryOptions getOption() {
        return option;
    }

    /**
     * @param option the option to set
     */
    public void setOption(RASupportQueryOptions option) {
        this.option = option;
    }

    /**
     * @return the ttl
     */
    public int getTtl() {
        return ttl;
    }

    /**
     * @param ttl the ttl to set
     */
    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    /**
     * @return the groups
     */
    public RASupportMap<String, RASupportQueryGroup> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(RASupportMap<String, RASupportQueryGroup> groups) {
        this.groups = groups;
    }

    /**
     * @return the groupRestrictions
     */
    public RASupportQueryRestrictionSet getGroupRestrictions() {
        return groupRestrictions;
    }

    /**
     * @param groupRestrictions the groupRestrictions to set
     */
    public void setGroupRestrictions(RASupportQueryRestrictionSet groupRestrictions) {
        this.groupRestrictions = groupRestrictions;
    }

    /**
     * @return the queryId
     */
    public long getQueryId() {
        return queryId;
    }

    /**
     * @param queryId the queryId to set
     */
    public void setQueryId(long queryId) {
        this.queryId = queryId;
    }

}
