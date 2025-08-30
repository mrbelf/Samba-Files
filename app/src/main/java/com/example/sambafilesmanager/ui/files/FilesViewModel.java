package com.example.sambafilesmanager.ui.files;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sambafilesmanager.server.SambaServer;
import com.example.sambafilesmanager.server.ServerFile;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;

import java.util.ArrayList;
import java.util.List;

public class FilesViewModel extends ViewModel {
    private final MutableLiveData<List<ServerFile>> filesLiveData = new MutableLiveData<>();

    public LiveData<List<ServerFile>> getFiles() {
        return filesLiveData;
    }

    public void loadFiles() {
        // Hard coded path for now
        filesLiveData.postValue(SambaServer.getInstance().listAllFiles("roms"));
    }

    public void setUri(String uri){
        SambaServer.getInstance().setLocalRootUri(uri);
    }

    public FilesViewModel() {
    }
}
