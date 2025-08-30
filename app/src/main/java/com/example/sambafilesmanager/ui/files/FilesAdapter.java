package com.example.sambafilesmanager.ui.files;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sambafilesmanager.R;
import com.example.sambafilesmanager.server.SambaServer;

import java.time.Duration;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesViewHolder> {

    private List<FileItem> files;
    private Context context;

    public FilesAdapter(List<FileItem> files, Context context) {

        this.files = files;
        this.context = context;
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
        holder.downloadButton.setOnClickListener((v) -> {
            Toast.makeText(this.context, "Downloading: "+file.getName(), Toast.LENGTH_LONG).show();
            new Thread(() -> {
                var server = SambaServer.getInstance();
                server.downloadFile(server.joinPaths(file.getPath(), file.getName()), this.context);
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return files == null ? 0 : files.size();
    }
}
