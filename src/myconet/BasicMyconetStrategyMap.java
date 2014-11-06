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

public class BasicMyconetStrategyMap extends DynamicsStrategyMap {
  public BasicMyconetStrategyMap() {
    this.put(HyphaType.BIOMASS, new BasicBiomassStrategy());
    this.put(HyphaType.EXTENDING, new BasicExtendingStrategy());
    this.put(HyphaType.BRANCHING, new BasicBranchingStrategy());
    this.put(HyphaType.IMMOBILE, new BasicImmobileStrategy());
    this.put(HyphaType.BULWARK, new BasicBulwarkStrategy());
  }
}
