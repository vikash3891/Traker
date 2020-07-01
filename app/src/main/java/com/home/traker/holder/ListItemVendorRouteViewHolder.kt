package com.home.traker.holder

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.traker.R
import com.home.traker.model.DriverVendorListModel
import com.home.traker.model.ListAttendanceModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val parsedDate =
                    LocalDateTime.parse(movie.current_time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("HH:mm AA"))
                driveTime?.text = formattedDate
            } else {
                val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val formatterSimple = SimpleDateFormat("HH:mm AA")
                val formattedDate = formatterSimple.format(parser.parse("movie.current_time"))
                driveTime?.text = formattedDate
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}