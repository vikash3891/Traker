package com.home.traker.holder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.traker.R
import com.home.traker.model.ListAttendanceModel
import com.home.traker.view.DriverProfileActivity
import kotlinx.android.synthetic.main.item_attendance.view.*

class ListItemAttendanceAdapter(
    private val context: Context,
    private val list: List<ListAttendanceModel>
) :
    RecyclerView.Adapter<ListItemAttendanceViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListItemAttendanceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListItemAttendanceViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ListItemAttendanceViewHolder, position: Int) {
        val movie: ListAttendanceModel = list[position]
        holder.bind(movie)

        holder.itemView.layoutList.setOnClickListener {
            //context.startActivity(Intent(context, DriverProfileActivity::class.java))
        }
        if (position % 2 == 0) {
            holder.itemView.layoutList.setBackgroundColor(context.resources.getColor(R.color.list2))
        } else {
            holder.itemView.layoutList.setBackgroundColor(context.resources.getColor(R.color.list1))
        }
    }

    override fun getItemCount(): Int = list.size

}