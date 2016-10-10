package com.esri.android.ecologicalmarineunitexplorer.summary;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.esri.android.ecologicalmarineunitexplorer.R;
import com.esri.android.ecologicalmarineunitexplorer.data.EMUObservation;
import com.esri.android.ecologicalmarineunitexplorer.data.WaterColumn;
import com.esri.android.ecologicalmarineunitexplorer.databinding.SummaryLayoutBinding;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

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

public class SummaryFragment extends Fragment implements SummaryContract.View {

  private SummaryLayoutBinding mSummaryLayoutBinding;
  private View mRoot;
  private List<EMUObservation> emuObservations = new ArrayList<>();
  private RecyclerView mEmuObsView;
  private EMUAdapter mEmuAdapter;

  public static SummaryFragment newInstance() {

    Bundle args = new Bundle();

    SummaryFragment fragment = new SummaryFragment();
    fragment.setArguments(args);
    return fragment;
  }
  @Override
  public final void onCreate(@NonNull final Bundle savedInstance) {

    super.onCreate(savedInstance);
    Log.i("SummaryFragment", "onCreate");
    mEmuAdapter = new EMUAdapter(getContext(), R.id.summary_container, emuObservations);
  }

  @Override
  @Nullable
  public  View onCreateView(final LayoutInflater layoutInflater, final ViewGroup container,
      final Bundle savedInstance){
    Log.i("SummaryFragment", "onCreateView");
    setUpToolbar();

    mEmuObsView = (RecyclerView) layoutInflater.inflate(R.layout.summary_recycler_view, container,false) ;

    mEmuObsView.setLayoutManager(new LinearLayoutManager(mEmuObsView.getContext() ));
    mEmuObsView.setAdapter(mEmuAdapter);

    return mEmuObsView;
  }
  @Override
  public final void onResume(){
    super.onResume();
    Log.i("SummaryFragment", "onResume");
  }

  @Override
  public final void onPause() {
    super.onPause();
    Log.i("SummaryFragment", "onPause");
  }
  /**
   * Override the application label used for the toolbar title
   */
  private void setUpToolbar() {
    final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ocean Summary for Location");
  }

  @Override public void showWaterColumn(WaterColumn waterColumn) {

    Set<EMUObservation> emuSet = waterColumn.getEmuSet();

    emuObservations.clear();
    for (EMUObservation observation : emuSet){
      emuObservations.add(observation);
    }
  }

  @Override public void setPresenter(SummaryContract.Presenter presenter) {

  }

  public class EMUAdapter extends RecyclerView.Adapter<RecycleViewHolder>{

    private List<EMUObservation> emuObservations = Collections.emptyList();

    public EMUAdapter(final Context context, final int resource, final List<EMUObservation> observations){
      emuObservations = observations;
    }

    public final void setEmuObservations(final List<EMUObservation> observations){
      checkNotNull(observations);
      emuObservations = observations;
      notifyDataSetChanged();
    }
    @Override public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      final View emuView = inflater.inflate(R.layout.summary_layout, parent, false);

      return new RecycleViewHolder(emuView);
    }

    @Override public void onBindViewHolder(RecycleViewHolder holder, int position) {
      final EMUObservation observation = emuObservations.get(position);
      holder.txtThickness.setText(observation.getThickness() + " meters");
      holder.txtName.setText(observation.getEmu().getName().toString());
      holder.txtNutrients.setText(observation.getEmu().getNutrientSummary());
      holder.txtSummary.setText(observation.getEmu().getPhysicalSummary());

      int top = observation.getTop();
      holder.txtTop.setText("Distance from surface " + top + " meters");
      holder.bind(observation);
    }

    @Override public int getItemCount() {
      return emuObservations.size();
    }
  }
  public class RecycleViewHolder extends RecyclerView.ViewHolder{

    public final TextView txtSummary ;
    public final TextView txtNutrients ;
    public final TextView txtName;
    public final TextView txtThickness;
    public final TextView txtTop;

    public RecycleViewHolder(final View emuView){
      super(emuView);
      txtSummary = (TextView) emuView.findViewById(R.id.physical_summary);
      txtNutrients = (TextView) emuView.findViewById(R.id.nutrient_summary);
      txtName = (TextView) emuView.findViewById(R.id.txtName);
      txtThickness = (TextView) emuView.findViewById(R.id.txt_thickness);
      txtTop = (TextView) emuView.findViewById(R.id.txt_top);
   //   Drawable rectangle = ResourcesCompat.getDrawable(R.drawable.rectangle, R.co)
    }
    public final void bind(final EMUObservation observation){
      //no op for now
    }
//    private Color getColorForCluster(int emuName){
//      int colorId =0;
//      switch (emuName){
//        case 3:
//          colorId = getResources().getColor(R.color.Cluster3);
//          break;
//      }
//      return
//    }
  }
}
