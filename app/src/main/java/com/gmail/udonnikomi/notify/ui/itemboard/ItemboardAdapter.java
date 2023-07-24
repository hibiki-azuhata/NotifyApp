package com.gmail.udonnikomi.notify.ui.itemboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.entities.Item;

import java.util.List;

public class ItemboardAdapter extends RecyclerView.Adapter<ItemboardViewHolder> {

    private Context context;
    private List<Item.ItemData> list;
    private OnItemClickListener listener;

    public ItemboardAdapter(Context context, List<Item.ItemData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemboard_table_row, parent, false);
        return new ItemboardViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemboardViewHolder holder, int position) {
        holder.itemboardTitle.setText(list.get(position).name);
        holder.itemboardImage.setImageResource(list.get(position).icon);
        if(position == 0) {
            holder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.itemboard_row_background_addbtn, null));
        } else {
            holder.itemView.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.itemboard_row_background, null));
        }
        holder.itemView.setOnClickListener((view) -> {
            listener.onItemClickListener(view, position, list.get(position).name);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    interface OnItemClickListener {
        void onItemClickListener(View view, int position, String text);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
