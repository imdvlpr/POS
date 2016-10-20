package com.ankit.pointofsolution.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ankit.pointofsolution.MainActivity;
import com.ankit.pointofsolution.Models.OrderDetails;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.dialog_fragments.RemoveItemODFragment;
import com.ankit.pointofsolution.storage.Preferences;

import java.util.List;

public class ExpandableListAdapterTest extends BaseExpandableListAdapter implements View.OnClickListener {

	private Context _context;
	private Activity activity;
	private List<OrderDetails> _listDataHeader; // header titles
	private String qnty = "";
	Preferences pref;
	double itemqty = 0;
	double itemprice;
	double itemtotal;
	double totalprice=0;
	private  String c;
	Resources res;

	public ExpandableListAdapterTest(Context context, List<OrderDetails> listDataHeader, Resources resources, Activity activity) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this.res = resources;
		this.activity = activity;
	}
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataHeader.get(groupPosition).getCouponsArrayList().size();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
			final String childText = (String) MainActivity.orderDetailsArrayList.get(groupPosition).getCouponsArrayList()
																.get(childPosition).getCpCouponSkuCode();
			final String childCouponvalue = (String) MainActivity.orderDetailsArrayList.get(groupPosition).getCouponsArrayList()
																.get(childPosition).getCpValue();
			final  double childCouponQty = MainActivity.orderDetailsArrayList.get(groupPosition).getItemQty();

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtchildCouponvalue = (TextView) convertView.findViewById(R.id.LitemPrice);
		TextView txtListChildCouponCode = (TextView) convertView.findViewById(R.id.couponcode);
		TextView txtListChildCouponTotal = (TextView) convertView.findViewById(R.id.CouponItemTotal);

		txtListChildCouponCode.setText(childText);
		txtchildCouponvalue.setText(getRs(Double.parseDouble(childCouponvalue)));
		double value=Double.parseDouble(childCouponvalue);
		double couponTotalPrice=value*childCouponQty;

		txtListChildCouponTotal.setText(getRs(couponTotalPrice));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		System.out.println("groupPosition:"+_listDataHeader.get(groupPosition).getOrderId());
		if(_listDataHeader.get(groupPosition).getCouponsArrayList()!=null)
		return _listDataHeader.get(groupPosition).getCouponsArrayList().size();
		else return 0;
		//	.size();
		//return this._listDataChild.get(this._listDataHeader.get(groupPosition))
			//	.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {


		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.customadapter, null);
		}

		TextView vitemName, vitemPrice, vitemTotal, vitemSku, vitemMoreQuantity,vItemOffer,vItemOfferValue;;
		TextView vitemQty,moreQuantity;
		LinearLayout linearLayout;
		ImageView vitemRemove;

		linearLayout = (LinearLayout)convertView.findViewById(R.id.Linearview);
		vitemName = (TextView) convertView.findViewById(R.id.LitemName);
		vitemSku = (TextView) convertView.findViewById(R.id.LitemSku);
		vitemPrice=(TextView)convertView.findViewById(R.id.LitemPrice);
		//vitemQty=(Spinner)convertView.findViewById(R.id.LitemQty);
		vitemQty=(TextView)convertView.findViewById(R.id.LitemQty);
		moreQuantity=(TextView)convertView.findViewById(R.id.moreQuantity);
		vitemTotal=(TextView)convertView.findViewById(R.id.LItemTotal);
		vitemRemove = (ImageView) convertView.findViewById(R.id.itemRemove);
		vitemMoreQuantity = (TextView)convertView.findViewById(R.id.moreQuantity);




		linearLayout.setVisibility(View.VISIBLE);
		//vitemQty.setFocusable(true);
		/************  Set Model values in Holder elements ***********/
		//System.out.println("data details:"+(data.get(position).getItemQty())+"::::"+position);
		itemqty = _listDataHeader.get(groupPosition).getItemQty();
		itemprice = _listDataHeader.get(groupPosition).getItemPrice();
		itemtotal = itemqty * itemprice;
		vitemPrice.setText(getRs(itemprice));
		vitemName.setText(_listDataHeader.get(groupPosition).getsItemName());
		vitemSku.setText(_listDataHeader.get(groupPosition).getItemSku());
		vitemTotal.setText(getRs(itemqty * itemprice));
		vitemQty.setText(String.valueOf(itemqty));

		//vitemQty.setSelection(0);
		if(_listDataHeader.get(groupPosition).getCouponsArrayList()!=null) {
			if(_listDataHeader.get(groupPosition).getCouponsArrayList().get(0).getCpValue()!=null &&
					Double.parseDouble(_listDataHeader.get(groupPosition).getCouponsArrayList().get(0).getCpValue())< (itemprice))
			{
				vitemTotal.setText(getRs((itemprice-Double.parseDouble(_listDataHeader.get(groupPosition).getCouponsArrayList()
							.get(0).getCpValue()))*itemqty));
			}
		}
		else
		{
			itemtotal = itemqty * itemprice;
			vitemTotal.setText(getRs(itemtotal));
		}
		totalprice = 0;
		int count = 0;
		for (int i = 0; i < MainActivity.orderDetailsArrayList.size(); i++) {
			if(MainActivity.orderDetailsArrayList.get(i).getCouponsArrayList()!=null) {
				c = MainActivity.orderDetailsArrayList.get(i).getCouponsArrayList().get(0).getCpValue();
			}
			else{
				c=null;
			}
			if(c==null) {
				totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() *
						MainActivity.orderDetailsArrayList.get(i).getItemPrice()) + totalprice;
			}
			else{
				if(Double.parseDouble(MainActivity.orderDetailsArrayList.get(i).getCouponsArrayList().get(0).getCpValue())>
						MainActivity.orderDetailsArrayList.get(i).getItemPrice())
				{
					totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() * MainActivity.orderDetailsArrayList.get(i)
												.getItemPrice()) + totalprice;
					count++;

				}
				else {

					totalprice = (MainActivity.orderDetailsArrayList.get(i).getItemQty() * MainActivity.orderDetailsArrayList.get(i)
																		.getItemPrice()) + totalprice;
					totalprice = totalprice - (Integer.parseInt(MainActivity.orderDetailsArrayList.get(i).getCouponsArrayList().get(0)
														.getCpValue()) *
							MainActivity.orderDetailsArrayList.get(i).getItemQty());
				}
			}
		}

		if(count>0)
		{
			Toast.makeText(convertView.getContext(), Messages.INVALID_COUPON, Toast.LENGTH_SHORT).show();
			count=0;
		}
		MainActivity.vTotalconut.setText(getRs(totalprice));
		MainActivity.itemCount.setText(String.valueOf(MainActivity.orderDetailsArrayList.size()));
		vitemRemove.setOnClickListener(new ExpandableListAdapterTest.OnItemClickListener(groupPosition));
		moreQuantity.setOnClickListener(new ExpandableListAdapterTest.OnItemClickListener(groupPosition));

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
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

			// TODO Auto-generated method stub
			switch(v.getId()) {
				case R.id.moreQuantity: {
					// do here code what u want on imagebutton click
					qdialog(mPosition);
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
	}

	public void qdialog(final int position) {
		final View[] focusView = {null};
		final boolean[] cancel = {false};
		final Dialog dialog = new Dialog(this._context);
		dialog.setContentView(R.layout.fragment_add_item__spinner);
		dialog.show();
		final EditText equantity = (EditText) dialog.findViewById(R.id.quantity);

		Button btnSaveButton = (Button) dialog
				.findViewById(R.id.addQuantity);
		btnSaveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				qnty = equantity.getText().toString();
				MainActivity.orderDetailsArrayList.get(position).setItemQty(Double.parseDouble(qnty));
				MainActivity.listViewAdpter.notifyDataSetChanged();
				dialog.hide();
			}
		});
	}
	public String getRs(double total){
		return String.format(res.getString(R.string.Rs), String.format("%.02f", total));
	}
}
