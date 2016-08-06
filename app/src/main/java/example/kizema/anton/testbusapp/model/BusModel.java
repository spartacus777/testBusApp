package example.kizema.anton.testbusapp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


@Table(name = "BusModel")
public class BusModel extends Model {

    public static final String ARRIVALS = "arrivals";
    public static final String LINE_CODE = "line_code";
    public static final String DIRECTION = "direction";
    public static final String THROUGH_THE_STATIONS = "through_the_stations";
    public static final String TIMESTAMP = "timestamp";
    public static final String TZ = "tz";


    @Column(name = LINE_CODE, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String id;

    @Column(name = ARRIVALS)
    public boolean isArrivals;

    @Column(name = DIRECTION)
    public String direction;

    @Column(name = TIMESTAMP)
    public long timestamp;

    @Column(name = TZ)
    public String timezone;

    @Column(name = THROUGH_THE_STATIONS)
    public String through;


    public static BusModel selectById(String id){
        return new Select().from(BusModel.class).where(LINE_CODE + " = ?", id).executeSingle();
    }

    public static BusModel create(String id,
                                  boolean isArrival,
                                  String direction,
                                  long timestamp,
                                  String timezone,
                                  String through){

        BusModel notif = selectById(id);
        if (notif == null){
            notif = new BusModel();
            notif.id = id;
        }

        notif.isArrivals = isArrival;
        notif.direction = direction;
        notif.timestamp = timestamp;
        notif.timezone = timezone;
        notif.through = through;

        notif.saveSafe();

        return notif;
    }

    public String string(){
        return  "ID:" + id + " ARRIVALS:"+isArrivals+" DIRECTION:"+direction+ " THROUGH_THE_STATIONS:"+through+
        "TIMESTAMP:"+timestamp+" TZ:"+timezone;
    }

    public void saveSafe(){
        try {
            save();
        } catch (Throwable e){
        }
    }

}

