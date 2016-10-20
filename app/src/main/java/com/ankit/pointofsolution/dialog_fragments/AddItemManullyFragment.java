package com.ankit.pointofsolution.dialog_fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ankit.pointofsolution.Models.Productdata;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.GetResponseDialogListener;
import com.ankit.pointofsolution.utility.Utility;

import java.util.List;

/**
 * Created by Ankit on 13-Sep-16.
 */
public class AddItemManullyFragment extends DialogFragment {
    private EditText eitemSkuCode;
    private String sitemSkuCode=null;
    private TextView vSkuCode;
    Preferences pref;
    private List<Productdata> productdataList;
    private Productdata productdata;
    private  boolean response = false;
    View focusView = null;
    boolean cancel = false;
    private DBHelper dbHelper;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_add_item__manually);
        pref = new Preferences(getActivity().getApplicationContext());
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        //dialog.setTitle("Title...");
        productdata = new Productdata();
        // set the custom dialog components - text, image and button
        eitemSkuCode = (EditText) dialog.findViewById(R.id.itemSkuCode);
        Button dialogButton = (Button) dialog.findViewById(R.id.addItemCart);

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the values from the add item form
                sitemSkuCode = eitemSkuCode.getText().toString();

                System.out.println("productdataList: " + sitemSkuCode);
                if (TextUtils.isEmpty(sitemSkuCode)) {
                    eitemSkuCode.setError(getString(R.string.error_field_required));
                    focusView = eitemSkuCode;
                    cancel = true;
                }
                else{ //set data
                    Utility utility = new Utility(pref,dbHelper);
                    productdata = utility.getProductDetailsbySku(sitemSkuCode);
                        if(productdata!=null) {
                            // TODO Auto-generated method stub
                            GetResponseDialogListener activity = (GetResponseDialogListener) getActivity();
                            activity.updateResultSkuCode(productdata);
                            dialog.hide();
                            dialog.cancel();
                        }
                    else{
                            eitemSkuCode.setError(getString(R.string.error_item_not_available));
                            focusView = eitemSkuCode;
                            cancel = true;
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


}
