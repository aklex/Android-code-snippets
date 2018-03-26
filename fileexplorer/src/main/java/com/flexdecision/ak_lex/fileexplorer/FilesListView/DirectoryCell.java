package com.flexdecision.ak_lex.fileexplorer.FilesListView;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by a_Lex on 3/26/2018.
 */

public class DirectoryCell extends DataCell{

    public DirectoryCell(File file, int image) {
        super(file, image);
    }
    @Override
    public String getDescription() {
        return super.getFile().getAbsolutePath();
    }

}
