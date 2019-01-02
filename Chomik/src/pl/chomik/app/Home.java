package pl.chomik.app;

public class Home {
    public Home(Long advid, String itemid, String city, Integer value, Double field, String rooms, String url){
        super();
        this.ADVID = advid;
        this.ITEMID = itemid;
        this.CITY = city;
        this.VALUE = value;
        this.FIELD = field;
        this.ROOMS = rooms;
        this.URL = url;
    }

    private Long ADVID;
    private String ITEMID;
    private String CITY;
    private Integer VALUE;
    private Double FIELD;
    private String ROOMS;
    private String URL;

    public Long getADVID() { return ADVID; }
    public String getITEMID() {
        return ITEMID;
    }
    public String getCITY() {
        return CITY;
    }
    public Integer getVALUE() {
        return VALUE;
    }
    public Double getFIELD() {
        return FIELD;
    }
    public String getROOMS() {
        return ROOMS;
    }
    public String getURL() { return URL; }

    public void setADVID(Long ADVID){
        this.ADVID = ADVID;
    }
    public void setITEMID(String ITEMID) {
        this.ITEMID = ITEMID;
    }
    public void setCITY(String CITY) {
        this.CITY = CITY;
    }
    public void setVALUE(Integer VALUE) {
        this.VALUE = VALUE;
    }
    public void setFIELD(Double FIELD) {
        this.FIELD = FIELD;
    }
    public void setROOMS(String ROOMS) {
        this.ROOMS = ROOMS;
    }
    public void setURL(String URL) { this.URL = URL; }
}
