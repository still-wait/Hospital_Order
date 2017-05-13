package bean;

import android.graphics.drawable.Drawable;

public class KeshiBean {
	private String name;
	private String discription;
	private Drawable imagehead;
	private String mark;

	public KeshiBean(String name, String discription,String mark, Drawable imagehead){
		this.name=name;
		this.discription=discription;
		this.mark=mark;
		this.imagehead=imagehead;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Drawable getimagehead() {
		return imagehead;
	}
	public void setimagehead(Drawable imagehead) {
		this.imagehead = imagehead;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
}
