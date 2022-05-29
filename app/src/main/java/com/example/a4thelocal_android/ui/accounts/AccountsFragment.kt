package com.example.a4thelocal_android.ui.accounts

import android.R
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a4thelocal_android.api.data.UserInfoResponse
import com.example.a4thelocal_android.api.services.RetrieverWithToken
import com.example.a4thelocal_android.databinding.FragmentAccountBinding
import com.example.a4thelocal_android.ui.auth.StartPageActivity
import com.example.a4thelocal_android.ui.home.MainActivity
import com.example.a4thelocal_android.utills.Constants
import com.example.a4thelocal_android.utills.MyPreference
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AccountsFragment : Fragment() {

    private var _binding: FragmentAccountBinding? =null
    private val binding get() = _binding!!

    companion object{
        var userInfoResponse: UserInfoResponse? = null
    }

    // Handle exceptions if any
    val errorHandler = CoroutineExceptionHandler { _, exception ->
        AlertDialog.Builder(activity).setTitle("Error")
            .setMessage(exception.message)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setIcon(R.drawable.ic_dialog_alert).show()

        binding.loadingPB.visibility = View.GONE
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAccountBinding.inflate(layoutInflater)

        if(MyPreference.shared.getBool(MyPreference.isLoggedIn) == false){
            openStartPage()
        }
        else{
            initView()
        }



        return binding.root
    }

    private fun initView() {

        val date = getCurrentDateTime()
        val dateInString = date.toString("dd MMM yyyy-hh:mm aa")

        binding.tvDate.text = "CURRENT DATE: " + dateInString.split("-")[0]
        binding.tvTime.text = "CURRENT TIME: " + dateInString.split("-")[1]

        binding.accountSettingsId.setOnClickListener {
            openAccountDetailPage()
        }

        binding.btnBusinessSignup.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.businessSignupLink))
            startActivity(browserIntent)
        }

        val userId = MyPreference.shared.getInt(MyPreference.userId)
        MyPreference.shared.getImgString(userId.toString())?.let {
            binding.imgProfile.setImageURI(Uri.fromFile(File(it)))
        }


        binding.imgProfile.setOnClickListener {
            (activity as MainActivity).openImagePicker(binding.imgProfile)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnAddress.setOnClickListener {
            openAddressPage()
        }

        binding.btnOrderList.setOnClickListener {
            openOrderListPage()
        }

        if (MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true) {
            binding.charityView.visibility = View.GONE
            binding.imgTitle.visibility = View.VISIBLE
            binding.imgTitleLocal.visibility = View.INVISIBLE
        } else {
            binding.charityView.visibility = View.GONE
            binding.imgTitle.visibility = View.INVISIBLE
            binding.imgTitleLocal.visibility = View.VISIBLE
        }

        getUserInfo()
    }

    private fun openAccountDetailPage(){
        val intent = Intent(activity, PasswordChangeActivity::class.java)
        startActivity(intent)
    }

    private fun openAddressPage(){
        val intent = Intent(activity, AddressActivity::class.java)
        startActivity(intent)
    }

    private fun openOrderListPage(){
        val intent = Intent(activity, OrderListActivity::class.java)
        startActivity(intent)
    }

    private fun logout(){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure want to logout?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            openStartPage()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }
        builder.show()
    }


    private fun openStartPage(){
        MyPreference.shared.clearAll()
        activity?.finish()
        val browserIntent = Intent(activity, StartPageActivity::class.java)
        startActivity(browserIntent)
    }

    private fun getUserInfo() {

        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            val resultList = RetrieverWithToken().getUserInfo()
            userInfoResponse = resultList
            Log.e("---APiResponse---", resultList.toString())
            binding.loadingPB.visibility = View.GONE

            binding.tvName.text = resultList.first_name + " " + resultList.last_name

            val sid = resultList.meta_data.single { it.key == "_wcs_subscription_ids_cache" }
            if(sid.value != null){
                val aaa = sid.value as ArrayList<Int>
                getUserSubscription(aaa.first())
            }

        }
    }

    private fun getUserSubscription(sId: Int) {

        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            val resultList = RetrieverWithToken().getUserSubscription(sId)
            Log.e("---APiResponse---", resultList.toString())
            binding.loadingPB.visibility = View.GONE
            if(resultList.subscription.status == "active"){
                binding.tvAccStatus.text = "ACTIVE"
                binding.tvAccStatus.setTextColor(resources.getColor(R.color.holo_green_light))
                binding.imgStatus.setImageResource(getResources().getIdentifier(
                    "border_bg_green", "drawable", activity?.packageName))
            }
            else{
                binding.tvAccStatus.text = "ON-HOLD"
                binding.tvAccStatus.setTextColor(resources.getColor(R.color.holo_red_light))
                binding.imgStatus.setImageResource(getResources().getIdentifier(
                    "border_bg_red", "drawable", activity?.packageName))
            }
        }
    }


    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}