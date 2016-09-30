package com.ankit.pointofsolution.adapter;

/**
 * Created by Ankit on 03-Sep-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ankit.pointofsolution.MainActivity;
import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.Models.Productdata;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.config.Constants;
import com.ankit.pointofsolution.dialog_fragments.RemoveItemODFragment;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.Utility;

import java.util.ArrayList;

import static android.R.layout.simple_spinner_item;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;

    private static LayoutInflater inflater=null;
    public Resources res;
    private Utility utility;
    private Productdata productdata;
    private ArrayList<OrderDetails> data;
    Preferences pref;
    int[] itemqty = {0};
    float itemprice;
    float itemtotal;
    boolean isSpinnerTouched = false;

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

        public TextView vitemName, vitemPrice, vitemTotal, vitemSku;
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
            holder.vitemQty.setSelection(data.get(position).getItemQty()-1);
            float totalprice=0;
            for (int i = 0; i < MainActivity.orderDetailsArrayList.size(); i++) {
                totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() *
                        MainActivity.orderDetailsArrayList.get(i).getItemPrice()) + totalprice;
            }
            MainActivity.vTotalconut.setText(getRs(totalprice));

            //holder.vitemQty.setText(String.valueOf(itemqty[0]));
            // attaching  onimtemselected code on spinner
            holder.vitemQty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
                 }
            });
            holder.vitemQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int selectedQnty, long l) {
                    if (isSpinnerTouched)
                    {
                    String item = adapterView.getItemAtPosition(selectedQnty).toString();
                    System.out.println("item selected:" + item);
                    System.out.println("position:" + position + ":quantity:" + (data.get(position).getItemQty()));
                    itemqty[0] = Integer.parseInt(item);
                    MainActivity.orderDetailsArrayList.get(position).setItemQty(itemqty[0]);
                    holder.vitemQty.setSelection(data.get(position).getItemQty() - 1);
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
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


           /*holder.vitemQty.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               }

               @Override
               public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               }

               @Override
               public void afterTextChanged(Editable editable) {

                   if (!editable.toString().equals("")) {

                       //holder.vitemQty.setFocusable(true);
                       itemqty[0] = Float.parseFloat(editable.toString());
                       System.out.println("itemqty[0]:" + MainActivity.orderDetailsArrayList.get(position).getItemQty());
                       //if (MainActivity.orderDetailsArrayList.contains(position) && position>=0) {
                       MainActivity.orderDetailsArrayList.get(position).setItemQty(itemqty[0]);
                       //}
                      *//* else{
                       MainActivity.orderDetailsArrayList.get(position-1).setItemQty(itemqty[0]);
                   }*//*
                   float total = itemqty[0] * itemprice;
                   holder.vitemTotal.setText(String.format("%.02f", total));

                   float totalprice = 0;
                   for (int i = 0; i < MainActivity.orderDetailsArrayList.size(); i++) {
                       totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() *
                               MainActivity.orderDetailsArrayList.get(i).getItemPrice()) + totalprice;
                   }
                       holder.vitemQty.clearFocus();
                   MainActivity.vTotalconut.setText(String.valueOf(String.format("%.02f", totalprice)));
               } else {
                   Toast.makeText(activity, "Please enter quantity.", Toast.LENGTH_LONG);
               }
               }
           });*/




            /*holder.vitemQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        System.out.println("hasFocus:" + position);
                            final EditText Caption = (EditText) view;
                            if (!Caption.getText().toString().equals("")) {
                                Caption.setFocusable(true);
                                holder.vitemQty.setFocusable(true);
                                itemqty[0] = Float.parseFloat(Caption.getText().toString());
                                System.out.println("itemqty[0]:" + itemqty[0]);
                                //if (MainActivity.orderDetailsArrayList.contains(position) && position>=0) {
                                    MainActivity.orderDetailsArrayList.get(position).setItemQty(itemqty[0]);
                                //}
                               *//* else{
                                    MainActivity.orderDetailsArrayList.get(position-1).setItemQty(itemqty[0]);
                                }*//*
                                float total = itemqty[0] * itemprice;
                                holder.vitemTotal.setText(String.format("%.02f", total));

                                float totalprice = 0;
                                for (int i = 0; i < MainActivity.orderDetailsArrayList.size(); i++) {
                                    totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() *
                                            MainActivity.orderDetailsArrayList.get(i).getItemPrice()) + totalprice;
                                }
                               MainActivity.vTotalconut.setText(String.valueOf(String.format("%.02f", totalprice)));
                            } else {
                                Toast.makeText(activity, "Please enter quantity.", Toast.LENGTH_LONG);
                            }

                    }
               }
            });*/
            //holder.vitemQty.setOnFocusChangeListener(new OnFocusChangeListener(position));
            holder.vitemRemove.setOnClickListener(new OnItemClickListener(position));
            //holder.vitemQty.setFocusable(true);
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

    /********* Called when Item click in ListView ************/
    /*private class OnFocusChangeListener  implements View.OnFocusChangeListener {
        private int mPosition;

        OnFocusChangeListener(int mPosition) {
            this.mPosition = mPosition;
        }
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) {
                System.out.println("hasFocus:" + position);
                final EditText Caption = (EditText) view;
                if (!Caption.getText().toString().equals("")) {
                    if (MainActivity.orderDetailsArrayList.contains(position)) {
                        itemqty[0] = Float.parseFloat(Caption.getText().toString());
                        MainActivity.orderDetailsArrayList.get(position).setItemQty(itemqty[0]);
                        float total = itemqty[0] * itemprice;
                        holder.vitemTotal.setText(String.format("%.02f", total));

                        float totalprice = 0;
                        for (int i = 0; i < MainActivity.orderDetailsArrayList.size(); i++) {
                            totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() *
                                    MainActivity.orderDetailsArrayList.get(i).getItemPrice()) + totalprice;
                        }
                        MainActivity.vTotalconut.setText(String.valueOf(String.format("%.02f", totalprice)));
                    }
                    } else {
                        Toast.makeText(activity, "Please enter quantity.", Toast.LENGTH_LONG);
                    }
            }

        }*/

    public String getRs(Float total){
       return String.format(res.getString(R.string.Rs), String.format("%.02f", total));
    }
}

