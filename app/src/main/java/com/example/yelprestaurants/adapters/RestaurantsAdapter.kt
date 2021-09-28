package com.example.yelprestaurants.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yelprestaurants.R
import com.example.yelprestaurants.model.RestaurantsBusinessModel
import kotlinx.android.synthetic.main.restaurants_list_item_layout.view.*
import java.math.RoundingMode

class RestaurantsAdapter : RecyclerView.Adapter< RestaurantsAdapter.VHolder>() {


    private lateinit var context : Context

    class VHolder(view : View) : RecyclerView.ViewHolder(view)

    var dataList = arrayListOf<RestaurantsBusinessModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        this.context = parent.context
        return VHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurants_list_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        dataList[position]?.let { data ->

            data.imageUrl?.let { imageUrl ->
                holder.itemView.restaurantItemIV.setImageURI(Uri.parse(imageUrl))
                Glide.with(context).load(imageUrl).into(holder.itemView.restaurantItemIV)
            }

            data.name?.let { name ->
                holder.itemView.restaurantsItemTitleTV.text = name
                holder.itemView.restaurantsItemTitleTV.visibility = View.VISIBLE
            }?: kotlin.run {
                holder.itemView.restaurantsItemTitleTV.visibility = View.GONE
            }

            var location : String = ""
            data.distance?.let { dist ->
                if (dist < 1000)
                    location = "${(dist.toInt())}m,"
                else
                    location = "${(dist/1000).toFloat().toBigDecimal().setScale(1, RoundingMode.UP).toDouble()}kms, "
            }

            data.location?.address1?.let { addr ->
                if (addr.isNotEmpty()) {
                    location = "$location ${addr[0]}"
                }
            }

            if (location.isNotEmpty()){
                holder.itemView.restaurantsItemLocationTV.text = location
                holder.itemView.restaurantsItemLocationTV.visibility = View.VISIBLE
            }else{
                holder.itemView.restaurantsItemLocationTV.visibility = View.GONE
            }


            data.is_closed?.let { is_closed ->
                if (is_closed){
                    holder.itemView.restaurantsItemStatusTV.text = "Currently CLOSED"
                }else{
                    holder.itemView.restaurantsItemStatusTV.text = "Currently OPEN"
                }
                holder.itemView.restaurantsItemStatusTV.visibility = View.VISIBLE
            }?: kotlin.run {
                holder.itemView.restaurantsItemStatusTV.visibility = View.GONE
            }

            data.rating?.let { rating ->
                holder.itemView.restaurantsItemRatingTV.text = "$rating"
                holder.itemView.restaurantsItemRatingTV.visibility = View.VISIBLE
            }?: kotlin.run {
                holder.itemView.restaurantsItemRatingTV.visibility = View.GONE
            }
        }
    }

    fun addMore(data : List<RestaurantsBusinessModel>){
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}