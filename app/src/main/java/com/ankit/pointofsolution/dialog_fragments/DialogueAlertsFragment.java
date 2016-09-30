package com.ankit.pointofsolution.dialog_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.ankit.pointofsolution.R;

/**
 * Created by Ankit on 06-Sep-16.
 */
public class DialogueAlertsFragment extends DialogFragment {


    public Dialog dialog;
    private AddItemFragment newFragment;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_Add_items)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Call Add item fragment to add new product in product catalog
                        newFragment = new AddItemFragment();
                        newFragment.show(getActivity().getFragmentManager().beginTransaction(),"Abd");
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //getActivity().finish();
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
