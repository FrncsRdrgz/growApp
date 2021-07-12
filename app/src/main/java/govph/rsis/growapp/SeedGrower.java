package govph.rsis.growapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SeedGrower {

    @PrimaryKey(autoGenerate = true)
    public int sgId;

    @ColumnInfo(name = "macaddress")
    public String macaddress;

    @ColumnInfo(name = "accredno")
    public String accredno;

    @ColumnInfo(name="latitude")
    public String latitude;

    @ColumnInfo (name="longitude")
    public String longitude;

    @ColumnInfo (name="variety")
    public String variety;

    @ColumnInfo(name="seedsource")
    public String seedsource;

    @ColumnInfo(name="otherseedsource")
    public String otherseedsource;

    @ColumnInfo(name="seedclass")
    public String seedclass;

    @ColumnInfo(name="dateplanted")
    public String dateplanted;

    @ColumnInfo(name="areaplanted")
    public String areaplanted;

    @ColumnInfo(name="quantity")
    public String quantity;

    @ColumnInfo(name="seedbedarea")
    public String seedbedarea;

    @ColumnInfo(name="seedlingage")
    public String seedlingage;

    @ColumnInfo (name="seedlot")
    public String seedlot;

    @ColumnInfo (name="controlno")
    public String controlno;

    @ColumnInfo (name="barangay")
    public String barangay;

    @ColumnInfo (name="datecollected")
    public String datecollected;

    @ColumnInfo (name="isSent")
    public Boolean isSent;

    @ColumnInfo (name="riceProgram")
    public String riceProgram;

    @ColumnInfo (name="coop")
    public String coop;

    @ColumnInfo (name="previousCrop")
    public String previousCrop;

    @ColumnInfo (name="previousVariety")
    public String previousVariety;

    @ColumnInfo (name="bought_id")
    public String bought_id;

    @ColumnInfo (name="transplanting_method")
    public String transplanting_method;

    @ColumnInfo (name="region")
    public String region;

    @ColumnInfo (name="province")
    public String province;

    @ColumnInfo (name="municipality")
    public String municipality;

    public SeedGrower(String macaddress, String accredno, String latitude, String longitude, String variety, String seedsource, String otherseedsource, String seedclass, String dateplanted,
                       String areaplanted, String quantity, String seedbedarea, String seedlingage, String seedlot, String controlno, String barangay, String datecollected, Boolean isSent,
                      String riceProgram, String coop, String previousCrop, String previousVariety,String transplanting_method){
        this.macaddress = macaddress;
        this.accredno = accredno;
        this.latitude = latitude;
        this.longitude = longitude;
        this.variety = variety;
        this.seedsource = seedsource;
        this.otherseedsource = otherseedsource;
        this.seedclass = seedclass;
        this.dateplanted = dateplanted;
        this.areaplanted = areaplanted;
        this.quantity = quantity;
        this.seedbedarea = seedbedarea;
        this.seedlingage = seedlingage;
        this.seedlot = seedlot;
        this.controlno = controlno;
        this.barangay = barangay;
        this.datecollected = datecollected;
        this.isSent = isSent;
        this.riceProgram = riceProgram;
        this.coop = coop;
        this.previousCrop = previousCrop;
        this.previousVariety = previousVariety;
        this.transplanting_method = transplanting_method;
    }


    public int getSgId() {return sgId;}
    public void setSgId(int sgId) {this.sgId = sgId; }
    public String getMacaddress() {return macaddress;}
    public void setMacaddress(String macaddress) {this.macaddress = macaddress;}

    public String getAccredno() {return accredno;}
    public void setAccredno(String accredno) {this.accredno = accredno;}

    public String getLatitude() {return latitude;}
    public void setLatitude(String latitude) {this.latitude = latitude;}

    public String getLongitude() {return longitude;}
    public void setLongitude(String longitude) {this.longitude = longitude;}

    public String getVariety() { return variety;}
    public void setVariety(String variety) {this.variety = variety;}

    public String getSeedsource() {return seedsource;}
    public void setSeedsource(String seedsource){this.seedsource = seedsource;}

    public String getOtherseedsource() {return otherseedsource;}
    public void setOtherseedsource(String otherseedsource) {this.otherseedsource = otherseedsource;}

    public String getSeedclass() {return seedclass;}
    public void setSeedclass(String seedclass) {this.seedclass = seedclass;}

    public String getDateplanted() {return dateplanted;}
    public void setDateplanted(String dateplanted) {this.dateplanted = dateplanted;}

    public String getAreaplanted() {return areaplanted;}
    public void setAreaplanted(String areaplanted) {this.areaplanted = areaplanted;}

    public String getQuantity() {return quantity;}
    public void setQuantity(String quantity) {this.quantity = quantity;}

    public String getSeedbedarea() {return seedbedarea;}
    public void setSeedbedarea(String seedbedarea) {this.seedbedarea = seedbedarea;}

    public String getSeedlingage() {return seedlingage;}
    public void setSeedlingage(String seedlingage) {this.seedlingage = seedlingage;}

    public String getSeedlot() {return seedlot;}
    public void setSeedlot(String seedlot) {this.seedlot = seedlot;}

    public String getControlno() {return controlno;}
    public void setControlno(String controlno) {this.controlno = controlno;}

    public String getBarangay() {return barangay;}
    public void setBarangay(String barangay) {this.barangay = barangay;}

    public String getDatecollected() {return datecollected;}
    public void setDatecollected(String datecollected) {this.datecollected = datecollected;}

    public void setIsSent(Boolean isSent) {this.isSent = isSent;}

    public String getRiceProgram() {return riceProgram;}

    public String getCoop() {return coop;}

    public String getPreviousCrop(){return previousCrop;}

    public String getPreviousVariety() { return previousVariety; }

    public void setBought_id(String bought_id) { this.bought_id = bought_id; }
    public String getBought_id() {return bought_id;}

    public String getTransplanting_method(){return transplanting_method;}
}
