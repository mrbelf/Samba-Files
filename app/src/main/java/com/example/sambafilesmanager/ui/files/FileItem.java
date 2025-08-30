package com.example.sambafilesmanager.ui.files;

public class FileItem {
    private final String name;
    private final String path;

    public FileItem(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() { return name; }
    public String getPath() { return path; }
}
