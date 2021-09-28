package com.example.yelprestaurants.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yelprestaurants.R
import com.example.yelprestaurants.presenter.BasePresenter
import kotlinx.android.synthetic.main.fragment_base.*

abstract class BaseFragment <out P : BasePresenter<V>, in V : BaseView>: Fragment(), BaseView{

    abstract fun initiatePresenter(): P

    abstract fun getFragmentView() : Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_base, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}