package com.gmail.udonnikomi.notify.ui.itemboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.entities.Item;
import com.gmail.udonnikomi.notify.ui.itemboard.dialog.ItemboardDialogFragment;

import java.util.List;

public class ItemboardAdapter extends RecyclerView.Adapter<ItemboardViewHolder> {

    private List<Item.ItemData> list;

    public ItemboardAdapter(List<Item.ItemData> list) {
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
