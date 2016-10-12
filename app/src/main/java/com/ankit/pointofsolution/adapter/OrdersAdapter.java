package com.ankit.pointofsolution.adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ankit.pointofsolution.Models.Orders;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.modules.ListofOrdersActivity;

import java.util.ArrayList;


public class OrdersAdapter extends BaseAdapter implements View.OnClickListener{
    /*********** Declare Used Variables *********/
    private Activity activity;

    private static LayoutInflater inflater=null;
    public Resources res;
    private ArrayList<Orders> data;


    /*************  OrdersAdapter Constructor *****************/
    public OrdersAdapter(Activity a, ArrayList<Orders> d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;
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

        public TextView orderStatus, orderId, orderDate;
        public LinearLayout linearLayout;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if(convertView==null){

            /****** Inflate customadapter.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.orderadapter, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout)vi.findViewById(R.id.Linearview);
            holder.orderId = (TextView) vi.findViewById(R.id.orderId);
            holder.orderStatus = (TextView) vi.findViewById(R.id.orderStatus);
            holder.orderDate = (TextView) vi.findViewById(R.id.orderDate);

            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        }
        else
            holder=(OrdersAdapter.ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.orderId.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            holder.linearLayout.setVisibility(View.VISIBLE);

            /************  Set Model values in Holder elements ***********/
            holder.orderId.setText(data.get(position).getOrderId());
            holder.orderStatus.setText(data.get(position).getOrderStatus());
            holder.orderDate.setText(data.get(position).getOrderCreatedAt());
            vi.setOnClickListener(new OnItemClickListener(position));
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
            System.out.println("ye yaha to he :"+ mPosition);
/*
            FragmentTransaction fragTransaction = activity.getFragmentManager().beginTransaction();
            fragTransaction.replace(R.id.frame_container, ListofOrdersActivity.itemFragment).addToBackStack(null);
            fragTransaction.show(ListofOrdersActivity.itemFragment);*/
        }
    }

}
