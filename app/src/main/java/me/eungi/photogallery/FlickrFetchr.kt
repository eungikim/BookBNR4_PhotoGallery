package me.eungi.photogallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.eungi.photogallery.api.FlickrApi
import me.eungi.photogallery.api.FlickrResponse
import me.eungi.photogallery.api.PhotoInterceptor
import me.eungi.photogallery.api.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "FlickrFetchr"

class FlickrFetchr {

    private val flickrApi: FlickrApi

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(flickrApi.fetchPhotos())
    }

    fun searchPhotos(query: String): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(flickrApi.searchPhotos(query))
    }

    private fun fetchPhotoMetadata(flickrRequest: Call<FlickrResponse>): LiveData<List<GalleryItem>> {
        val responseLiveDate: MutableLiveData<List<GalleryItem>> = MutableLiveData()

        flickrRequest.enqueue(object : Callback<FlickrResponse> {
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }
            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
                Log.d(TAG, "Response received")
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveDate.value = galleryItems
            }
        })

        return responseLiveDate
    }

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from response=$response")
        return bitmap
    }
}