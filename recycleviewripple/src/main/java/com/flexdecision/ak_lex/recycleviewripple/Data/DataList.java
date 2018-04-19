package com.flexdecision.ak_lex.recycleviewripple.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by a_Lex on 3/22/2018.
 */

public class DataList {
    private List<Data> items = new ArrayList<>();

    public static DataList newInstance(DataList initial){
        DataList newDataList = new DataList();
        for (Data item: initial.getItems()){
            newDataList.addData(item);
        }
        return newDataList;
    }

    public boolean addData(Data item){
        return items.add(item);
    }

    public void insertData(Data item){
        items.add(0, item);
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

    public int getItemNumber(Data dataItem){
        return items.lastIndexOf(dataItem);
    }

    public boolean contains(Data data){
        return items.contains(data);
    }

    public String printAllHeaders(){
        StringBuilder str = new StringBuilder();
        for(Data item: items){
            str.append(item.getHeadline() + "\n");
        }
        return str.toString();
    }

    public void clear(){
        items.clear();
    }


    public void addAll(DataList newItems) {
        items.addAll(newItems.getItems());
    }


}
