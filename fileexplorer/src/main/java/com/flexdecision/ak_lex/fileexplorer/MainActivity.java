package com.flexdecision.ak_lex.fileexplorer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.flexdecision.ak_lex.fileexplorer.FilesListView.DataCell;
import com.flexdecision.ak_lex.fileexplorer.FilesListView.DirectoryCell;
import com.flexdecision.ak_lex.fileexplorer.FilesListView.FileCell;
import com.flexdecision.ak_lex.fileexplorer.FilesListView.FileList;
import com.flexdecision.ak_lex.fileexplorer.FilesListView.FileListViewAdapter;

import java.io.File;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements  ActivityCompat.OnRequestPermissionsResultCallback{
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 2201;
    View mainView;
    private ListView simpleLV;
    private FileList filesList;
    private FileListViewAdapter adapter;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_EXTERNAL_STORAGE){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted.
                accessPermission();
                showDirFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

            } else {
                // Permission request was denied.
                denialPermission();
                if (ExternalStorages.externalStorageAvailable() && ExternalStorages.isExternalStorageReadable()) {
                    showDirFiles(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
                }else{
                    showDirFiles(getFilesDir());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = findViewById(R.id.mainView);

        simpleLV = findViewById(R.id.filesLV);
        filesList = new FileList();
        adapter = new FileListViewAdapter(filesList, this);
        simpleLV.setAdapter(adapter);

        simpleLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Click: " + position);
                DataCell fileInfo = filesList.getItems().get(position);
                if (fileInfo instanceof DirectoryCell){
                    DirectoryCell dir = (DirectoryCell) fileInfo;
                    showDirFiles(dir.getFile());
                }
                else{
                    Toast.makeText(MainActivity.this, "Opening..." + fileInfo.getHeadline(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        showDefaultDir();

    }
    /*public FileList getAllData(){
        FileList allData = new FileList();
        String[] headlines = getResources().getStringArray(R.array.version_name);
        String[] descriptions = getResources().getStringArray(R.array.version_description);
        int[] images = { R.drawable.android_none, R.drawable.android_none, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
                R.drawable.ginderbread, R.drawable.honeycomb, R.drawable.ice_cream_sandwich,
                R.drawable.jelly_bean, R.drawable.kitkat, R.drawable.lollipop, R.drawable.marshmallow,
                R.drawable.nougat, R.drawable.oreo, R.drawable.android_none};

        for(int i=0; i< headlines.length; i++){
            allData.addFileInfo(new Data(headlines[i], descriptions[i], images[i]));
        }
        return allData;
    }*/



    private void showDirFiles(File dir){
        Log.d(TAG, "Show dir's files - " + dir.getAbsolutePath());
        if (dir == null) {
            Log.d(TAG, "Dir - null");
            return;
        }
        //Log.d(TAG, "Parent: " + dir.getParentFile().toString() + ", " + dir.getParent());
        filesList.clear();


        if (dir.isDirectory()){
            File[] files = dir.listFiles();
            if (files != null) {
                Log.d(TAG, "Dir: " + dir.getName() +": " + dir.listFiles().length);
                for (File i : files) {
                    Log.d(TAG, "File: " + i.getName());
                    DataCell fileInfo;
                    if (i.isDirectory()) {
                        fileInfo = new DirectoryCell(i, R.drawable.ic_folder);
                    } else {
                        fileInfo = new FileCell(i, R.drawable.ic_file);
                    }
                    filesList.addFileInfo(fileInfo);

                }
                Collections.sort(filesList.getItems());
            }else {
                Log.d(TAG, "Files - null");
            }
        }else {
            Log.d(TAG, "Doesn't directory");

        }
        if (dir.getParent() != null){
            filesList.insertFileInfo(0, new DirectoryCell(dir.getParentFile(), R.drawable.ic_parent));
        }

        adapter.notifyDataSetChanged();

    }

    private void showDefaultDir() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            showDirFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestStoragePermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mainView , "External storage access is required to display the image gallery.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Request the permission
                    //TODO check api version
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MainActivity.PERMISSION_REQUEST_EXTERNAL_STORAGE);
                }
            }).show();

        } else {
            Snackbar.make(mainView,
                    "Permission is not available. Requesting external storage permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            //TODO check api version
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MainActivity.PERMISSION_REQUEST_EXTERNAL_STORAGE);
        }
    }



    public void accessPermission(){
        Snackbar.make(mainView, "External storage permission was granted. Starting image gallery.",
                Snackbar.LENGTH_SHORT)
                .show();
        //startImageGallery();
    }

    public void denialPermission(){
        Snackbar.make(mainView, "External storag permission request was denied.",
                Snackbar.LENGTH_SHORT)
                .show();
    }
}
