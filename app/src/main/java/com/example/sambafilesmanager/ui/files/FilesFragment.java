package com.example.sambafilesmanager.ui.files;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sambafilesmanager.databinding.FragmentFilesBinding;

public class FilesFragment extends Fragment {
    private FragmentFilesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FilesViewModel slideshowViewModel =
                new ViewModelProvider(this).get(FilesViewModel.class);

        binding = FragmentFilesBinding.inflate(inflater, container, false);
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
