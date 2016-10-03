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
import com.esri.android.ecologicalmarineunitexplorer.R;
import com.esri.android.ecologicalmarineunitexplorer.data.EMU;
import com.esri.android.ecologicalmarineunitexplorer.data.EMUObservation;
import com.esri.android.ecologicalmarineunitexplorer.data.WaterColumn;
import com.esri.android.ecologicalmarineunitexplorer.summary.SummaryFragment;
import com.esri.android.ecologicalmarineunitexplorer.util.ActivityUtils;

import java.util.Set;

public class MapActivity extends AppCompatActivity {

  private MapPresenter mMapPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    // Set up fragments
    configureFragments();
  }

  /**
   * Configure all the associated fragments
   */
  private void configureFragments(){
    final FragmentManager fm = getSupportFragmentManager();
    MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.fragment_container);

    if (mapFragment == null) {
      mapFragment = MapFragment.newInstance();
      ActivityUtils.addFragmentToActivity(
          getSupportFragmentManager(), mapFragment, R.id.fragment_container, "map fragment");
    }
    mMapPresenter = new MapPresenter(mapFragment);
  }

  public void showSummary(WaterColumn waterColumn){


    Set<EMUObservation> emuObservations = waterColumn.getEmuSet();
    EMUObservation [] observations = emuObservations.toArray(new EMUObservation[emuObservations.size()]);
    EMUObservation observation = observations[0];

    Bundle bundle = new Bundle();
    bundle.putString(getString(R.string.bundle_physical_summary), observation.getEmu().getPhysicalSummary());
    bundle.putString(getString(R.string.bundle_nutrient_summary), observation.getEmu().getNutrientSummary());
    bundle.putInt(getString(R.string.bundle_emu_number), observation.getEmu().getName());
    bundle.putInt(getString(R.string.bundle_thickness), observation.getThickness());

    final FragmentManager fm = getSupportFragmentManager();
    SummaryFragment summaryFragment = SummaryFragment.newInstance();
    summaryFragment.setArguments(bundle);
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    // Replace whatever is in the fragment_container view with this fragment,
    // and add the transaction to the back stack so the user can navigate back
    transaction.replace(R.id.fragment_container, summaryFragment);
    transaction.addToBackStack("summary fragment");

    // Commit the transaction
    transaction.commit();

  }

}
