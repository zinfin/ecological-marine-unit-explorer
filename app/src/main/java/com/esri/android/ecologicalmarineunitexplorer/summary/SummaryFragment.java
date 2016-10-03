package com.esri.android.ecologicalmarineunitexplorer.summary;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.esri.android.ecologicalmarineunitexplorer.R;
import com.esri.android.ecologicalmarineunitexplorer.databinding.SummaryLayoutBinding;
import org.w3c.dom.Text;

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

public class SummaryFragment extends Fragment {

  private SummaryLayoutBinding mSummaryLayoutBinding;

  public static SummaryFragment newInstance() {

    Bundle args = new Bundle();

    SummaryFragment fragment = new SummaryFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  @Nullable
  public final View onCreateView(final LayoutInflater layoutInflater, final ViewGroup container,
      final Bundle savedInstance){
    setUpToolbar();
    mSummaryLayoutBinding = DataBindingUtil.inflate(layoutInflater, R.layout.summary_layout, container,false);
    View root = mSummaryLayoutBinding.getRoot();

    Bundle bundle = getArguments();
    if (bundle != null){
      String physicalSummary = bundle.getString(getString(R.string.bundle_physical_summary));
      String nutrientSummary = bundle.getString(getString(R.string.bundle_nutrient_summary));
      Integer emuNumber = bundle.getInt(getString(R.string.bundle_emu_number));
      Integer thickness = bundle.getInt(getString(R.string.bundle_thickness));
      TextView txView = (TextView) root.findViewById(R.id.physical_summary);
      txView.setText(physicalSummary);
      TextView txtNutrient = (TextView) root.findViewById(R.id.nutrient_summary);
      txtNutrient.setText(nutrientSummary);
      TextView txtNumber = (TextView) root.findViewById(R.id.txtName);
      txtNumber.setText(emuNumber.toString());
      TextView txtThick = (TextView) root.findViewById(R.id.txt_thickness);
      txtThick.setText(thickness + " meters");


    }

    return root;
  }
  /**
   * Override the application label used for the toolbar title
   */
  private void setUpToolbar() {
    final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ocean Summary for Location");
  }
}
