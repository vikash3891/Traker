package com.home.traker.holder

import android.content.Context
import android.content.Intent
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.traker.model.ListItemModel
import com.home.traker.utils.Constants
import com.home.traker.view.DriverProfileActivity
import kotlinx.android.synthetic.main.item_driver_details.view.*

class ListItemAdapter(private val context: Context, private val list: List<ListItemModel>) :
    RecyclerView.Adapter<ListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListItemViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val movie: ListItemModel = list[position]
        holder.bind(movie)

        holder.itemView.listItemLayout.setOnClickListener {
            //context.startActivity(Intent(context, DriverProfileActivity::class.java))

            var intent1 = Intent(context, DriverProfileActivity::class.java)
            intent1.putExtra(Constants.DRIVER_ID, list[position].id)
            context.startActivity(intent1)
        }
    }

    override fun getItemCount(): Int = list.size

}