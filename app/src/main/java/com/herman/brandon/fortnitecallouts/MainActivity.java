package com.herman.brandon.fortnitecallouts;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.herman.brandon.fortnitecallouts.database.Player;
import com.herman.brandon.fortnitecallouts.database.PlayerDbHelper;
import com.herman.brandon.fortnitecallouts.Helpers.StatsContent;

public class MainActivity extends AppCompatActivity implements StatsFragment.OnStatsFragmentInteractionListener {
    /**
     * Single instance of a request queue
     */
    public static RequestQueue requestQueue;

    private BottomNavigationView navigation;
    private MapFragment mapFragment;
    private StatsFragment statsFragment;
    private StoreFragment storeFragment;

    private PlayerDbHelper playerDbHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    fragmentTransaction.replace(R.id.frame_layout, mapFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_stats:
                    fragmentTransaction.replace(R.id.frame_layout, statsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_weapons:
                    fragmentTransaction.replace(R.id.frame_layout, storeFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerDbHelper = new PlayerDbHelper(this);
        requestQueue = Volley.newRequestQueue(this);

        String userName = getUsersEpicAccountName();
        statsFragment = StatsFragment.newInstance(userName, 2);
        storeFragment = new StoreFragment();
        mapFragment = new MapFragment();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_stats);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.clear:
                clearAccountName();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        playerDbHelper.close();
        super.onDestroy();
    }

    protected String getUsersEpicAccountName() {
        SQLiteDatabase db = playerDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                Player.PlayerEntry.COLUMN_NAME_EPIC_ACCOUNT_NAME,
                Player.PlayerEntry.COLUMN_NAME_IS_FRIEND
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = Player.PlayerEntry.COLUMN_NAME_IS_FRIEND + " = ?";
        String[] selectionArgs = { "0" };

        Cursor cursor = db.query(
                Player.PlayerEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        String playerName = null;
        if (cursor.moveToNext()) {
            playerName = cursor.getString(
                    cursor.getColumnIndexOrThrow(Player.PlayerEntry.COLUMN_NAME_EPIC_ACCOUNT_NAME));
        }
        cursor.close();
        db.close();

        return playerName;
    }

    protected void clearAccountName() {
        SQLiteDatabase db = playerDbHelper.getReadableDatabase();

        db.execSQL("delete from "+ Player.PlayerEntry.TABLE_NAME);
        db.close();

        finish();
        startActivity(getIntent());
    }

    @Override
    public void onEpicAccountLinked(String accountName) {
        // Gets the data repository in write mode
        SQLiteDatabase db = playerDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Player.PlayerEntry.COLUMN_NAME_EPIC_ACCOUNT_NAME, accountName);
        values.put(Player.PlayerEntry.COLUMN_NAME_IS_FRIEND, 0);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(Player.PlayerEntry.TABLE_NAME, null, values);
    }
}
