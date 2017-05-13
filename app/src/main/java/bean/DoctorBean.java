package bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Y-GH on 2017/5/12.
 */
public class DoctorBean {
    private String keshiname;
    private String doctor_name;
    private String yishengrank;
    private String doctor_flag;
    private String doctor_info;
    private String time;
    private String yuyue_status;
    private String yishenghaoyuan;
    private Drawable imagehead;

    public DoctorBean(String keshiname, String doctor_name, String yishengrank, String doctor_flag, String doctor_info, String time, String yuyue_status, String yishenghaoyuan,Drawable imagehead) {
        this.keshiname = keshiname;
        this.doctor_name = doctor_name;
        this.yishengrank = yishengrank;
        this.doctor_flag = doctor_flag;
        this.doctor_info = doctor_info;
        this.time = time;
        this.yuyue_status = yuyue_status;
        this.yishenghaoyuan = yishenghaoyuan;
        this.imagehead = imagehead;
    }

    public String getKeshiname() {
        return keshiname;
    }


    public String getYishengrank() {
        return yishengrank;
    }





    public String getYishenghaoyuan() {
        return yishenghaoyuan;
    }

    public void setKeshiname(String keshiname) {
        this.keshiname = keshiname;
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

    public String getTime() {
        return time;
    }

    public String getYuyue_status() {
        return yuyue_status;
    }

    public Drawable getImagehead() {
        return imagehead;
    }

    public void setYishengrank(String yishengrank) {
        this.yishengrank = yishengrank;
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

    public void setTime(String time) {
        this.time = time;
    }

    public void setYuyue_status(String yuyue_status) {
        this.yuyue_status = yuyue_status;
    }

    public void setImagehead(Drawable imagehead) {
        this.imagehead = imagehead;
    }

    public void setYishenghaoyuan(String yishenghaoyuan) {
        this.yishenghaoyuan = yishenghaoyuan;
    }
}
