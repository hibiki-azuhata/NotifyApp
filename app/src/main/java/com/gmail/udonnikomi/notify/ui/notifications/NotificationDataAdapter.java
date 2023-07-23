package com.gmail.udonnikomi.notify.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.entities.Item;
import com.gmail.udonnikomi.notify.ui.itemboard.ItemboardAdapter;

import java.util.List;

public class NotificationDataAdapter extends RecyclerView.Adapter<NotificationDataViewHolder> {

    private Context context;
    private List<Item.ItemData> list;
    private OnItemClickListener listener;

    public NotificationDataAdapter(Context context, List<Item.ItemData> list) {
        this.context = context;
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
        if(list.get(position).status) { // ステータスがtrueへと変更されている場合

            holder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.notification_table_border, null));
        } else {
            holder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.notification_table_border_nonselect, null));
        }
        holder.itemView.setLongClickable(true);
        holder.itemView.setOnLongClickListener((view) -> {
            listener.onItemClickListener(view, position, list.get(position).name);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    interface OnItemClickListener {
        void onItemClickListener(View view, int position, String text);
    }

    void setOnItemClickListener(NotificationDataAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
