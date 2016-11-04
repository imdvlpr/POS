
package com.ankit.pointofsolution.dialog_fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ankit.pointofsolution.MainActivity;
import com.ankit.pointofsolution.Models.Coupons;
import com.ankit.pointofsolution.Models.Productdata;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.GetResponseDialogListener;
import com.ankit.pointofsolution.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ankit on 13-Sep-16.
 */
public class AddItemCouponsFragment extends DialogFragment {
    private EditText eitemSkuCode;
    private String sitemSkuCode = null;
    private TextView vSkuCode;
    Preferences pref;
    private List<Productdata> productdataList;
    private Productdata productdata;
    private boolean response = false;
    View focusView = null;
    boolean cancel = false;
    private DBHelper dbHelper;
    private Utility utility;
    private GetResponseDialogListener activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_add_coupons__manually);
        pref = new Preferences(getActivity().getApplicationContext());
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        activity = (GetResponseDialogListener) getActivity();
        //dialog.setTitle("Title...");
        productdata = new Productdata();
        // set the custom dialog components - text, image and button
        eitemSkuCode = (EditText) dialog.findViewById(R.id.CouponsSkuCode);
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
                } else { //set data
                    utility = new Utility(pref, dbHelper);
                    //dbHelper=new DBHelper(DBHelper.this);
                    final Coupons coupons = dbHelper.getItemCodeByCouponsCode(sitemSkuCode);
                    if (coupons.getCpItemSkuCode() != null) {
                        // TODO Auto-generated method stub
                        String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());
                        int count=0;
                        if ((currentTimestamp.compareTo(coupons.getCpStartDate()) >= 0)
                                && (currentTimestamp.compareTo(coupons.getCpEndDate()) <= 0) ) {
                            //System.out.println("MainActivity123"+MainActivity.orderDetailsArrayList.size());
                            for (int i=0;i<MainActivity.orderDetailsArrayList.size();i++) {

                                System.out.println("narraylist:"+i+"---"+MainActivity.orderDetailsArrayList.get(i).getCouponsArrayList());
                                if ((MainActivity.orderDetailsArrayList.get(i).getCouponsArrayList()!=null)
                                        && (MainActivity.orderDetailsArrayList.get(i).getCouponsArrayList().get(0).getCpCouponSkuCode()!=null)
                                                    && (!MainActivity.orderDetailsArrayList.get(i).getCouponsArrayList().get(0).getCpCouponSkuCode().equals(null)))
                                {
                                    if(MainActivity.orderDetailsArrayList.get(i).getCouponsArrayList().get(0)
                                            .getCpItemSkuCode().equals(coupons.getCpItemSkuCode())) {
                                        count++;
                                    }

                                }
                            }
                            if(count==1) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(Messages.EXIST_COUPON)
                                        .setMessage(Messages.REPLACE_COUPON)
                                        .setNegativeButton(android.R.string.no, null)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface arg0, int arg1) {
                                                productdata = utility.getProductDetailsbyCouponsItemSku12(coupons.getCpItemSkuCode(),coupons.getCpCouponSkuCode());
                                                activity.updateResultSkuCode(productdata);

                                            }
                                        }).create().show();

                            }
                            else{

                                productdata = utility.getProductDetailsbyCouponsItemSku12(coupons.getCpItemSkuCode(),coupons.getCpCouponSkuCode());
                                activity.updateResultSkuCode(productdata);

                            }
                            dialog.hide();
                            dialog.cancel();


                        } else {

                            dialog.hide();
                            dialog.cancel();
                            Toast.makeText(getActivity(), Messages.EXPIRE_COUPON, Toast.LENGTH_SHORT).show();
                        }

                    } else {
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
}
