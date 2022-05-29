package com.example.a4thelocal_android.ui.auth

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.api.services.Retriever
import com.example.a4thelocal_android.databinding.ActivityStartPageBinding
import com.example.a4thelocal_android.ui.home.MainActivity
import com.example.a4thelocal_android.utills.Constants
import com.example.a4thelocal_android.utills.MyPreference
import kotlinx.coroutines.*


class StartPageActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityStartPageBinding? =null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(MyPreference.shared.getBool(MyPreference.isLoggedIn) == true){
            openHomePage()
            finish()
        }

        _binding = ActivityStartPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.continueBtnId.isEnabled = false
        binding.btnLocal.setOnClickListener(this)
        binding.btnScl.setOnClickListener(this)
        binding.continueBtnId.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        if (v!!.id == R.id.btn_local){
//            binding.continueBtnId.isEnabled = false
            binding.continueBtnId.isEnabled = true
            binding.btnLocal.isSelected = true
            binding.btnScl.isSelected = false
        }

        else if (v!!.id == R.id.btn_scl){
            binding.continueBtnId.isEnabled = true
            binding.btnLocal.isSelected = false
            binding.btnScl.isSelected = true
        }

        else if (v!!.id == R.id.continue_btn_id){
            MyPreference.shared.saveString(MyPreference.apiToken, null)
            if (binding.btnScl.isSelected){
                Constants.setSclConfig()
                MyPreference.shared.saveBool(MyPreference.isSchoolSelected, true)
            }
            else{
                Constants.setLocalConfig()
                MyPreference.shared.saveBool(MyPreference.isSchoolSelected, false)
            }
            SubscriptionState.selectedCharities.clear()
            SubscriptionState.selectedProducts.clear()
            loginAdmin()
        }
    }

    private fun loginAdmin() {

        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //2 Handle exceptions if any
        val errorHandler = CoroutineExceptionHandler { _, exception ->
            AlertDialog.Builder(this).setTitle("Error")
                .setMessage(exception.message)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()

            binding.loadingPB.visibility = View.GONE
        }

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            val resultList = Retriever().loginAdmin(Constants.adminUser, Constants.adminPass)
            //Log.e("---AdminResponse---", resultList.toString())
            MyPreference.shared.saveString(MyPreference.apiToken, resultList.token)
            binding.loadingPB.visibility = View.GONE
            openLoginPage()
            finish()
        }
    }

    private fun openLoginPage(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun openHomePage(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}