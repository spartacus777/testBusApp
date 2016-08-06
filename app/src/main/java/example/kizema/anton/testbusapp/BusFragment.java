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

import example.kizema.anton.testbusapp.control.Controller;
import example.kizema.anton.testbusapp.model.BusModel;

public class BusFragment extends Fragment {

    protected static final String TAG = BusFragment.class.getSimpleName();
    protected static final String TYPE = "type";
    protected static final String UPDATE = "UPDATE";

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

    public interface OnFinished{
        void onFinished();
    }

    public static BusFragment newInstance(Type type, boolean firstStarted) {
        BusFragment fragment = new BusFragment();
        Bundle bndl = new Bundle();
        bndl.putSerializable(TYPE, type);
        bndl.putBoolean(UPDATE, firstStarted);
        fragment.setArguments(bndl);
        return fragment;
    }

    public BusFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = (Type) getArguments().getSerializable(TYPE);
        isFirstStarted = getArguments().getBoolean(UPDATE);

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("ANT", "onActivityCreated this" + this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("ANT", "onCreateView this" + this);
        View parentView = inflater.inflate(R.layout.fragment_list, container, false);
        initViews(parentView);

        return parentView;
    }

    private void initViews(View parentView){
        swipeLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.swipeContainer);
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorStribe),
                getResources().getColor(R.color.textColorDark));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BusFragment.this.update();
            }
        });

        rvEntries = (RecyclerView) parentView.findViewById(R.id.rvUsers);

        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnPopularOrNearUserClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
