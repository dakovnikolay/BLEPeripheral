package com.example.dlv4119.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu");
        return true;
    }

    @Override
    protected void onDestroy() {
        // TODO 自動生成されたメソッド・スタブ
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    protected void onPause() {
        // TODO 自動生成されたメソッド・スタブ
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onRestart() {
        // TODO 自動生成されたメソッド・スタブ
        super.onRestart();
        Log.d(TAG,"onRestart");
    }

    @Override
    protected void onResume() {
        // TODO 自動生成されたメソッド・スタブ
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onStart() {
        // TODO 自動生成されたメソッド・スタブ
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onStop() {
        // TODO 自動生成されたメソッド・スタブ
        super.onStop();
        Log.d(TAG, "onStop");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
