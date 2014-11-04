package RASupport.tests;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.DataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.PostscriptTerminal;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportDynamicAttributes;
import RASupport.rasupport.rasupportconfig.resourcesmodel.RASupportStaticAttributes;

/**
 *
 * @author damianarellanes
 */
public class UpdatingBenchmark {   
    
    private static Map patterns = new HashMap();
    static {
        for(RASupportDynamicAttributes a: RASupportDynamicAttributes.values()) {
            Pattern p = Pattern.compile("<" + a + ">(.*)" + "</" + a + ">");
            patterns.put(a, p);
        }
        
        for(RASupportStaticAttributes a: RASupportStaticAttributes.values()) {
            Pattern p = Pattern.compile("<" + a + ">(.*)" + "</" + a + ">");
            patterns.put(a, p);
        }
    }
    
    public static String deserializeString(File file)
    throws IOException {
        int len;
        char[] chr = new char[4096];
        final StringBuffer buffer = new StringBuffer();
        final FileReader reader = new FileReader(file);
        try {
            while ((len = reader.read(chr)) > 0) {
                buffer.append(chr, 0, len);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }
    
    public static void performReplaceNonOptimal(Object tag, String toSearch, String newValue,
            long iterations) {
                
        for(int i = 0; i < iterations; i++) {
            String pattern = "<" + tag + ">(.*)" + "</" + tag + ">";        
            toSearch = toSearch.replaceFirst(pattern, newValue);            
        }
    }
    
    public static void performReplaceNonOptimal2(Object tag, String toSearch, String newValue, 
            long iterations) {
                
        for(int i = 0; i < iterations; i++) {            
            toSearch = Pattern.compile("<" + tag + ">(.*)" + "</" + tag + ">").matcher(toSearch).replaceFirst(newValue);
        }
    }
    
    public static void performReplaceOptimal(Object tag, String toSearch, String newValue, 
            Object attribute, long iterations) {
                
        for(int i = 0; i < iterations; i++) {            
           toSearch = ((Pattern)patterns.get(attribute)).matcher(toSearch).replaceFirst(newValue);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        String xml = UpdatingBenchmark.deserializeString(new File("updating.xml"));
        
        RASupportStaticAttributes xmlTag = RASupportStaticAttributes.cpu_speed;
        System.out.println(xml);                
        String newValue = "<" + xmlTag + ">1000.24" + "</" + xmlTag + ">";
        
        long time_start, time_end;
        int iterations = 100000;
        int jump = 10000;
        long[][] nonOptimal = new long[iterations/jump][];
        long[][] nonOptimal2 = new long[iterations/jump][];
        long[][] optimal = new long[iterations/jump][];
                
        for(int i= 0, count = 0; i < iterations; count++, i += jump) {
            time_start = System.currentTimeMillis();
            UpdatingBenchmark.performReplaceNonOptimal(xmlTag, xml, newValue, i);
            time_end = System.currentTimeMillis();            
            nonOptimal[count] = new long[]{i,time_end-time_start};            
            
            time_start = System.currentTimeMillis();
            UpdatingBenchmark.performReplaceNonOptimal2(xmlTag, xml, newValue, i);
            time_end = System.currentTimeMillis();            
            nonOptimal2[count] = new long[]{i,time_end-time_start};
            
            time_start = System.currentTimeMillis();
            UpdatingBenchmark.performReplaceOptimal(xmlTag, xml, newValue, xmlTag, i);
            time_end = System.currentTimeMillis();            
            optimal[count] = new long[]{i,time_end-time_start};
        }
        
        //PostscriptTerminal eps = new PostscriptTerminal("benchmark_replace.eps");
        
        JavaPlot p = new JavaPlot();                
        //p.setTerminal(eps);
        //p.setTitle("Rendimiento del algoritmo de actualización de recursos dinámicos"); 
        p.set("xlabel", "'Número de actualizaciones'");
        p.set("ylabel", "'Tiempo (ms)'");
        p.setKey(JavaPlot.Key.TOP_RIGHT);
        
        PlotStyle plotStyle = new PlotStyle();        
        plotStyle.setStyle(Style.LINES);
        plotStyle.setPointType(5);
        plotStyle.setPointSize(8);
        
        DataSetPlot s1 = new DataSetPlot(nonOptimal);
        s1.setPlotStyle(plotStyle);
        s1.setTitle("Algoritmo con patrones no pre-compilados");
        
        DataSetPlot s2 = new DataSetPlot(nonOptimal2);
        s2.setPlotStyle(plotStyle);
        s2.setTitle("Algoritmo con patrones pre-compilados (sin programación dinámica)");
        
        DataSetPlot s3 = new DataSetPlot(optimal);
        s3.setPlotStyle(plotStyle);
        s3.setTitle("Algoritmo con patrones pre-compilados (con programación dinámica)");
                        
        p.addPlot(s1);
        p.addPlot(s2);
        p.addPlot(s3);
        p.plot();
                
    }

}
