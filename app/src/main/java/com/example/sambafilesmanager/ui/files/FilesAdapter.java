package com.example.sambafilesmanager.ui.files;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sambafilesmanager.R;

import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesViewHolder> {

    private List<FileItem> files;

    public FilesAdapter(List<FileItem> files) {
        this.files = files;
    }

    public void setFiles(List<FileItem> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.files_view_holder, parent, false);
        return new FilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesViewHolder holder, int position) {
        FileItem file = files.get(position);
        holder.fileNameTextView.setText(file.getName());
    }

    @Override
    public int getItemCount() {
        return files == null ? 0 : files.size();
    }
}
