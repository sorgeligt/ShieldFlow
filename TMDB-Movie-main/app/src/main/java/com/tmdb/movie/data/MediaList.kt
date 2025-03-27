package com.tmdb.movie.data

import com.google.gson.annotations.SerializedName

data class MediaList(
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("favorite_count")
    val favoriteCount: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("iso_639_1")
    val iso6391: String? = null,
    @SerializedName("item_count")
    val itemCount: Int = 0,
    @SerializedName("list_type")
    val listType: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null
)