package example.kizema.anton.testbusapp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;


@Table(name = "BusModel")
public class BusModel extends Model {

    public static final String ARRIVALS = "arrivals";
    public static final String LINE_CODE = "line_code";
    public static final String DIRECTION = "direction";
    public static final String THROUGH_THE_STATIONS = "through_the_stations";
    public static final String TIMESTAMP = "timestamp";
    public static final String TZ = "tz";


    @Column(name = LINE_CODE)
    public String lineId;

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


    public static BusModel selectById(String id, boolean isArrivals){
        return new Select().from(BusModel.class)
                .where(LINE_CODE + " = ?", id)
                .where(ARRIVALS + " = ?", isArrivals)
                .executeSingle();
    }

    public static List<BusModel> selectByArrivals(boolean isArrivals){
        return new Select().from(BusModel.class).where(ARRIVALS + " = ?", isArrivals)
                .orderBy(BusModel.TIMESTAMP + " ASC").execute();
    }

    public static BusModel create(String id,
                                  boolean isArrival,
                                  String direction,
                                  long timestamp,
                                  String timezone,
                                  String through){

        BusModel busModel = selectById(id, isArrival);
        if (busModel == null){
            busModel = new BusModel();
            busModel.lineId = id;
            busModel.isArrivals = isArrival;
        }

        busModel.direction = direction;
        busModel.timestamp = timestamp;
        busModel.timezone = timezone;
        busModel.through = through;

        busModel.saveSafe();

        return busModel;
    }

    public String string(){
        return  "ID:" + lineId + " ARRIVALS:"+isArrivals+" DIRECTION:"+direction+ " THROUGH_THE_STATIONS:"+through+
        "TIMESTAMP:"+timestamp+" TZ:"+timezone;
    }

    public void saveSafe(){
        save();
    }

}

