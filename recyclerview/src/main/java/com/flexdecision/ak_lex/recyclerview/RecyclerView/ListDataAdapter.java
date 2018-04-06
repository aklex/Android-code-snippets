package com.flexdecision.ak_lex.recyclerview.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flexdecision.ak_lex.recyclerview.Data.Data;
import com.flexdecision.ak_lex.recyclerview.Data.DataList;
import com.flexdecision.ak_lex.recyclerview.R;

/**
 * Created by a_Lex on 4/4/2018.
 */

public class ListDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final String TAG = ListDataAdapter.class.getSimpleName();
    private DataList dataList;
    private DataList selected;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView headlineTV;
        private TextView descriptionTV;
        private ImageView androidItemIV;


        public ViewHolder(View itemView) {
            super(itemView);
            headlineTV = itemView.findViewById(R.id.headlineTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            androidItemIV = itemView.findViewById(R.id.androidItemIV);
        }
    }

    public ListDataAdapter(DataList dataList, DataList selected) {
        this.dataList = dataList;
        this.selected = selected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Data data = dataList.getData(position);
        ViewHolder current = (ViewHolder) holder;

        if (selected.contains(data)){
            current.itemView.setBackgroundColor(Color.CYAN);
        }else {
            current.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        current.headlineTV.setText(data.getHeadline());
        current.descriptionTV.setText(data.getDescription());
        current.androidItemIV.setImageResource(data.getImage());

    }

    @Override
    public int getItemCount() {
        if (dataList == null){
            return 0;
        }
        return dataList.getSize();
    }
}
