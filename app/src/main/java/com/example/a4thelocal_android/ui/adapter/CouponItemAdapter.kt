package com.example.a4thelocal_android.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.api.data.CouponCodeResponseItem


class CouponItemAdapter(
    val context: Context,
    private val items: ArrayList<CouponCodeResponseItem>
) : RecyclerView.Adapter<CouponItemAdapter
.CustomViewHolder>() {

    var deletedItem = MutableLiveData<CouponCodeResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.item_order, parent, false)
        return CustomViewHolder(cellForRow)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
         holder.bind(items.get(position)!!)

        val btnDelete = holder.itemView.findViewById<ImageButton>(R.id.btn_delete)
        btnDelete.setOnClickListener {
            deletedItem.value = items[position]
        }
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CouponCodeResponseItem) {
            val title = itemView.findViewById<TextView>(R.id.tv_title)
            title.text = "Coupon: " + item.code

            val price = itemView.findViewById<TextView>(R.id.tv_price)
            price.text = "-$" + item.amount


        }
    }


}