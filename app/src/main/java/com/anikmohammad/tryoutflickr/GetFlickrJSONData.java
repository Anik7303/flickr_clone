package com.anikmohammad.tryoutflickr;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJSONData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetFlickrJSONData";

    private String mBaseURL;
    private String mLanguage;
    private boolean mMatchAll;
    private List<Photo> mPhotoList;
    private DownloadStatus mDownloadStatus;
    private boolean runningInSameThread;

    private final OnDataAvailable mCallback;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJSONData(OnDataAvailable callback, String baseURL, String language, boolean matchAll) {
        this.mCallback = callback;
        this.mDownloadStatus = DownloadStatus.IDLE;
        mBaseURL = baseURL;
        mLanguage = language;
        mMatchAll = matchAll;
        runningInSameThread = false;
    }

    public void executeOnSameThread(String link) {
        Log.d(TAG, "executeOnSameThread: starts");
        runningInSameThread = true;
        GetRawData getRawData = new GetRawData(GetFlickrJSONData.this);
        getRawData.execute(link);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");
        if(mCallback != null) {
            mCallback.onDataAvailable(photos, mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts with " + strings[0]);
        GetRawData getRawData = new GetRawData(GetFlickrJSONData.this);
        getRawData.runInSameThread(createURL(strings[0]));
        Log.d(TAG, "doInBackground: ends");
        return mPhotoList;
    }

    private String createURL(String criteria) {
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags", criteria)
                .appendQueryParameter("tagmode", mMatchAll ? "all" : "any")
                .appendQueryParameter("lang", mLanguage)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts with status -> " + status);
        if(status == DownloadStatus.OK) {
            mDownloadStatus = status;
            try {
                mPhotoList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(data);
                JSONArray itemsArray = jsonObject.getJSONArray("items");
                int limit = itemsArray.length();
                for (int index = 0; index < limit; index++) {
                    JSONObject itemObject = itemsArray.getJSONObject(index);
                    String title = itemObject.getString("title");
                    String author = itemObject.getString("author");
                    String author_id = itemObject.getString("author_id");
                    String tags = itemObject.getString("tags");
                    String imageURL = itemObject.getJSONObject("media").getString("m");
                    String link = imageURL.replaceFirst("_m.", "_b.");
                    Photo tempPhoto = new Photo(title, author, author_id, tags, link, imageURL);
                    Log.d(TAG, "onDownloadComplete: " + index + " -> " + tempPhoto);
                    mPhotoList.add(tempPhoto);
                }
            } catch (JSONException e) {
                Log.d(TAG, "onDownloadComplete: JSONException -> " + e.getMessage());
            }
        }

        if(runningInSameThread && mCallback != null) {
            mCallback.onDataAvailable(mPhotoList, mDownloadStatus);
        }
    }
}
