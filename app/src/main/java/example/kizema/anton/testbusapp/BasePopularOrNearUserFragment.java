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

import java.util.ArrayList;
import java.util.List;

public class BasePopularOrNearUserFragment extends Fragment {

    protected static final String TAG = BasePopularOrNearUserFragment.class.getSimpleName();

    private OnPopularOrNearUserClick listener;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView rvUsers;
    private BusRouteAdapter busAdapter;
    private ViewGroup noPeopleNear;

    public enum SearchType{
        POPULAR, NEARBY
    }

    public interface OnPopularOrNearUserClick {
        void onPopularOrNearUserClick(String str);
    }

    public interface OnFinished{
        void onFinished();
    }

    public static BasePopularOrNearUserFragment newInstance() {
        BasePopularOrNearUserFragment fragment = new BasePopularOrNearUserFragment();
//        Bundle bndl = new Bundle();
//        bndl.putInt(CONTENT_HEIGTH, contentHeight);
//        fragment.setArguments(bndl);
        return fragment;
    }

    public BasePopularOrNearUserFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("ANT", "onCreate this" + this);

        Log.d("ANT", "onCreate");
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
//        swipeLayout.setColorSchemeColors(ColorManager.rose, ColorManager.violet);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO
//                update();
            }
        });

        rvUsers = (RecyclerView) parentView.findViewById(R.id.rvUsers);

        LinearLayoutManager mChatLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvUsers.setLayoutManager(mChatLayoutManager);


        List<String> demo = new ArrayList<>();
        demo.add("buss stop 1");
        demo.add("buss stop 2");

        busAdapter = new BusRouteAdapter(demo);

        busAdapter.setOnUserClickListener(new BusRouteAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(String str) {
                listener.onPopularOrNearUserClick(str);
            }
        });
        rvUsers.setAdapter(busAdapter);
        rvUsers.setHasFixedSize(true);

//        noPeopleNear = (ViewGroup) parentView.findViewById(R.id.noPeopleNear);
//        noPeopleNear.setVisibility(View.GONE);
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
