package com.anikmohammad.tryoutflickr;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

enum DownloadStatus {
    IDLE,
    PROCESSING,
    NOT_INITIALIZED,
    WRONG_SETTINGS,
    PERMISSION_MISSING,
    FAILED_OR_EMPTY,
    OK
}

class GetRawData extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetRawData";
    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallback;

    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public GetRawData(OnDownloadComplete callback) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mCallback = callback;
    }

    public void runInSameThread(String downloadLink) {
        Log.d(TAG, "runInSameThread: starts");
        if(mCallback != null && downloadLink != null) {
            mCallback.onDownloadComplete(doInBackground(downloadLink), mDownloadStatus);
        }
        Log.d(TAG, "runInSameThread: ends");
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: starts with status " + mDownloadStatus);
        if(mCallback != null) {
            mCallback.onDownloadComplete(s, mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected String doInBackground(String... strings) {
        mDownloadStatus = DownloadStatus.PROCESSING;
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            Log.d(TAG, "doInBackground: responseCode -> " + connection.getResponseCode());
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder result = new StringBuilder();
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();
        } catch(ClassCastException e) {
            mDownloadStatus = DownloadStatus.WRONG_SETTINGS;
            Log.d(TAG, "doInBackground: ClassCastException -> " + e.getMessage());
            return null;
        } catch(MalformedURLException e) {
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            Log.d(TAG, "doInBackground: MalformedURLException -> " + e.getMessage());
            return null;
        } catch(IOException e) {
            mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            Log.d(TAG, "doInBackground: IOException -> " + e.getMessage());
            return null;
        } catch(SecurityException e) {
            mDownloadStatus = DownloadStatus.PERMISSION_MISSING;
            Log.d(TAG, "doInBackground: SecurityException -> " + e.getMessage());
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch(IOException e) {
                    Log.d(TAG, "doInBackground: Error closing reader -> " + e.getMessage());
                }
            }
        }
    }
}
