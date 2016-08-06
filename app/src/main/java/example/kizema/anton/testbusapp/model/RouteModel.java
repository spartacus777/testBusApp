package example.kizema.anton.testbusapp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;


@Table(name = "RouteModel")
public class RouteModel extends Model {

    public static final String ID = "route_id";
    public static final String ARRIVALS = "arrivals";
    public static final String LINE_CODE = "line_code";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";

    @Column(name = ID)
    public String id;

    @Column(name = LINE_CODE)
    public String lineId;

    @Column(name = ARRIVALS)
    public boolean isArrivals;

    @Column(name = NAME)
    public String name;

    @Column(name = ADDRESS)
    public String address;

    public static List<RouteModel> selectByLineId(String lineId, boolean isArrivals) {
        return new Select().from(RouteModel.class)
                .where(LINE_CODE + " = ?", lineId)
                .where(ARRIVALS + " = ?", isArrivals).execute();
    }

    public static RouteModel selectById(String id, String lineId, boolean isArrivals) {
        return new Select().from(RouteModel.class)
                .where(ID + " = ?", id)
                .where(LINE_CODE + " = ?", lineId)
                .where(ARRIVALS + " = ?", isArrivals).executeSingle();
    }

    public static RouteModel create(String id,
                                    String lineId,
                                    boolean isArrival,
                                    String name,
                                    String address) {

        RouteModel rm = selectById(id, lineId, isArrival);
        if (rm == null) {
            rm = new RouteModel();
            rm.id = id;
        }

//        RouteModel rm = new RouteModel();
//        rm.id = id;

        rm.lineId = lineId;
        rm.isArrivals = isArrival;
        rm.name = name;
        rm.address = address;
        rm.save();

        return rm;
    }

}