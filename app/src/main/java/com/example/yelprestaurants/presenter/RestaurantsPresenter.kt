package com.example.yelprestaurants.presenter

import android.util.Log
import com.example.yelprestaurants.model.RestaurantsListingRequestModel
import com.example.yelprestaurants.model.RestaurantsListingResponseModel
import com.example.yelprestaurants.utils.RetrofitApiCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantsPresenter(var view : RestaurantsListContract.View)
    : BasePresenter<RestaurantsListContract.View>, RestaurantsListContract.Presenter{

    private lateinit var restaurantsRequest : RestaurantsListingRequestModel

    override fun loadData(model: RestaurantsListingRequestModel, isPaginate : Boolean) {
        view.showLoader()
        this.restaurantsRequest = model
        var restaurantsCall = RetrofitApiCall.apiService.getRestaurants(model.term,
            model.location, model.radius, model.sort_by, model.limit, model.offset)
        restaurantsCall.enqueue(object : Callback<RestaurantsListingResponseModel>{
            override fun onResponse(
                call: Call<RestaurantsListingResponseModel>,
                response: Response<RestaurantsListingResponseModel>
            ) {
                if (response.body() != null) {
                    view.showData(response = response.body()!!, isPaginate)
                }
                Log.d("TAG", "onResponse: $response")
            }

            override fun onFailure(call: Call<RestaurantsListingResponseModel>, t: Throwable) {
                view.hideLoader()
                Log.d("TAG", "onResponse: $t")
            }

        })
    }

    override fun loadDataPaginate(offset: Int) {
        if (!this::restaurantsRequest.isInitialized){
            restaurantsRequest = RestaurantsListingRequestModel()
        }
        restaurantsRequest.offset = offset
        loadData(restaurantsRequest, true)
    }

    override fun loadDataKms(km: Int) {
        if (!this::restaurantsRequest.isInitialized){
            restaurantsRequest = RestaurantsListingRequestModel()
        }
        restaurantsRequest.radius = km
        loadData(restaurantsRequest)
    }

    override fun attach(view: RestaurantsListContract.View) {

    }

    override fun detach() {

    }

    override fun load(reload: Boolean) {
        if (reload){
            loadData(getInitialRequestObject())
        }
    }

    fun getInitialRequestObject(): RestaurantsListingRequestModel {
        if (restaurantsRequest == null){
            restaurantsRequest = RestaurantsListingRequestModel()
        }
        restaurantsRequest.term = "restaurants"
        restaurantsRequest.limit = 15
        restaurantsRequest.location = "NYC"
        restaurantsRequest.sort_by = "distance"
        restaurantsRequest.offset = 0
        return restaurantsRequest
    }

}