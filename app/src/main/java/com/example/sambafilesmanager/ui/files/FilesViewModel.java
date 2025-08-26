package com.example.sambafilesmanager.ui.files;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sambafilesmanager.server.SambaServer;

import java.util.ArrayList;
import java.util.List;

public class FilesViewModel extends ViewModel {
    private final MutableLiveData<List<String>> filesLiveData = new MutableLiveData<>();

    public LiveData<List<String>> getFiles() {
        return filesLiveData;
    }

    public void loadFiles() {
        // Hard coded path for now
        filesLiveData.postValue(SambaServer.getInstance().listAllFiles("roms"));
    }

    public FilesViewModel() {
    }
}
