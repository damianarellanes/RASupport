package RASupport.rasupport.rasupportconfig.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.*;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.RASupportMode.*;
import RASupport.rasupport.rasupportconfig.common.RASupportCommon.RASupportRules;
import static RASupport.rasupport.rasupportconfig.common.RASupportCommon.RASupportRules.*;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logError;
import static RASupport.rasupport.rasupportconfig.log.LogManager.logMessage;
import RASupport.rasupport.rasupportconfig.modules.RASupportModulesConfiguration.ResourceAggregationToolkits;
import static RASupport.rasupport.rasupportconfig.modules.RASupportModulesConfiguration.ResourceAggregationToolkits.*;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportMap;
import static RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportUseRestrictions.*;

/**
 * RASupport: manager of the file configuration of the support
 * @author Damian Arellanes
 */
public class RASupportConfigParser {
    
    public static Properties configuration;
    public static final String configFileName = "rasupport.properties";
    
    public static final String alias_tag = "peer_alias";
    public static String raConfig_peerAlias = "";
    
    public static final String mode_tag = "mode";
    public static final String mode_random = "random";
    public static final String mode_real = "real";    
    public static RASupportMode raConfig_Mode = NO_MODE; // MODE
    
    public static RASupportRules raConfig_RSim_Behaviour = NO_RULE; // RSIM BEHAVIOUR
    
    public static final String toolkit_tag = "toolkit";
    public static final String toolkit_ratoolkit = "ratoolkit";    
    public static ResourceAggregationToolkits raConfig_Toolkit = NO_TOOLKIT; 
    
    /*public static final String configMessages_tag = "show_messages";
    public static final String configMessages_true = "true";
    public static final String configMessages_false = "false";    
    public static String configMessages_selected = null;// USE ERROR DEFAULT TAG*/
    
    /*********************************************************************/
    /* START Use restrictions tags  */
    /*********************************************************************/
    // Availability
    public static final String availabilityStart_tag = 
            "use_restriction.availability.start";
    public static final String availabilityEnd_tag = 
            "useRestriction.availability.end";   
    
    // Max CPU utilization
    public static final String maxCPUutilized_tag = 
            "use_restriction.max_cpu_utilization";
    public static int raConfig_MaxCPU = NO_CONFIG_CPU_UTILIZED; // MAX CPU UTILIZED
    
    // Only for friends
    /*public static final String onlyForFriendsValue_tag = 
            "use_restriction.only_for_friends.value";
    public static boolean raConfig_OnlyForFriends = 
            NO_CONFIG_ONLY_FOR_FRIENDS; // ONLY FOR FRIENDS (VALUE)*/
    public static final String onlyForFriends_tag = 
            "use_restriction.only_for_friends";
    public static RASupportMap raConfig_OnlyForFriends = 
            NO_CONFIG_ONLY_FOR_FRIENDS;     
    /*********************************************************************/
    /* END Use restrictions tags  */
    /*********************************************************************/
    public static final RASupportMap raConfig_UseRestrictions = 
            new RASupportMap(); // Use restrictions
    
