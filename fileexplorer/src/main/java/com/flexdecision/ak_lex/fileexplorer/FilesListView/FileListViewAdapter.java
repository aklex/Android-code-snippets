package com.flexdecision.ak_lex.fileexplorer.FilesListView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flexdecision.ak_lex.fileexplorer.R;

/**
 * Created by a_Lex on 3/26/2018.
 */

public class FileListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public static final String TAG = FileListViewAdapter.class.getSimpleName();

    private FileList items;
    private Context context;

    //Pattern ViewHolder increases performance
    static class ViewHolder{
        public TextView headlineTV;
        public TextView descriptionTV;
        public ImageView androidItemIV;
    }

    public FileListViewAdapter(FileList items, Context context) {
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
        return items.getFileInfo(position);
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
            itemView = inflater.inflate(R.layout.file_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.headlineTV = itemView.findViewById(R.id.headlineTV);
            viewHolder.descriptionTV = itemView.findViewById(R.id.descriptionTV);
            viewHolder.androidItemIV = itemView.findViewById(R.id.androidItemIV);
            itemView.setTag(viewHolder);
            //itemView.setOnClickListener(v-> showToast(fileInfoItem.getHeadline()));
        }
        ViewHolder holder = (ViewHolder) itemView.getTag();
        DataCell dataCell = items.getFileInfo(position);
        holder.headlineTV.setText(dataCell.getHeadline());
        holder.descriptionTV.setText(dataCell.getDescription());
        holder.androidItemIV.setImageResource(dataCell.getImage());

        return itemView;
    }

    private void showToast(String headline) {
        Toast.makeText(context, "Clicked: " + headline, Toast.LENGTH_SHORT).show();
    }
}