package bean;

/**
 * Created by Y-GH on 2017/5/13.
 */

public class OrderBean {
    private String orderid;
    private String userid;
    private String category;
    private String time;
    private String idcard;
    private String price;
    private String sort;
    private String repert;
    private String keshi;
    private String paystatus;

    public OrderBean(String orderid, String userid, String category, String time, String idcard, String price, String sort, String repert, String keshi, String paystatus) {
        this.orderid = orderid;
        this.userid = userid;
        this.category = category;
        this.time = time;
        this.idcard = idcard;
        this.price = price;
        this.sort = sort;
        this.repert = repert;
        this.keshi = keshi;
        this.paystatus = paystatus;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getUserid() {
        return userid;
    }

    public String getCategory() {
        return category;
    }

    public String getTime() {
        return time;
    }

    public String getIdcard() {
        return idcard;
    }

    public String getPrice() {
        return price;
    }

    public String getSort() {
        return sort;
    }

    public String getRepert() {
        return repert;
    }

    public String getKeshi() {
        return keshi;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setRepert(String repert) {
        this.repert = repert;
    }

    public void setKeshi(String keshi) {
        this.keshi = keshi;
    }
}
