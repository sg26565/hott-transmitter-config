/**
 *  HoTT Transmitter Config
 *  Copyright (C) 2013  Oliver Treichel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.treichels.hott.ui.android;

import gde.model.enums.ModelType;
import gde.model.enums.Section;
import gde.model.enums.TransmitterType;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author oli
 * 
 */
public class SectionAdapter extends BaseAdapter {
  private final LayoutInflater inflater;
  private final List<Section>  sections;

  /**
   * @param modelType
   * @param transmitterType
   */
  public SectionAdapter(final Context context, final ModelType modelType, final TransmitterType transmitterType) {
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    sections = new ArrayList<Section>();

    for (final Section s : Section.values()) {
      if (s.isValidFor(modelType) && s.isValidFor(transmitterType)) {
        sections.add(s);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.widget.Adapter#getCount()
   */
  @Override
  public int getCount() {
    return sections.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.widget.Adapter#getItem(int)
   */
  @Override
  public Object getItem(final int position) {
    return sections.get(position).name();
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.widget.Adapter#getItemId(int)
   */
  @Override
  public long getItemId(final int position) {
    return sections.get(position).ordinal();
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.widget.Adapter#getView(int, android.view.View,
   * android.view.ViewGroup)
   */
  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent) {
    TextView textView;

    if (convertView != null) {
      textView = (TextView) convertView;
    } else {
      textView = (TextView) inflater.inflate(R.layout.drawer_list_item, parent, false);
    }

    textView.setText(sections.get(position).toString());

    return textView;
  }
}