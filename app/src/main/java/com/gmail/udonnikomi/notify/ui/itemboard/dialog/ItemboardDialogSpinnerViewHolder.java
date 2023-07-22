package com.gmail.udonnikomi.notify.ui.itemboard.dialog;

import android.widget.ImageView;
import android.widget.TextView;

public class ItemboardDialogSpinnerViewHolder {
    public TextView itemboardDialogTitle;
    public ImageView itemboardDialogImage;
    ItemboardDialogSpinnerViewHolder(TextView text, ImageView image) {
        this.itemboardDialogImage = image;
        this.itemboardDialogTitle = text;
    }
}