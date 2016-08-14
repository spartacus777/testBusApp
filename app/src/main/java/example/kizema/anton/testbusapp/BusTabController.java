package example.kizema.anton.testbusapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import example.kizema.anton.testbusapp.adapters.BusRouteAdapter;
import example.kizema.anton.testbusapp.model.BusModel;

public class BusTabController extends Fragment {

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

    public static BusTabController newInstance(Type type){
        BusTabController c = new BusTabController();
        Bundle b = new Bundle();
        b.putInt("A", type.ordinal());
        c.setArguments(b);

        Log.d("FF", "newInstance " + c);

        return c;
    }

    public BusTabController(){}

//    public BusTabController(Type type, final View parentView, OnBusTabCallback listener) {
//        this.type = type;
//        this.listener = listener;
//
//        initViews(parentView);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FF", "onCreate " + this);

        int arg = getArguments().getInt("A", 0);
        type = Type.values()[arg];
        setRetainInstance(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("FF", "onCreateView " + this);

        View parent = inflater.inflate(R.layout.fragment_list, container, false);
        initViews(parent);
        return parent;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnBusTabCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnBusTabCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void initViews(View parentView) {
        Log.d("FF", "initViews " + this);

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
            public void onRouteCLick(BusModel model) {
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

    public void logSmth(){
        Log.e("rr", "info received!");
    }

}
