package com.flexdecision.ak_lex.zip;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        Log.d(TAG, dir.getAbsolutePath());
        String file = dir.getAbsolutePath() + "/imagesqwe.zip";
        String destination  = dir.getAbsolutePath() + "/unzipped/";
        Decompress.unzip(file, destination);

        /*String[] filesStr = { dir.getAbsolutePath() + "/Bit text.txt", dir.getAbsolutePath() + "/parcel.png"};
        String destFile = dir.getAbsolutePath() + "/dest.zip";
        Decompress.zip(filesStr, destFile);*/
    }


}
