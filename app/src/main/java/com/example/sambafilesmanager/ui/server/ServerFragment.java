package com.example.sambafilesmanager.ui.server;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sambafilesmanager.databinding.FragmentServerBinding;

public class ServerFragment extends Fragment {

    private FragmentServerBinding binding;
    private ServerViewModel serverViewModel;
    private EditText ipField;
    private EditText usernameField;
    private EditText passwordField;
    private Button connectButton;
    private Button disconnectButton;
    private FrameLayout loadingOverlay;
    private EditText sharenameField;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        serverViewModel =
                new ViewModelProvider(this).get(ServerViewModel.class);

        binding = FragmentServerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ipField = binding.serverIp;
        usernameField = binding.username;
        passwordField = binding.password;
        connectButton = binding.connect;
        disconnectButton = binding.disconnect;
        loadingOverlay = binding.loadingOverlay;
        sharenameField = binding.sharename;

        loadingOverlay.setOnTouchListener((v, event) -> true);
        loadingOverlay.bringToFront();

        serverViewModel.getIsLoading()
                .observe(getViewLifecycleOwner(), (Observer<Boolean>) this::setLoading);
        serverViewModel.getIsConnected().observe(getViewLifecycleOwner(), (Observer<Boolean>) this::setConnected);

        connectButton.setOnClickListener((v) -> {
            serverViewModel.attemptConnection(
                    ipField.getText().toString(),
                    usernameField.getText().toString(),
                    passwordField.getText().toString(),
                    sharenameField.getText().toString());
        });

        disconnectButton.setOnClickListener((v) -> {
            serverViewModel.disconnect();
        });

        return root;
    }

    private void setLoading(boolean loading){
        loadingOverlay.setVisibility(loading ? VISIBLE : GONE);
        connectButton.setEnabled(!loading);
    }

    private void setConnected(boolean connected){
        connectButton.setVisibility(connected ? GONE : VISIBLE);
        disconnectButton.setVisibility(connected ? VISIBLE : GONE);

        ipField.setEnabled(!connected);
        usernameField.setEnabled(!connected);
        passwordField.setEnabled(!connected);
        sharenameField.setEnabled(!connected);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
