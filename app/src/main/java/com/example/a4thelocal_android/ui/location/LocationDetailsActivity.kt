package com.example.a4thelocal_android.ui.location

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.a4thelocal_android.databinding.ActivityLocationDetailsBinding
import com.example.a4thelocal_android.ui.data.ImageXX
import com.example.a4thelocal_android.utills.MyPreference
import com.google.gson.Gson

class LocationDetailsActivity : AppCompatActivity() {


    private var _binding: ActivityLocationDetailsBinding? =null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLocationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtnId.setOnClickListener {
            finish()
        }

        LocationFragment.selectedItem.let {


            var imglink = ""
            if(MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true){
                imglink = it?.acf?.image as String
            }
            else{
                val img = it?.acf?.image
                val gs = Gson()
                val js = gs.toJson(img)
                val aaa = gs.fromJson<ImageXX>(js, ImageXX::class.java)
                Log.e("----aaaa----", aaa.url)
                imglink = aaa.url
            }


            Glide.with(this)
                .load(imglink)
                .into(binding.image)

            binding.tvDiscount.text = it?.acf?.discount
            binding.tvDesc.text = it?.acf?.description
            binding.tvAddress.text = it?.acf?.address_1
            binding.tvPhone.text = it?.acf?.business_phone
            binding.tvWebsite.text = it?.acf?.website

            binding.btnFb.setOnClickListener { view ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it?.acf?.facebook))
                startActivity(browserIntent)
            }

            binding.btnInsta.setOnClickListener { view ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it?.acf?.instagram))
                startActivity(browserIntent)
            }

            binding.tvWebsite.setOnClickListener { view ->
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it?.acf?.website))
                startActivity(browserIntent)
            }

            binding.tvPhone.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + binding.tvPhone.text.toString())
                startActivity(dialIntent)
            }
        }
    }
}