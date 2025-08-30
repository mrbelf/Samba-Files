package com.example.sambafilesmanager.server;

public class ServerFile {
    public final String path;
    public final String name;

    protected ServerFile(String path, String name){
        this.name = name;
        this.path = path;
    }
}
