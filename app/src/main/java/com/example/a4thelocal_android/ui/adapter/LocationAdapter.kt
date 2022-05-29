package com.example.a4thelocal_android.ui.adapter

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.api.data.LocationResponseItem
import com.example.a4thelocal_android.ui.data.ImageXX
import com.example.a4thelocal_android.utills.MyPreference
import com.google.gson.Gson


class LocationAdapter(
    val context: Context,
    private val items: ArrayList<LocationResponseItem>
) : RecyclerView.Adapter<LocationAdapter
.CustomViewHolder>() {

    var detailItem = MutableLiveData<LocationResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val cellForRow = inflater.inflate(R.layout.item_location, parent, false)
        return CustomViewHolder(cellForRow)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
         holder.bind(items.get(position)!!)
        holder.itemView.setOnClickListener {
            detailItem.value = items[position]
        }
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: LocationResponseItem) {
            val title = itemView.findViewById<TextView>(R.id.tv_title)
            //title.text = item.title.rendered
            title.text = Html.fromHtml(item.title.rendered )

            val city = itemView.findViewById<TextView>(R.id.tv_city)
            city.text = item.acf.city

            val discount = itemView.findViewById<TextView>(R.id.tv_discount)
            discount.text = item.acf.discount

            val image = itemView.findViewById<ImageView>(R.id.img_item)

            var imglink = ""
            if(MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true){
                imglink = item.acf.image as String
            }
            else{
                val img = item.acf.image
                val gs = Gson()
                val js = gs.toJson(img)
                val aaa = gs.fromJson<ImageXX>(js, ImageXX::class.java)
                Log.e("----aaaa----", aaa.url)
                imglink = aaa.url
            }

            Glide.with(itemView.context)
                .load(imglink)
                .override(300, 200)
                .into(image)
        }
    }


    inline fun <reified T: Any> Any.cast(): T{
        return this as T
    }

}