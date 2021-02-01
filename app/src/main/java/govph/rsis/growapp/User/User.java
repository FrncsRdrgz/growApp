package govph.rsis.growapp.User;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {



    @ColumnInfo(name = "serialNum")
    public String serialNum;

    @ColumnInfo(name ="fullname")
    public String fullname;
    
    @PrimaryKey(autoGenerate = true)
    public int userId;

    public void setSerialNum(String serialNum) { this.serialNum = serialNum; }

    public String getSerialNum() { return serialNum; }

    public String getFullname() { return fullname; }

    public void setFullname(String fullname) { this.fullname = fullname; }
}
