package com.home.traker.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.traker.R
import com.home.traker.model.ListItemModel

class ListItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_driver_details, parent, false)) {
    private var driverName: TextView? = null
    private var driverPhoneNumber: TextView? = null
    private var address: TextView? = null
    private var complaint: TextView? = null
    private var appointmentTime: TextView? = null
    private var appointmentStatus: TextView? = null


    init {
        driverName = itemView.findViewById(R.id.driverName)
        driverPhoneNumber = itemView.findViewById(R.id.driverPhoneNumber)
        address = itemView.findViewById(R.id.address)
    }

    fun bind(movie: ListItemModel) {
        driverName?.text = movie.drive_name
        driverPhoneNumber?.text = movie.phone
        address?.text = movie.details
    }

}