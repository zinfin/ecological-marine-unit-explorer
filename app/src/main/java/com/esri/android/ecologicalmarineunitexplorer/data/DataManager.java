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

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import com.esri.android.ecologicalmarineunitexplorer.R;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.datasource.Feature;
import com.esri.arcgisruntime.datasource.FeatureQueryResult;
import com.esri.arcgisruntime.datasource.QueryParameters;
import com.esri.arcgisruntime.datasource.arcgis.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;

import java.util.Iterator;
import java.util.Map;

public class DataManager {
  private ServiceFeatureTable mMeshClusterTable;

  private ServiceFeatureTable mMeshPointTable;

  private ServiceFeatureTable mClusterPolygonTable;

  private Context mContext;

  public DataManager(Context applicationContext){

    mContext = applicationContext;
    mClusterPolygonTable = new ServiceFeatureTable(mContext.getString(R.string.service_emu_polygon));

    mMeshClusterTable = new ServiceFeatureTable(mContext.getString(R.string.service_emu_mesh_cluster));

    mMeshPointTable = new ServiceFeatureTable(mContext.getString(R.string.service_emu_point_mesh)
    );
  }

  /**
   * Query for water column data at the given geometry
   * @param envelope - represents a buffered geometry around selected point in map
   * @return a WaterColumn at the location within the geometry
   */
  public void queryForEmuAtLocation(Envelope envelope){
    QueryParameters queryParameters = new QueryParameters();
    queryParameters.setGeometry(envelope);
    ListenableFuture<FeatureQueryResult> futureResult = mMeshClusterTable.queryFeaturesAsync(queryParameters, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
    processQueryForEMuAtLocation(futureResult);
  }

  /**
   * Process the listenable future by creating a WaterColumn for
   * any returned data.
   * @param futureResult - a ListenableFuture<FeatureQueryResult> to process
   * @return - a WaterColumn representing returned features.
   */
  private void processQueryForEMuAtLocation(final ListenableFuture<FeatureQueryResult> futureResult){
    final WaterColumn waterColumn =  new WaterColumn();
    futureResult.addDoneListener(new Runnable() {
      @Override public void run() {
        try{
          Log.i("ProcessQuery", "Query done...");
          FeatureQueryResult fqr = futureResult.get();
          if (fqr != null){
            Log.i("ProcessQuery", "Processing features...");
            int x = 0;
            final Iterator<Feature> iterator = fqr.iterator();
            while (iterator.hasNext()){
              Feature feature = iterator.next();
              x+=1;
              Map<String,Object> map = feature.getAttributes();
              waterColumn.addObservation(createEMUObservation(map));
            }
            Log.i("ProcessingQuery", "Processing complete");
          }
          if (waterColumn != null){
            
            Log.i("WaterColumn", "Water column EMU count = " + waterColumn.emuCount()+ " water column depth = "+ waterColumn.getDepth()+ " meters.");
          }
        }catch (Exception e){
            e.printStackTrace();
        }
      }
    });
  }

  /**
   * Given a Map containing strings as keys and objects as values,
   * create an EMUObservation
   * @param map Map<String,Object> representing field values indexed by field names
   * @return an EMUObservation for map.
   */
  private EMUObservation createEMUObservation(Map<String,Object> map){
//    for (String key : map.keySet()){
//      String v = map.get(key) != null ? map.get(key).toString() : "";
//      Log.i("Key", "Key = " + key + " value = " + v);
//    }
    EMUObservation observation = new EMUObservation();

    EMU emu = new EMU();
    observation.setEmu(emu);

    // Set emu number
    Integer emuNumber = Integer.parseInt( extractValueFromMap(mContext.getString(R.string.emu_number),map));
    emu.setName(emuNumber);

    // Set descriptive name
    String emuName = extractValueFromMap(mContext.getString(R.string.name_emu),map);

    // Get the physical and nutrient summaries which are concatenated in the emuName
    String [] results = emuName.split(" with ");
    if (results.length == 2){
      emu.setPhysicalSummary(results[0]);
      emu.setNutrientSummary(results[1]);
    }else{
      emu.setPhysicalSummary("not found");
      emu.setNutrientSummary("not found");
    }

    // Set geomorphology base for emu
    String geoBase = extractValueFromMap(mContext.getString(R.string.geo_base),map);
    emu.setGeomorphologyBase(geoBase);

    // Set geomorphology features
    String geoFeatures = extractValueFromMap(mContext.getString(R.string.geo_features),map);
    emu.setGeomorphologyFeatures(geoFeatures);

    // Set the top
    observation.setTop(Integer.parseInt(map.get("UnitTop").toString()));

    // Set the geoLocation
    double x = Double.parseDouble(extractValueFromMap(mContext.getString(R.string.point_x),map));
    double y = Double.parseDouble(extractValueFromMap(mContext.getString(R.string.point_y),map));
    Point point = new Point(x,y);
    observation.setLocation(point);

    // Set the thickness
    observation.setThickness(Integer.parseInt(extractValueFromMap(mContext.getString(R.string.thickness),map)));

    Log.i("Observation", "observation: " + observation.toString());
    return observation;
  }

  private String extractValueFromMap(@NonNull String columnName, @NonNull Map<String,Object> map){
    String value = "";
    if (map.containsKey(columnName) && map.get(columnName) != null){
      value = map.get(columnName).toString();
    }
    return value;
  }
}
