package jp.co.deliv.android.imasugu.sugucafe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by dlv4119 on 2016/02/24.
 */
public class ShopFragmentPagerAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"TEST 1", "TEST 2"};

    public ShopFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ShopMapSearchFragment();
            case 1:
                return new ShopListSearchFragment();
        }

        return null;
    }

    /** ページ数を返す */
    @Override
    public int getCount() {
        return TITLES.length;
    }

    /** ページのタイトルを返す */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "地図";
            case 1:
                return "リスト";
        }
        return null;
    }

}
