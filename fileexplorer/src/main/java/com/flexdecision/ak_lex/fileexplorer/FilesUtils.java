package com.flexdecision.ak_lex.fileexplorer;

import java.io.File;

/**
 * Created by a_Lex on 3/26/2018.
 */

public class FilesUtils {
    public static void cleanDir(File dir) {
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            file.delete();
        }
    }
}
