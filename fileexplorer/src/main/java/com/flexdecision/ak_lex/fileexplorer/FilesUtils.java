package com.flexdecision.ak_lex.fileexplorer;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by a_Lex on 3/26/2018.
 */

public class FilesUtils {
    public static final String TAG = FilesUtils.class.getSimpleName();
    public static void cleanDir(File dir) {
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            file.delete();
        }
    }

    public static void copyFile(File source, File destination){
        copyFile(source.getAbsolutePath(), destination.getAbsolutePath());
    }

    public static void copyFile(String source, String destination){
        FileInputStream in = null;
        FileOutputStream out = null;
        BufferedInputStream bin= null;
        BufferedOutputStream bout = null;

        try {
            bin = new BufferedInputStream(new FileInputStream(source));
            bout = new BufferedOutputStream(new FileOutputStream(destination));

            int c = 0;
            while ( (c = bin.read()) != -1) {
                System.out.println(c);
                bout.write(c);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bin != null){
                try {
                    bin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bout != null){
                try {
                    bout.close();
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

    public static void readFile(File file){

    }
}
