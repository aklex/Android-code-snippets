package com.flexdecision.ak_lex.listview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by a_Lex on 3/22/2018.
 */

public class DataList {
    private List<DataItem> items = new ArrayList<>();

    public void addData(DataItem item){
        items.add(item);
    }

    public int getSize(){
        return items.size();
    }

    public DataItem getData(int itemNumber){
        if (itemNumber >= 0 && itemNumber < items.size()){
            return items.get(itemNumber);
        }else{
            return new DataItem("", "", 0);
        }
    }

    public boolean removeData(DataItem itemToDelete){
        Iterator<DataItem> iterator = items.iterator();
        while(iterator.hasNext()) {
            DataItem item = iterator.next();
            if (item.equals(itemToDelete)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public List<DataItem> getItems() {
        return items;
    }


}
