package example.kizema.anton.testbusapp.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;

public class App extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();

        long timeAsyncInit = System.currentTimeMillis();
        ActiveAndroid.initialize(appContext);
        Log.d("UI", "ActiveAndroid init time : " + (System.currentTimeMillis() - timeAsyncInit) + " ms");

        UIHelper.init(appContext);
    }

    public static Context getAppContext(){
        return appContext;
    }

}
