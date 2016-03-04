package com.example.dlv4114.counter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by dlv4114 on 2016/01/19.
 */
public class UnderContentFragment extends Fragment {
        View mContentView = null;
            private Button bt_1_2;
            private Button bt_1_3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.under_content, container, false);

        bt_1_2 = (Button)mContentView.findViewById(R.id.fragment1_for2);
        bt_1_3 = (Button)mContentView.findViewById(R.id.fragment1_for3);

        bt_1_2.setOnClickListener(new View.OnClickListener() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment2 = new UnderFragment2();
                @Override
               public void onClick(View v) {
                    transaction.replace(R.id.under_content, fragment2);
                    transaction.addToBackStack(null);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.commit();
                }
            });

        bt_1_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        return mContentView;
    }
}
