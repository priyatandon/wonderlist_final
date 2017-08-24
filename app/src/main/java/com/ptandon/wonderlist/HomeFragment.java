package com.ptandon.wonderlist;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.ptandon.wonderlist.ListContract.ListEntry.TABLE_NAME;


public class HomeFragment extends Fragment implements
        EditDialogFragment.EditDialogListener,DeleteDialogFragment.DeleteDialogListener {

    private static String TAG = "HomeFragment";

    private static Calendar dateTime = Calendar.getInstance();
    private static long dateInMilliSeconds;
    private final int REQUEST_CODE=1;
    ArrayList<List> items;
    CustomAdapter itemsAdapter ;
    ListView itemList;
    private DatePickerDialog.OnDateSetListener mDateListener;
    String ID_upd_str;
    int updateID;
    int del_pos;
    String ID_del_str;
    int delID;
    Fragment fragment;

    private SQLiteDatabase db;
    DBHelper dbHelper;

  /*  private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;*/


    public HomeFragment() {

    }

   /* public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.content_main, container,
                false);
        dbHelper=new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();

       // populateArrayItems();

        itemList = (ListView)view.findViewById(R.id.itemList);
        TextView emptyText = (TextView)view.findViewById(android.R.id.empty);
        itemList.setEmptyView(emptyText);
        items=dbHelper.getData(1);
        /*if(items.isEmpty()){

            Toast.makeText(getContext(),"item is empty",Toast.LENGTH_LONG).show();
        }*/
        itemsAdapter= new CustomAdapter(getContext(),R.layout.custom_row,items);
        itemList = (ListView)view.findViewById(R.id.itemList);
        itemList.setAdapter(itemsAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ID_upd_str=((TextView)view.findViewById(R.id.list_ID)).getText().toString();
                updateID=Integer.parseInt(ID_upd_str);

                final String selected_item =((TextView)view.findViewById(R.id.listText)).getText().toString();
                final String selected_date =((TextView)view.findViewById(R.id.listDate)).getText().toString();
                final String selected_tasknote=((TextView)view.findViewById(R.id.listTaskNote)).getText().toString();


                showEditDialog(selected_item, selected_date,selected_tasknote);
            }


        });

        itemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override

            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                del_pos=i;
                String selected_item =((TextView)view.findViewById(R.id.listText)).getText().toString();
                ID_del_str=((TextView)view.findViewById(R.id.list_ID)).getText().toString();
                delID=Integer.parseInt(ID_del_str);

                showDeleteDialog(selected_item);
                return true;
            }
        });

        return view;
    }

    public void deleteItem(int id) {

        db.delete(TABLE_NAME, ListContract.ListEntry._ID + "="+ id,null);

    }

    public void updateItem(int id, String itemText, String due_date, String priority, String status, String taskNoteInput) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = sdf.parse(due_date);

            dateInMilliSeconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();

        }
        long dateInSeconds = dateInMilliSeconds / 1000;
        ContentValues values = new ContentValues();
        String selection = ListContract.ListEntry._ID + " =? ";
        String[] selectionArgs = {String.valueOf(id)};
        values.put(ListContract.ListEntry.COLUMN_TODOTEXT, itemText);
        values.put(ListContract.ListEntry.COLUMN_DUE_DATE, dateInSeconds);
        values.put(ListContract.ListEntry.COLUMN_PRIORITY, priority);
        values.put(ListContract.ListEntry.COLUMN_STATUS, status);
        values.put(ListContract.ListEntry.COLUMN_TASKNOTE, taskNoteInput);
        db.update(TABLE_NAME, values, selection, new String[]{Integer.toString(id)});
    }



    public void setDate(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        //DatePickerDialog dialog = new DatePickerDialog(this,mDateListener, year, month, day);
        DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog,mDateListener, year, month, day);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void onsetDate(){
        mDateListener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                dateTime.set(year, month , dayOfMonth);
                dateInMilliSeconds = dateTime.getTimeInMillis();

                month++;
                String date="0"+month+"/"+dayOfMonth+"/"+year;
               // Toast.makeText(MainActivity.this,date, Toast.LENGTH_SHORT).show();

            }
        };
    }





    public void onClick() {
        items.clear();
        items.addAll(dbHelper.getData(2));
        itemsAdapter.notifyDataSetChanged();

    }

    private void showEditDialog(String editname, String due_date,String taskNote) {
        FragmentManager fm = getFragmentManager();
        EditDialogFragment editDialogFragment = new EditDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("editname", editname);
        bundle.putString("due_date", due_date);
        bundle.putString("taskNote",taskNote);
        editDialogFragment.setArguments(bundle);
        editDialogFragment.setTargetFragment(HomeFragment.this, 1);
        editDialogFragment.show(fm, "fragment_edit_dialog");
    }

    public void refreshAdapter(int i ){
        items.clear();
        items.addAll(dbHelper.getData(i));
        itemsAdapter.notifyDataSetChanged();

    }

    private void showDeleteDialog(String deleteTask){
        FragmentManager fm = getFragmentManager();
        DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("deletetask",deleteTask);
        deleteDialogFragment.setArguments(bundle);
        deleteDialogFragment.setTargetFragment(HomeFragment.this, 0);
        deleteDialogFragment.show(fm, "fragment_delete_dialog");
    }


    public void onFinishDeleteDialog(Boolean delete) {
        if(delete){
            deleteItem(delID);
            itemsAdapter.remove(itemsAdapter.getItem(del_pos));
            itemsAdapter.notifyDataSetChanged();
        }

    }






    @Override
    public void onFinishEditDialog(String editItem, String due_date_text, String priority_spinner, String status_spinner, String taskNote) {
        updateItem(updateID, editItem, due_date_text, priority_spinner, status_spinner,taskNote);
        refreshAdapter(1);

    }



    public void onResume() {
        super.onResume();
        Log.d(TAG,"onresume");
        refreshAdapter(1);


    }
}
