package me.eungi.photogallery.api

import com.google.gson.annotations.SerializedName
import me.eungi.photogallery.GalleryItem

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems: List<GalleryItem>
}