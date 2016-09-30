package com.ankit.pointofsolution.dialog_fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ankit.pointofsolution.MainActivity;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 19-Sep-16.
 */
public class RemoveItemODFragment extends DialogFragment {

    public Dialog dialog;
    private AddItemFragment newFragment;
    private int mPosition;
    private Preferences pref;
    DBHelper dbHelper;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        pref = new Preferences(getActivity());
        dbHelper = new DBHelper(getActivity());
        Bundle b = getArguments();
        mPosition =  b.getInt("pos");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_Remove_items_od)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Call Add item fragment to add new product in product catalog
                        float totalprice = 0;
                        for(int i=0;i<MainActivity.orderDetailsArrayList.size();i++)
                        {
                            totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() *
                                    MainActivity.orderDetailsArrayList.get(i).getItemPrice())+totalprice;
                        }
                         dbHelper.deleteOrderDetails(pref.getCurrentOdrerId(),
                                MainActivity.orderDetailsArrayList.get(mPosition).getItemSku());

                        MainActivity.vTotalconut.setText(String.valueOf(totalprice));
                        MainActivity.orderDetailsArrayList.remove(mPosition);
                       //update adapter using notify function
                        MainActivity.adapter.notifyDataSetChanged();
                        //Check the size of list if there is no item then set invisible list also
                        // set a msg to populate that there is no items in list.
                        if(MainActivity.orderDetailsArrayList.size()<=0)
                        {
                            MainActivity.listView.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), Messages.NO_ITEMS_IN_CART,Toast.LENGTH_LONG).show();
                        }
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
