package example.kizema.anton.testbusapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import example.kizema.anton.testbusapp.app.BaseActivity;
import example.kizema.anton.testbusapp.app.UIHelper;
import example.kizema.anton.testbusapp.control.Controller;
import example.kizema.anton.testbusapp.model.BusModel;

public class MainActivity extends BaseActivity implements BusFragment.OnPopularOrNearUserClick {

    private static final String TAB_ARRIVAL = "TAB_ARRIVAL";
    private static final String TAB_DEPARTURE = "TAB_DEPARTURE";


    private FragmentTabHost tabHost;
    private ViewPager viewPager;
    private SearchActivityViewPagerAdapter appActivtyPagerAdapter;
    private BroadcastReceiver dataFetchedReceiver;

    private boolean firstStarted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.getBoolean("KK")){
            firstStarted = false;
        }

        init();
        registerReceiver();
    }

    private void registerReceiver(){
        dataFetchedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                for (BusFragment f : appActivtyPagerAdapter.getAll()){
                    f.onUpdated();
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

        tabHost.setup(tabHost.getContext(), getSupportFragmentManager());
        tabHost.getTabWidget().setStripEnabled(false);
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        View tab = createTab(R.string.arrival, TAB_ARRIVAL);
        createTab(R.string.departure, TAB_DEPARTURE);

        tabHost.setOnTabChangedListener(new TabChangeListener(tab));

        appActivtyPagerAdapter = new SearchActivityViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(appActivtyPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setCurrentTab(position);
            }
        });
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
    public void onPopularOrNearUserClick(BusModel model) {

    }

    private class SearchActivityViewPagerAdapter extends FragmentPagerAdapter {

        private Map<BusFragment.Type, BusFragment> fragments;

        public SearchActivityViewPagerAdapter(FragmentManager fm) {
            super(fm);

            fragments = new HashMap<>();
        }

        public Collection<BusFragment> getAll(){
            return fragments.values();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (fragments.get(BusFragment.Type.ARRIVALS) == null) {
                        fragments.put(BusFragment.Type.ARRIVALS,
                                BusFragment.newInstance(BusFragment.Type.ARRIVALS, firstStarted));
                    }
                    return fragments.get(BusFragment.Type.ARRIVALS);

                case 1:
                    if (fragments.get(BusFragment.Type.DEPARTURES) == null) {
                        fragments.put(BusFragment.Type.DEPARTURES,
                                BusFragment.newInstance(BusFragment.Type.DEPARTURES, firstStarted));
                    }
                    return fragments.get(BusFragment.Type.DEPARTURES);

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
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
        outState.putBoolean("KK", true);
    }
}
