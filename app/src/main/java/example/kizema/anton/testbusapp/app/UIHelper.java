package example.kizema.anton.testbusapp.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class UIHelper {
    public static final int HIDE_KEYBOARD_TIME = 200;

    private static DisplayMetrics metrics;

    private static final int WIDTH_HD = 1080;
    private static final int HEIGHT_HD = 1920;
    private static Context appContext;

    public static int keyboardHeight;

    public static void init(Context c){
        appContext = c;

        metrics = appContext.getResources().getDisplayMetrics();

        keyboardHeight = getPixel(215);
    }

    public static int getW() {
        return ((WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public static int getH() {
        return ((WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

//    public static int getRelativeW() {
//        return width / WIDTH_HD;
//    }
//
//    public static int getRelativeH() {
//        return height / HEIGHT_HD;
//    }

    public static int getPixel(float dpi){
        return (int)(metrics.density * dpi);
    }

    public static float getPixelF(float dpi){
        return metrics.density * dpi;
    }

    public static float getPixelFHacked(float dpi){
        if (metrics.density <= 1){
            return dpi;
        }

        if (metrics.density <= 2){
            return 2 * dpi;
        }

        if (metrics.density <= 3){
            return 3 * dpi;
        }

        return 4 * dpi;
    }


    public static int getDPI(int px){
        return (int)(px / metrics.density);
    }

    public static void setKeyboardHeight(int h){
        keyboardHeight = h;
    }

    public static int getKeyboardHeight(){
        return keyboardHeight;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                inputManager.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void hideKeyboard(Activity activity, IBinder binder) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (binder != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(binder, 0);//HIDE_NOT_ALWAYS
                inputManager.showSoftInputFromInputMethod(binder, 0);
            }
        }
    }

    public static void showKeyboard(Activity activity, View view) {
        if (activity != null && view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                view.requestFocus();
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    public static int getActionBarHeight() {
        final TypedArray styledAttributes = appContext.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });

        int result = (int) styledAttributes.getDimension(0, UIHelper.getPixel(40));
        styledAttributes.recycle();

        return result;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}

