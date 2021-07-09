package govph.rsis.growapp.Province;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Province {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="province_id")
    public int province_id;

    @ColumnInfo(name="region_id")
    public int region_id;

    @ColumnInfo(name="province")
    public String province;

    public void setProvince_id(int province_id) { this.province_id = province_id; }
    public void setRegion_id(int region_id) {this.region_id = region_id;}
    public void setProvince(String province){this.province = province;}

    public int getProvince_id(){return province_id;}
    public int getRegion_id(){return region_id;}
    public String getProvince(){return province;}

}
