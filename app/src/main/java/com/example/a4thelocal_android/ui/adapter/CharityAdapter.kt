package com.example.a4thelocal_android.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.api.data.CharityItem
import com.example.a4thelocal_android.ui.auth.BillingDetailsActivity
import com.example.a4thelocal_android.ui.auth.SubscriptionState


class CharityAdapter(
    val context: Context,
    private val items: ArrayList<CharityItem>
) : RecyclerView.Adapter<CharityAdapter
.CustomViewHolder>() {

    var deletedItem = MutableLiveData<CharityItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.item_charity, parent, false)
        return CustomViewHolder(cellForRow)
    }


    override fun getItemCount(): Int {
        Log.d("sizeA", "sizeA: "+items.size)
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(items.get(position)!!)

        val container = holder.itemView.findViewById<ConstraintLayout>(R.id.container)
        container.setOnClickListener {
//            deletedItem.value = items[position]

            //container.setBackgroundColor(Color.GRAY)

            SubscriptionState.selectedCharities.add(items[position])

            val intent = Intent(holder.itemView.context, BillingDetailsActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }

        container.setBackgroundColor(Color.WHITE)

    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CharityItem) {
            val name = itemView.findViewById<TextView>(R.id.tv_name)
            name.text = item.title.rendered

            val desc = itemView.findViewById<TextView>(R.id.tv_desc)
            desc.text = item.acf.description


        }
    }


}