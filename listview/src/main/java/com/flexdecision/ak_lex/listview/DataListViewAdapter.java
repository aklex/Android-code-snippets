package com.flexdecision.ak_lex.listview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by a_Lex on 3/22/2018.
 */

public class DataListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public static final String TAG = DataListViewAdapter.class.getSimpleName();

    private DataList items;
    private Context context;

    //Pattern ViewHolder increases performance
    static class ViewHolder{
        public TextView headlineTV;
        public TextView descriptionTV;
        public ImageView androidItemIV;
    }

    public DataListViewAdapter(DataList items, Context context) {
        this.items = items;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.getSize();
    }

    @Override
    public Object getItem(int position) {
        return items.getData(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (convertView == null) {
            Log.d(TAG, "Create View");
            itemView = inflater.inflate(R.layout.data_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.headlineTV = itemView.findViewById(R.id.headlineTV);
            viewHolder.descriptionTV = itemView.findViewById(R.id.descriptionTV);
            viewHolder.androidItemIV = itemView.findViewById(R.id.androidItemIV);
            itemView.setTag(viewHolder);
            //itemView.setOnClickListener(v-> showToast(dataItem.getHeadline()));
        }
        ViewHolder holder = (ViewHolder) itemView.getTag();
        Data dataItem = items.getData(position);
        holder.headlineTV.setText(dataItem.getHeadline());
        holder.descriptionTV.setText(dataItem.getDescription());
        holder.androidItemIV.setImageResource(dataItem.getImage());

        return itemView;
    }

    private void showToast(String headline) {
        Toast.makeText(context, "Clicked: " + headline, Toast.LENGTH_SHORT).show();
    }
}
