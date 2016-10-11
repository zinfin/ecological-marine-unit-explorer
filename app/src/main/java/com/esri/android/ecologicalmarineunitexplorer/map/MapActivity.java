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


package com.esri.android.ecologicalmarineunitexplorer.map;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.esri.android.ecologicalmarineunitexplorer.R;
import com.esri.android.ecologicalmarineunitexplorer.data.EMUObservation;
import com.esri.android.ecologicalmarineunitexplorer.data.WaterColumn;
import com.esri.android.ecologicalmarineunitexplorer.summary.SummaryFragment;
import com.esri.android.ecologicalmarineunitexplorer.summary.SummaryPresenter;
import com.esri.android.ecologicalmarineunitexplorer.util.ActivityUtils;
import com.esri.android.ecologicalmarineunitexplorer.watercolumn.WaterColumnFragment;

import java.util.Set;

public class MapActivity extends AppCompatActivity {

  private MapPresenter mMapPresenter;
  private SummaryPresenter mSummaryPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    // Set up fragments
    setUpMagFragment();
  }

  /**
   * Configure the map fragment
   */
  private void setUpMagFragment(){
    final FragmentManager fm = getSupportFragmentManager();
    MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_container);

    if (mapFragment == null) {
      mapFragment = MapFragment.newInstance();
      mMapPresenter = new MapPresenter(mapFragment);
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), mapFragment, R.id.map_container, "map fragment");
    }

  }

  public void showSummary(WaterColumn waterColumn){
    final FragmentManager fm = getSupportFragmentManager();
    SummaryFragment summaryFragment = (SummaryFragment) fm.findFragmentById(R.id.summary_recycler_view) ;
    if (summaryFragment == null){
      summaryFragment = SummaryFragment.newInstance();
      mSummaryPresenter = new SummaryPresenter(summaryFragment);
    }
    mSummaryPresenter.setWaterColumn(waterColumn);

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    // Adjust the map's layout
    FrameLayout mapLayout = (FrameLayout) findViewById(R.id.map_container);
    mapLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,700));
    mapLayout.requestLayout();

    FrameLayout summaryLayout = (FrameLayout) findViewById(R.id.summary_container);
    summaryLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 900));
    summaryLayout.requestLayout();

    // Replace whatever is in the summary_container view with this fragment,
    // and add the transaction to the back stack so the user can navigate back
    transaction.replace(R.id.summary_container, summaryFragment);
    transaction.addToBackStack("summary fragment");

    // Commit the transaction
    transaction.commit();

    WaterColumnFragment waterColumnFragment = (WaterColumnFragment) fm.findFragmentById(R.id.water_column_linear_layout_top);
    if (waterColumnFragment == null){
      waterColumnFragment = WaterColumnFragment.newInstance();
    }
    waterColumnFragment.setWaterColumn(waterColumn);
    FragmentTransaction wcTransaction = getSupportFragmentManager().beginTransaction();
    wcTransaction.replace(R.id.column_container, waterColumnFragment);
    wcTransaction.commit();


  }

}
