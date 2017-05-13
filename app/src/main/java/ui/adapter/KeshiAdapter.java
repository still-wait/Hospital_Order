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
import ui.tool.XCRoundImageView;


public class KeshiAdapter extends ArrayAdapter<KeshiBean> {

	private int resourceId;

	public KeshiAdapter(Context context, int textViewResourceId,
                        List<KeshiBean> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		KeshiBean keshi = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.headImage = (ImageView) view.findViewById(R.id.head_image_keshi);
			viewHolder.Name = (TextView) view.findViewById(R.id.keshi_name);
			viewHolder.discription = (TextView) view.findViewById(R.id.discription);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		if(keshi.getimagehead()!=null){
			viewHolder.headImage.setImageDrawable(keshi.getimagehead());
		}
//		viewHolder.headImage.setType(XCRoundImageView.TYPE_ROUND);
//		viewHolder.headImage.setRoundBorderRadius(20);//
		viewHolder.Name.setText(keshi.getName());
		viewHolder.discription.setText(keshi.getDiscription());
		return view;
	}
	
	class ViewHolder {
		
		ImageView headImage;
		
		TextView Name;
		TextView discription;
		

	}
}
