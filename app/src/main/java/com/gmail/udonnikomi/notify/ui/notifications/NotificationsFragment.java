package com.gmail.udonnikomi.notify.ui.notifications;

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
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.databinding.FragmentNotificationsBinding;
import com.gmail.udonnikomi.notify.entities.Item;
import com.gmail.udonnikomi.notify.services.Database;
import com.gmail.udonnikomi.notify.services.dao.ItemDao;
import com.gmail.udonnikomi.notify.ui.itemboard.ItemboardFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private List<Disposable> disposables = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel homeViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if(actionBar != null) {
                actionBar.setTitle(R.string.title_notifications);
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A0CCC6")));
            }
        }

        RecyclerView rv = (RecyclerView) root.findViewById(R.id.notifications_recycler);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(
                root.getContext(),
                3,
                RecyclerView.VERTICAL,
                false
        ));
        List<Item.ItemData> data = new ArrayList<>();
        ItemDao idao = Database.getInstance(getContext()).itemDao();
        final Item[] items = idao.findAll().blockingFirst();
        Arrays.stream(items).forEach(item ->
                data.add(new Item.ItemData(
                        item.id,
                        item.name,
                        item.icon,
                        item.status
                ))
        );
        NotificationDataAdapter adapter = new NotificationDataAdapter(getContext(), data);
        adapter.setOnItemClickListener((view, position, text) -> {
            Item item = items[position];
            if(item != null) {
                disposables.add(idao.updateStatus(item.id, !item.status).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            if(!item.status) { // ステータスがtrueへと変更されている場合
                                view.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.notification_row_background_selected, null));
                            } else {
                                view.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.notification_row_background, null));
                            }
                        }, System.out::println));
                reload();
            }
        });
        rv.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.forEach(Disposable::dispose);
        binding = null;
    }

    private void reload() {
        if(getActivity() != null) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_layout, new NotificationsFragment());
            transaction.commit();
        }
    }
}