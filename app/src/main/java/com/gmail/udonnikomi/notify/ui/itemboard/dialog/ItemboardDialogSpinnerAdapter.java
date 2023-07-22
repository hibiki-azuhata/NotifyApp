package com.gmail.udonnikomi.notify.ui.itemboard.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

import com.gmail.udonnikomi.notify.R;

public class ItemboardDialogSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final LayoutInflater inflater;
    private final int layoutId;
    private final String[] titles;
    private final Integer[] icons;

    ItemboardDialogSpinnerAdapter(Context context, int itemLayoutId, String[] titles, Integer[] icons) {
        this.inflater = LayoutInflater.from(context);
        this.layoutId = itemLayoutId;
        this.titles = titles;
        this.icons = icons;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ItemboardDialogSpinnerViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(this.layoutId, null);
            holder = new ItemboardDialogSpinnerViewHolder(
                    convertView.findViewById(R.id.itemboard_dialog_spinner_row_title),
                    convertView.findViewById(R.id.itemboard_dialog_spinner_row_image)
            );
            convertView.setTag(holder);
        } else {
            holder = (ItemboardDialogSpinnerViewHolder) convertView.getTag();
        }

        holder.itemboardDialogImage.setImageResource(this.icons[position]);
        holder.itemboardDialogTitle.setText(this.titles[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return new ItemData(titles[position], icons[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemboardDialogSpinnerViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(this.layoutId, null);
            holder = new ItemboardDialogSpinnerViewHolder(
                    convertView.findViewById(R.id.itemboard_dialog_spinner_row_title),
                    convertView.findViewById(R.id.itemboard_dialog_spinner_row_image)
            );
            convertView.setTag(holder);
        } else {
            holder = (ItemboardDialogSpinnerViewHolder) convertView.getTag();
        }

        holder.itemboardDialogImage.setImageResource(this.icons[position]);
        holder.itemboardDialogTitle.setText(this.titles[position]);

        return convertView;
    }

    public static class ItemData {
        public final String name;
        public final int icon;
        ItemData(String name, int icon) {
            this.name = name;
            this.icon = icon;
        }
    }
}
