package com.example.dlv4119.intentsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by dlv4119 on 2016/01/14.
 */

public class SubActivity extends AppCompatActivity {

    private final String TAG = "sub_test";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Log.d(TAG, "sub_onCreate");

        Button sub_btn = (Button)findViewById(R.id.sub_button);
        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubActivity.this, com.example.dlv4119.intentsample.MainActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onDestroy() {
        // TODO 自動生成されたメソッド・スタブ
        super.onDestroy();
        Log.d(TAG, "sub_onDestroy");
    }

    @Override
    protected void onPause() {
        // TODO 自動生成されたメソッド・スタブ
        super.onPause();
        Log.d(TAG, "sub_onPause");
    }

    @Override
    protected void onRestart() {
        // TODO 自動生成されたメソッド・スタブ
        super.onRestart();
        Log.d(TAG, "sub_onRestart");
    }

    @Override
    protected void onResume() {
        // TODO 自動生成されたメソッド・スタブ
        super.onResume();
        Log.d(TAG, "sub_onResume");
    }

    @Override
    protected void onStart() {
        // TODO 自動生成されたメソッド・スタブ
        super.onStart();
        Log.d(TAG, "sub_onStart");
    }

    @Override
    protected void onStop() {
        // TODO 自動生成されたメソッド・スタブ
        super.onStop();
        Log.d(TAG, "sub_onStop");
    }
}
