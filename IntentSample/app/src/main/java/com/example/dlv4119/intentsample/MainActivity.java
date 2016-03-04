package com.example.dlv4119.intentsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        Button btn = (Button)findViewById(R.id.main_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, com.example.dlv4119.intentsample.SubActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onDestroy() {
        // TODO 自動生成されたメソッド・スタブ
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        // TODO 自動生成されたメソッド・スタブ
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        // TODO 自動生成されたメソッド・スタブ
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        // TODO 自動生成されたメソッド・スタブ
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStart() {
        // TODO 自動生成されたメソッド・スタブ
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        // TODO 自動生成されたメソッド・スタブ
        super.onStop();
        Log.d(TAG, "onStop");
    }
}
