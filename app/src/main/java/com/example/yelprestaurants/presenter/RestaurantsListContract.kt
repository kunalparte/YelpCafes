package com.example.yelprestaurants.presenter

import com.example.yelprestaurants.model.RestaurantsListingRequestModel
import com.example.yelprestaurants.model.RestaurantsListingResponseModel
import com.example.yelprestaurants.view.BaseView

interface RestaurantsListContract {

    interface View : BaseView {
        fun showData(response : RestaurantsListingResponseModel, isPaginate: Boolean = false)
        fun showLoader()
        fun hideLoader()
        fun showError()
    }

    interface Presenter : BasePresenter<View>{
        fun loadData(model : RestaurantsListingRequestModel, isPaginate : Boolean = false)
        fun loadDataPaginate(offset: Int)
        fun loadDataKms(km : Int)
    }
}