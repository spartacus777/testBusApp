package example.kizema.anton.testbusapp.control;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import example.kizema.anton.testbusapp.helpers.JsonHelper;
import example.kizema.anton.testbusapp.model.BusModel;

/**
 * Created by somename on 06.08.2016.
 */
public class Controller {

    private static Controller instance;

    public static synchronized Controller getInstance(){
        if (instance == null){
            instance = new Controller();
        }

        return instance;
    }

    private Controller(){}

    public void getBusses(){
        HttpHelper.getInstance().getAsync(ApiHelper.URL_HOST + ApiHelper.URL_GET_BUSSES,
                new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d("rr", "exception :: " + e);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String body = response.body().string();

                        List<BusModel> busModels = JsonHelper.getInstance().parse(body);
                        for (BusModel m : busModels){
                            Log.d("rr", "MODEL : " + m.string());
                        }
                    }
                });
    }

}
