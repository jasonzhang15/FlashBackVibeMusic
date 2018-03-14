package com.android.flashbackmusic;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import static android.content.Context.DOWNLOAD_SERVICE;

public class SimpleDownloader {

    private Application app;
    private DownloadManager downloadManager;

    public SimpleDownloader(Application app) {
        this.app = app;
    }


    public long downloadSong(String url) {

        Uri uri = Uri.parse(url);

        // Create request for android download manager
        downloadManager = (DownloadManager) app.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle((url.split("id="))[1]);

        //Setting description of request
        request.setDescription("Android Data download using DownloadManager.");

        //Set the local destination for the downloaded file to a path within the application's external files directory
        Log.v("LOOK", "set with: " + (url.split("id="))[1]);
        request.setDestinationInExternalFilesDir(app, Environment.DIRECTORY_DOWNLOADS, (url.split("id="))[1] + ".mp3");

        //Enqueue download and save into referenceId

        long downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    public void Check_Music_Status(long downloadId) {

        DownloadManager.Query ImageDownloadQuery = new DownloadManager.Query();
        //set the query filter to our previously Enqueued download
        ImageDownloadQuery.setFilterById(downloadId);

        //Query the download manager about downloads that have been requested.
        Cursor cursor = downloadManager.query(ImageDownloadQuery);
        if(cursor.moveToFirst()){
            DownloadStatus(cursor, downloadId);
        }

    }

    private void DownloadStatus(Cursor cursor, long downloadId) {

        //column for download  status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename

        String statusText = "";
        String reasonText = "";

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "STATUS_SUCCESSFUL";
                break;
        }


        Log.v("LOOK", statusText + ": " + reason + " " + downloadId);

        Toast toast = Toast.makeText(app,
                "Music Download Status:" + "\n" + statusText + "\n" +
                        reasonText,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }

    public BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


            Toast toast = Toast.makeText(app,
                    "Music Download Complete", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }
    };
}