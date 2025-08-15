package com.example.sambafilesmanager.ui.server;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ServerViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ServerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is server fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
