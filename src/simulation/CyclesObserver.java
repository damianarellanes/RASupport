package simulation;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 * RASupport: class that represents an experiment for RASupport
 * @author Damian Arellanes
 */
public class CyclesObserver implements Control {
    
    private volatile static int initialAgents;
    private volatile static int updatingAgents;
    private int[][] pointsInitial = null;
    private int[][] pointsUpdating = null;
    
    private static final String PAR_PROTO = "protocol";    
    private static final String PAR_CYCLES = "cycles";    
    private static final String PAR_FILERESULTS = "fileresults";
    private final String name;
    private final int pid;
    private final int cycles;
    private String fileresults = "";
    
    public CyclesObserver(String name) {
        this.name = name;
        this.pid = Configuration.getPid(name + "." + PAR_PROTO);    
        this.cycles = Configuration.getInt(name + "." + PAR_CYCLES);
        this.fileresults = Configuration.getString(name + "." + PAR_FILERESULTS);
        
        initialAgents = updatingAgents = 0;
        pointsInitial = new int[cycles][];
        pointsUpdating = new int[cycles][];
    }
    
    public synchronized static void incInitialAgentsCount() {
        initialAgents++;
    }
    
    public synchronized static void incUpdatingAgentsCount() {
        updatingAgents++;
    }

    @Override
    public boolean execute() { 
        
        int currentCycle = CDState.getCycle();
        
        System.err.println("Initial agents count: " + initialAgents);
        System.err.println("Updating agents count: " + updatingAgents);
        
        pointsInitial[currentCycle] = new int[]{currentCycle, initialAgents};
        pointsUpdating[currentCycle] = new int[]{currentCycle, updatingAgents};
        initialAgents = updatingAgents = 0;
        
        if(currentCycle == cycles-1) { 
            System.err.println("SIMULATION FINISHED!");
            
            JavaPlot p = new JavaPlot();                                        
            p.set("xlabel", "'Ciclos'");
            p.set("ylabel", "'Número de agentes'");
            p.setKey(JavaPlot.Key.TOP_RIGHT);
            
            PlotStyle plotStyle = new PlotStyle();        
            plotStyle.setStyle(Style.LINESPOINTS);
            plotStyle.setPointType(4);
            plotStyle.setPointSize(1);
            
            PlotStyle plotStyle2 = new PlotStyle();        
            plotStyle2.setStyle(Style.LINESPOINTS);
            plotStyle2.setPointType(9);
            plotStyle2.setPointSize(1);
            
            DataSetPlot s1 = new DataSetPlot(pointsInitial);
            s1.setPlotStyle(plotStyle);
            s1.setTitle("Agentes de anunciamiento inicial");

            DataSetPlot s2 = new DataSetPlot(pointsUpdating);
            s2.setPlotStyle(plotStyle2);
            s2.setTitle("Agentes de anunciamiento de actualización");
            
            p.addPlot(s1);            
            p.addPlot(s2);
            p.plot();
            
            String path = "/home/damianarellanes/Documentos/CINVESTAV/Tesis/Soporte P2P para la colaboración de recursos/Tesis/Disertación/Figures/Resultados/Anunciamiento/" +
                    fileresults;
            File file = new File(path);            
            FileWriter fw = null;
            BufferedWriter bw = null;
            
            try {
                
                if (!file.exists()) {
                    file.createNewFile();
                }
                
                StringBuilder result = new StringBuilder();                
                fw = new FileWriter(file.getAbsoluteFile());                                        
                bw = new BufferedWriter(fw);
                
                System.err.println("RESULT: ");                    
                for(int i = 0; i < cycles; i++) {
                    bw.append(pointsUpdating[i][0] + "\t\t" + pointsUpdating[i][1] +  "\n");
                }

                bw.write(result.toString());
                bw.close();
                fw.close();
                
            } catch (IOException ex) {            
                Logger.getLogger(CyclesObserver.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(CyclesObserver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return false;
    }

}
