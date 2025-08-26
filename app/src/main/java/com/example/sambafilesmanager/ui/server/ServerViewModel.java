package com.example.sambafilesmanager.ui.server;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sambafilesmanager.server.SambaServer;

public class ServerViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isConnected = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<Boolean> getIsConnected() {
        return isConnected;
    }

    public void attemptConnection(String ipAddress, String username, String password, String sharename){
        setIsLoading(true);
        new Thread(()->{
            boolean result = SambaServer.getInstance().connectToServer(ipAddress, username, password, sharename);
            setIsConnected(result);
            setIsLoading(false);
        }).start();
    }

    public void disconnect(){
        setIsLoading(true);
        new Thread(()->{
            SambaServer.getInstance().closeConnection();
            setIsConnected(false);
            setIsLoading(false);
        }).start();
    }

    private void setIsLoading(boolean loading){
        isLoading.postValue(loading);
    }

    private void setIsConnected(boolean loading){
        isConnected.postValue(loading);
    }

    public ServerViewModel() {
    }
}
