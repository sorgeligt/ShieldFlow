package com.tmdb.movie.data

import com.google.gson.annotations.SerializedName

data class BaseListData<T>(
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int = 0,
    @SerializedName("results")
    val results: List<T>? = null
)