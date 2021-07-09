package govph.rsis.growapp.Municipality;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Municipality {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="municipality_id")
    public int municipality_id;

    @ColumnInfo(name="province_id")
    public int province_id;

    @ColumnInfo(name="municipality")
    public String municipality;

    public void setMunicipality_id(int municipality_id){this.municipality_id = municipality_id;}
    public void setProvince_id(int province_id){this.province_id = province_id;}
    public void setMunicipality(String municipality){this.municipality = municipality;}

    public int getMunicipality_id(){return municipality_id;}
    public int getProvince_id(){return province_id;}
    public String getMunicipality(){return municipality;}
}
