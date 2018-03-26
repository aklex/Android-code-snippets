package com.flexdecision.ak_lex.fileexplorer.FilesListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by a_Lex on 3/26/2018.
 */

public class FileList {
    private List<DataCell> items = new ArrayList<>();

    public void addFileInfo(DataCell item){
        items.add(item);
    }

    public void insertFileInfo(int position, DataCell item){
        items.add(position, item);
    }

    public int getSize(){
        return items.size();
    }

    public DataCell getFileInfo(int itemNumber){
        if (itemNumber >= 0 && itemNumber < items.size()) {
            return items.get(itemNumber);
        }
        return null;
    }

    public boolean removeFileInfo(DataCell itemToDelete){
        Iterator<DataCell> iterator = items.iterator();
        while(iterator.hasNext()) {
            DataCell item = iterator.next();
            if (item.equals(itemToDelete)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public List<DataCell> getItems() {
        return items;
    }


    public void clear() {
        items.clear();
    }
}