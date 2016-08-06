package example.kizema.anton.testbusapp.app;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;

public class App extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();

        ActiveAndroid.initialize(appContext);
        UIHelper.init(appContext);
    }

    public static Context getAppContext(){
        return appContext;
    }

}
