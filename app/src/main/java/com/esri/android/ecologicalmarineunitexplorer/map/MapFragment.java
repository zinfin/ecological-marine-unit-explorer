package com.esri.android.ecologicalmarineunitexplorer.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.Toast;
import com.esri.android.ecologicalmarineunitexplorer.R;
import com.esri.android.ecologicalmarineunitexplorer.data.WaterColumn;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;

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

public class MapFragment extends Fragment implements MapContract.View {

  private GraphicsOverlay mGraphicOverlay;

  private MapView mMapView;

  private MapContract.Presenter mPresenter;



  public MapFragment(){}

  public static MapFragment newInstance(){
    return new MapFragment();
  }

  @Override
  public final void onCreate(@NonNull final Bundle savedInstance) {

    super.onCreate(savedInstance);
    // retain this fragment
    setRetainInstance(true);
    mPresenter.start();
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

    // Create and add layers that need to be visible in the map
    mGraphicOverlay  = new GraphicsOverlay();
    mMapView.getGraphicsOverlays().add(mGraphicOverlay);

    // EMU Ocean Surface
    ArcGISTiledLayer tiledLayerBaseMap = new ArcGISTiledLayer(getString(R.string.emu_ocean_surface_layer));
    map.getOperationalLayers().add(tiledLayerBaseMap);

    final MapTouchListener mapTouchListener = new MapTouchListener(getActivity().getApplicationContext(), mMapView);

    // Once map has loaded, remove the progress dialog
    // and enable touch listener
    mMapView.addDrawStatusChangedListener(new DrawStatusChangedListener() {
      @Override public void drawStatusChanged(DrawStatusChangedEvent drawStatusChangedEvent) {
        if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.COMPLETED) {
          // Stop listening to any more draw status changes
          mMapView.removeDrawStatusChangedListener(this);
          // Start listening to touch interactions on the map
          mMapView.setOnTouchListener(mapTouchListener);
          progressDialog.dismiss();
        }
      }
    });
  }

  /**
   * Override the application label used for the toolbar title
   */
  private void setUpToolbar() {
    final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Explore An Ocean Location");
  }

  @Override
  public final void onResume(){
    super.onResume();
    mMapView.resume();
  }

  @Override
  public final void onPause() {
    super.onPause();
    mMapView.pause();
  }
  /**
   * Obtain the geo location for a given point
   * on the screen
   */
  public Point getScreenToLocation(android.graphics.Point mapPoint){
    return mMapView.screenToLocation(mapPoint);
  }

  @Override public void setPresenter(MapContract.Presenter presenter) {
      mPresenter = presenter;
  }
  public void showSelectedRegion(Polygon polygon){
    SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3);
    SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.NULL, Color.BLUE,lineSymbol);
    mGraphicOverlay.getGraphics().clear();
    mGraphicOverlay.getGraphics().add( new Graphic(polygon, fillSymbol));
  }

  @Override public void showDataNotFound() {
    Toast.makeText(getActivity(), R.string.no_emu_found, Toast.LENGTH_SHORT).show();
  }

  @Override public void showSummary(WaterColumn column) {
    ((MapActivity) getActivity()).showSummary(column);
  }

  public class MapTouchListener extends DefaultMapViewOnTouchListener {
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
      Log.i("ScreenPoints", "x = " + motionEvent.getX() + " y = "+ motionEvent.getY());
      android.graphics.Point mapPoint = new android.graphics.Point((int) motionEvent.getX(),
          (int) motionEvent.getY());
      mPresenter.setSelectedPoint(getScreenToLocation(mapPoint));
      return true;
    }
  }
}
