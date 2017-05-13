package ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hospital2.R;

import java.util.List;

import bean.DoctorBean;
import bean.OrdinaryBean;
import bean.RepertBean;
import ui.tool.XCRoundImageView;

public class RepertAdapter extends ArrayAdapter<DoctorBean> {

	private int resourceId;

	public RepertAdapter(Context context, int textViewResourceId,
                         List<DoctorBean> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DoctorBean bean = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.headImage = (XCRoundImageView) view.findViewById(R.id.photo);
			viewHolder.doctor_name = (TextView) view.findViewById(R.id.doctor_name);
			viewHolder.doctor_flag=(TextView) view.findViewById(R.id.doctor_flag);
			viewHolder.doctor_info=(TextView) view.findViewById(R.id.doctor_info);
			viewHolder.yuyue_status=(TextView) view.findViewById(R.id.yuyue_status);
			viewHolder.time=(TextView) view.findViewById(R.id.yuyue_time);
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
		viewHolder.doctor_name.setText(bean.getDoctor_name());
		viewHolder.doctor_flag.setText(bean.getDoctor_flag());
		viewHolder.doctor_info.setText(bean.getDoctor_info());
		viewHolder.yuyue_status.setText(bean.getYuyue_status());
		viewHolder.time.setText(bean.getTime());
		return view;
	}
	
	class ViewHolder {
		
		XCRoundImageView headImage;
		TextView doctor_name;
		TextView doctor_flag;
		TextView doctor_info;
		TextView yuyue_status;
		TextView time;
		
	}
}
