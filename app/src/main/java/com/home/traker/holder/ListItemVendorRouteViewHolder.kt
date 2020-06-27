package com.home.traker.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.traker.R
import com.home.traker.model.DriverVendorListModel
import com.home.traker.model.ListAttendanceModel

class ListItemVendorRouteViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_driver_route_details, parent, false)) {
    private var vendorName: TextView? = null
    private var driverPhoneNumber: TextView? = null
    private var vendorDetails: TextView? = null
    private var driveTime: TextView? = null
    private var vendorLocation: TextView? = null


    init {
        vendorName = itemView.findViewById(R.id.vendorName)
        driverPhoneNumber = itemView.findViewById(R.id.driverPhoneNumber)
        vendorDetails = itemView.findViewById(R.id.vendorDetails)
        driveTime = itemView.findViewById(R.id.driveTime)
        vendorLocation = itemView.findViewById(R.id.vendorLocation)
    }

    fun bind(movie: DriverVendorListModel) {
        vendorName?.text = movie.vendor_name
        driverPhoneNumber?.text = movie.phone
        vendorDetails?.text = movie.details
        driveTime?.text = movie.current_time
    }

}