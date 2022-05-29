package com.example.a4thelocal_android.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.databinding.ActivityCharitySearchPageBinding
import com.example.a4thelocal_android.databinding.ActivityCharitySelectPageBinding

class CharitySearchPageActivity : AppCompatActivity() {

    private var _binding: ActivityCharitySearchPageBinding? =null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCharitySearchPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setFields()
        binding.backImgBtnId.setOnClickListener {
            closePage()
        }


    }

    private fun closePage(){
        onBackPressed()
    }


}