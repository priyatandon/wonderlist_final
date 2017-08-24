package com.ptandon.wonderlist;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    private static String TAG="MainActivity";
    Fragment fragment;
    Class fragmentClass;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private final int REQUEST_CODE = 1;
    private SQLiteDatabase db;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar, R.string.open, R.string.close);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navList);
        setupDrawerContent(navigationView);

        if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_1, 0);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {


                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        Log.d(TAG,"menu "+menuItem);
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

    //    Log.d(TAG,"test"+menuItem.getItemId());


      switch (menuItem.getItemId()) {
            case R.id.nav_1:

                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_2:

                fragmentClass = CompletedTaskFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment,fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        invalidateOptionsMenu();
        mDrawerLayout.closeDrawers();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                onAddItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    public void onAddItem() {
        Intent addIntent = new Intent(this, AddItemActivity.class);
        startActivityForResult(addIntent, REQUEST_CODE);
    }

    public void addItem(String itemText, long due_date, String priority, String taskNoteInput) {

        Log.d(TAG,"add here"+itemText+" "+due_date+" "+taskNoteInput+" "+priority);

        due_date = due_date / 1000;
        ContentValues values = new ContentValues();
        values.put(ListContract.ListEntry.COLUMN_TODOTEXT, itemText);
        values.put(ListContract.ListEntry.COLUMN_DUE_DATE, due_date);
        values.put(ListContract.ListEntry.COLUMN_PRIORITY, priority);
        values.put(ListContract.ListEntry.COLUMN_STATUS, "TODO");
        values.put(ListContract.ListEntry.COLUMN_TASKNOTE, taskNoteInput);
        Log.d(TAG, taskNoteInput);
        Log.d(TAG, "insert status" + db.insert(ListContract.ListEntry.TABLE_NAME, null, values));

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                String itemToSave = data.getStringExtra("itemToSave");
                long due_date = data.getLongExtra("due_date", 0);
                String taskNote = data.getStringExtra("taskNote");
                String priority_spinner_value = data.getStringExtra("priority_spinner_value");

                Log.d(TAG,taskNote+" "+itemToSave+" "+due_date+" "+priority_spinner_value);
                addItem(itemToSave, due_date, priority_spinner_value, taskNote);

            }
        }
    }

    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }


}