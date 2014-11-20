package RASupport.tests;

import RASupport.rasupport.rasupportconfig.modules.*;
import RASupport.rasupport.rasupportconfig.queries.*;
import RASupport.rasupport.rasupportconfig.resourcesmodel.*;
import RASupport.rasupport.rasupportconfig.xml.XMLQueryReader;
import RASupport.rasupport.rasupportmain.RASupportMain;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author damianarellanes
 */
public class IRandomWalkTest {
    
    public static double getSimilarityAttributes(RASupportQueryRequirement requeriment1, RASupportQueryRequirement requeriment2) {
        
        double similarity = 0.0;        
        
        if(requeriment2.isNumerical() && requeriment1.isNumerical()) {
            
            int count = 0;
            if(requeriment2.getMin_val().doubleValue() == requeriment1.getMin_val().doubleValue()) count++;
            if(requeriment2.getMin_ideal().doubleValue() == requeriment1.getMin_ideal().doubleValue()) count++;
            if(requeriment2.getMax_ideal().doubleValue() == requeriment1.getMax_ideal().doubleValue()) count++;
            if(requeriment2.getMax_val().doubleValue() == requeriment1.getMax_val().doubleValue()) count++;
            
            similarity = (100 * count) / 4;
        }   
        else if(requeriment2.isString() && requeriment1.isString()) {
            
            if(requeriment2.getValue().equals(requeriment1.getValue())) {
                similarity = 100.0;
            } else {
                similarity = 0.0;
            }
        }
        
        
        return similarity;
    }
    
    // Proposed algorithm
    public static double getSimilarity(RASupportQueryGroup existentGroup, RASupportQueryGroup targetGroup) {
        
        double similarity = 0.0;        
        
        for(RASupportQueryRequirement requirement: targetGroup.getAttributes().values()) {
            
            RASupportAttributesInterface attribute = requirement.getAttribute();
            if(existentGroup.containsAttribute(attribute)) {
                
                similarity += getSimilarityAttributes(existentGroup.getAttribute(attribute), requirement);
            }
        }
        
        
        return similarity / Math.max(existentGroup.getNumAttributes(), targetGroup.getNumAttributes());
    }
    
    // phpSimilar from Oliver's book used by PHP
    public static float similar_text(String first, String second)   {
        first = first.toLowerCase();
        second = second.toLowerCase();
        return (float)(phpSimilar(first, second)*200)/(first.length()+second.length());
    }

    public static int phpSimilar(String first, String second)  { 
        int p, q, l, sum;
        int pos1=0;
        int pos2=0;
        int max=0;
        char[] arr1 = first.toCharArray();
        char[] arr2 = second.toCharArray();
        int firstLength = arr1.length;
        int secondLength = arr2.length;

        for (p = 0; p < firstLength; p++) {
            for (q = 0; q < secondLength; q++) {
                for (l = 0; (p + l < firstLength) && (q + l < secondLength) && (arr1[p+l] == arr2[q+l]); l++);            
                if (l > max) {
                    max = l;
                    pos1 = p;
                    pos2 = q;
                }

            }
        }
        sum = max;
        if (sum > 0) {
            if (pos1 > 0 && pos2 > 0) {
                sum += phpSimilar(first.substring(0, pos1>firstLength ? firstLength : pos1), second.substring(0, pos2>secondLength ? secondLength : pos2));
            }

            if ((pos1 + max < firstLength) && (pos2 + max < secondLength)) {
                sum += phpSimilar(first.substring(pos1 + max, firstLength), second.substring(pos2 + max, secondLength));
            }
        }       
        return sum;
    }
    
