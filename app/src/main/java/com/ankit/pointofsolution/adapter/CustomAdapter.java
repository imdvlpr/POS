package com.ankit.pointofsolution.adapter;

/**
 * Created by Ankit on 03-Sep-16.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ankit.pointofsolution.MainActivity;
import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.Models.Productdata;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.config.Constants;
import com.ankit.pointofsolution.dialog_fragments.AddItemSpinnerFragment;
import com.ankit.pointofsolution.dialog_fragments.RemoveItemODFragment;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.GetResponseDialog;
import com.ankit.pointofsolution.utility.GetResponseDialogListener;
import com.ankit.pointofsolution.utility.Utility;

import java.util.ArrayList;

import static android.R.layout.simple_spinner_item;
import static com.ankit.pointofsolution.R.string.cancel;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter implements View.OnClickListener,GetResponseDialog {

    /*********** Declare Used Variables *********/
    private Activity activity;

    private static LayoutInflater inflater=null;
    public Resources res;
    private Utility utility;
    private Productdata productdata;
    private ArrayList<OrderDetails> data;
    Preferences pref;
    float[] itemqty = {0};
    float itemprice;
    float itemtotal;
    boolean isSpinnerTouched = false;
    boolean isMoreselected = false;
    private String moreqnty;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity a, ArrayList<OrderDetails> d,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;
        pref = new Preferences(activity);
        productdata = new Productdata();

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }



    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView vitemName, vitemPrice, vitemTotal, vitemSku, vitemMoreQuantity;
        public Spinner vitemQty;
        public LinearLayout linearLayout;
        public ImageView vitemRemove;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;


        if(convertView==null){

            /****** Inflate customadapter.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.customadapter, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/
            System.out.println("getcount : "+getCount());
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout)vi.findViewById(R.id.Linearview);
            holder.vitemName = (TextView) vi.findViewById(R.id.LitemName);
            holder.vitemSku = (TextView) vi.findViewById(R.id.LitemSku);
            holder.vitemPrice=(TextView)vi.findViewById(R.id.LitemPrice);
            //holder.vitemQty=(EditText)vi.findViewById(R.id.LitemQty);
            holder.vitemQty=(Spinner)vi.findViewById(R.id.LitemQty);
            holder.vitemTotal=(TextView)vi.findViewById(R.id.LItemTotal);
            holder.vitemRemove = (ImageView) vi.findViewById(R.id.itemRemove);
            holder.vitemMoreQuantity = (TextView)vi.findViewById(R.id.moreQuantity);
         /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.vitemName.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            /*tempValues=null;
            tempValues = (String) data.get(position);*/
            //productdata = utility.getProductDetailsbySku(tempValues);
            holder.linearLayout.setVisibility(View.VISIBLE);
            //holder.vitemQty.setFocusable(true);
        /************  Set Model values in Holder elements ***********/
            //
            //System.out.println("data details:"+(data.get(position).getItemQty())+"::::"+position);
            itemqty[0] = data.get(position).getItemQty();
            itemprice = data.get(position).getItemPrice();
            itemtotal = itemqty[0] * itemprice;
            holder.vitemQty.setAdapter(MainActivity.dataAdapter);
            //System.out.println("itemtotal:"+itemtotal);
            holder.vitemPrice.setText(getRs(itemprice));
            holder.vitemName.setText(data.get(position).getsItemName());
            holder.vitemSku.setText(data.get(position).getItemSku());
            holder.vitemTotal.setText(getRs(itemqty[0] * itemprice));


            float totalprice=0;
            for (int i = 0; i < MainActivity.orderDetailsArrayList.size(); i++) {
                System.out.println("getItemQty:" +MainActivity.orderDetailsArrayList.get(i).getItemQty());
                totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() *
                        MainActivity.orderDetailsArrayList.get(i).getItemPrice()) + totalprice;
            }
            MainActivity.vTotalconut.setText(getRs(totalprice));
            //holder.vitemQty.setSelection((Math.round(data.get(position).getItemQty())-1),false);
            if(!MainActivity.spinnerList.contains(String.valueOf(Math.round(data.get(position).getItemQty()))))
            {
                holder.vitemQty.setSelection(5);
                holder.vitemMoreQuantity.setText(String.valueOf(data.get(position).getItemQty()));
            }
            else
            {
                holder.vitemQty.setSelection((Math.round(data.get(position).getItemQty())-1));
                holder.vitemMoreQuantity.setText("");
            }
            //holder.vitemQty.setText(String.valueOf(itemqty[0]));
            // attaching  onimtemselected code on spinner
            holder.vitemQty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
                 }
            });
            holder.vitemQty.post(new Runnable() {
            @Override
            public void run() {
            holder.vitemQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int selectedQnty, long l) {
                    if(isSpinnerTouched)
                    {
                    String item = adapterView.getItemAtPosition(selectedQnty).toString();
                    //System.out.println("item selected:" + item);

                        if(item.equals("more")) {
                            isMoreselected = true;
                            /*AddItemSpinnerFragment newFragment = new AddItemSpinnerFragment();
                            newFragment.show(activity.getFragmentManager().beginTransaction(), "Abd");*/
                            qdialog(position);
                        }else {
                            itemqty[0] = Float.parseFloat(item);
                            MainActivity.orderDetailsArrayList.get(position).setItemQty(itemqty[0]);

                            if(!MainActivity.spinnerList.contains(String.valueOf(Math.round(data.get(position).getItemQty()))))
                            {
                                holder.vitemQty.setSelection(5);
                            }
                            else
                            {
                                holder.vitemQty.setSelection((Math.round(data.get(position).getItemQty())-1));
                            }
                            float total = itemqty[0] * data.get(position).getItemPrice();

                            holder.vitemTotal.setText(getRs(total));
                            float totalprice = 0;
                            for (int i = 0; i < MainActivity.orderDetailsArrayList.size(); i++) {
                                totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() *
                                        MainActivity.orderDetailsArrayList.get(i).getItemPrice()) + totalprice;
                            }
                            holder.vitemQty.clearFocus();
                            MainActivity.vTotalconut.setText(getRs(totalprice));
                        }
                }
            }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
                }
            });
            if(isMoreselected && moreqnty!=null)
            {
                totalprice = 0;
                itemqty[0] = Float.parseFloat(moreqnty);
                MainActivity.orderDetailsArrayList.get(position).setItemQty(itemqty[0]);
                System.out.println("position:" + position + ":selectedQnty:" + itemqty[0]);
                holder.vitemQty.setSelection(5);
                float total = itemqty[0] * data.get(position).getItemPrice();
                holder.vitemTotal.setText(getRs(total));
                for (int i = 0; i < MainActivity.orderDetailsArrayList.size(); i++) {
                    totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() *
                            MainActivity.orderDetailsArrayList.get(i).getItemPrice()) + totalprice;
                }
                MainActivity.vTotalconut.setText(getRs(totalprice));
                isMoreselected = false;
                moreqnty = null;
            }
            holder.vitemRemove.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(final View v) {
            System.out.println("onClick");
            RemoveItemODFragment newFragment = new RemoveItemODFragment();
            Bundle b = new Bundle();
            b.putInt("pos",mPosition);
            newFragment.setArguments(b);
            newFragment.show(activity.getFragmentManager().beginTransaction(), "Abd");
        }
    }

    public String getRs(Float total){
       return String.format(res.getString(R.string.Rs), String.format("%.02f", total));
    }

    //@Override
    //public void moreQuantity(String qnty) {
     //   moreqnty = qnty;
    //}

    public void qdialog(final int position)
    {
        final View[] focusView = {null};
        final boolean[] cancel = {false};
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.fragment_add_item__spinner);

        //initialize custom dialog items.**
        final EditText equantity = (EditText) dialog.findViewById(R.id.quantity);
        Button btnSaveButton = (Button) dialog
                .findViewById(R.id.addQuantity);
        dialog.show();
        btnSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("mood "+ equantity.getText().toString()+" position "+position);
                moreqnty = equantity.getText().toString();
                if (TextUtils.isEmpty(moreqnty)) {
                    System.out.println("hii");

                    equantity.setError("Please enter quantity");
                    focusView[0] = equantity;
                    cancel[0] = true;
                }
                else {
                    int count = 0;
                    for (String spinnerlistcheck : MainActivity.spinnerList) {
                        if (spinnerlistcheck.contains(moreqnty)) {
                            count++;
                            equantity.setError("Please enter other quantity");
                            focusView[0] = equantity;
                            cancel[0] = true;
                            break;
                        }
                    }
                }
                dialog.hide();
            }
        });
    }
}

