package com.ptandon.wonderlist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;


public class EditDialogFragment extends DialogFragment{

    public EditDialogFragment() {
    }

    public interface EditDialogListener {
        public void onFinishEditDialog(String editItem,String due_date_text,String priority_spinner,String status_spinner,String taskNote );
    }

    EditDialogListener Listener;
    private EditText editItem;
    private EditText due_date_text;
    private Spinner status_spinner;
    private Spinner priority_spinner;
    private EditText taskNote;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private static final String TAG="editdialog";
    String date;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

            Listener = (EditDialogListener) getTargetFragment();
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString()
                    + " must implement EditDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context c=getContext();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        LayoutInflater inflater = LayoutInflater.from(c);
        final View editDialogView = inflater.inflate(R.layout.edit_dailog, null);
        alertDialogBuilder.setView(editDialogView);
        Bundle args = getArguments();

       // Log.d(TAG,fragment);
        String selected_name = args.getString("editname", "");
        String selected_date = args.getString("due_date","");
        String selected_task_note=args.getString("taskNote","");
        editItem = (EditText)editDialogView.findViewById(R.id.editItem);
        editItem.setText(selected_name);
        taskNote=(EditText)editDialogView.findViewById(R.id.taskNoteInput);
        taskNote.setText(selected_task_note);
        due_date_text =(EditText) editDialogView.findViewById(R.id.due_date_text);
        due_date_text.setText(selected_date);
        due_date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
                mDateListener =new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        date=month+"/"+dayOfMonth+"/"+year;
                        due_date_text.setText(date);

                    }
                };
            }
        });

        status_spinner =(Spinner)editDialogView.findViewById(R.id.status_spinner);
        priority_spinner=(Spinner)editDialogView.findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> adapter_priority = ArrayAdapter.createFromResource(c,
                R.array.task_priority, android.R.layout.simple_spinner_item);
        adapter_priority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority_spinner.setAdapter(adapter_priority);

        ArrayAdapter<CharSequence> adapter_status = ArrayAdapter.createFromResource(c,
                R.array.task_status, android.R.layout.simple_spinner_item);
        adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_spinner.setAdapter(adapter_status);


        String fragment=getTargetFragment().toString();
        if(fragment.contains("HomeFragment")){
            alertDialogBuilder.setTitle("Edit Task : "+"' "+selected_name+" '");
           }

        else
        {
            alertDialogBuilder.setTitle("Add Task "+"' "+selected_name+ " '"+" again!");
        }
        alertDialogBuilder.setIcon(android.R.drawable.arrow_down_float);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                                                String set_date=due_date_text.getText().toString();
                                                if(editItem.getText().toString().equals("")){
                                                    Toast.makeText(getContext(),"Task field empty.Please try again",
                                                            Toast.LENGTH_SHORT).show();
                                                }else if(taskNote.getText().toString().equals("")) {
                                                    Toast.makeText(getContext(), "Task Note field empty.Please try again",
                                                            Toast.LENGTH_SHORT).show();
                                                }else{
                                                    sendBackResult();
                                                }

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });
        return alertDialogBuilder.create();

    }

    public void setDate(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog,mDateListener, year, month, day);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void sendBackResult() {

        EditDialogListener listener = (EditDialogListener) getTargetFragment();
        String editItemDialog=editItem.getText().toString();
        String editDateDialog=due_date_text.getText().toString();
        String statusSpinnerDialog=status_spinner.getSelectedItem().toString();
        String prioritySpinnerDialog=priority_spinner.getSelectedItem().toString();
        String taskNoteDialog=taskNote.getText().toString();
        Listener.onFinishEditDialog(editItemDialog ,editDateDialog,prioritySpinnerDialog,statusSpinnerDialog,taskNoteDialog);

        dismiss();
    }


}