    // Levenshtein distance algorithm   
    public static double LevenshteinDistance(String s0, String s1) {                          
        int len0 = s0.length() + 1;                                                     
        int len1 = s1.length() + 1;                                                     

        // the array of distances                                                       
        int[] cost = new int[len0];                                                     
        int[] newcost = new int[len0];                                                  

        // initial cost of skipping prefix in String s0                                 
        for (int i = 0; i < len0; i++) cost[i] = i;                                     

        // dynamicaly computing the array of distances                                  

        // transformation cost for each letter in s1                                    
        for (int j = 1; j < len1; j++) {                                                
            // initial cost of skipping prefix in String s1                             
            newcost[0] = j;                                                             

            // transformation cost for each letter in s0                                
            for(int i = 1; i < len0; i++) {                                             
                // matching current letters in both strings                             
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;             

                // computing cost for each transformation                               
                int cost_replace = cost[i - 1] + match;                                 
                int cost_insert  = cost[i] + 1;                                         
                int cost_delete  = newcost[i - 1] + 1;                                  

                // keep minimum cost                                                    
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }                                                                           

            // swap cost/newcost arrays                                                 
            int[] swap = cost; cost = newcost; newcost = swap;                          
        }                                                                               

        // the distance is the cost for transforming all letters in both strings = cost[len0 - 1]              
        
        double lfd = (double) cost[len0 - 1];
        
        return 100 - ((lfd / (Math.max(s0.length(), s1.length()))) * 100); // the number of chars that do not match
        
        //return ((1 - (cost[len0 - 1]) / Math.max(len0,len1) ) ) *100;
        
        /*int bigger = Math.max(s0.length(), s1.length());
        return  ((Math.max(s0.length(), s1.length()) - cost[len0 - 1]) / bigger) * 100;*/
    }
    
    /*****************************************************************************************************************/
    
    public static List<String> symbolsArray = Arrays.asList( // An array of symbols that can appear in a XML query
            "<", ">", "/", ".", ",", "\"", "-", "_", ":", "?", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "="
    );
    public static int sizeSymbols = symbolsArray.size();
    
    public static double getSimilarity(String groupToCompare, Integer[] comparisonOccurrences) {
        
        String toCompare = groupToCompare.toLowerCase();
        double similarity = 0.0;
        int i = 0;        
        int toCompareLength = toCompare.length();
                
        for( ; i < comparisonOccurrences.length; i++) {
            
            int groupOcurrences = toCompareLength - toCompare.replace(String.valueOf(symbolsArray.get(i)), "").length();
            
            if(groupOcurrences > comparisonOccurrences[i] && groupOcurrences != 0) {
                similarity += comparisonOccurrences[i] / groupOcurrences;
            }
            else if(comparisonOccurrences[i] > groupOcurrences && comparisonOccurrences[i] != 0) {
                similarity += groupOcurrences / comparisonOccurrences[i];
            }
            else if(comparisonOccurrences[i] == groupOcurrences) {
                similarity ++;
            }
        }
        
        return (similarity * 100) / i;
    }
    
    // Algorithm 
    public static double getSimilarity2(String groupToCompare, String reference) {
                
        double similarity = 0.0;
        int bound = Math.min(groupToCompare.length(), reference.length());
        int count = 0;
        
        for(int i = 0; i< bound; i++) {
            
            if(groupToCompare.charAt(i) == reference.charAt(i)) {
                count++;
            }
        }
        
        return (count * 100) / (Math.max(groupToCompare.length(), reference.length()));
        
    }
    
    public static double similarity(String s1, String s2) {
        if (s1.length() < s2.length()) { // s1 should always be bigger
            String swap = s1; s1 = s2; s2 = swap;
        }
        int bigLen = s1.length();
        if (bigLen == 0) { return 1.0; /* both strings are zero length */ }
        return (bigLen - computeEditDistance(s1, s2)) / (double) bigLen;
    }

