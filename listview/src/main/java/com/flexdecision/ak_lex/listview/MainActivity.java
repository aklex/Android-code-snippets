package com.flexdecision.ak_lex.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private ListView simpleLV;
    private DataList data;
    private DataListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleLV = findViewById(R.id.simpleLV);
        data = getAllData();
        adapter = new DataListViewAdapter(data, this);
        simpleLV.setAdapter(adapter);
        simpleLV.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                Log.d(TAG, "Position: " + position + "-" + checked);
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                Log.d(TAG, "Item clicked: " + item.getItemId());
                switch (item.getItemId()){
                    case R.id.showSelected:
                        Log.d(TAG, "Selected - " + simpleLV.getCheckedItemCount());
                        showSelected();
                        mode.finish();
                        return true;
                    case R.id.deleteSelected:
                        Log.d(TAG, "Delete - " + simpleLV.getCheckedItemCount());
                        deleteSelected();
                        mode.finish();
                        return true;
                    default:
                        Log.d(TAG, "Other item");
                        return false;
                }

            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {

            }
        });

        simpleLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "position: " + position);
                view.setSelected(true);
                return true;
            }
        });

    }

    private void deleteSelected() {
        DataList list = getSelectedDataItems();
        if (list == null || list.getSize() == 0){
            Log.d(TAG, "Nothing to delete");
            return;
        }

        for(int i=0; i < list.getSize(); i++){
            data.removeData(list.getData(i));
        }


        adapter.notifyDataSetChanged();
    }


    private void showSelected() {
        DataList list = getSelectedDataItems();
        StringBuilder str = new StringBuilder();
        for (Data i: list.getItems()){
            str.append(i.getHeadline());
            str.append(", ");
        }
        Toast.makeText(this, str.toString(), Toast.LENGTH_SHORT).show();
    }

    private DataList getSelectedDataItems(){
        SparseBooleanArray checked = simpleLV.getCheckedItemPositions();
        DataList selectedData = new DataList();
        Log.d(TAG, "size: " + checked.size());
        Log.d(TAG, checked.toString());
        if (checked == null || checked.size() == 0){
            Log.d(TAG, "Nothing");
            return selectedData;
        }
        for (int i = 0; i < checked.size(); i++) {
            selectedData.addData(data.getData(checked.keyAt(i)));
        }
        return selectedData;
    }


    public DataList getAllData(){
        DataList allData = new DataList();
        String[] headlines = getResources().getStringArray(R.array.version_name);
        String[] descriptions = getResources().getStringArray(R.array.version_description);
        int[] images = { R.drawable.android_none, R.drawable.android_none, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
                            R.drawable.ginderbread, R.drawable.honeycomb, R.drawable.ice_cream_sandwich,
                            R.drawable.jelly_bean, R.drawable.kitkat, R.drawable.lollipop, R.drawable.marshmallow,
                            R.drawable.nougat, R.drawable.oreo, R.drawable.android_none};

        for(int i=0; i< headlines.length; i++){
            allData.addData(new Data(headlines[i], descriptions[i], images[i]));
        }
        return allData;
    }

}
