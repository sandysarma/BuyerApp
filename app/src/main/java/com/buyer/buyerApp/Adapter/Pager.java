package com.buyer.buyerApp.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.buyer.buyerApp.Fragment.CancelListFragment;
import com.buyer.buyerApp.Fragment.CompletedFragment;
import com.buyer.buyerApp.Fragment.OrderListFragment;

public class Pager extends FragmentStatePagerAdapter {

    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                OrderListFragment tab1 = new OrderListFragment();
                return tab1;
            case 1:
                CompletedFragment tab2 = new CompletedFragment();
                return tab2;
            case 2:
              CancelListFragment tab3 = new CancelListFragment();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
