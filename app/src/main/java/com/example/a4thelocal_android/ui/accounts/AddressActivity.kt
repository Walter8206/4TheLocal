package com.example.a4thelocal_android.ui.accounts

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a4thelocal_android.databinding.ActivityAddressBinding
import com.example.a4thelocal_android.ui.auth.BillingDetailsActivity

class AddressActivity : AppCompatActivity() {

    private var _binding: ActivityAddressBinding? =null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AccountsFragment.userInfoResponse.let {
            binding.tvName.text = it?.first_name + " " + it?.last_name
            binding.tvAddress.text = it?.billing?.address_1
        }

        binding.btnEdit.setOnClickListener {
            openBillingPage()
        }

        binding.backBtnId.setOnClickListener {
            finish()
        }
    }


    private fun openBillingPage(){
        val browserIntent = Intent(this, BillingDetailsActivity::class.java)
        startActivity(browserIntent)
    }
}