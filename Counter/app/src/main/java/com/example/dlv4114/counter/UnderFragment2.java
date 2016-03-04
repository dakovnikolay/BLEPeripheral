package com.example.dlv4114.counter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dlv4114 on 2016/01/19.
 */
public class UnderFragment2 extends Fragment {
    View mContentView2 = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView2 = inflater.inflate(R.layout.under_fragment2, null);
        return mContentView2;
    }
}