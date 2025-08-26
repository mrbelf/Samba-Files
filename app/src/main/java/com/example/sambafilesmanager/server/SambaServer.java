package com.example.sambafilesmanager.server;
import static android.content.ContentValues.TAG;

import android.util.Log;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.msfscc.fileinformation.FileDirectoryQueryableInformation;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class SambaServer {
    private static SambaServer instance = null;

    private Session session = null;
    private DiskShare share = null;


    private SambaServer(){};

    public static SambaServer getInstance(){
        if(instance == null)
            instance = new SambaServer();
        return instance;
    }

    public boolean connectToServer(String ip, String username, String password, String sharename) {
        SMBClient client = new SMBClient();
        try {
            Connection connection = client.connect(ip);
            AuthenticationContext ac = new AuthenticationContext(
                    username,
                    password.toCharArray(),
                    null
            );

            session = connection.authenticate(ac);

            Log.d(TAG, "Connected to SMB server at " + ip);

            share = (DiskShare) session.connectShare(sharename);

            boolean success = share.isConnected();
            if(success)
                Log.d(TAG, "Connected share " + sharename);
            else
                Log.d(TAG, "Failed to connect to " + sharename);

            return success;
        } catch (IOException e) {
            Log.e(TAG, "Could not connect to SMB server: " + e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, "Authentication failed: " + e.getMessage(), e);
        }
        return false;
    }

    public void closeConnection(){
        try {
            share.close();
            share = null;
            Log.d(TAG,"Closed share");
        } catch (IOException e) {
            Log.d(TAG,"Failed to close share");
            throw new RuntimeException(e);
        }
        try {
            session.close();
            session = null;
            Log.d(TAG,"Closed session");
        } catch (IOException e) {
            Log.d(TAG,"Failed to close session");
            throw new RuntimeException(e);
        }
    }

    private String joinPaths(String base, String path){
        return base + "/" + path;
    }

    public List<String> listAllFiles(String root){
        List<String> result = new ArrayList<>();
        for(var item : share.list(root)){
            boolean isDir = (item.getFileAttributes() & FileAttributes.FILE_ATTRIBUTE_DIRECTORY.getValue()) != 0;
            String name = item.getFileName();
            Log.d(TAG, name);
            if("..".contains(name)){
                Log.d(TAG, "Skipping path: " + name);
                continue;
            }
            if(isDir){
                result.addAll(listAllFiles(this.joinPaths(root, name)));
            } else {
                result.add(name);
            }
        }
        return result;
    }


    public void downloadFile(DiskShare share, String remoteFilePath, java.io.File localFile) {
        try {
            File remoteFile = share.openFile(
                    remoteFilePath,
                    EnumSet.of(AccessMask.GENERIC_READ),
                    null,
                    SMB2ShareAccess.ALL,
                    SMB2CreateDisposition.FILE_OPEN,
                    null
            );

            try (InputStream is = remoteFile.getInputStream();
                 OutputStream os = new FileOutputStream(localFile)) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            Log.i(TAG,"Download complete: " + localFile.getAbsolutePath());

        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG,"Failed to download file: " + remoteFilePath);
        }
    }
}
