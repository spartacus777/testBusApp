package example.kizema.anton.testbusapp.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.kizema.anton.testbusapp.model.BusModel;
import example.kizema.anton.testbusapp.model.RouteModel;

/**
 * Created by somename on 06.08.2016.
 */
public class JsonHelper {

    private static JsonHelper instance;

    public static final String TIMETABLE = "timetable";
    public static final String ARRIVALS = "arrivals";
    public static final String DEPARTURES = "departures";
    public static final String LINE_CODE = "line_code";
    public static final String DIRECTION = "direction";
    public static final String DATATIME = "datetime";
    public static final String THROUGH_THE_STATIONS = "through_the_stations";
    public static final String ROUTE = "route";
    public static final String TIMESTAMP = "timestamp";
    public static final String TZ = "tz";
    public static final String ADDRESS = "address";
    public static final String NAME = "name";
    public static final String ID = "id";


    public static synchronized JsonHelper getInstance(){
        if (instance == null){
            instance = new JsonHelper();
        }

        return instance;
    }

    private JsonHelper(){}

    public List<BusModel> parse(String bussJson){
        try {
            JSONObject obj = new JSONObject(bussJson);

            JSONArray arrayArrivals = obj.getJSONObject(TIMETABLE).getJSONArray(ARRIVALS);
            JSONArray arrayDepartures = obj.getJSONObject(TIMETABLE).getJSONArray(DEPARTURES);

            List<BusModel> busModels = new ArrayList<>();

            privateParse(arrayArrivals, true, busModels);
            privateParse(arrayDepartures, false, busModels);

            return busModels;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void privateParse(JSONArray array, boolean isArrival, List<BusModel> busModels) {

        for (int i = 0; i < array.length(); ++i){

            try {
                String lineCode = array.getJSONObject(i).getString(LINE_CODE);
                String direction = array.getJSONObject(i).getString(DIRECTION);
                String through_the_stations = array.getJSONObject(i).getString(THROUGH_THE_STATIONS);
                JSONObject datetime = array.getJSONObject(i).getJSONObject(DATATIME);
                long timestamp = datetime.getLong(TIMESTAMP);
                String tz = datetime.getString(TZ);

                JSONArray route = array.getJSONObject(i).getJSONArray(ROUTE);

                for (int j = 0; j < route.length(); ++j) {
                    String name = route.getJSONObject(j).getString(NAME);
                    String address = route.getJSONObject(j).getString(ADDRESS);
                    String id = route.getJSONObject(j).getString(ID);

                    RouteModel.create(id, lineCode, isArrival, name, address);

//                    Log.d("rr", "route created : " + lineCode + " , " + isArrival + " , " + name + " , " + address);
                }

                BusModel model = BusModel.create(lineCode, isArrival, direction, timestamp, tz, through_the_stations);
                busModels.add(model);
            } catch (JSONException ex){
                //probably broken entry, ignore it
            }
        }
    }

}
