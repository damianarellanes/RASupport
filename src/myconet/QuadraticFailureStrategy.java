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


package myconet;

import peersim.config.*;

public class QuadraticFailureStrategy implements FailureStrategy {
  private static final String PREFIX = "config.quadraticfailure.";
  private static final String PAR_SQ = PREFIX + "sq";
  private static final String PAR_LI = PREFIX + "li";
  private static final String PAR_CO = PREFIX + "co";

  private double sq;
  private double li;
  private double co;

  public QuadraticFailureStrategy() {
    sq = Configuration.getDouble(PAR_SQ);
    li = Configuration.getDouble(PAR_LI);
    co = Configuration.getDouble(PAR_CO);
  }

  public Double apply(MycoNode thisNode, HyphaType thisType,
                      int thisDegree, MycoNode failedNode,
                      HyphaType failedType, int failedDegree) {
    double ret = (sq * failedDegree * failedDegree) +
        (li * failedDegree) + co;
    return (ret >= 0.0 ? ret : 0.0);
  }
}
