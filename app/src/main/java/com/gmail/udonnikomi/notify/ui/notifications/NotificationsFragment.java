package com.gmail.udonnikomi.notify.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.databinding.FragmentNotificationsBinding;
import com.gmail.udonnikomi.notify.entities.Item;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel homeViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView rv = (RecyclerView) root.findViewById(R.id.notifications_recycler);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(
                root.getContext(),
                3,
                RecyclerView.VERTICAL,
                false
        ));
        List<Item.ItemData> testData = new ArrayList<>();
        testData.add(new Item.ItemData(
                1,
                "testtest",
                R.drawable.ic_launcher_foreground,
                true
        ));
        testData.add(new Item.ItemData(
                2,
                "testtest2",
                R.drawable.ic_launcher_background,
                true
        ));
        testData.add(new Item.ItemData(
                3,
                "testtest3",
                android.R.drawable.ic_input_add,
                true
        ));
        rv.setAdapter(new NotificationDataAdapter(testData));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}