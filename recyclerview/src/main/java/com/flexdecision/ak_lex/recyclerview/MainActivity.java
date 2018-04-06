package com.flexdecision.ak_lex.recyclerview;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.flexdecision.ak_lex.recyclerview.RecyclerView.ListDataFragment;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    public static final String FRAGMENT_ANDROID_V = "android versions";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> addItem());

        ListDataFragment listRV = ListDataFragment.newInstance(ListDataFragment.LINEAR_LAYOUT);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContainer, listRV, FRAGMENT_ANDROID_V)
                //.addToBackStack(null)
                .commit();
    }

    private void addItem() {
        FragmentManager manager = getSupportFragmentManager();
        ListDataFragment listDataFragment = (ListDataFragment) manager.findFragmentByTag(FRAGMENT_ANDROID_V);
        if (listDataFragment == null){
            return;
        }
        listDataFragment.addDataItem();
    }


}
