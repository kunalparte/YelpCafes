package com.example.yelprestaurants.presenter

interface BasePresenter<in V>{

    fun attach(view: V)
    fun detach()
    fun load(reload: Boolean = false)

}