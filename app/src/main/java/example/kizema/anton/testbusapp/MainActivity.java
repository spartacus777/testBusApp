package example.kizema.anton.testbusapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.kizema.anton.testbusapp.app.BaseActivity;
import example.kizema.anton.testbusapp.app.UIHelper;
import example.kizema.anton.testbusapp.control.Controller;
import example.kizema.anton.testbusapp.helpers.DetailsDialog;
import example.kizema.anton.testbusapp.model.BusModel;

public class MainActivity extends BaseActivity implements BusTabController.OnBusTabCallback {

    private static final String TAB_ARRIVAL = "TAB_ARRIVAL";
    private static final String TAB_DEPARTURE = "TAB_DEPARTURE";
    private static final String RECREATED = "KK";

    private FragmentTabHost tabHost;
    private ViewPager viewPager;
    private MyViewPagerAdapter appActivtyPagerAdapter;
    private BroadcastReceiver dataFetchedReceiver;

    private boolean firstStarted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.getBoolean(RECREATED)){
            firstStarted = false;
        }

        init();
        registerReceiver();
    }

    private void registerReceiver(){
        dataFetchedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                for (Fragment f : appActivtyPagerAdapter.getRegisteredFragments()){
                    ((BusTabController)f).onUpdated();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Controller.FETCH_ACTION);
        registerReceiver(dataFetchedReceiver, intentFilter, null, null);
    }

    private void init(){

        tabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        viewPager = (ViewPager) findViewById(R.id.viewpagerFragmentContainer);

        tabHost.setup(MainActivity.this, getSupportFragmentManager());
        tabHost.getTabWidget().setStripEnabled(false);
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        View tab = createTab(R.string.arrival, TAB_ARRIVAL);
        createTab(R.string.departure, TAB_DEPARTURE);

        tabHost.setOnTabChangedListener(new TabChangeListener(tab));

        appActivtyPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());

        if (!firstStarted){
            for (Fragment f : appActivtyPagerAdapter.getRegisteredFragments()){
                ((BusTabController)f).logSmth();
            }
        }

        viewPager.setAdapter(appActivtyPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setCurrentTab(position);
            }
        });

        if (firstStarted) {
            onFetchData();
        }
    }

    private View createTab(int labelId, String tabId){
        View tab = LayoutInflater.from(this).inflate(R.layout.bus_tab, tabHost, false);
        TextView tvLable = (TextView) tab.findViewById(R.id.tvLable);
        tvLable.setText(labelId);
        tab.getLayoutParams().width = UIHelper.getW()/2;
        tabHost.addTab(tabHost.newTabSpec(tabId).setIndicator(tab), Fragment.class, null);

        return tab;
    }

    @Override
    public void onBusRouteClick(BusModel model) {
        DetailsDialog dlg = new DetailsDialog(MainActivity.this, model.lineId, model.isArrivals);
        dlg.show();
    }

    @Override
    public void onFetchData() {
        Controller.getInstance().getBusses();

        for (Fragment f : appActivtyPagerAdapter.getRegisteredFragments()){
            ((BusTabController)f).setRefreshing();
        }
    }

    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        private Context ctx;
        private List<Fragment> registeredFragments = new ArrayList<>();

        public MyViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 2;
        }

        public List<Fragment> getRegisteredFragments(){
            return registeredFragments;
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("rr", "getItem");
            BusTabController inst = BusTabController.newInstance((position == 0 ? BusTabController.Type.ARRIVALS :
                    BusTabController.Type.DEPARTURES));
            return inst;
        }

//        @Override
//        public Object instantiateItem(ViewGroup collection, int position) {
//            Log.d("TY", "Creating item at pos : " + position);
//
//            View view = LayoutInflater.from(ctx).inflate(R.layout.fragment_list, collection, false);
//            BusTabController bf = BusTabController.newInstance((position == 0 ? BusTabController.Type.ARRIVALS :
//                    BusTabController.Type.DEPARTURES));
//
////            BusTabController bf = new BusTabController((position == 0 ? BusTabController.Type.ARRIVALS :
////                    BusTabController.Type.DEPARTURES), view, MainActivity.this);
//
//            busTabControllers.add(bf);
//
//            collection.addView(view);
//            return view;
//        }

        // Register the fragment when the item is instantiated
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.e("rr", "instantiateItem");
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.add(fragment);
            return fragment;
        }

        // Unregister when the item is inactive
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.e("rr", "destroyItem");
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        // Returns the fragment for the position (if instantiated)
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    private class TabChangeListener implements TabHost.OnTabChangeListener{

        private View selectedView;

        public TabChangeListener(View selectedView){
            this.selectedView = selectedView;
        }

        @Override
        public void onTabChanged(String tabId) {
            if (selectedView != null) {
                selectedView.setSelected(false);
            }

            tabHost.getCurrentTabView().setSelected(true);
            selectedView = tabHost.getCurrentTabView();

            viewPager.setCurrentItem(tabHost.getCurrentTab());
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(dataFetchedReceiver);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RECREATED, true);
    }
}
