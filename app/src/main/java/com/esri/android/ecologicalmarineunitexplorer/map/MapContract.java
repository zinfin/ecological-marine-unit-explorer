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

import android.content.Context;
import com.esri.android.ecologicalmarineunitexplorer.BasePresenter;
import com.esri.android.ecologicalmarineunitexplorer.BaseView;
import com.esri.android.ecologicalmarineunitexplorer.data.WaterColumn;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;

public interface MapContract {
  interface View extends BaseView<Presenter> {
    Point getScreenToLocation(android.graphics.Point mapPoint);
    Context getContext();
    void showSelectedRegion(Polygon polygon);
    void showDataNotFound();
    void showSummary(WaterColumn column);
  }
  interface Presenter extends BasePresenter {

    void setSelectedPoint(Point point);
    Polygon getBufferPolygonForPoint(Point point, double distance);

  }
}
