package example.kizema.anton.testbusapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import example.kizema.anton.testbusapp.adapters.BusRouteAdapter;
import example.kizema.anton.testbusapp.model.BusModel;

public class BusTabController {

    protected static final String TAG = BusTabController.class.getSimpleName();

    private OnBusTabCallback listener;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView rvEntries;
    private BusRouteAdapter busAdapter;

    private Type type;

    public enum Type implements Serializable {
        ARRIVALS, DEPARTURES
    }

    public interface OnBusTabCallback {
        void onBusRouteClick(BusModel model);
        void onFetchData();
    }

    public BusTabController(Type type, final View parentView, OnBusTabCallback listener) {
        this.type = type;
        this.listener = listener;

        initViews(parentView);
    }

    private void initViews(View parentView) {
        swipeLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.swipeContainer);
        swipeLayout.setColorSchemeColors(parentView.getResources().getColor(R.color.colorStribe),
                parentView.getResources().getColor(R.color.textColorDark));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BusTabController.this.update();
            }
        });

        rvEntries = (RecyclerView) parentView.findViewById(R.id.rvUsers);

        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(parentView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        rvEntries.setLayoutManager(mChatLayoutManager);

        busAdapter = new BusRouteAdapter(getFromDb());

        busAdapter.setOnUserClickListener(new BusRouteAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(BusModel model) {
                listener.onBusRouteClick(model);
            }
        });
        rvEntries.setAdapter(busAdapter);
        rvEntries.setHasFixedSize(true);
    }

    private List<BusModel> getFromDb() {
        return BusModel.selectByArrivals(type == Type.ARRIVALS);
    }

    public void setRefreshing() {
        swipeLayout.setRefreshing(true);
    }

    public void update() {
        swipeLayout.setRefreshing(true);
        listener.onFetchData();
    }

    public void onUpdated() {
        busAdapter.update(getFromDb());
        swipeLayout.setRefreshing(false);
        Log.v("rr", "getBusses() onUpdated");
    }

}
