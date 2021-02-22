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

    @ColumnInfo(name ="fullname")
    public String fullname;

    @ColumnInfo (name = "accredArea")
    public String accredArea;

    @ColumnInfo (name = "isLoggedIn")
    public String isLoggedIn;


    public void setSerialNum(String serialNum) { this.serialNum = serialNum; }

    public String getSerialNum() { return serialNum; }

    public String getFullname() { return fullname; }

    public void setFullname(String fullname) { this.fullname = fullname; }

    public void setAccredArea(String accredArea) { this.accredArea = accredArea; }

    public String getAccredArea() { return accredArea; }

    public void setLoggedIn(String loggedIn) { isLoggedIn = loggedIn; }
    public String getIsLoggedIn(){return isLoggedIn;}
}
