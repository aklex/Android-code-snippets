package com.flexdecision.ak_lex.recycleviewripple;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.flexdecision.ak_lex.recycleviewripple.RecyclerView.ListDataFragment;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String FRAGMENT_ANDROID_V = "android versions";

    public interface IOnBackPressed{
        boolean onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.toolbar_title);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ANDROID_V);
        if ( !(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}

