package com.esri.android.ecologicalmarineunitexplorer.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.esri.android.ecologicalmarineunitexplorer.R;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.*;

import java.util.Calendar;

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

public class MapFragment extends Fragment {

  private GraphicsOverlay mGraphicOverlay;

  private MapView mMapView;

  public MapFragment(){}

  public static MapFragment newInstance(){
    return new MapFragment();
  }

  @Override
  public final void onCreate(@NonNull final Bundle savedInstance) {

    super.onCreate(savedInstance);
    // retain this fragment
    setRetainInstance(true);
  }

  @Override
  @Nullable
  public final View onCreateView(final LayoutInflater layoutInflater, final ViewGroup container,
      final Bundle savedInstance){

    final View root = layoutInflater.inflate(R.layout.map_view, container,false);

    setUpMap(root);

    setUpToolbar();
    return root;
  }

  private final void setUpMap(View root){

    // Show a dialog while the map loads
    final ProgressDialog progressDialog = ProgressDialog
        .show(this.getActivity(), null, "Loading map data...", true);

    mMapView = (MapView) root.findViewById(R.id.map);

    // Create a map using Ocean basemap and
    // zoom to Galapagos Islands
    ArcGISMap map = new ArcGISMap(Basemap.Type.OCEANS,-0.3838312, -91.5727346, 4  );

    mMapView.setMap(map);

    // Add layers that need to be visible in the map
    // EMU OCean Surface
    ArcGISTiledLayer tiledLayerBaseMap = new ArcGISTiledLayer(getString(R.string.emu_ocean_surface_layer));
    map.getOperationalLayers().add(tiledLayerBaseMap);

    final MapTouchListener mapTouchListener = new MapTouchListener(getActivity().getApplicationContext(), mMapView);

    // Once map has loaded, remove the progress dialog
    // and enable touch listener
    mMapView.addDrawStatusChangedListener(new DrawStatusChangedListener() {
      @Override public void drawStatusChanged(DrawStatusChangedEvent drawStatusChangedEvent) {
        if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.COMPLETED) {
          mMapView.removeDrawStatusChangedListener(this);
          progressDialog.dismiss();
          mMapView.setOnTouchListener(mapTouchListener);
        }
      }
    });
  }

  private void setUpToolbar() {
    final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Explore An Ocean Location");
  }

  /**
   * Obtain the geo location for a given point
   * on the screen
   */
  private Point getScreenToLocation(android.graphics.Point mapPoint){
    return mMapView.screenToLocation(mapPoint);
  }

  private class MapTouchListener extends DefaultMapViewOnTouchListener {
    /**
     * Instantiates a new DrawingMapViewOnTouchListener with the specified
     * context and MapView.
     *
     * @param context the application context from which to get the display
     *                metrics
     * @param mapView the MapView on which to control touch events
     */
    public MapTouchListener(Context context, MapView mapView) {
      super(context, mapView);
    }
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {

      android.graphics.Point mapPoint = new android.graphics.Point((int) motionEvent.getX(),
          (int) motionEvent.getY());

      return true;
    }
  }
}
