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
import java.io.PrintWriter;
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

    /*public static void unzip(String zipFile, String location) {
        try {
            FileInputStream fin = new FileInputStream(zipFile);
            unzip(fin, location);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }*/

    public static void unzip(String zipFile, String destination) {
        dirChecker(destination, "");
        FileInputStream fin = null;
        ZipInputStream zin = null;
        try {
            fin = new FileInputStream(zipFile);
            zin = new ZipInputStream(fin);
            ZipEntry ze = null;

            while ((ze = zin.getNextEntry()) != null) {
                Log.v(TAG, "Unzipping " + ze.getName());

                if (ze.isDirectory()) {
                    dirChecker(destination, ze.getName());
                } else {
                    File file = new File(destination + ze.getName());
                    if (!file.exists()) {
                        writeEntryToFile(file, zin);
                    }else {
                        Log.d(TAG, "Override existed file? Check yes or no");
                        if (clearFile(file)){
                            writeEntryToFile(file, zin);
                        }
                    }
                }

            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found", e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zin != null){
                try {
                    zin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fin != null){
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void writeEntryToFile(File file, ZipInputStream zin) {
        byte[] buffer = new byte[BUFFER_SIZE];
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);

            int count;
            while ((count = zin.read(buffer)) != -1) {
                fout.write(buffer, 0, count);

            }
            zin.closeEntry();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean clearFile(File file){
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            fout.write("".getBytes());
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            if (fout != null) {
                try {
                    fout.close();
                    Log.d(TAG, "Clear file" + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
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
            if (f.mkdirs()) {
                Log.w(TAG, "Failed to create folder " + f.getName());
            }
        }
    }
}
