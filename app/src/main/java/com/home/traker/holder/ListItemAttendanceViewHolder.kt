package com.home.traker.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.home.traker.R
import com.home.traker.model.ListAttendanceModel

class ListItemAttendanceViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_attendance, parent, false)) {
    private var driveDateValue: TextView? = null
    private var driveDateInValue: TextView? = null
    private var driveDateOutValue: TextView? = null


    init {
        driveDateValue = itemView.findViewById(R.id.driveDateValue)
        driveDateInValue = itemView.findViewById(R.id.driveDateInValue)
        driveDateOutValue = itemView.findViewById(R.id.driveDateOutValue)
    }

    fun bind(movie: ListAttendanceModel) {
        driveDateValue?.text = movie.currentdate
        driveDateInValue?.text = movie.INTIME
        driveDateOutValue?.text = movie.OUTTIME
    }

}