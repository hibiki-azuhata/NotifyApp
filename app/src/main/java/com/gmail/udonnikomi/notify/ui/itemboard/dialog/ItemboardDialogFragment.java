package com.gmail.udonnikomi.notify.ui.itemboard.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.entities.Item;
import com.gmail.udonnikomi.notify.services.Database;
import com.gmail.udonnikomi.notify.services.dao.ItemDao;
import com.gmail.udonnikomi.notify.ui.itemboard.dialog.ItemboardDialogSpinnerAdapter.ItemData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ItemboardDialogFragment extends DialogFragment {
    private int itemIcon = -1;
    private List<Disposable> disposables = new ArrayList<>();

    private static ItemData[] data = new ItemData[]{
            new ItemData("ダッシュボード", R.drawable.ic_dashboard_black_24dp),
            new ItemData("アイコン", R.drawable.ic_launcher_foreground),
            new ItemData("家", R.drawable.ic_home_black_24dp)
    };

    @Override
    public void onDismiss(@Nullable DialogInterface dialog) {
        disposables.forEach(Disposable::dispose);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] titles = Arrays.stream(data).map(d -> d.name).toArray(String[]::new);
        final Integer[] icons = Arrays.stream(data).map(d -> d.icon).toArray(Integer[]::new);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.itemboard_dialog, null);
        Spinner spinner = view.findViewById(R.id.itemboad_dialog_icons);
        ItemboardDialogSpinnerAdapter adapter = new ItemboardDialogSpinnerAdapter(
                view.getContext(),
                R.layout.itemboard_dialog_spinner_row,
                titles,
                icons
        );
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                itemIcon = icons[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                itemIcon = icons[0];
            }
        });
        spinner.setAdapter(adapter);
        ItemDao itemDao = Database.getInstance(view.getContext()).itemDao();
        Item item = new Item();
        if(getArguments() == null || getArguments().getInt(Item.ItemKey.ID.toString()) < 0) {
            builder.setView(view)
                    .setPositiveButton(R.string.btn_create, (dialogInterface, id) -> {
                        EditText et = view.findViewById(R.id.itemboard_dialog_name);
                        item.name = et.getText().toString();
                        item.icon = itemIcon;
                        item.status = false;
                        disposables.add(itemDao.insert(item).subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(dialogInterface::dismiss, System.out::println));
                        view.getRootView().requestLayout();
                    })
                    .setNegativeButton(R.string.btn_cancel, (dialogInterface, id) -> {
                        dialogInterface.dismiss();
                    });
        } else {
            item.id = getArguments().getInt(Item.ItemKey.ID.toString());
            item.icon = getArguments().getInt(Item.ItemKey.ICON.toString());
            item.name = getArguments().getString(Item.ItemKey.NAME.toString());
            item.status = getArguments().getBoolean(Item.ItemKey.STATUS.toString());
            int position = 0;
            for(int i = 0; i < data.length; i++) {
                if(item.icon == data[i].icon) {
                    position = i;
                    break;
                }
            }
            spinner.setSelection(position);
            ((EditText) view.findViewById(R.id.itemboard_dialog_name)).setText(item.name, TextView.BufferType.NORMAL);
            builder.setView(view)
                    .setPositiveButton(R.string.btn_update, (dialogInterface, id) -> {
                        EditText et = view.findViewById(R.id.itemboard_dialog_name);
                        item.name = et.getText().toString();
                        item.icon = itemIcon;
                        disposables.add(itemDao.update(item).subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(dialogInterface::dismiss, System.out::println));
                        view.getRootView().requestLayout();
                    })
                    .setNeutralButton(R.string.btn_delete, (dialogInterface, id) -> {
                        disposables.add(itemDao.delete(item).subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(dialogInterface::dismiss, System.out::println));
                    })
                    .setNegativeButton(R.string.btn_cancel, (dialogInterface, id) -> {
                        dialogInterface.dismiss();
                    });
        }
        return builder.create();
    }
}
