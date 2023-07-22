package com.gmail.udonnikomi.notify.ui.itemboard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gmail.udonnikomi.notify.R;

public class ItemboardViewHolder extends RecyclerView.ViewHolder {
    public TextView itemboardTitle;
    public ImageView itemboardImage;
    public ItemboardViewHolder(View view) {
        super(view);
        this.itemboardTitle = view.findViewById(R.id.itemboard_title);
        this.itemboardImage = view.findViewById(R.id.itemboard_image);
    }
}
