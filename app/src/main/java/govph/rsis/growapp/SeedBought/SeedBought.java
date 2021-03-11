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

    @ColumnInfo (name = "riceProgram")
    public String riceProgram;

    @ColumnInfo (name = "quantity")
    public int quantity;
    @ColumnInfo (name = "area")
    public double area;
    @ColumnInfo (name = "usedQuantity")
    public int usedQuantity;

    @ColumnInfo (name = "tableName")
    public String tableName;

    //setters
    public void setSerialNum(String serialNum) { this.serialNum = serialNum; }

    public void setPalletCode(String palletCode) { this.palletCode = palletCode; }

    public void setVariety(String variety) { this.variety = variety; }

    public void setSeedClass(String seedClass) { this.seedClass = seedClass; }

    public String getRiceProgram() { return riceProgram; }

    public void setArea(double area) { this.area = area; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void setUsedQuantity(int usedQuantity){ this.usedQuantity = usedQuantity; }

    public void setTableName(String tableName) { this.tableName = tableName; }

    //getters
    public String getSerialNum() { return serialNum; }

    public String getPalletCode() { return palletCode; }

    public String getSeedClass() { return seedClass; }

    public String getVariety() { return variety; }

    public void setRiceProgram(String riceProgram) { this.riceProgram = riceProgram; }

    public String getTableName() { return tableName; }

    public int getUsedQuantity() { return usedQuantity; }

    public int getQuantity() { return quantity; }

    public double getArea() { return area; }
}
