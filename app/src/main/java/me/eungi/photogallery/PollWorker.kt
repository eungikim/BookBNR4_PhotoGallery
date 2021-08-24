package me.eungi.photogallery

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG = "PollWorker"

class PollWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams){

    override fun doWork(): Result {
        Log.i(TAG, "Work request triggered")
        val query = QueryPreferences.get(context).getStoredQuery()
        val lastResultId = QueryPreferences.get(context).getLastResultId()
        val items: List<GalleryItem> = if (query.isEmpty()) {
            FlickrFetchr().fetchPhotosRequest().execute().body()?.photos?.galleryItems
        } else {
            FlickrFetchr().searchPhotosRequest(query).execute().body()?.photos?.galleryItems
        } ?: emptyList()

        if (items.isEmpty()) return Result.success()

        val resultId = items.first().id
        if (resultId == lastResultId) {
            Log.i(TAG, "Got an old result: $resultId")
        } else {
            Log.i(TAG, "Got a new result: $resultId")
            QueryPreferences.get(context).setLastResultId(resultId)
        }

        return Result.success()
    }

}