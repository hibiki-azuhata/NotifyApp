package com.gmail.udonnikomi.notify.ui.itemboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ItemboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}