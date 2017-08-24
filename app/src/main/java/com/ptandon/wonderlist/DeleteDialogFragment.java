package com.ptandon.wonderlist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class DeleteDialogFragment extends DialogFragment{

    public DeleteDialogFragment(){

    }

    public interface DeleteDialogListener {
        public void onFinishDeleteDialog(Boolean delete);
    }

    DeleteDialogListener Listener;
    private static final String TAG="deletedialog";


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {

            Listener = (DeleteDialogListener) getTargetFragment();
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString()
                    + " must implement DeleteDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context c=getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        Bundle args = getArguments();
        String deleteTask = args.getString("deletetask", "");
       // builder.setIcon(android.R.drawable.alert_dark_frame);
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setTitle("Delete "+ deleteTask + " ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sendBackResult();
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public void sendBackResult() {

        DeleteDialogListener listener = (DeleteDialogListener) getTargetFragment();
        listener.onFinishDeleteDialog(true);
        dismiss();
    }

}
