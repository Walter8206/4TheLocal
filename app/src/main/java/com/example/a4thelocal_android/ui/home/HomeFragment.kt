package com.example.a4thelocal_android.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.databinding.FragmentHomeBinding
import com.example.a4thelocal_android.ui.auth.PricingActivity
import com.example.a4thelocal_android.utills.Constants
import com.example.a4thelocal_android.utills.MyPreference

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    private fun initView(){

        if(MyPreference.shared.getBool(MyPreference.isLoggedIn) == true){
            binding.skipView.container.visibility = View.GONE
            binding.container.visibility = View.VISIBLE
        }
        else{
            binding.skipView.container.visibility = View.VISIBLE
            binding.container.visibility = View.GONE
        }


        binding.btnSave2.setOnClickListener {
            (activity as MainActivity).gotoLocationTab()
        }
        binding.skipView.btnSave.setOnClickListener {
            (activity as MainActivity).gotoLocationTab()
        }

        binding.btnBusiness.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.businessSignupLink))
            startActivity(browserIntent)
        }
        binding.skipView.btnSignup.setOnClickListener {
            openPricingPage()
        }
        binding.skipView.btnPlanChoose.setOnClickListener {
            openPricingPage()
        }

        binding.btnSchool.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.schoolSignupLink))
            startActivity(browserIntent)
        }

        binding.btnAcc.setOnClickListener {
            (activity as MainActivity).gotoAccTab()
        }

        if(MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true){
            binding.imgTitle.setImageResource(R.drawable.home_page_logo)
            binding.tvNorthernMichigans.visibility = View.GONE
        }
        else{
            binding.imgTitle.setImageResource(R.drawable.home_page_logo_local)
            binding.btnSchool.visibility = View.GONE
        }
    }


    private fun openPricingPage(){
        val intent = Intent(activity, PricingActivity::class.java)
        startActivity(intent)
    }
}