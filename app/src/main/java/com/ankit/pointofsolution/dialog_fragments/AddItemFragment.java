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

/**
 * Created by Ankit on 07-Sep-16.
 */
public class AddItemFragment extends DialogFragment {

    private EditText eitemPrice,eitemName,eadminCode,eitemBrand;
    private String sitemPrice=null,sitemName=null,sadminCode=null,sitemBrand=null;
    private TextView vSkuCode;
    Preferences pref;
    private Productdata productdata;
    private  boolean response = false;
    View focusView = null;
    boolean cancel = false;
    private DBHelper dbHelper;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_add_item);
        pref = new Preferences(getActivity().getApplicationContext());
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        //dialog.setTitle("Add Item");
        productdata = new Productdata();
        // set the custom dialog components - text, image and button
        eitemPrice = (EditText) dialog.findViewById(R.id.itemPrice);
        eitemName = (EditText) dialog.findViewById(R.id.itemName);
        eitemBrand = (EditText) dialog.findViewById(R.id.itemBrand);
        eadminCode = (EditText) dialog.findViewById(R.id.adminCode);
        vSkuCode = (TextView) dialog.findViewById(R.id.skuCode);
        vSkuCode.setText("Item code: "+pref.getSkuCode());



        Button dialogButton = (Button) dialog.findViewById(R.id.additem);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the values from the add item form
                sitemPrice = eitemPrice.getText().toString();
                sitemName = eitemName.getText().toString();
                sadminCode = eadminCode.getText().toString();
                sitemBrand = eitemBrand.getText().toString();
                System.out.println("productdataList: " + sitemPrice+", "+sitemName+" , "+sitemBrand);
                if (TextUtils.isEmpty(sitemName)) {
                    eitemName.setError(getString(R.string.error_field_required));
                    focusView = eitemName;
                    cancel = true;
                }else if (TextUtils.isEmpty(sitemPrice)) {
                    eitemPrice.setError(getString(R.string.error_field_required));
                    focusView = eitemPrice;
                    cancel = true;
                }else
                if (TextUtils.isEmpty(sadminCode)) {
                    eadminCode.setError(getString(R.string.error_field_required));
                    focusView = eadminCode;
                    cancel = true;
                }
                else{ //set data
                    /*if(!pref.getProductData().contains(pref.getSkuCode()))
                    {
                    productdata.setProductName(sitemName);
                    productdata.setPrice(sitemPrice);
                    productdata.setBrand(sitemBrand);
                    productdata.setSku(pref.getSkuCode());
                    Utility utility = new Utility(pref, dbHelper);
                    if(utility.AdditemCatalog(productdata))response = true;*/
                    dbHelper.insertItem(sitemPrice,sitemName,sitemBrand,pref.getSkuCode(),sadminCode);
                    Utility utility=new Utility(pref,dbHelper);
                    productdata=utility.getProductDetailsbySku(pref.getSkuCode());
                    // TODO Auto-generated method stub
                    GetResponseDialogListener activity = (GetResponseDialogListener) getActivity();
                    activity.updateResultSkuCode(productdata);

                    dialog.hide();
                    dialog.cancel();
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
