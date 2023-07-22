package com.gmail.udonnikomi.notify.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.entities.Item;

import java.util.List;

public class NotificationDataAdapter extends RecyclerView.Adapter<NotificationDataViewHolder> {

    private List<Item.ItemData> list;

    public NotificationDataAdapter(List<Item.ItemData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_table_row, parent, false);
        return new NotificationDataViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationDataViewHolder holder, int position) {
        holder.itemName.setText(list.get(position).name);
        holder.itemIcon.setImageResource(list.get(position).icon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
