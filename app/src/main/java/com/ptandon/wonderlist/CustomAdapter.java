package com.ptandon.wonderlist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends ArrayAdapter{
    private Context context;
    private ArrayList<List> list;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList list) {
        super(context,textViewResourceId, list);

        this.context= context;
        this.list=list;
    }

    private class ViewHolder
    {
        TextView listText;
        TextView listDate;
        TextView listPriority;
        TextView listID;
        TextView listBox;
        TextView listTaskNote;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.custom_row, null);
            holder = new ViewHolder();
            holder.listText = (TextView) convertView.findViewById(R.id.listText);
            holder.listDate = (TextView) convertView.findViewById(R.id.listDate);
            holder.listID = (TextView) convertView.findViewById(R.id.list_ID);
            holder.listPriority=(TextView) convertView.findViewById(R.id.listPriority);
            holder.listBox=(TextView) convertView.findViewById(R.id.colorBox);
            holder.listTaskNote=(TextView) convertView.findViewById(R.id.listTaskNote);

            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        List newList= list.get(position);

        long dt=newList.getDueDate();
        dt=dt*1000;
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(dt));


        holder.listText.setText(newList.getTodoText());
        holder.listDate.setText(dateString);
        holder.listPriority.setText(newList.getPriority());
        holder.listID.setText(String.valueOf(newList.getId()));

        holder.listTaskNote.setText(String.valueOf(newList.getTaskNote()));
        String priority=holder.listPriority.getText().toString();
        if(priority.equals("HIGH")) {

            holder.listBox.setBackgroundColor(Color.RED);
        }
        else if (priority.equals("MEDIUM")) {

            holder.listBox.setBackgroundColor(Color.GREEN);
        }
        else if(priority.equals("LOW")) {
            holder.listBox.setBackgroundColor(Color.YELLOW);

        }
        return convertView;
    }
}
