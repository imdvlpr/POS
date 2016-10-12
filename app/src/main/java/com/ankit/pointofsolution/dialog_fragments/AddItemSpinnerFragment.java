package com.ankit.pointofsolution.dialog_fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ankit.pointofsolution.MainActivity;
import com.ankit.pointofsolution.Models.Productdata;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.adapter.CustomAdapter;
import com.ankit.pointofsolution.config.Constants;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.GetResponseDialog;
import com.ankit.pointofsolution.utility.GetResponseDialogListener;
import com.ankit.pointofsolution.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_spinner_item;

/**
 * Created by Ankit on 13-Sep-16.
 */
public class AddItemSpinnerFragment extends DialogFragment {
    private EditText equantity;
    private String squantity=null;
    Preferences pref;
    private Productdata productdata;
    public MainActivity spinnerdata;
    private  boolean response = false;
    View focusView = null;
    boolean cancel = false;
    private DBHelper dbHelper;
   // public String[] spinnerList;
    //String[] spinnerList;
    private int length;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_add_item__spinner);
        pref = new Preferences(getActivity().getApplicationContext());
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        //dialog.setTitle("Title...");
        productdata = new Productdata();
        // set the custom dialog components - text, image and button
        equantity = (EditText) dialog.findViewById(R.id.quantity);
        Button dialogButton = (Button) dialog.findViewById(R.id.addQuantity);

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the values from the add item form
                squantity = equantity.getText().toString();

                System.out.println("productdataList: " + squantity);
               if (TextUtils.isEmpty(squantity)) {
                   System.out.println("hii");

                   equantity.setError(getString(R.string.error_field_required));
                   focusView = equantity;
                    cancel = true;
                }
                else {
                   int count=0;
                   for (String spinnerlistcheck : MainActivity.spinnerList) {
                       if (spinnerlistcheck.contains(squantity)) {
                           count++;
                           equantity.setError(getString(R.string.error_field_required));
                           focusView = equantity;
                           cancel = true;
                           break;
                       }
                   }
                   if(count==0) {
                       GetResponseDialog activity = (GetResponseDialog) getParentFragment();
                       activity.moreQuantity(squantity);
                       //int size= MainActivity.spinnerList.size();
                       //System.out.println("spinneritem size:" + size);
                       dialog.hide();
                       dialog.cancel();
                   }
                   }
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(Toolbar.LayoutParams.FILL_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        // Create the AlertDialog object and return it
        return dialog;
    }

    /*
    get response weather item added or not in local product list
     */
    public interface GetResponseDialog {
        void moreQuantity(String qnty);
    }

}
