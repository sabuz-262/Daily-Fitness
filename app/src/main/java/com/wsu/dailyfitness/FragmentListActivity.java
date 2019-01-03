package com.wsu.dailyfitness;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

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

            DailyStatsFragment fragment = new DailyStatsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(SensorBackgroundService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcasrReceiver, filter);
        // or `registerReceiver(broadcasrReceiver, filter)` for a normal broadcast
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcasrReceiver);
    }


    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            if (id.equals("1")) {
                DailyStatsFragment fragment = new DailyStatsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment).commit();
            }
            else if (id.equals("2")) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
               startActivity(intent);
            }
        }
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver broadcasrReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                String resultValue = intent.getStringExtra("resultValue");
                showFeedback(resultValue);
            }
        }
    };

    private void showFeedback(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(msg);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