    public static int computeEditDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        RASupportTopologyNode node = new RASupportTopologyNode() {

            @Override
            public void createRASupport() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public long getIdNode() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getAlias() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public RASupportMain getRASupport() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isSuperpeer() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        XMLQueryReader qw = new XMLQueryReader(new File("RASupportQueries/queryTest.xml").getAbsolutePath(), 
                node);
        RASupportQuery query = qw.getQuery();
        query.saveXMLQuery("TOCOMPARE");
        //String toCompare = query.getContent();
        
        XMLQueryReader qw2 = new XMLQueryReader(new File("RASupportQueries/similarRef.xml").getAbsolutePath(), 
                node);
        RASupportQuery query2 = qw2.getQuery();
        query2.saveXMLQuery("REF");
        //String existentGroup = query2.getContent().toLowerCase();
        
        RASupportMap<String, Integer[]> statistics = new RASupportMap<>();
        RASupportMap<RASupportQueryGroup, List<String>> statistics2 = new RASupportMap<>();
        
        for(RASupportQueryGroup group: query2.getGroups().values()) {
            
            Integer occurrences[] = new Integer[symbolsArray.size()];
            String existentGroup = group.getContent().toLowerCase();
            for(int i = 0, size = symbolsArray.size(); i < size; i++) {

                int groupOcurrences = existentGroup.length() - existentGroup.replace(symbolsArray.get(i), "").length();
                occurrences[i] = groupOcurrences;
            }
            statistics.put("PeerXYZ", occurrences);
            //System.out.println(group.getContent()); 
            
            statistics2.put(new RASupportQueryGroup(group), Arrays.asList("PeerXYZ", "PeerXXX"));
        }
        
        /*int occurrences2[] = new int[symbolsArray.size()];
        for(int i = 0, size = symbolsArray.size(); i < size; i++) {
            
            int groupOcurrences2 = toCompare.length() - toCompare.replace(symbolsArray.get(i), "").length();
            occurrences2[i] = groupOcurrences2;
        }*/
        
        long time_start, time_end;
        int iterations = 1;
        int jump = 1;
        long[][] levenshtein = new long[iterations][];
        long[][] jaroWinkler = new long[iterations][];
        long[][] similarText = new long[iterations][];
        long[][] proposed = new long[iterations][];
        
        RASupportQueryGroup group1 = query.getGroups().get("Processing");
        RASupportQueryGroup group2 = query2.getGroups().get("Processing");        
        String groupStr1 = group1.getContent();
        String groupStr2 = group2.getContent();
        
        System.out.println("GROUP 1: " + groupStr1);
        System.out.println("GROUP 2: " + groupStr2);
                
        /******************************************************************************************/
        // Levenshtein distance algorithm                
        /******************************************************************************************/        
        for(int i = 0; i< iterations; i++) {
            
            time_start = System.currentTimeMillis();
            System.out.println(LevenshteinDistance(groupStr1, groupStr2));
            LevenshteinDistance(groupStr1, groupStr2);
            time_end = System.currentTimeMillis(); 
        }        
        //System.out.println("Leveshtein distance = " + (time_end-time_start));
        
        /******************************************************************************************/
        // Levenshtein distance algorithm (apache commons)                
        /******************************************************************************************/        
        for(int i = 0; i< iterations; i++) {
            
            time_start = System.currentTimeMillis();
            System.out.println(100 - ((StringUtils.getLevenshteinDistance(groupStr1, groupStr2)) 
                    / (Math.max(groupStr1.length(), groupStr2.length())) * 100));
            
            double res = 100 - ((StringUtils.getLevenshteinDistance(groupStr1, groupStr2)) / (Math.max(groupStr1.length(), groupStr2.length())) * 100);
            //StringUtils.getLevenshteinDistance(groupStr1, groupStr2);
                        
            time_end = System.currentTimeMillis(); 
            levenshtein[i] = new long[]{i,time_end-time_start};
        }           
        //System.out.println("Leveshtein distance (apache commons) = " + (time_end-time_start));
        
        /******************************************************************************************/
        // Jaro Winkler distance algorithm                
        /******************************************************************************************/        
        for(int i = 0; i< iterations; i++) {
            
            time_start = System.currentTimeMillis();
            
            //System.out.println(StringUtils.getJaroWinklerDistance(groupStr1, groupStr2) * 100);
            StringUtils.getJaroWinklerDistance(groupStr1, groupStr2);
            
            time_end = System.currentTimeMillis(); 
            
            jaroWinkler[i] = new long[]{i,time_end-time_start};
        }        
        //System.out.println("Jaro Winkler distance (apache commons) = " + (time_end-time_start));
        
        /******************************************************************************************/
        // similar_text algorithm from Programming Classics: Implementing the World's Best Algorithms by Oliver
        /******************************************************************************************/                        
        for(int i = 0; i< iterations; i++) {
            
            time_start = System.currentTimeMillis();
            
            similar_text(groupStr1, groupStr2);
            //System.out.println(similar_text(groupStr1, groupStr2));
            
            time_end = System.currentTimeMillis(); 
            
            similarText[i] = new long[]{i,time_end-time_start};
        }        
        //System.out.println("similar_text = " + (time_end-time_start));
        
        /******************************************************************************************/
        // Proposed algorithm
        /******************************************************************************************/        
        for(int i = 0; i< iterations; i++) {
            
            time_start = System.currentTimeMillis();
            
            //System.out.println(getSimilarity(group1, group2));
            getSimilarity(group1, group2);
            
            time_end = System.currentTimeMillis(); 
            
            proposed[i] = new long[]{i,time_end-time_start};
        }        
        //System.out.println("Proposed = " + (time_end-time_start));
        
        /*for(int i = 0; i< iterations; i++) {
            
            time_start = System.currentTimeMillis();
            double res = 100 - ((StringUtils.getLevenshteinDistance(groupStr1, groupStr2)) / (Math.max(groupStr1.length(), groupStr2.length())) * 100);
            time_end = System.currentTimeMillis();
            levenshtein[i] = new long[]{i,time_end-time_start};
            
        }*/
                
        // Proposed algorithm
        /*for(RASupportQueryGroup group: query.getGroups().values()) {

            //System.out.println(group.getContent());
            for(Map.Entry<RASupportQueryGroup, List<String>> entry: statistics2.entrySet()) {

                System.out.println("Comparing with group " + entry.getKey().getName() + " from " + entry.getValue());                
                System.out.println("Similarity = " + getSimilarity(entry.getKey(), group));
            }

        }*/
        
        
        
        
        
        JavaPlot p = new JavaPlot();                
        //p.setTerminal(eps);
        //p.setTitle("Rendimiento del algoritmo de actualización de recursos dinámicos"); 
        p.set("xlabel", "'Iteraciones'");
        p.set("ylabel", "'Tiempo (ms)'");
        p.set("xrange", "[0:10]");
        //p.set("mxtics", "2"); // To divide x range between 2
        p.setKey(JavaPlot.Key.TOP_LEFT);
        
        PlotStyle plotStyle = new PlotStyle();        
        plotStyle.setStyle(Style.LINESPOINTS);        
        plotStyle.setLineWidth(1);
        plotStyle.setPointType(4);
        plotStyle.setPointSize(1);
        
        PlotStyle plotStyle2 = new PlotStyle();        
        plotStyle2.setStyle(Style.LINESPOINTS);
        plotStyle2.setPointType(9);
        plotStyle2.setPointSize(1);
        
        PlotStyle plotStyle3 = new PlotStyle();        
        plotStyle3.setStyle(Style.LINESPOINTS);
        plotStyle3.setPointType(15);
        plotStyle3.setPointSize(1);
        
        PlotStyle plotStyle4 = new PlotStyle();        
        plotStyle4.setStyle(Style.LINESPOINTS);
        plotStyle4.setPointType(17);
        plotStyle4.setPointSize(1);
        
        DataSetPlot s1 = new DataSetPlot(levenshtein);
        s1.setPlotStyle(plotStyle);
        s1.setTitle("Distancia de Levenshtein");
        
        DataSetPlot s2 = new DataSetPlot(jaroWinkler);
        s2.setPlotStyle(plotStyle2);
        s2.setTitle("Distancia de Jaro WInler");
        
        DataSetPlot s3 = new DataSetPlot(similarText);
        s3.setPlotStyle(plotStyle3);
        s3.setTitle("Algoritmo similar_text propuesto por Oliver");
        
        DataSetPlot s4 = new DataSetPlot(proposed);
        s4.setPlotStyle(plotStyle4);
        s4.setTitle("Algoritmo propuesto");
                        
        p.addPlot(s1);
        /*p.addPlot(s2);
        p.addPlot(s3);
        p.addPlot(s4);*/
        p.plot();
        
    }

}
