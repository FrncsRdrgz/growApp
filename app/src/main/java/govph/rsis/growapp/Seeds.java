package govph.rsis.growapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Seeds {

    @PrimaryKey(autoGenerate = true)
    public int seed_id;

    @ColumnInfo(name="variety")
    public String variety;

    public Seeds(String variety){
        this.variety = variety;
    }
    public void setVariety(String variety){this.variety = variety;}
    public String getVariety() {
        return variety;
    }
}
