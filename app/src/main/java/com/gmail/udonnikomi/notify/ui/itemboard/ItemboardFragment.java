package com.gmail.udonnikomi.notify.ui.itemboard;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.udonnikomi.notify.MovementNotify;
import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.databinding.FragmentItemboardBinding;
import com.gmail.udonnikomi.notify.entities.Item;
import com.gmail.udonnikomi.notify.services.Database;
import com.gmail.udonnikomi.notify.services.Preference;
import com.gmail.udonnikomi.notify.services.dao.ItemDao;
import com.gmail.udonnikomi.notify.ui.itemboard.dialog.ItemboardDialogFragment;
import com.gmail.udonnikomi.notify.ui.notifications.NotificationDataAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemboardFragment extends Fragment {

    private FragmentItemboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ItemboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ItemboardViewModel.class);

        binding = FragmentItemboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if(getActivity() != null && getActivity() instanceof  AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(R.string.title_itemboard);
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A0CCC6")));
            }
        }
        RecyclerView rv = (RecyclerView) root.findViewById(R.id.itemboard_recycler);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(root.getContext()));
        List<Item.ItemData> data = new ArrayList<>();
        ItemDao idao = Database.getInstance(getContext()).itemDao();
        data.add(new Item.ItemData(
                -1,
                "分類を追加",
                R.drawable.ic_plus,
                false
        ));
        final Item[] items = idao.findAll().blockingFirst();
        Arrays.stream(items).forEach(item ->
            data.add(new Item.ItemData(
                item.id,
                item.name,
                item.icon,
                item.status
            ))
        );
        ItemboardAdapter adapter = new ItemboardAdapter(getContext(), data);
        adapter.setOnItemClickListener((view, position, text) -> {
            Bundle args = new Bundle();
            if(position == 0 || items.length - 1 < position - 1) { // itemsの最大のindexよりもposition-1が大きいとIllegalArgumentException
                args.putInt(Item.ItemKey.ID.toString(), -1);
            } else {
                Item item = items[position - 1]; // create用Item分を引く
                args.putInt(Item.ItemKey.ID.toString(), item.id);
                args.putString(Item.ItemKey.NAME.toString(), item.name);
                args.putInt(Item.ItemKey.ICON.toString(), item.icon);
                args.putBoolean(Item.ItemKey.STATUS.toString(), item.status);
            }
            ItemboardDialogFragment dialog = new ItemboardDialogFragment();
            dialog.setArguments(args);
            dialog.show(getParentFragmentManager(), "Dialog");
        });
        rv.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}