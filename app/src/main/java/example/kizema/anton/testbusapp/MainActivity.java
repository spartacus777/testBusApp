package example.kizema.anton.testbusapp;

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

import java.util.HashMap;
import java.util.Map;

import example.kizema.anton.testbusapp.app.BaseActivity;
import example.kizema.anton.testbusapp.app.UIHelper;

public class MainActivity extends BaseActivity implements BasePopularOrNearUserFragment.OnPopularOrNearUserClick {

    private static final String TAB_POPULAR = "TAB_POPULAR";
    private static final String TAB_NEAR = "TAB_NEAR";


    private FragmentTabHost tabHost;
    private ViewPager viewPager;
    private SearchActivityViewPagerAdapter appActivtyPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){

        tabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        viewPager = (ViewPager) findViewById(R.id.viewpagerFragmentContainer);

        tabHost.setup(tabHost.getContext(), getSupportFragmentManager());
        tabHost.getTabWidget().setStripEnabled(false);
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        View tab = createTab(R.string.arrival, TAB_POPULAR);
        createTab(R.string.departure, TAB_NEAR);

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
    public void onPopularOrNearUserClick(String str) {

    }

    private class SearchActivityViewPagerAdapter extends FragmentPagerAdapter {

        private Map<BasePopularOrNearUserFragment.SearchType, BasePopularOrNearUserFragment> fragments;

        public SearchActivityViewPagerAdapter(FragmentManager fm) {
            super(fm);

            fragments = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (fragments.get(BasePopularOrNearUserFragment.SearchType.POPULAR) == null) {
                        fragments.put(BasePopularOrNearUserFragment.SearchType.POPULAR,
                                BasePopularOrNearUserFragment.newInstance());
                    }
                    return fragments.get(BasePopularOrNearUserFragment.SearchType.POPULAR);

                case 1:
                    if (fragments.get(BasePopularOrNearUserFragment.SearchType.NEARBY) == null) {
                        fragments.put(BasePopularOrNearUserFragment.SearchType.NEARBY,
                                BasePopularOrNearUserFragment.newInstance());
                    }
                    return fragments.get(BasePopularOrNearUserFragment.SearchType.NEARBY);

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
}
