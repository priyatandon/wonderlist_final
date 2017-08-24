package com.ptandon.wonderlist;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddItemActivity extends AppCompatActivity  implements OnItemSelectedListener{

    private static final String TAG="AddItemActivity";
    String priority_spinner_value;
    private static long dateInMilliSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Spinner prioritySpinner=(Spinner)findViewById(R.id.priority_spinner);
        prioritySpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter_priority = ArrayAdapter.createFromResource(this,
                R.array.task_priority, android.R.layout.simple_spinner_item);
        adapter_priority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        prioritySpinner.setAdapter(adapter_priority);

    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        priority_spinner_value = adapterView.getItemAtPosition(i).toString();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onSave(){

        EditText addItemInput=(EditText)findViewById(R.id.addItemInput);
        String itemToSave=addItemInput.getText().toString();
        EditText due_date_text=(EditText)findViewById(R.id.due_date_text);
        EditText taskNoteInput=(EditText)findViewById(R.id.taskNoteInput);
        if(itemToSave.length()==0){
            addItemInput.setHint("Please Enter the task");
            addItemInput.setHintTextColor(Color.RED);
        }
        else if (due_date_text.length()==0){
            due_date_text.setHint("Please Enter Date");
            due_date_text.setHintTextColor(Color.RED);
        }
        else if(taskNoteInput.length()==0){
            taskNoteInput.setHint("Please Enter the task notes");
            taskNoteInput.setHintTextColor(Color.RED);
        }

        else{
            Intent saveIntent=new Intent(this,MainActivity.class);
            String dateToProcess = due_date_text.getText().toString();
            String taskNote=taskNoteInput.getText().toString();

            SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
            try {
                Date date=sdf.parse(dateToProcess);
                dateInMilliSeconds=date.getTime();
            }catch (ParseException e){
                e.printStackTrace();
            }

            Log.d(TAG,taskNote+" "+itemToSave+" "+dateInMilliSeconds+" "+priority_spinner_value);

            saveIntent.putExtra("itemToSave",itemToSave);
            saveIntent.putExtra("due_date",dateInMilliSeconds);
            saveIntent.putExtra("priority_spinner_value",priority_spinner_value);
            saveIntent.putExtra("taskNote",taskNote);
            setResult(RESULT_OK,saveIntent);
            finish();
        }
    }


    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new mDatePicker();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onCancel(View view) {
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                onSave();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
