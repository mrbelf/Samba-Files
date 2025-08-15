package com.example.sambafilesmanager.ui.server;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sambafilesmanager.databinding.FragmentServerBinding;

public class ServerFragment extends Fragment {

    private FragmentServerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ServerViewModel slideshowViewModel =
                new ViewModelProvider(this).get(ServerViewModel.class);

        binding = FragmentServerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.sampleText;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
