package ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hospital2.R;

import java.util.List;

import bean.KeshiBean;
import bean.OrderBean;


public class OrderAdapter extends ArrayAdapter<OrderBean> {

	private int resourceId;

	public OrderAdapter(Context context, int textViewResourceId,
                        List<OrderBean> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OrderBean keshi = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.orderid = (TextView) view.findViewById(R.id.orderid);
			viewHolder.time = (TextView) view.findViewById(R.id.ordertime);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.orderid.setText(keshi.getOrderid());
		viewHolder.time.setText(keshi.getTime());
		return view;
	}
	
	class ViewHolder {
		

		TextView orderid;
		TextView time;
		

	}
}
