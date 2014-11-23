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
package de.treichels.hott.ui.android.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * A {@link BaseAdapter}, that is backed by a list.
 *
 * @author oli
 */
public abstract class GenericListAdaper<T> extends BaseAdapter {
  private final Context context;
  private final List<T> list = new ArrayList<T>();

  public GenericListAdaper(final Context context) {
    this.context = context;
  }

  protected void add(final T item) {
    list.add(item);
    notifyDataSetChanged();
  }

  protected void clear() {
    list.clear();
    notifyDataSetChanged();
  }

  protected T get(final int position) {
    return list.get(position);
  }

  protected Context getContext() {
    return context;
  }

  @Override
  public int getCount() {
    return list.size();
  }

  @Override
  public Object getItem(final int position) {
    return list.get(position);
  }

  @Override
  public long getItemId(final int position) {
    return position;
  }

  protected boolean isEmtpy() {
    return list.isEmpty();
  }

  public abstract void reload();
}
