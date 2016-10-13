package com.esri.android.ecologicalmarineunitexplorer.watercolumn;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.esri.android.ecologicalmarineunitexplorer.R;
import com.esri.android.ecologicalmarineunitexplorer.data.EMUObservation;
import com.esri.android.ecologicalmarineunitexplorer.data.WaterColumn;
import com.esri.android.ecologicalmarineunitexplorer.util.EmuHelper;

import java.util.Set;

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

public class WaterColumnFragment extends Fragment {

  private LinearLayout mRoot;
  private LinearLayout mButtonContainer;
  private WaterColumn mWaterColumn;

  public static WaterColumnFragment newInstance(){
    return new WaterColumnFragment();
  }
  @Override
  @Nullable
  public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup container,
      final Bundle savedInstance){
    mRoot = (LinearLayout) layoutInflater.inflate(R.layout.water_column, container,false);
    mButtonContainer = (LinearLayout) mRoot.findViewById(R.id.buttonContainer);
    if (mWaterColumn != null){
      buildWaterColumn(mWaterColumn);
    }
    return  mRoot;
  }
  public void setWaterColumn(WaterColumn waterColumn){
    mWaterColumn = waterColumn;
  }
  /**
   * Dynamically add a button for each EMU represented
   * in the water column.
   * @param waterColumn
   */
  public void buildWaterColumn(WaterColumn waterColumn){

    int count = mRoot.getChildCount();
    for (int z=0 ; z < count; z++){
      View v = mRoot.getChildAt(z);
      if (v instanceof Button){
        mRoot.removeViewAt(z);
      }
    }

    // Each button will be added to layout with a layout_weight
    // relative to the ratio of the EUMObservation to
    // the depth of the water column
    Set<EMUObservation> emuObservationSet = waterColumn.getEmuSet();
    float depth = (float) waterColumn.getDepth();
    TextView tv = (TextView) mRoot.findViewById(R.id.txtBottom);
    tv.setText(waterColumn.getDepth()+" m");

    int buttonId = 0;
    for (EMUObservation observation: emuObservationSet){
      float relativeSize = (observation.getThickness()/depth) * 100;
      Log.i("WaterColumnFragment", "thickness = "+ observation.getThickness()+ " depth= "+ depth+ " relative size " + relativeSize);
      Button button = new Button(getContext());
      LinearLayout.LayoutParams  layoutParams  =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, relativeSize);
      button.setLayoutParams(layoutParams);
      button.setBackgroundColor(Color.parseColor(EmuHelper.getColorForEMUCluster(getContext(), observation.getEmu().getName())));
      button.setId(buttonId);
      mButtonContainer.addView(button);
      buttonId = buttonId + 1;
    }
  }
}
