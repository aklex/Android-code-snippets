package com.flexdecision.ak_lex.recycleviewripple.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flexdecision.ak_lex.recycleviewripple.Data.Data;
import com.flexdecision.ak_lex.recycleviewripple.Data.DataList;
import com.flexdecision.ak_lex.recycleviewripple.MainActivity;
import com.flexdecision.ak_lex.recycleviewripple.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by a_Lex on 4/13/2018.
 */

public class ListDataFragment extends Fragment implements MainActivity.IOnBackPressed{
    public static final String TAG = ListDataFragment.class.getSimpleName();
    private DataList androidVersions = new DataList();
    private RecyclerView.LayoutManager manager;
    private AdapterRV adapterRV;
    private RecyclerView listRV;
    public static final int LINEAR_LAYOUT = 1;
    public static final int GRID_LAYOUT = 2;
    public static final String LAYOUT_TYPE = "layoutType";
    public SwipeRefreshLayout swipeRefreshLayout;
    private int counter = 0;
    private SearchView searchView;

    private ActionMode actionMode;

    public static ListDataFragment newInstance(int layoutType){
        Bundle args = new Bundle();
        args.putInt(LAYOUT_TYPE, layoutType);
        ListDataFragment listRV = new ListDataFragment();
        listRV.setArguments(args);
        return listRV;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterRV.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterRV.getFilter().filter(newText);
                return false;
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search){
            return true;
        }
        if (item.getItemId() == android.R.id.home){
            //Close Search console if opened
            if (!searchView.isIconified()) {
                searchView.setIconified(true);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.showSelected:
                    showSelected();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.deleteSelected:
                    deleteSelected();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapterRV.clearSelection();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_list_fragment, container, false);

        initializeAllData();

        manager = new LinearLayoutManager(getActivity());


        listRV = view.findViewById(R.id.listRV);
        adapterRV = new AdapterRV(androidVersions);
        listRV.setLayoutManager(manager);
        listRV.setAdapter(adapterRV);

        listRV.setItemAnimator(new DefaultItemAnimator());
        listRV.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        ItemClickSupport.addTo(listRV).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.d(TAG, "Click - " + position);
                if (actionMode != null) {
                    toggleSelection(position);
                }
            }
        });
        ItemClickSupport.addTo(listRV).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                Log.d(TAG, "Long click - " + position);

                if (actionMode == null) {
                    actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                }
                toggleSelection(position);

                return false;
            }
        });

        adapterRV.notifyDataSetChanged();
        manager.scrollToPosition(0);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        //adapterRV.swapItems(newDataList);

        return view;
    }

    private void refreshData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterRV.swapItems(getNewDataBatch());
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    public void addDataItem(){
        if (androidVersions == null){
            return;
        }

        Data newItem = new Data("Unknown new Version", "empty", R.drawable.android_none);
        if (androidVersions.addData(newItem)){
            int number = androidVersions.getItemNumber(newItem);
            adapterRV.notifyItemChanged(number);
            //adapterRV.notifyDataSetChanged();
            //adapterRV.swapItems(androidVersions);
            listRV.scrollToPosition(number);
        }


    }

    private void insertDataItem() {
        if (androidVersions == null){
            return;
        }
        Data newItem = new Data("Unknown new Version", "empty" + counter++, R.drawable.android_none);
        androidVersions.insertData(newItem);
        adapterRV.notifyDataSetChanged();
        //adapterRV.swapItems(androidVersions);
        listRV.scrollToPosition(0);
    }

    public void initializeAllData(){
        String[] headlines = getResources().getStringArray(R.array.version_name);
        String[] descriptions = getResources().getStringArray(R.array.version_description);
        int[] images = { R.drawable.android_none, R.drawable.android_none, R.drawable.cupcake, R.drawable.donut, R.drawable.eclair, R.drawable.froyo,
                R.drawable.ginderbread, R.drawable.honeycomb, R.drawable.ice_cream_sandwich,
                R.drawable.jelly_bean, R.drawable.kitkat, R.drawable.lollipop, R.drawable.marshmallow,
                R.drawable.nougat, R.drawable.oreo, R.drawable.android_none};

        for(int i=0; i< headlines.length; i++){
            androidVersions.addData(new Data(headlines[i], descriptions[i], images[i]));
        }
    }

    public DataList getNewDataBatch(){
        DataList newData = new DataList();
        String[] headlines = getResources().getStringArray(R.array.version_name);
        String[] descriptions = getResources().getStringArray(R.array.version_description);
        int[] images = { R.drawable.android_none, R.drawable.android_none, R.drawable.android_none, R.drawable.android_none, R.drawable.eclair, R.drawable.froyo,
                R.drawable.ginderbread, R.drawable.honeycomb, R.drawable.ice_cream_sandwich,
                R.drawable.jelly_bean, R.drawable.kitkat, R.drawable.lollipop, R.drawable.marshmallow,
                R.drawable.nougat, R.drawable.oreo, R.drawable.android_none};

        for(int i=0; i< headlines.length; i++){
            newData.addData(new Data(headlines[i], descriptions[i], images[i]));
        }
        return newData;
    }

    private void showSelected() {
        Log.d(TAG, "Show Selected");
        StringBuffer buffer = new StringBuffer();
        for(Integer i : adapterRV.getSelectedItems()){
            buffer.append(androidVersions.getData(i).getHeadline() + "\t");
        }
        Toast.makeText(getActivity(), buffer.toString(), Toast.LENGTH_LONG).show();
    }


    private void toggleSelection(int position) {
        adapterRV.toggleSelection(position);
        int count = adapterRV.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }


    private void deleteSelected() {
        Log.d(TAG, "Delete");

        DataList newDataList = DataList.newInstance(androidVersions);
        Log.d(TAG, "size before = " + newDataList.getSize());
        List<Integer> selected = adapterRV.getSelectedItems();
        List<Data> delete = new ArrayList<>();
        for(Integer i: selected){
            delete.add(androidVersions.getData(i));
        }

        for(Data item: delete){
            newDataList.removeData(item);
        }
        Log.d(TAG, "size after = " + newDataList.getSize());

        adapterRV.swapItems(newDataList);
        /*for(Data item: selected.getItems()){
            int number = androidVersions.getItemNumber(item);
            androidVersions.removeData(item);
            adapterRV.notifyItemRemoved(number);
        }*/
    }

    @Override
    public boolean onBackPressed() {
        if (!searchView.isIconified()){
            searchView.setIconified(true);
            return true;
        }
        return false;
    }

}
