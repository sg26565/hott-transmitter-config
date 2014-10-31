package de.treichels.hott.ui.android.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class GenericListAdaper<T> extends BaseAdapter {
    private final Context context;
    private final List<T> list = new ArrayList<T>();

    public GenericListAdaper(final Context context) {
        this.context = context;
    }

    protected void add(final T item) {
        list.add(item);
    }

    protected void clear() {
        list.clear();
    }

    protected Context getContext() {
        return context;
    }

    protected boolean isEmtpy() {
        return list.isEmpty();
    }

    protected T get(final int position) {
        return list.get(position);
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
}
