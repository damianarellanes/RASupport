package RASupport.tests;

import RASupport.rasupport.rasupportconfig.modules.RASupportTopologyNode;
import RASupport.rasupport.rasupportconfig.queries.RASupportQuery;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryGroup;
import RASupport.rasupport.rasupportconfig.queries.RASupportQueryRequirement;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportAttributesInterface;
import RASupport.rasupport.rasupportconfig.xml.XMLQueryReader;
import RASupport.rasupport.rasupportmain.RASupportMain;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import java.io.File;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author damianarellanes
 */
public class SimilarityBenchmark {
    
    /******************************************************************************************/
    // Levenshtein distance algorithm                
    /******************************************************************************************/
    public static void levenshteinDistance(String grp1, String grp2, int iterations) {                
        for(int i = 0; i< iterations; i++) {
                        
            double res = 100 - ((StringUtils.getLevenshteinDistance(grp1, grp2)) / (Math.max(grp1.length(), grp2.length())) * 100);            
            //System.out.println(res);            
        }
    }
    
    /******************************************************************************************/
    // Jaro Winkler distance algorithm                
    /******************************************************************************************/        
    public static void jaroWinklerDistance(String grp1, String grp2, int iterations) {
        for(int i = 0; i< iterations; i++) {
            
            //System.out.println(StringUtils.getJaroWinklerDistance(groupStr1, groupStr2) * 100);
            StringUtils.getJaroWinklerDistance(grp1, grp2);            
        }
    }     
    
    /******************************************************************************************/
    // similar_text algorithm from Programming Classics: Implementing the World's Best Algorithms by Oliver
    /******************************************************************************************/
    public static void similarText(String grp1, String grp2, int iterations) {
        for(int i = 0; i< iterations; i++) {
            
            similar_text(grp1, grp2);
            //System.out.println(similar_text(groupStr1, groupStr2));                        
        } 
    }
    
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
    
    /******************************************************************************************/
    // Proposed algorithm
    /******************************************************************************************/
    public static void proposedAlgorithm(RASupportQueryGroup grp1, RASupportQueryGroup grp2, int iterations) {
        for(int i = 0; i< iterations; i++) {
            
            //System.out.println(getSimilarity(group1, group2));
            getSimilarity(grp1, grp2);         
        }
        
    }
    
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        long time_start, time_end;
        int iterations = 1000;
        int jump = 100;
        long[][] levenshtein = new long[iterations/jump][];
        long[][] jaroWinkler = new long[iterations/jump][];
        long[][] similarText = new long[iterations/jump][];
        long[][] proposed = new long[iterations/jump][];
        
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
        
        RASupportQueryGroup group1 = query.getGroups().get("Processing");
        RASupportQueryGroup group2 = query2.getGroups().get("Processing");        
        String grp1 = group1.getContent();
        String grp2 = group2.getContent();
        
        System.out.println("GROUP 1: " + grp1);
        System.out.println("GROUP 2: " + grp2);
        
        // Iteraciones vs tiempo
        /*for(int i= 0, count = 0; i < iterations; count++, i += jump) {
            
            time_start = System.currentTimeMillis();
            levenshteinDistance(grp1, grp2, i);
            time_end = System.currentTimeMillis();            
            levenshtein[count] = new long[]{i,time_end-time_start};            
            
            time_start = System.currentTimeMillis();
            jaroWinklerDistance(grp1, grp2, i);
            time_end = System.currentTimeMillis();            
            jaroWinkler[count] = new long[]{i,time_end-time_start};
            
            time_start = System.currentTimeMillis();
            similarText(grp1, grp2, i);
            time_end = System.currentTimeMillis();            
            similarText[count] = new long[]{i,time_end-time_start}; 
            
            time_start = System.currentTimeMillis();
            proposedAlgorithm(group1, group2, i);
            time_end = System.currentTimeMillis();            
            proposed[count] = new long[]{i,time_end-time_start}; 
            
        }*/
        
        // Algoritmos vs porcentaje
        
        System.out.println("Levenshtein = " + (100 - ((StringUtils.getLevenshteinDistance(grp1, grp2)) 
                    / (Math.max(grp1.length(), grp2.length())) * 100)));
        System.out.println("Jaro-Winkler = " + StringUtils.getJaroWinklerDistance(grp1, grp2) * 100);        
        System.out.println("similar_text = " + similar_text(grp1, grp2));        
        System.out.println("Porposed = " + getSimilarity(group1, group2));
        
        /*JavaPlot p = new JavaPlot();                
        //p.setTerminal(eps);
        //p.setTitle("Rendimiento del algoritmo de actualización de recursos dinámicos"); 
        p.set("xlabel", "'Iteraciones'");
        p.set("ylabel", "'Tiempo (ms)'");
        p.set("xrange", "[0:500]");
        p.set("yrange", "[0:200]");
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
        plotStyle4.setPointType(18);
        plotStyle4.setPointSize(1);
        
        DataSetPlot s1 = new DataSetPlot(levenshtein);
        s1.setPlotStyle(plotStyle);
        s1.setTitle("Distancia de Levenshtein");
        
        DataSetPlot s2 = new DataSetPlot(jaroWinkler);
        s2.setPlotStyle(plotStyle2);
        s2.setTitle("Distancia de Jaro-Winkler");
        
        DataSetPlot s3 = new DataSetPlot(similarText);
        s3.setPlotStyle(plotStyle3);
        s3.setTitle("Algoritmo de Ian Oliver");
        
        DataSetPlot s4 = new DataSetPlot(proposed);
        s4.setPlotStyle(plotStyle4);
        s4.setTitle("Algoritmo propuesto por Damian Arellanes");
                        
        p.addPlot(s1);
        p.addPlot(s2);
        p.addPlot(s3);
        p.addPlot(s4);
        p.plot();*/
    }

}
