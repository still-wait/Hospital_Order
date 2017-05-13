package bean;

/**
 * Created by Y-GH on 2017/5/11.
 */

public class Userbean {
    private String userid;
    private String password;
    private String username;
    private String sex;
    private String idcard;

    public Userbean(String userId, String password, String username, String idcard, String sex) {
        this.userid = userId;
        this.password = password;
        this.username = username;
        this.sex=sex;
        this.idcard = idcard;
    }
    public Userbean(){

    }

    public String getUserId() {
        return userid;
    }
    public void setUserId(String userId) {
        this.userid = userId;
    }
    public String getUserPassword() {
        return password;
    }
    public void setUserPassword(String userPassword) {
        this.password = userPassword;
    }
    public String getusername() {
        return username;
    }
    public void setusername(String username) {
        this.username = username;
    }
    public String getidcard() {
        return idcard;
    }
    public void setidcard(String idcard) {
        this.idcard = idcard;
    }
    public String getsex() {
        return sex;
    }
    public void setsex(String sex) {
        this.sex = sex;
    }
}
