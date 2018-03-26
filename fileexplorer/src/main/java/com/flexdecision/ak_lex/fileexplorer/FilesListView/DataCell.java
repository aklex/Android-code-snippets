package com.flexdecision.ak_lex.fileexplorer.FilesListView;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by a_Lex on 3/27/2018.
 */

public abstract class DataCell implements Comparable<DataCell> {
    private File file;
    private int image;

    public DataCell(File file, int image) {
        this.file = file;
        this.image = image;
    }

    public String getHeadline() {
        return file.getName();
    }


    public abstract String getDescription();

    public int getImage() {
        return image;
    }

    public File getFile(){
        return file;
    }
    @Override
    public int compareTo(@NonNull DataCell o) {
        return file.getName().compareTo(o.getFile().getName());
    }
}
