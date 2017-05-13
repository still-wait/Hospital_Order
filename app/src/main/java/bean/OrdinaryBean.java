package bean;

import android.graphics.drawable.Drawable;

public class OrdinaryBean {
	private String keshi_name;
	private String doctor_info;
	private String keshihaoyue;
	private String yuyue_status;
	private Drawable imagehead;
	private String time;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public OrdinaryBean(String keshi_name, String doctor_info,String keshihaoyue, String yuyue_status,Drawable imagehead, String time){
		this.keshi_name=keshi_name;
		this.doctor_info=doctor_info;
		this.keshihaoyue=keshihaoyue;
		this.yuyue_status=yuyue_status;
		this.imagehead=imagehead;
		this.time=time;
	}



	public String getDoctor_info() {
		return doctor_info;
	}

	public String getYuyue_status() {
		return yuyue_status;
	}

	public Drawable getImagehead() {
		return imagehead;
	}

	public String getKeshi_name() {
		return keshi_name;
	}

	public String getKeshihaoyue() {
		return keshihaoyue;
	}

	public void setKeshi_name(String keshi_name) {
		this.keshi_name = keshi_name;
	}

	public void setKeshihaoyue(String keshihaoyue) {
		this.keshihaoyue = keshihaoyue;
	}

	public void setDoctor_info(String doctor_info) {
		this.doctor_info = doctor_info;
	}

	public void setYuyue_status(String yuyue_status) {
		this.yuyue_status = yuyue_status;
	}

	public void setImagehead(Drawable imagehead) {
		this.imagehead = imagehead;
	}
}
