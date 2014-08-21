package de.treichels.hott.ui.android;

import gde.model.serial.ModelInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ModelInfoAdapter extends BaseAdapter {
  private final Context         context;
  private final List<ModelInfo> modelInfos;

  /**
   * @param usbDevice
   */
  public ModelInfoAdapter(final Context context, final UsbDevice usbDevice) {
    this.context = context;
    modelInfos = new ArrayList<ModelInfo>();

    new GetAllModelsTask(context) {
      @Override
      protected void onProgressUpdate(final ModelInfo... values) {
        for (final ModelInfo info : values) {
          modelInfos.add(info);
          notifyDataSetChanged();
        }
      }
    }.execute(usbDevice);
  }

  @Override
  public int getCount() {
    return modelInfos.size();
  }

  @Override
  public Object getItem(final int position) {
    return modelInfos.get(position);
  }

  @Override
  public long getItemId(final int position) {
    return position;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent) {
    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final TextView view = (TextView) inflater.inflate(android.R.layout.simple_list_item_single_choice, parent);
    final ModelInfo info = modelInfos.get(position);
    view.setText(String.format("%2d: %s (%s)", info.getModelNumber(), info.getModelName(), info.getModelType()));
    return view;
  }
}
