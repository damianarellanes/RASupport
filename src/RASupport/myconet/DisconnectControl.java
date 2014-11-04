/* Copyright (c) 2014, Paul L. Snyder <paul@pataprogramming.com>,
 * Daniel Dubois, Nicolo Calcavecchia.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * Any later version. It may also be redistributed and/or modified under the
 * terms of the BSD 3-Clause License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */


package RASupport.myconet;

import peersim.config.*;
import peersim.core.*;
import peersim.util.*;
import java.lang.Math;
import java.util.logging.*;
import java.util.Map;
import java.util.HashMap;

import cern.jet.stat.Descriptive;
import cern.colt.list.DoubleArrayList;
import org.apache.commons.math3.stat.Frequency;

public class DisconnectControl implements Control {
  //private static final String PAR_PROTO = "protocol";

  private static Logger log =
      Logger.getLogger(DisconnectControl.class.getName());

  private static final String PAR_PARENT_STRATEGY = "parent_strategy";
  private static Class parentClass;
  private static ParentStrategy parentStrategy;
  private final String name;
  //private final int pid;

  public static int disconnectCount = 0;
  public static int enteringCount = 0;
  public static Frequency knownFailures;
  public static Frequency preDiffusion;

  // NOTE: Must run AFTER the JungGraph is created.
  // NOTE: Should also run after Nodulators
  public DisconnectControl(String name) {
    this.name = name;
    //this.pid = Configuration.getPid(name + "." + PAR_PROTO);

    try {
      parentClass = Configuration.getClass(name + "." + PAR_PARENT_STRATEGY);
      if (ParentStrategy.class.isAssignableFrom(parentClass))
      {
        parentStrategy = (ParentStrategy) parentClass.newInstance();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    ExperimentWriter.addMetric(new Metric<Integer>("disconnectCount") {public Integer fetch() { return DisconnectControl.disconnectCount; }});
    ExperimentWriter.addMetric(new Metric<Integer>("enteringCount") {public Integer fetch() { return DisconnectControl.enteringCount; }});
  }

  @SuppressWarnings("unchecked")
  public boolean execute() {
    MycoGraph g = JungGraphObserver.getGraph();

    disconnectCount = 0;
    enteringCount = 0;

    knownFailures = new Frequency();
    preDiffusion = new Frequency();


    HashMap<MycoNode,Double> oldValues = new HashMap<MycoNode,Double>();
    HashMap<MycoNode,Double> newValues = new HashMap<MycoNode,Double>();
    for (MycoNode n : g.getVertices()) {
      newValues.put(n, n.getHyphaData().getKnownDisconnect());
      preDiffusion.addValue(n.getHyphaData().getKnownDisconnect());
    }

    // Diffuse knownDisconnect info (generated by listeners during nodulation)
    int loops = 2;
    for (int i = 0; i < loops; i++) {
      oldValues = new HashMap(newValues);
      for (MycoNode n : g.getVertices()) {
        double myKnown = oldValues.get(n);
        for (MycoNode m : n.getHyphaLink().getNeighbors()) {
          newValues.put(m, Math.max(myKnown, oldValues.get(m)));
          //m.getHyphaData().recordKnownDisconnect(myKnown);
        }
      }
    }

    for (MycoNode n : g.getVertices()) {
      HyphaData data = n.getHyphaData();
      int deg = n.getHyphaLink().neighborCount();
      //int deg = g.outDegree(n);

      data.setKnownDisconnect(newValues.get(n));
      knownFailures.addValue(data.getKnownDisconnect());

      if (deg == 0 && !data.isEnteringNode()) {
        data.setDisconnectDetected();
        disconnectCount += 1;
      } else if (data.isEnteringNode()) {
        enteringCount += 1;
      } else {
        data.clearDisconnectDetected();
      }

      parentStrategy.apply(n);
      data.clearKnownDisconnect();

    }

    // System.out.println("PRE-DIFFUSION DISTRIB: \n" +
    //                    preDiffusion.toString());
    // System.out.println("KNOWN FAILURE DISTRIB: \n" +
    //                    knownFailures.toString());

    log.info("DISCONNECT COUNT: " + disconnectCount);
    log.info("ENTERING COUNT: " + enteringCount);
    return false;
  }
}
