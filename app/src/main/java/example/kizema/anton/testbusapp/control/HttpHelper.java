package example.kizema.anton.testbusapp.control;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class HttpHelper {

    private static HttpHelper instance;

    private OkHttpClient okHttpClient;

    public static synchronized HttpHelper getInstance(){
        if (instance == null){
            instance = new HttpHelper();
        }

        return instance;
    }

    private HttpHelper(){
        init();
    }

    public void init(){
        okHttpClient = new OkHttpClient();
        okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
        okHttpClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    public void getAsync(String url, com.squareup.okhttp.Callback responseCallback) {
        Request request = new Request.Builder()
                .addHeader(ApiHelper.HEADER, ApiHelper.HEADER_TOKEN)
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(responseCallback);
    }

}
