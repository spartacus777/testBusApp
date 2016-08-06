package example.kizema.anton.testbusapp.control;

import android.content.Intent;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import example.kizema.anton.testbusapp.app.App;
import example.kizema.anton.testbusapp.helpers.JsonHelper;
import example.kizema.anton.testbusapp.model.BusModel;

public class Controller {

    public static final String FETCH_ACTION = "FETCH_ACTION";

    private static Controller instance;

    public static synchronized Controller getInstance(){
        if (instance == null){
            instance = new Controller();
        }

        return instance;
    }

    private Controller(){}

    public void getBusses(){
        Log.v("rr", "getBusses() request");

        HttpHelper.getInstance().getAsync(ApiHelper.URL_HOST + ApiHelper.URL_GET_BUSSES,
                new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.v("rr", "getBusses() exception " + e);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String body = response.body().string();

                        Log.v("rr", "getBusses() onResponse " + body);

                        List<BusModel> busModels = JsonHelper.getInstance().parse(body);
//                        for (BusModel m : busModels) {
//                            Log.d("rr", "MODEL : " + m.string());
//                        }

                        Log.v("rr", "getBusses() parse ready");

                        Intent intent = new Intent();
                        intent.setAction(FETCH_ACTION);
                        App.getAppContext().sendOrderedBroadcast(intent, null);
                    }
                });
    }

}
