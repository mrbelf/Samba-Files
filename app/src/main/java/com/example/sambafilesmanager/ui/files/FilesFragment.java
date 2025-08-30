package com.example.sambafilesmanager.ui.files;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sambafilesmanager.R;
import com.example.sambafilesmanager.databinding.FragmentFilesBinding;
import com.example.sambafilesmanager.server.SambaServer;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;

import java.util.ArrayList;
import java.util.List;

public class FilesFragment extends Fragment {
    private FragmentFilesBinding binding;
    private TextView topText;
    private RecyclerView filesRecyclerView;
    private FilesAdapter adapter;

    private static final int REQUEST_CODE_PICK_FOLDER = 1001;
    private ActivityResultLauncher<Intent> folderPickerLauncher;
    private String URI_KEY = "saved_folder_uri";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FilesViewModel filesViewModel =
                new ViewModelProvider(this).get(FilesViewModel.class);

        binding = FragmentFilesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        topText = binding.topText;
        filesRecyclerView = binding.filesRecyclerView;

        adapter = new FilesAdapter(new ArrayList<>(), this.getContext());
        filesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        filesRecyclerView.setAdapter(adapter);
        filesViewModel.getFiles().observe(getViewLifecycleOwner(), filenames -> {
            List<FileItem> items = new ArrayList<>();
            for (var file : filenames) {
                items.add(new FileItem(file.name, file.path));
            }
            adapter.setFiles(items);
        });


        updateTopText();

        if(getSavedUri() == null){
            askForPickingFolder();
        }

        return root;
    }

    @Override
    public void onStart(){
        super.onStart();

        var viewModel = new ViewModelProvider(this).get(FilesViewModel.class);
        new Thread(() -> {
            viewModel.loadFiles();
            viewModel.setUri(this.getSavedUri());
        }).start();
    }

    private void askForPickingFolder(){
        folderPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri treeUri = result.getData().getData();
                        requireContext().getContentResolver().takePersistableUriPermission(
                                treeUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        );
                        saveUri(treeUri.toString());
                    }
                }
        );

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        folderPickerLauncher.launch(intent);
    }

    private void updateTopText(){
        String savedUri = getSavedUri();
        if(savedUri != null){
            String path = Uri.parse(savedUri).getPath();
            topText.setText(String.format(getText(R.string.files_location).toString(), path));
        }
        else
            topText.setText(R.string.files_placeholder);
    }

    private void saveUri(String uriString) {
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString(URI_KEY, uriString).apply();
    }

    @Nullable
    private String getSavedUri() {
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return prefs.getString(URI_KEY, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
