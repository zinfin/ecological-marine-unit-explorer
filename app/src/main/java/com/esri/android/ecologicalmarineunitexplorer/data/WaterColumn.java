package com.esri.android.ecologicalmarineunitexplorer.data;
/* Copyright 2016 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For additional information, contact:
 * Environmental Systems Research Institute, Inc.
 * Attn: Contracts Dept
 * 380 New York Street
 * Redlands, California, USA 92373
 *
 * email: contracts@esri.com
 *
 */

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class WaterColumn {
  @NonNull private Set<EMUObservation> emuSet;

  public WaterColumn(){
    emuSet = new TreeSet<>();
  }
  @NonNull public Set<EMUObservation> getEmuSet() {
    return emuSet;
  }

  public void setEmuSet(@NonNull Set<EMUObservation> emuSet) {
    this.emuSet = emuSet;
  }

  public void addObservation(EMUObservation observation){
    emuSet.add(observation);
  }
  public int emuCount(){
    return emuSet.size();
  }

  public int getDepth(){
    int depth = 0;
    Iterator<EMUObservation> iter = emuSet.iterator();
    while (iter.hasNext()){
      EMUObservation observation = iter.next();
      depth = depth + observation.getThickness();
    }
    return depth;
  }

  @Override public String toString() {
    return "WaterColumn{" +
        "emuSet=" + emuSet +
        '}';
  }
}