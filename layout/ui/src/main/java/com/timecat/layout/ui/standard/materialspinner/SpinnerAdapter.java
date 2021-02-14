package com.timecat.layout.ui.standard.materialspinner;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Spinner适配器
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:08
 */
public class SpinnerAdapter<T> extends MaterialSpinnerAdapter<T> implements Filterable {

    private final Object mLock = new Object();
    private List<T> mOriginalValues;

    public SpinnerAdapter(Context context, List<T> items) {
        super(context, items);
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() - 1 : 0;
    }

    @Override
    public T getItem(int position) {
        if (position >= getSelectedIndex()) {
            return mItems.get(position + 1);
        } else {
            return mItems.get(position);
        }
    }

    @Override
    public T get(int position) {
        if (mItems != null && position >= 0 && position <= mItems.size() - 1) {
            return mItems.get(position);
        } else {
            return null;
        }
    }

    @Override
    public List<T> getItems() {
        return mItems;
    }

    private Filter mFilter;

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    /**
     * <p>An array filter constrains the content of the array adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mItems);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                final ArrayList<T> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                final ArrayList<T> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = valueText.split(" ");
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mItems = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}