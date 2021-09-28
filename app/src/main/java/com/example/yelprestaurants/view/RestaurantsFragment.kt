package com.example.yelprestaurants.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yelprestaurants.R
import com.example.yelprestaurants.adapters.RestaurantsAdapter
import com.example.yelprestaurants.databinding.FragmentFirstBinding
import com.example.yelprestaurants.model.RestaurantsBusinessModel
import com.example.yelprestaurants.model.RestaurantsListingRequestModel
import com.example.yelprestaurants.model.RestaurantsListingResponseModel
import com.example.yelprestaurants.presenter.RestaurantsListContract
import com.example.yelprestaurants.presenter.RestaurantsPresenter
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import kotlinx.android.synthetic.main.fragment_first.*
import java.math.RoundingMode

class RestaurantsFragment : BaseFragment<RestaurantsListContract.Presenter, RestaurantsListContract.View>(), RestaurantsListContract.View {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    private val presenter by lazy { initiatePresenter() }

    private lateinit var restaurantsRequestModel : RestaurantsListingRequestModel

    private val restaurantsAdapter by lazy { RestaurantsAdapter() }

    private var mIsLoading : Boolean = false

    private var totalCaptureResult : Int = 0

    private var restaurantsList = arrayListOf<RestaurantsBusinessModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restaurantsRequestModel = RestaurantsListingRequestModel()
        restaurantsRequestModel.term = "restaurants"
        restaurantsRequestModel.limit = 15
        restaurantsRequestModel.location = "NYC"
        restaurantsRequestModel.sort_by = "distance"
        restaurantsRequestModel.radius = 100
        restaurantsRequestModel.offset = 0
        presenter.loadData(restaurantsRequestModel)
        setupRangeBar()

        swipeRefresh.setOnRefreshListener {
            if (!mIsLoading){
                presenter.load(true)
            }
        }

        restaurantsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = restaurantsRV.layoutManager?.childCount ?: 0
                val totalItemCount = restaurantsRV.layoutManager?.itemCount ?: 0
                val firstVisibleItemPosition = (restaurantsRV.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (!mIsLoading) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                            mIsLoading = true
                            presenter.loadDataPaginate(restaurantsAdapter.itemCount)
//
                    }else{
                        mIsLoading = false
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initiatePresenter(): RestaurantsListContract.Presenter {
        return RestaurantsPresenter(this)
    }

    override fun getFragmentView(): Int {
        return R.layout.fragment_first
    }

    override fun showData(response : RestaurantsListingResponseModel, isPaginate: Boolean) {
        errorLayout.visibility = View.GONE
        if (swipeRefresh.isRefreshing){
            swipeRefresh.isRefreshing = false
        }
        mIsLoading = false
        hideLoader(true)
        if (!isPaginate) {
            response.businesses?.let { list ->
                if (list.isNotEmpty()){
                restaurantsRV.layoutManager =
                    LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
                restaurantsRV.adapter = restaurantsAdapter
                restaurantsAdapter.dataList = list as ArrayList<RestaurantsBusinessModel>
                    restaurantsRV.visibility = View.VISIBLE
                }else{
                    restaurantsRV.visibility = View.GONE
                    showError()
                }
            }?: kotlin.runCatching {
                showError()
            }
        } else {
            response.businesses?.let { list ->
                if (list.isNotEmpty()) {
                    restaurantsAdapter.addMore(list)
                }
            }
        }

        totalCaptureResult = restaurantsAdapter.itemCount
    }

    override fun showLoader() {
        hideLoader(false)
    }

    override fun hideLoader() {
        hideLoader(true)
    }

    override fun showError() {
        hideLoader(true)
        errorLayout.visibility = View.VISIBLE
    }

    private fun setupRangeBar(){
        kmRangeBar.setRange(    100f, 5000f)
        kmRangeBar.setOnRangeChangedListener(object :  OnRangeChangedListener{
            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                view!!.setOnTouchListener(OnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_UP -> {
                            if (leftValue < 1000){
                                rangeSelectedTV.text = "${leftValue.toInt()} M"
                            }else{
                                rangeSelectedTV.text = "${(leftValue/1000).toFloat().toBigDecimal().setScale(1, RoundingMode.UP).toDouble()} KM"
                            }
                            presenter.loadDataKms(leftValue.toInt())
                            return@OnTouchListener true
                        }
                    }
                    false
                })
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

        })
    }


    private fun hideLoader(hide: Boolean) = if(hide) {
            loading?.cancelAnimation()
            loaderLayout?.visibility = View.GONE
    } else {
            loading?.playAnimation()
            loaderLayout?.visibility = View.VISIBLE
    }
}