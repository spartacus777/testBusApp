package example.kizema.anton.testbusapp.helpers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import example.kizema.anton.testbusapp.R;
import example.kizema.anton.testbusapp.adapters.RouteAdapter;
import example.kizema.anton.testbusapp.app.UIHelper;
import example.kizema.anton.testbusapp.model.RouteModel;

public class DetailsDialog extends Dialog {

    private RecyclerView rvNames;
    private RouteAdapter routeAdapter;
    private TextView tvNoItems;

    private String lineId;
    private boolean arrival;

    public DetailsDialog(Context context, String lineId, boolean arrival) {
        super(context, R.style.OpacityDialog);

        this.lineId = lineId;
        this.arrival = arrival;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.details_dialog);

        Window window = this.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        ViewGroup parent = (ViewGroup) findViewById(R.id.parent);
        parent.getLayoutParams().height = UIHelper.getH() - UIHelper.getPixel(60);
        parent.getLayoutParams().width = UIHelper.getW() - UIHelper.getPixel(30);

        rvNames = (RecyclerView) findViewById(R.id.rvNames);
        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.VERTICAL, false);
        rvNames.setLayoutManager(mChatLayoutManager);

        routeAdapter = new RouteAdapter(getFromDb());
        rvNames.setAdapter(routeAdapter);
        rvNames.setHasFixedSize(true);

        tvNoItems = (TextView) findViewById(R.id.tvNoItems);
        if (routeAdapter.getItemCount() == 0){
            tvNoItems.setVisibility(View.VISIBLE);
        }
    }

    private float px, py;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            px = ev.getX();
            py = ev.getY();
        }

        if (ev.getAction() == MotionEvent.ACTION_UP &&
                (Math.pow(px - ev.getX(), 2) + Math.pow(py - ev.getY(), 2) < 100 )){
            dismiss();
        }
        return super.dispatchTouchEvent(ev);
    }

    private List<RouteModel> getFromDb(){
        return RouteModel.selectByLineId(lineId, arrival);
    }
}

