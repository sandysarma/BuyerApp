package com.buyer.buyerApp.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.buyer.buyerApp.Activity.OrderHistoryDetails;
import com.buyer.buyerApp.Adapter.Pager;
import com.buyer.buyerApp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class OrderHistoryFragment extends Fragment {
    String strValue = "";
    private TabLayout tabLayout1;
    private ViewPager viewPager1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view =  inflater.inflate(R.layout.fragment_order_history, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE);

       // strValue = this.getArguments().getString("value");

         tabLayout1 = view.findViewById(R.id.tabLayout1);
        tabLayout1.setupWithViewPager(viewPager1);


     viewPager1 = view.findViewById(R.id.viewPager1);
     setupViewPager(viewPager1);

        if (strValue.equals("cancel")) {
            viewPager1.setCurrentItem(2);
        }

        tabLayout1 = view.findViewById(R.id.tabLayout1);
        tabLayout1.addTab(tabLayout1.newTab().setText("History"));
        tabLayout1.addTab(tabLayout1.newTab().setText("Completed"));
        tabLayout1.addTab(tabLayout1.newTab().setText("Cancelled"));
        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager1 = view.findViewById(R.id.viewPager1);
        Pager adapter = new Pager(getFragmentManager(), tabLayout1.getTabCount());
        viewPager1.setAdapter(adapter);

        tabLayout1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager1.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new OrderListFragment(), "History");
        adapter.addFragment(new CompletedFragment(), "Completed");
        adapter.addFragment(new CancelListFragment(), "Cancelled");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitleList.get(position);
        }
    }

}