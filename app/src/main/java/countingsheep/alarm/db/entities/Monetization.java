package countingsheep.alarm.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import countingsheep.alarm.util.TimestampConverter;

@Entity
public class Monetization implements Serializable {

    @PrimaryKey(autoGenerate = false)
    private int id;

    //The monetization type id used by the server
    private int typeId;

    private int choosenCostPerSnooze;

    @ColumnInfo(name = "dateChosen")
    @TypeConverters({TimestampConverter.class})
    private Date dateChosen;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getChoosenCostPerSnooze() {
        return choosenCostPerSnooze;
    }

    public void setChoosenCostPerSnooze(int choosenCostPerSnooze) {
        this.choosenCostPerSnooze = choosenCostPerSnooze;
    }

    public Date getDateChosen() {
        return dateChosen;
    }

    public void setDateChosen(Date dateChosen) {
        this.dateChosen = dateChosen;
    }
}
