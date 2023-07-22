package com.gmail.udonnikomi.notify.ui.notifications;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gmail.udonnikomi.notify.R;

public class NotificationDataViewHolder extends RecyclerView.ViewHolder {
    public TextView itemName;
    public ImageView itemIcon;
    public NotificationDataViewHolder(View view) {
        super(view);
        this.itemName = view.findViewById(R.id.itemName);
        this.itemIcon = view.findViewById(R.id.itemIcon);
    }
}
