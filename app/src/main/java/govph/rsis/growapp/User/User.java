package govph.rsis.growapp.User;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;

    @ColumnInfo(name = "serialNum")
    public String serialNum;

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getSerialNum() {
        return serialNum;
    }
}
