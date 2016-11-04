package com.ankit.pointofsolution.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ankit.pointofsolution.Models.Userdata;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.modules.UserActivity;
import com.ankit.pointofsolution.storage.DBHelper;

import java.util.ArrayList;


public class UsersAdapter extends BaseAdapter  {
    /***********
     * Declare Used Variables
     *********/
    private Activity activity;
    private DBHelper dbHelper;

    private static LayoutInflater inflater = null;
    public Resources res;
    private ArrayList<Userdata> data;
    private CompoundButton switchButton;


    /*************
     * OrdersAdapter Constructor
     *****************/
    public UsersAdapter(Activity a, ArrayList<Userdata> d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data = d;
        res = resLocal;
        dbHelper = new DBHelper(activity);

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /********
     * What is the size of Passed Arraylist Size
     ************/
    public int getCount() {
        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public class ViewHolder {

        public TextView userRole, username;


        public Switch userstatus;
        public LinearLayout linearLayout;
        public ToggleButton toggle;

    }
        /******
         * Depends upon data size called for each row , Create each ListView row
         *****/

        public View getView(final int position, View convertView, ViewGroup parent) {

            View vi = convertView;
            final ViewHolder holder;


            if (convertView == null) {

                /****** Inflate customadapter.xml file for each row ( Defined below ) *******/
                vi = inflater.inflate(R.layout.useradapter, null);

                /****** View Holder Object to contain tabitem.xml file elements ******/
                holder = new ViewHolder();
                holder.username = (TextView) vi.findViewById(R.id.userName);
                holder.userRole = (TextView) vi.findViewById(R.id.userRole);
                //holder.toggle = (ToggleButton) vi.findViewById(R.id.userStatus);
                holder.userstatus = (Switch) vi.findViewById(R.id.userStatus);


                /************  Set holder with LayoutInflater ************/
                vi.setTag(holder);
            } else
                holder = (UsersAdapter.ViewHolder) vi.getTag();

            if (data.size() <= 0) {
                holder.username.setText("No Data");
            } else {

                /************  Set Model values in Holder elements ***********/
                holder.username.setText(data.get(position).getUserId());
                holder.userRole.setText(data.get(position).getRole());
                if (data.get(position).getUsersStatus().equals("active")) {
                    holder.userstatus.setChecked(true);
                } else {
                    holder.userstatus.setChecked(false);
                }

                holder.userstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        System.out.println("userid---"+position+"----"+data.get(position).getUserId());
                        boolean result = dbHelper.deactivateUserByAdmin(data.get(position).getUserId(),
                                data.get(position).getUsersStatus());
                        String user_status;
                        if (data.get(position).getUsersStatus().equals("active")) {
                            user_status = "deactive";
                        }else {
                            user_status = "active";
                        }
                        UserActivity.userDetailsArrayList.get(position).setUsersStatus(user_status);
                        UserActivity.usersAdapter.notifyDataSetChanged();
                        /*if(!isChecked) {
                            System.out.println("userid isChecked ------"+data.get(position).getUsersStatus());
                            //UserActivity.usersAdapter.notifyDataSetChanged();
                            //if (data.get(position).getUsersStatus().equals("active")) {
                            holder.userstatus.setChecked(false);
                            //} else if (data.get(position).getUsersStatus().equals("deactive")) { {
                            //holder.userstatus.setChecked(true);
                            }else{holder.userstatus.setChecked(true);}
                                */
                        //}
                    }
                });
            }
            return vi;
        }

}




