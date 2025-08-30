package com.example.sambafilesmanager.ui.files;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sambafilesmanager.R;

public class FilesViewHolder extends RecyclerView.ViewHolder {
    TextView fileNameTextView;
    Button downloadButton;

    public FilesViewHolder(@NonNull View itemView) {
        super(itemView);
        fileNameTextView = itemView.findViewById(R.id.file_name);
        downloadButton = itemView.findViewById(R.id.download_button);
    }
}
