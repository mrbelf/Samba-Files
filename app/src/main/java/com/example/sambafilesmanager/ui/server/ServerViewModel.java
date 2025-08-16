package com.example.sambafilesmanager.ui.server;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ServerViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void attemptConnection(String ipAddress, String username, String password){
        setIsLoading(true);
        new Thread(()->{
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            setIsLoading(false);
        }).start();
    }

    private void setIsLoading(boolean loading){
        isLoading.postValue(loading);
    }

    public ServerViewModel() {
    }
}
