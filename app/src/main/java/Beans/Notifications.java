package Beans;

public class Notifications {
    private String city;
    private String fuid;
    private String img_url;
    private String name;
    private String nid;
    private String on;
    private String phone_no;
    private String pid;
    private String status;
    private int type;

    public String getName() {
        return this.name;
    }

    public String getImg_url() {
        return this.img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone_no() {
        return this.phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNid() {
        return this.nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOn() {
        return this.on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getFuid() {
        return this.fuid;
    }

    public void setFuid(String fuid) {
        this.fuid = fuid;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
