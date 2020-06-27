package com.home.traker.holder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.home.traker.R
import com.home.traker.model.DriverVendorListModel
import com.home.traker.utils.Constants
import com.home.traker.view.DriverProfileActivity
import com.home.traker.view.DriverRouteMapActivity
import kotlinx.android.synthetic.main.item_attendance.view.*
import kotlinx.android.synthetic.main.item_driver_route_details.view.*

class ListItemVendorRouteAdapter(
    private val context: Context,
    private val list: List<DriverVendorListModel>
) :
    RecyclerView.Adapter<ListItemVendorRouteViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListItemVendorRouteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListItemVendorRouteViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ListItemVendorRouteViewHolder, position: Int) {
        val movie: DriverVendorListModel = list[position]
        holder.bind(movie)

        holder.itemView.vendorLocation.setOnClickListener {

            var intent1 = Intent(context, DriverRouteMapActivity::class.java)
            intent1.putExtra(Constants.LAT, list[position].lat)
            intent1.putExtra(Constants.LONG, list[position].lang)
            context.startActivity(intent1)
        }
        if (position % 2 == 0) {
            holder.itemView.listItemLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.list2
                )
            )
        } else {
            holder.itemView.listItemLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.list1
                )
            )
        }
    }

    override fun getItemCount(): Int = list.size

}