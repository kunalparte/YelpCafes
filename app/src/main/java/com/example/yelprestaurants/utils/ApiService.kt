package com.example.yelprestaurants.utils

import com.example.yelprestaurants.model.RestaurantsListingResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v3/businesses/search")
    fun getRestaurants(@Query("term") term : String?,
                       @Query("location") location : String?,
                       @Query("radius") radius : Int?,
                       @Query("sort_by") sortBy : String?,
                       @Query("limit") limit : Int?,
                       @Query("offset") offset : Int?) : Call<RestaurantsListingResponseModel>

}