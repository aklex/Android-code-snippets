package com.flexdecision.ak_lex.recycleviewripple.RecyclerView;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.flexdecision.ak_lex.recycleviewripple.Data.Data;
import com.flexdecision.ak_lex.recycleviewripple.Data.DataList;
import com.flexdecision.ak_lex.recycleviewripple.R;


/**
 * Created by a_Lex on 4/13/2018.
 */

public class AdapterRV extends SelectableAdapter<RecyclerView.ViewHolder> implements Filterable{
    public static final String TAG = AdapterRV.class.getSimpleName();
    private DataList dataList;
    private DataList filteredDataList;


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty()){
                    filteredDataList = dataList;
                }else{
                    DataList filtered = new DataList();
                    for (Data item: dataList.getItems()){
                        if (item.getHeadline().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(constraint.toString().toLowerCase())){
                            filtered.addData(item);
                        }
                    }

                    filteredDataList = filtered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredDataList = (DataList) results.values;
                for (Data item: filteredDataList.getItems()){
                    Log.d(TAG, item.getHeadline());
                }
                Log.d(TAG, "Size: " + filteredDataList.getSize());
                notifyDataSetChanged();
            }
        };
    }

    //private DataList selected;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout mainLL;
        private TextView simpleTV;
        private TextView descriptionTV;
        private ImageView androidItemIV;

        public ViewHolder(View itemView) {
            super(itemView);
            mainLL = itemView.findViewById(R.id.mainLL);
            simpleTV = itemView.findViewById(R.id.simpleTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            androidItemIV = itemView.findViewById(R.id.androidItemIV);
        }
    }

    public AdapterRV(DataList dataList) {
        this.dataList = dataList;
        this.filteredDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Data item = filteredDataList.getData(position);
        ViewHolder current = (ViewHolder) holder;
        current.simpleTV.setText(item.getHeadline());
        current.descriptionTV.setText(item.getDescription());
        current.androidItemIV.setImageResource(item.getImage());
        Log.d("TAG", "Update: " + position + ", image: " + item.getImage());

        if (isSelected(position)){
            current.mainLL.setBackgroundColor(Color.CYAN);
        }else{
            current.mainLL.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.getSize();
    }

    public void swapItems(DataList newDataList){
        Log.d(TAG, "Swap items");
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DataDiffCallback(this.dataList, newDataList), true);

        this.dataList.clear();
        this.dataList.addAll(newDataList);
        //this.dataList = newDataList;

        diffResult.dispatchUpdatesTo(this);
    }




}
