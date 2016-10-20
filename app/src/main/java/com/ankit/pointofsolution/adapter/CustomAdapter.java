package com.ankit.pointofsolution.adapter;
/**
 * Created by Ankit on 03-Sep-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.R;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter {

    /*********** Declare Used Variables *********/
    private Activity activity;

    private static LayoutInflater inflater=null;
    public Resources res;
    private ArrayList<OrderDetails> data;
    private double qty;
    private double price;
    private  double totalPrice;
    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity a, ArrayList<OrderDetails> d,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;
        System.out.println("sizearray"+data.size());
        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {
        if(data.size()<=0)
            return 1;
        System.out.println("data.size"+data.size());
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

        public TextView vitemName, vitemPrice, vitemTotal, vitemSku, vitemOrderId,vitemQty;
        public LinearLayout linearLayout;
    }
    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;



        if(convertView==null){

            /****** Inflate customadapter.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.order_detail_show_adapter, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout)vi.findViewById(R.id.Linearview);
            holder.vitemName = (TextView) vi.findViewById(R.id.LitemName);
            holder.vitemSku = (TextView) vi.findViewById(R.id.LitemSku);
            holder.vitemPrice=(TextView)vi.findViewById(R.id.LitemPrice);
            //holder.vitemOrderId=(TextView)vi.findViewById(R.id.LitemQty);
            holder.vitemQty=(TextView)vi.findViewById(R.id.LitemQty);
            //holder.moreQuantity=(TextView)vi.findViewById(R.id.moreQuantity);
            holder.vitemTotal=(TextView)vi.findViewById(R.id.LItemTotal);
            // holder.vitemOrderId.setText(data.get(position).getOrderId());
            // double.Parsedouble
            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
            System.out.println("data.size()"+data.size());
        }
        else
            holder=(CustomAdapter.ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.vitemOrderId.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            holder.vitemName.setText(data.get(position).getsItemName());
            holder.vitemSku.setText(data.get(position).getItemSku());
            qty=data.get(position).getItemQty();
            holder.vitemQty.setText(String.valueOf(qty));
            price=data.get(position).getItemPrice();
            holder.vitemPrice.setText(getRs(price));
            totalPrice=qty*price;
            holder.vitemTotal.setText(getRs(totalPrice));

        }
        return vi;
    }

    /**@Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    ******* Called when Item click in ListView ***********
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;
        OnItemClickListener(int position){
            mPosition = position;
        }
        @Override
        public void onClick(final View v) {

            // TODO Auto-generated method stub
            switch(v.getId()) {
                case R.id.moreQuantity: {
                    // do here code what u want on imagebutton click
                    //qdialog(mPosition);
                    break;
                }
                case R.id.itemRemove: {
                    // do here code what u want on textview click
                    System.out.println("onClick");
                    RemoveItemODFragment newFragment = new RemoveItemODFragment();
                    Bundle b = new Bundle();
                    b.putInt("pos", mPosition);
                    newFragment.setArguments(b);
                    newFragment.show(activity.getFragmentManager().beginTransaction(), "Abd");
                    break;
                }
            }
        }
    }*/
    public String getRs(double total){
        return String.format(res.getString(R.string.Rs), String.format("%.02f", total));
    }
}
