package com.anikmohammad.tryoutflickr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlickrJSONData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);

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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryString = sharedPreferences.getString(FLICKR_QUERY, "");

        queryString = queryString.replaceAll(" ", ",");

        String baseLink = "https://api.flickr.com/services/feeds/photos_public.gne";
        GetFlickrJSONData getFlickrJSONData = new GetFlickrJSONData(MainActivity.this, baseLink, "es-us", true);
        getFlickrJSONData.execute(queryString);
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

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK) {
            mAdapter.loadNewData(data);
        } else {
            Log.d(TAG, "onDataAvailable: Error with status -> " + status);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
//        Toast.makeText(MainActivity.this, "Normal tap at position " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, PhotoDetailActivity.class);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
//        Toast.makeText(MainActivity.this, "Long tap at position " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, mAdapter.getPhoto(position));
        startActivity(intent);
    }
}
