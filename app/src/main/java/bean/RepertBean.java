package bean;

import android.graphics.drawable.Drawable;

public class RepertBean {
	private String doctor_name;
	private String doctor_flag;
	private String doctor_info;
	private String yuyue_status;
	private Drawable imagehead;
	private String time;

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public RepertBean(String doctor_name, String doctor_flag, String doctor_info, String yuyue_status, Drawable imagehead, String time){
		this.doctor_name=doctor_name;
		this.doctor_flag=doctor_flag;
		this.doctor_info=doctor_info;
		this.yuyue_status=yuyue_status;
		this.imagehead=imagehead;
		this.time=time;
	}

	public String getDoctor_name() {
		return doctor_name;
	}

	public String getDoctor_flag() {
		return doctor_flag;
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

	public void setDoctor_name(String doctor_name) {
		this.doctor_name = doctor_name;
	}

	public void setDoctor_flag(String doctor_flag) {
		this.doctor_flag = doctor_flag;
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
