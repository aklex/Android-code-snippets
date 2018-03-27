package com.flexdecision.ak_lex.zip;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by a_Lex on 3/27/2018.
 */

public class Decompress {
    private static final int BUFFER_SIZE = 1024 * 10;
    private static final String TAG = Decompress.class.getSimpleName();

    /*public static void unzipFromAssets(Context context, String zipFile, String destination) {
        try {
            if (destination == null || destination.length() == 0)
                destination = context.getFilesDir().getAbsolutePath();
            InputStream stream = context.getAssets().open(zipFile);
            unzip(stream, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void unzip(String zipFile, String location) {
        try {
            FileInputStream fin = new FileInputStream(zipFile);
            unzip(fin, location);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void unzip(InputStream stream, String destination) {
        dirChecker(destination, "");
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            ZipInputStream zin = new ZipInputStream(stream);
            ZipEntry ze = null;

            while ((ze = zin.getNextEntry()) != null) {
                Log.v(TAG, "Unzipping " + ze.getName());

                if (ze.isDirectory()) {
                    dirChecker(destination, ze.getName());
                } else {
                    File f = new File(destination + ze.getName());
                    if (!f.exists()) {
                        FileOutputStream fout = new FileOutputStream(destination + ze.getName());
                        int count;
                        while ((count = zin.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                        }
                        zin.closeEntry();
                        fout.close();
                    }
                }

            }
            zin.close();
        } catch (Exception e) {
            Log.e(TAG, "unzip", e);
        }

    }

    public static void zip(String[] files, String zipFileName){
        try {
            BufferedInputStream bis = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(dest));
            byte[] buffer = new byte[BUFFER_SIZE];
            for (int i=0; i < files.length; i++){
                Log.v(TAG, "Zipping: " + files[i]);
                FileInputStream fis = new FileInputStream(files[i]);
                bis = new BufferedInputStream(fis, BUFFER_SIZE);

                ZipEntry entry = new ZipEntry(files[i].substring((files[i]).lastIndexOf("/") +1));
                zos.putNextEntry(entry);

                int count;
                while ( (count = bis.read(buffer, 0, BUFFER_SIZE)) != -1){
                    zos.write(buffer, 0, count);
                }
                bis.close();
            }
            zos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void dirChecker(String destination, String dir) {
        File f = new File(destination + dir);

        if (!f.isDirectory()) {
            boolean success = f.mkdirs();
            if (!success) {
                Log.w(TAG, "Failed to create folder " + f.getName());
            }
        }
    }
}
