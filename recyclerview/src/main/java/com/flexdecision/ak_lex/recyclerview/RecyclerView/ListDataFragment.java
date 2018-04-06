package com.flexdecision.ak_lex.recyclerview.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.flexdecision.ak_lex.recyclerview.Data.Data;
import com.flexdecision.ak_lex.recyclerview.Data.DataList;
import com.flexdecision.ak_lex.recyclerview.R;

/**
 * Created by a_Lex on 4/4/2018.
 */

public class ListDataFragment extends Fragment {
    private RecyclerView listRV;
    private RecyclerView.Adapter adapterRV;
    private RecyclerView.LayoutManager layoutManager;
    public static final String LAYOUT_TYPE = "layoutType";
    public static final String TAG = ListDataFragment.class.getSimpleName();
    public static final int LINEAR_LAYOUT = 1;
    public static final int GRID_LAYOUT = 2;
    private ActionMode actionMode;
    private DataList androidVersions = new DataList();
    private DataList selected = new DataList();

    public static ListDataFragment newInstance(int layoutType){
        Bundle args = new Bundle();
        args.putInt(LAYOUT_TYPE, layoutType);
        ListDataFragment listRV = new ListDataFragment();
        listRV.setArguments(args);
        return listRV;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout, container, false);
        if (getArguments()!= null){
            Log.i(TAG, "type: " + getArguments().getInt(LAYOUT_TYPE));
            if (getArguments().getInt(LAYOUT_TYPE) == 1){
                layoutManager = new LinearLayoutManager(getActivity());
            }else {
                layoutManager = new GridLayoutManager(getActivity(), 2);
            }

        }
        listRV = view.findViewById(R.id.listRV);

        /*It improves efficient, if we not going to change the list data.
        listRV.setHasFixedSize(true);
        */


        listRV.setLayoutManager(layoutManager);
        listRV.setItemAnimator(new DefaultItemAnimator());
        listRV.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        adapterRV = new ListDataAdapter(androidVersions, selected);
        listRV.setAdapter(adapterRV);
        listRV.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), listRV, new RecyclerClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d(TAG, "Click - " + position);
                if (actionMode != null){
                    changeState(view, position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.d(TAG, "Long click - " + position);
                if (actionMode != null){
                    return;
                }

                actionMode = getActivity().startActionMode(actionModeCallback);
                changeState(view, position);
            }
        }));

        initializeAllData();
        adapterRV.notifyDataSetChanged();

        return view;
    }

    private void changeState(View view, int position){
        if (selected.contains(androidVersions.getData(position))){
            //view.setBackgroundColor(Color.TRANSPARENT);
            selected.removeData(androidVersions.getData(position));
            adapterRV.notifyItemChanged(position);
        }else {
            //view.setBackgroundColor(getResources().getColor(R.color.pressed_color));
            Log.d(TAG, "Set selected: " + position);
            selected.addData(androidVersions.getData(position));
            adapterRV.notifyItemChanged(position);
        }
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

    public void addDataItem(){
        if (androidVersions == null){
            return;
        }

        Data newItem = new Data("Unknown new Version", "empty", R.drawable.android_none);
        if (androidVersions.addData(newItem)){
            int number = androidVersions.getItemNumber(newItem);
            adapterRV.notifyItemChanged(number);
            listRV.scrollToPosition(number);
        }
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
            clearSelections();
        }
    };

    private void showSelected() {
        Log.d(TAG, "Show Selected");
        Log.d(TAG, selected.printAllHeaders());

    }

    private void clearSelections() {
        Log.d(TAG, "Clear selections");
        selected.clear();
        adapterRV.notifyDataSetChanged();
    }

    private void deleteSelected() {
        Log.d(TAG, "Delete");
        for(Data item: selected.getItems()){
            int number = androidVersions.getItemNumber(item);
            androidVersions.removeData(item);
            adapterRV.notifyItemRemoved(number);
        }
    }

}
