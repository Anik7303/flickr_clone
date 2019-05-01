package com.anikmohammad.tryoutflickr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetFlickrJSONData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, recyclerView, MainActivity.this));

        // or
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, recyclerView,
//                new RecyclerItemClickListener.OnRecyclerClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Log.d(TAG, "onItemClick: starts");
//                        Toast.makeText(MainActivity.this, "Single Tap at Position " + position, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//                        Log.d(TAG, "onItemLongClick: starts");
//                        Toast.makeText(MainActivity.this, "Long Tap at Position " + position, Toast.LENGTH_SHORT).show();
//                    }
//                }));

        mAdapter = new FlickrRecyclerViewAdapter(this, new ArrayList<Photo>());

        recyclerView.setAdapter(mAdapter);


        Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        String baseLink = "https://api.flickr.com/services/feeds/photos_public.gne";
        GetFlickrJSONData getFlickrJSONData = new GetFlickrJSONData(MainActivity.this, baseLink, "es-us", true);
        getFlickrJSONData.execute("android");
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if(status == DownloadStatus.OK) {
            mAdapter.loadNewData(data);
        } else {
            Log.d(TAG, "onDataAvailable: Error with status -> " + status);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Toast.makeText(MainActivity.this, "Normal tap at position " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
        Toast.makeText(MainActivity.this, "Long tap at position " + position, Toast.LENGTH_SHORT).show();
    }
}
