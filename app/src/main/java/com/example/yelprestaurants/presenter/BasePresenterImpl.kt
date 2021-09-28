package com.example.yelprestaurants.presenter

import com.example.yelprestaurants.view.BaseView

abstract class BasePresenterImpl<in V> : BasePresenter<V> {

    private var view: BaseView? = null

    override fun attach(view: V) {
        this.view = view as BaseView
    }
}