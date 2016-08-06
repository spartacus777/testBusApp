package example.kizema.anton.testbusapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import example.kizema.anton.testbusapp.control.Controller;
import example.kizema.anton.testbusapp.model.BusModel;

public class BusTabController {

    protected static final String TAG = BusTabController.class.getSimpleName();

    private OnPopularOrNearUserClick listener;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView rvEntries;
    private BusRouteAdapter busAdapter;
    private ViewGroup noEntries;

    private Type type;
    private boolean isFirstStarted;

    public enum Type implements Serializable{
        ARRIVALS, DEPARTURES
    }

    public interface OnPopularOrNearUserClick {
        void onPopularOrNearUserClick(BusModel model);
    }

    public BusTabController(Type type, boolean isFirstStarted, View parentView) {
        this.type = type;
        this.isFirstStarted = isFirstStarted;
        initViews(parentView);
    }

    private void initViews(View parentView){
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
                listener.onPopularOrNearUserClick(model);
            }
        });
        rvEntries.setAdapter(busAdapter);
        rvEntries.setHasFixedSize(true);

        if (isFirstStarted) {
            update();
        }
//        noPeopleNear = (ViewGroup) parentView.findViewById(R.id.noPeopleNear);
//        noPeopleNear.setVisibility(View.GONE);
    }

    private List<BusModel> getFromDb(){
        List<BusModel> busModels;
        switch (type){
            case ARRIVALS:
                busModels = BusModel.selectByArrivals(true);
                break;
            default:
                busModels = BusModel.selectByArrivals(false);
                break;
        }

        return busModels;
    }

    public void update(){
        swipeLayout.setRefreshing(true);
        Controller.getInstance().getBusses();
    }

    public void onUpdated(){
        busAdapter.update(getFromDb());
        swipeLayout.setRefreshing(false);
    }

    public void addListener(OnPopularOrNearUserClick listener){
        this.listener = listener;
    }

}
