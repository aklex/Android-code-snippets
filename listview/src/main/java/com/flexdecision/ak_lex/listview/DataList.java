package com.flexdecision.ak_lex.listview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by a_Lex on 3/22/2018.
 */

public class DataList {
    private List<Data> items = new ArrayList<>();

    public void addData(Data item){
        items.add(item);
    }

    public int getSize(){
        return items.size();
    }

    public Data getData(int itemNumber){
        if (itemNumber >= 0 && itemNumber < items.size()){
            return items.get(itemNumber);
        }else{
            return new Data("", "", 0);
        }
    }

    public boolean removeData(Data itemToDelete){
        Iterator<Data> iterator = items.iterator();
        while(iterator.hasNext()) {
            Data item = iterator.next();
            if (item.equals(itemToDelete)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public List<Data> getItems() {
        return items;
    }


}
