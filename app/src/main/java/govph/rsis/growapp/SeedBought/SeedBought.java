package govph.rsis.growapp.SeedBought;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SeedBought {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo (name = "serialNum")
    public String serialNum;

    @ColumnInfo (name = "palletCode")
    public String palletCode;

    @ColumnInfo (name = "variety")
    public String variety;

    @ColumnInfo (name = "seedClass")
    public String seedClass;

    @ColumnInfo (name = "quantity")
    public int quantity;

    @ColumnInfo (name = "usedQuantity")
    public int usedQuantity;

    @ColumnInfo (name = "tableName")
    public String tableName;
}
