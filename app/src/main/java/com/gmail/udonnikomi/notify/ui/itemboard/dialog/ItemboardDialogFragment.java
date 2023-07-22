package com.gmail.udonnikomi.notify.ui.itemboard.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.gmail.udonnikomi.notify.R;
import com.gmail.udonnikomi.notify.entities.Item;
import com.gmail.udonnikomi.notify.services.Database;
import com.gmail.udonnikomi.notify.services.dao.ItemDao;
import com.gmail.udonnikomi.notify.ui.itemboard.dialog.ItemboardDialogSpinnerAdapter.ItemData;

import java.util.Arrays;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ItemboardDialogFragment extends DialogFragment {
    private int itemIcon = -1;

    private static ItemData[] data = new ItemData[]{
            new ItemData("ダッシュボード", R.drawable.ic_dashboard_black_24dp),
            new ItemData("アイコン", R.drawable.ic_launcher_foreground),
            new ItemData("家", R.drawable.ic_home_black_24dp)
    };

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
            }
        });
        spinner.setAdapter(adapter);
        builder.setView(view)
                .setPositiveButton(R.string.app_name, (dialogInterface, id) -> {
                    EditText et = view.findViewById(R.id.itemboard_dialog_name);
                    ItemDao itemDao = Database.getInstance(view.getContext()).itemDao();
                    Item item = new Item();
                    item.name = et.getText().toString();
                    item.icon = itemIcon;
                    item.status = false;
                    itemDao.insert(item).subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> dialogInterface.dismiss(),
                                            throwable -> {});
                })
                .setNegativeButton(R.string.app_name, (dialogInterface, id) -> {
                    dialogInterface.dismiss();
                });
        return builder.create();
    }
}
