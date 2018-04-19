package com.flexdecision.ak_lex.recycleviewripple.RecyclerView;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.flexdecision.ak_lex.recycleviewripple.Data.DataList;


/**
 * Created by a_Lex on 4/11/2018.
 */

public class DataDiffCallback extends DiffUtil.Callback {
    private DataList oldData;
    private DataList newData;
    public static final String TAG = DataDiffCallback.class.getSimpleName();

    public DataDiffCallback(DataList oldData, DataList newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData.getSize();
    }

    @Override
    public int getNewListSize() {
        return newData.getSize();
    }
    /*Compare ids of the each item, from old and new data lists.
     If ids are different, there are no sense to compare object's data by areContentsTheSame method.
     If ids are same, or data object has no such special property as unique id, return true and compare
     data objects by areContentsTheSame method.
    */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true; //oldData.getData(oldItemPosition) == newData.getData(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        boolean res = oldData.getData(oldItemPosition).equals(newData.getData(newItemPosition));
        Log.d(TAG, oldItemPosition + "-" + newItemPosition + ", res: " + res);
        return res;
    }


}