    static {
        InputStream inputStream = null;
        try {
            // Loading the configuration file of the support
            logMessage("loading the configuration file of the support: " +
                    configFileName);
            configuration = new Properties();
            inputStream = new FileInputStream(configFileName);
            configuration.load(inputStream);
            if (inputStream == null) {
                throw new FileNotFoundException("Configuration file '" + configFileName + "' not found in the classpath");
            }   
            
            loadConfiguration();
            
            logMessage("configuration file of the support loaded");
            
        } catch (FileNotFoundException ex) {
            logError("error loading the configuration file!");
        } catch (IOException ex) {
            logError("error loading the configuration file!");
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
              logError("error loading the configuration file!");  
            }
        }
    }
            
    private static void loadConfiguration() {        
        
        loadInternalConfiguration();
        loadUseRestrictions();
    }
    
    private static void loadInternalConfiguration() {
        
        /*------------------------------------------------------------------------*/
        // Alias: alias of the peer        
        /*------------------------------------------------------------------------*/        
        raConfig_peerAlias = configuration.getProperty(alias_tag);
        
        /*------------------------------------------------------------------------*/
        // Mode: random attributes (RANDOM MODE) or real attributes (REAL_MODE)
        /*------------------------------------------------------------------------*/        
        String mode = configuration.getProperty(mode_tag);
        if(mode.equals(mode_random)) {
            raConfig_Mode = RANDOM_MODE;
            logMessage("random mode initialized");
        }            
        else if(mode.equals(mode_real)) {
            raConfig_Mode = REAL_MODE;
            logMessage("real mode initialized");
        }
        else {
            raConfig_Mode = NO_MODE;
            logError("unrecognized mode, please specify random or real");
            exit(1);
        }
        
        /*------------------------------------------------------------------------*/
        // Number of attributes generated by ResourceSim: all (ALL_ATTRIBUTES) or 
        //random number (RANDOM_ATTRIBUTES) 
        /*------------------------------------------------------------------------*/
        raConfig_RSim_Behaviour = GENERATE_ALL_ATTRIBUTES;
        
        
        /*------------------------------------------------------------------------*/
        // Toolkit for resource aggregation in collaborative P2P systems
        /*------------------------------------------------------------------------*/
         String toolkit = configuration.getProperty(toolkit_tag);
         if(toolkit.equals(toolkit_ratoolkit)) {
             raConfig_Toolkit = RATOOLKIT;
             logMessage("selected toolkit: RAToolkit");
         }
         else {
             logError("unrecognized toolkit for resource aggregation, "
                     + "please check the documentation to select an appropiate toolkit");
             exit(0);
         }
    }
    
    // Parse the configuration file searchin the use restrictions
    private static void loadUseRestrictions() {
            
        /*------------------------------------------------------------------------*/
        // Use restriction: availability of a node 
        /*------------------------------------------------------------------------*/
        if(configuration.containsKey(availabilityStart_tag) && 
                configuration.containsKey(availabilityEnd_tag)) {
         
            try {
                String startAvailability = configuration.getProperty(
                        availabilityStart_tag);
                String endAvailability = configuration.getProperty(
                        availabilityEnd_tag);

                // Use java.util.Calendar to extract hours and minutes
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date availabilityStart = sdf.parse(startAvailability);
                Date availabilityEnd = sdf.parse(endAvailability);

                Calendar start = Calendar.getInstance();
                start.setTime(availabilityStart);
                Calendar end = Calendar.getInstance();
                end.setTime(availabilityEnd);

                logMessage("usage restriction found -> Availability: " + 
                        "from " + start.get(Calendar.HOUR_OF_DAY) + ":" + start.get(Calendar.MINUTE) + 
                        " to " + end.get(Calendar.HOUR_OF_DAY) + ":" + end.get(Calendar.MINUTE));
                
                /*// Putting an arraylist (start, end) related with the availability
                raConfig_UseRestrictions.put(AVAILABILITY, 
                        Arrays.asList(availabilityStart, availabilityEnd));*/
                
                // Putting an start and end related with the availability
                raConfig_UseRestrictions.put(availability_start, startAvailability);
                raConfig_UseRestrictions.put(availability_end, endAvailability);
                
            } catch (ParseException | NullPointerException e) {
                //logError("error parsing availability in the configuration file, please check start and end properties");
                //availabilityStart = NO_CONFIG_AVAILABILITY_START;
                //availabilityEnd = NO_CONFIG_AVAILABILITY_END;
            }
        }        
        
        /*------------------------------------------------------------------------*/
        // Use restriction: percentage of max allowed CPU utilized
        /*------------------------------------------------------------------------*/
        if(configuration.containsKey(maxCPUutilized_tag)) {
                     
            try {
                raConfig_MaxCPU = Integer.valueOf(
                        configuration.getProperty(maxCPUutilized_tag));

                if(raConfig_MaxCPU < 0 || raConfig_MaxCPU > 99) {
                    logError("max CPU utilization must to be in the interval [0,99]");
                }
                else {
                    logMessage("usage restriction found -> Max CPU utilization: " + raConfig_MaxCPU);        
                    
                    // Putting max CPU in use restrictions
                    raConfig_UseRestrictions.put(max_cpu_utilization, raConfig_MaxCPU);
                }

            } catch (NumberFormatException | NullPointerException e) {            
                //logError("error parsing max_cpu_utilization in the configuration file");
                raConfig_MaxCPU = NO_CONFIG_CPU_UTILIZED;
            }
        }        
        
        /*------------------------------------------------------------------------*/
        // Use restriction: only for friends
        /*------------------------------------------------------------------------*/
        if(configuration.containsKey(onlyForFriends_tag)) {
            try {            
                /*// Value
                raConfig_OnlyForFriends = Boolean.parseBoolean(
                        configuration.getProperty(onlyForFriendsValue_tag));*/
                
                /*logMessage("usage restriction found -> Only for friends (value): " + 
                        raConfig_OnlyForFriends);*/
                
                // If only_for_friends.value == false
                /*if(raConfig_OnlyForFriends == NO_CONFIG_ONLY_FOR_FRIENDS) {
                    throw new NullPointerException();
                }*/

                // Friends
                raConfig_OnlyForFriends = new RASupportMap();
                String friends = configuration.getProperty(onlyForFriends_tag);
                String[] friendsDetected = friends.split(",");
                
                // If only_for_friends doesn't have a friends list separated by comma
                if(friendsDetected.length == 0) {                    
                    throw new NullPointerException();
                }
                
                // Checking for duplicates
                for(int i = 0, size = friendsDetected.length; i < size; i++) {                   
                    if(!raConfig_OnlyForFriends.containsKey(friendsDetected[i])) {
                       raConfig_OnlyForFriends.put(friendsDetected[i], friendsDetected[i]);
                    }
                    else {
                        logError("you cannot have duplicates in the alias of your friends: "
                                + friendsDetected[i] + " duplicated");
                        
                        throw new NullPointerException();
                    }
                }

                logMessage("usage restriction found -> Only for friends: " + 
                        raConfig_OnlyForFriends.keySet());
                
                // Putting a hash map of friends in use restrictions
                raConfig_UseRestrictions.put(only_for_friends, 
                        raConfig_OnlyForFriends);
                
            } catch (NumberFormatException | NullPointerException e) {
                //logError("error parsing only_for_friends.value in the configuration file");               
                raConfig_OnlyForFriends = NO_CONFIG_ONLY_FOR_FRIENDS;
            }        
        }
                
    }

}
