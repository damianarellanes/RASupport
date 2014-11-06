package simulation;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 * RASupport: class that represents an experiment for RASupport
 * @author Damian Arellanes
 */
public class AdvertisementObserver implements Control {
    
    private volatile static int initialAgents;
    private volatile static int updatingAgents;
    private int[][] pointsInitial = null;
    private int[][] pointsUpdating = null;
    
    private static final String PAR_PROTO = "protocol";    
    private static final String PAR_CYCLES = "cycles";    
    private final String name;
    private final int pid;
    private final int cycles;
    
    public AdvertisementObserver(String name) {
        this.name = name;
        this.pid = Configuration.getPid(name + "." + PAR_PROTO);    
        this.cycles = Configuration.getInt(name + "." + PAR_CYCLES);
        
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
            plotStyle.setStyle(Style.LINES);
            plotStyle.setPointType(5);
            plotStyle.setPointSize(8);
            
            DataSetPlot s1 = new DataSetPlot(pointsInitial);
            s1.setPlotStyle(plotStyle);
            s1.setTitle("Agentes de anunciamiento inicial");

            DataSetPlot s2 = new DataSetPlot(pointsUpdating);
            s2.setPlotStyle(plotStyle);
            s2.setTitle("Agentes de anunciamiento de actualización");
            
            p.addPlot(s1);            
            p.addPlot(s2);
            p.plot();
        }
        
        return false;
    }

}
