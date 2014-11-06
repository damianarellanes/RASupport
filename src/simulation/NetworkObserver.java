package simulation;

import cern.jet.random.Poisson;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import java.util.Collections;
import java.util.logging.*;
import myconet.MycoCast;
import myconet.MycoList;
import myconet.MycoNode;
import myconet.MycoNodeDegreeComparator;
import myconet.Nodulator;

import peersim.cdsim.*;
import peersim.config.Configuration;
import peersim.core.*;
import peersim.dynamics.*;

/**
 *
 * @author Damian Arellanes
 */
public class NetworkObserver extends DynamicNetwork {
  
  private static final String PAR_FROM = "from";
  private static final String PAR_TO = "to";
  private static final String PAR_TYPE = "type";
  private static final String PAR_PERIOD = "period";
  private static final String PAR_POISSON = "poisson";
  protected final double from;
  protected final double to;
  protected final int period;
  protected final String type;
  protected final boolean poisson;

  protected final MycoNodeDegreeComparator degreeComparator =
      new MycoNodeDegreeComparator();

  private static Logger log = Logger.getLogger(NetworkObserver.class.getName());

  protected static Poisson gen;
  protected MycoList candidates;
  
  private static final String PAR_JUMP = "jump";
  private static final String PAR_MAXSIZE = "maxsize";
  private static final String PAR_MAXCYCLE = "maxcycle";
  private static final String PAR_TOTALCYCLES = "totalcycles"; 
  protected final int jump;  
  protected final int maxsize;  
  protected final int maxcycle; 
  private int currentSize;
  private int totalCycles;
  
  // To count the number of advertisement agents
  private volatile static int initialAgents;
  private volatile static int updatingAgents;
  private int[][] pointsInitial = null;
  private int[][] pointsUpdating = null;
  private int networkSizeCounter = 0;

  public NetworkObserver(String prefix) {
    super(prefix);
    from = Configuration.getDouble(prefix + "." + PAR_FROM);
    to = Configuration.getDouble(prefix + "." + PAR_TO);
    type = Configuration.getString(prefix + "." + PAR_TYPE);
    period = Configuration.getInt(prefix + "." + PAR_PERIOD);
    poisson = Configuration.getBoolean(prefix + "." + PAR_POISSON);
        
    jump = Configuration.getInt(prefix + "." + PAR_JUMP);    
    maxsize = Configuration.getInt(prefix + "." + PAR_MAXSIZE);
    maxcycle = Configuration.getInt(prefix + "." + PAR_MAXCYCLE);
    currentSize = Network.size();
    totalCycles = Configuration.getInt(prefix + "." + PAR_TOTALCYCLES);

    if (poisson) {
      if (gen == null) {
        gen = new Poisson(1.0, new cern.jet.random.engine.MersenneTwister(CommonState.r.nextInt(Integer.MAX_VALUE)));
      }
    }

    log.info("Initialized with params (from=" +Double.toString(from) + ",to=" +
             Double.toString(to) + ",add=" + Double.toString(add) + ",min=" +
             Double.toString(minsize) + ",max=" + Double.toString(maxsize) +
             ",subst=" + Boolean.toString(substitute) + ",period=" +
             Integer.toString(period) + ",poisson=" +
             Boolean.toString(poisson) +")");
    
    // Initializes variables to count advertisement agents
    initialAgents = updatingAgents = 0;
    pointsInitial = new int[totalCycles/maxcycle][];
    pointsUpdating = new int[totalCycles/maxcycle][];
  }
  
    public synchronized static void incInitialAgentsCount() {
        initialAgents++;
    }

    public synchronized static void incUpdatingAgentsCount() {
        updatingAgents++;
    }
    
