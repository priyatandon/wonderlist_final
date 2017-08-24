package com.ptandon.wonderlist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.EditText;

import java.util.Calendar;


public class mDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        String date=month+"/"+dayOfMonth+"/"+year;
        EditText due_date_text=(EditText)getActivity().findViewById(R.id.due_date_text);
        due_date_text.setText(date);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog,this,year, month, day);
        dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() -1000);

        return dpd;
    }
}
