package com.esri.android.ecologicalmarineunitexplorer.map;


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
import android.util.Log;
import com.esri.android.ecologicalmarineunitexplorer.data.DataManager;
import com.esri.android.ecologicalmarineunitexplorer.data.ServiceApi;
import com.esri.android.ecologicalmarineunitexplorer.data.WaterColumn;
import com.esri.arcgisruntime.geometry.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapPresenter implements MapContract.Presenter {

  private  MapContract.View mMapView;
  private DataManager mDataManager;

  public MapPresenter(@NonNull final MapContract.View mapView ){
    mMapView = checkNotNull(mapView, "map view cannot be null");
    mMapView.setPresenter(this);

  }
  @Override public void start() {
    checkNotNull(mMapView.getContext().getApplicationContext());
    mDataManager = new DataManager(mMapView.getContext().getApplicationContext());
  }

  @Override public void setSelectedPoint(Point point) {
    Log.i("MapPresenter", "Selcted point coordinates = x= " + point.getX() + " y= " + point.getY());
    Polygon polygon = getBufferPolygonForPoint(point, 32000);
    PolygonBuilder builder = new PolygonBuilder(polygon);
    Envelope envelope = builder.getExtent();
    mMapView.showSelectedRegion(polygon);
    mDataManager.queryForEmuAtLocation(envelope, new ServiceApi.SummaryCallback() {
      @Override public void onWaterColumnsLoaded(WaterColumn column) {
        WaterColumn waterColumn =   column;
        if (waterColumn == null){
          mMapView.showDataNotFound();
        }
      }
    });
  }

  @Override public Polygon getBufferPolygonForPoint(Point point, double distance) {
    return GeometryEngine.buffer(point, distance);

  }
}
