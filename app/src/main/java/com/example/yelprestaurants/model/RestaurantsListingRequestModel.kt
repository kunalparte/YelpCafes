package com.example.yelprestaurants.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantsListingRequestModel(

    var term : String ?= null,

    var location : String ?= null,

    var radius : Int ?= null,

    var sort_by : String ?= null,

    var limit : Int ?= null,

    var offset : Int ?= null

): Parcelable


@Parcelize
data class RestaurantsListingResponseModel(

    @field:SerializedName("total")
    var total : Int ?= null,

    @field:SerializedName("businesses")
    var businesses : List<RestaurantsBusinessModel> ?= null

): Parcelable



@Parcelize
 data class RestaurantsBusinessModel(

    @field:SerializedName("rating")
    var rating : Float ?= null,

    @field:SerializedName("id")
    var id : String ?= null,

    @field:SerializedName("is_closed")
    var is_closed : Boolean ?= null,

    @field:SerializedName("name")
    var name : String ?= null,

    @field:SerializedName("image_url")
    var imageUrl : String ?= null,

    @field:SerializedName("distance")
    var distance : Double ?= null,

    @field:SerializedName("location")
    var location: LocationObj ?= null

 ): Parcelable


@Parcelize
data class LocationObj(

    @field:SerializedName("display_address")
    var address1 : List<String> ?= null
) : Parcelable