    private void plot(int currentCycle) {
        if(currentCycle == totalCycles-1) { 
        System.err.println("SIMULATION FINISHED!");

        JavaPlot p = new JavaPlot();                                        
        p.set("xlabel", "'Tamaño de la red'");
        p.set("ylabel", "'Número de agentes de anunciamiento'");
        p.setKey(JavaPlot.Key.TOP_RIGHT);
        p.set("mxtics", "10");
        p.set("mytics", "10");

        PlotStyle plotStyle = new PlotStyle();        
        //plotStyle.setStyle(Style.LINES);
        plotStyle.setStyle(Style.LINESPOINTS);
        plotStyle.setPointType(4);
        plotStyle.setPointSize(1);

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
    }

  public boolean execute() {
      
    int currentCycle = CDState.getCycle();
          
     
    if ((currentCycle % maxcycle) == 0 || (currentCycle == totalCycles -1)) {
        
        System.err.println("INITIAL Plot(network, agents)=" + currentSize + "," + initialAgents);
        System.err.println("UPDATING Plot(network, agents)=" + currentSize + "," + updatingAgents);
        
        pointsInitial[networkSizeCounter] = new int[]{currentSize, initialAgents};
        pointsUpdating[networkSizeCounter] = new int[]{currentSize, updatingAgents};
                
        if((currentCycle != totalCycles -1)) {
            candidates = MycoCast.getAllNodes();
        this.remove(currentSize);        
        currentSize += jump;
        this.add(currentSize);                
        }        
        
        initialAgents = updatingAgents = 0;
        networkSizeCounter++;
    }
    
    plot(currentCycle);
        
      
    // NO churn because memory issues
    
    return false;
  }

  /**
   * Adds n nodes to the network. Extending classes can implement
   * any algorithm to do that. The default algorithm adds the given
   * number of nodes after calling all the configured initializers
   * on them.
   *
   * @param n
   *            the number of nodes to add, must be non-negative.
   */
  protected void add(int n) {
    System.out.println("Adding " + n + " nodes to the network");
    log.fine("Adding " + n + " nodes to the network");
    for (int i = 0; i < n; i++) {
      Node newnode = (Node) Network.prototype.clone();
      for (int j = 0; j < inits.length; ++j) {
        inits[j].initialize(newnode);
      }
      Network.add(newnode);
      log.finer("ADDED NEW NODE " + newnode.getID());
    }
  }

  // ------------------------------------------------------------------

  /**
   * Removes n nodes from the network. Extending classes can
   * implement any algorithm to do that. The default algorithm
   * removes <em>random</em> nodes <em>permanently</em> simply by
   * calling {@link Network#remove(int)}.
   *
   * @param n
   *            the number of nodes to remove
   */
  protected void remove(int n) {
    log.fine("Killing " + n + " " + type);
    System.out.println("Killing " + n + " " + type);

    for (int i = 0; i < n; i++) {
      // int pick = CommonState.r.nextInt(candidates.size());
      // log.finer("Picked " + candidates.get(pick) + " for death");
      int toKill = Network.findIndex(candidates.get(i));
      if (toKill >= 0) {
        log.finer(candidates.get(i) + " has index " + toKill);
        Network.remove(toKill);
        // log.finer("KILLED " + toKill.getID());
      } else {
        log.finer("Couldn't find index for " + candidates.get(i));
      }
      MycoCast.kill(candidates.get(i));
    }
    /*
     * for (int i = 0; i < n; ++i) { int toKill =
     * CommonState.r.nextInt(Network.size());
     *
     * Network.remove(toKill); }
     */
  }

  protected void replace(int n) {
    log.fine("Replacing " + n + " " + type);
    for (int i = 0; i < n; i++) {
      int toReplace = Network.findIndex(candidates.get(i));
      MycoNode node = (MycoNode) Network.get(toReplace);
      //Network.remove(toReplace);

      //for (MycoNode node : myNode.getHyphaLink().getNeighbors()) {
      //node.getHyphaLink().onKill();
      // node.getFailureAlerter().neighborFailed(myNode,
      //         myNode.getHyphaData().getState(),
      //         myNode.getHyphaLink().degree());
      // myNode.getHyphaLink().removeNeighbor(node);
      //}

      // Call onKill() for all protocols on the node to ensure proper operation
      System.out.println("Killing " + node);
      node.setFailState(Fallible.DEAD);
      for (int j = 0; j < inits.length; ++j) {
        inits[j].initialize(node);
      }
      node.setFailState(Fallible.OK);
      //node.getHyphaData().clean();
      //node.getHyphaData().becomeBiomass(node);
      //MycoCast.become(myNode, HyphaType.BIOMASS);
    }
  }

}
