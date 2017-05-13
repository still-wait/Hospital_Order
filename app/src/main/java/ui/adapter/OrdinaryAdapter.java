package ui.adapter;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hospital2.R;

import bean.OrdinaryBean;
import ui.tool.XCRoundImageView;

public class OrdinaryAdapter extends ArrayAdapter<OrdinaryBean> {

	private int resourceId;

	public OrdinaryAdapter(Context context, int textViewResourceId,
                           List<OrdinaryBean> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OrdinaryBean bean = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.headImage = (XCRoundImageView) view.findViewById(R.id.photo_o);
			viewHolder.doctor_name = (TextView) view.findViewById(R.id.doctor_name_o);
			viewHolder.doctor_info=(TextView) view.findViewById(R.id.doctor_info_o);
			viewHolder.yuyue_status=(TextView) view.findViewById(R.id.yuyue_o);
			viewHolder.time=(TextView) view.findViewById(R.id.yuyue_time_o);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		
		if(bean.getImagehead()!=null){
			viewHolder.headImage.setImageDrawable(bean.getImagehead());
		}
		viewHolder.headImage.setType(XCRoundImageView.TYPE_ROUND);
		viewHolder.headImage.setRoundBorderRadius(20);
		if(bean.getKeshi_name()!=null){
			viewHolder.doctor_name.setText(bean.getKeshi_name());
		}
		if(bean.getDoctor_info()!=null){
			viewHolder.doctor_info.setText(bean.getDoctor_info());
		}
		viewHolder.yuyue_status.setText(bean.getYuyue_status());
		viewHolder.time.setText(bean.getTime());
		return view;
	}
	
	class ViewHolder {
		
		XCRoundImageView headImage;
		TextView doctor_name;
		TextView doctor_info;
		TextView yuyue_status;
		TextView time;
		
	}
}
