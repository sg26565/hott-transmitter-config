package de.treichels.hott.ui.android.usb;

import gde.model.serial.FileInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.usb.UsbDevice;

public class ListDirectoryTask extends UsbTask<String, FileInfo, List<FileInfo>> {
    public ListDirectoryTask(final Context context, final UsbDevice device) {
        super(context, device);
    }

    @Override
    protected List<FileInfo> doInternal(final String... params) throws IOException {
        final String path = params[0];
        final List<FileInfo> infos = new ArrayList<FileInfo>();

        final String[] content = port.listDir(path);

        for (final String name : content) {
            if (isCancelled()) {
                break;
            }

            final FileInfo info = port.getFileInfo(name);
            if (!isCancelled()) {
                infos.add(info);
                publishProgress(info);
            }
        }

        return isCancelled() ? null : infos;
    }
}
