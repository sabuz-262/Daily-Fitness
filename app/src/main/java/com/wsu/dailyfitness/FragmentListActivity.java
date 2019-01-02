package com.wsu.dailyfitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;



public class FragmentListActivity extends FragmentActivity implements ItemListFragment.Callbacks {
    DatabaseHelper dbHelper;
    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_twopane);

        Intent intent = new Intent(getApplicationContext(), SensorBackgroundService.class);
        getApplicationContext().startService(intent);

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
            ((ItemListFragment) getSupportFragmentManager().findFragmentById(
                    R.id.item_list)).setActivateOnItemClick(true);

        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            if (id.equals("1")) {
            }

            else if (id.equals("2")) {

            }
        }
    }

}
