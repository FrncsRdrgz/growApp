package govph.rsis.growapp.Region;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Region {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="region_id")
    public int region_id;

    @ColumnInfo(name="region")
    public String region;

    public int getRegion_id() { return region_id; }
    public void setRegion_id(int region_id) { this.region_id = region_id; }

    public String getRegion(){return region;}
    public void setRegion(String region) { this.region = region; }
}
