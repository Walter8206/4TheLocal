package com.example.a4thelocal_android.ui.auth

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.api.services.Retriever
import com.example.a4thelocal_android.databinding.ActivityLoginBinding
import com.example.a4thelocal_android.ui.home.MainActivity
import com.example.a4thelocal_android.utills.Constants
import com.example.a4thelocal_android.utills.MyPreference
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? =null
    private val binding get() = _binding!!

    // Handle exceptions if any
    val errorHandler = CoroutineExceptionHandler { _, exception ->
        AlertDialog.Builder(this).setTitle("Error")
            .setMessage("The username or password you entered is incorrect.")
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()

        binding.loadingPB.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLocalOrSchool()

        //binding.etUsername.setText("uu1@school.com")
        //binding.etPassword.setText("123321")

        binding.backBtnId.setOnClickListener {
            openStartPage()
            finish()
        }

        binding.skipBtnId.setOnClickListener {
            finish()
           openHomePage()
        }

        binding.btnLogin.setOnClickListener {
            if(checkValidation()) {
                login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
            }
        }

        binding.btnAccCreate.setOnClickListener {
           openPricingPage()
        }

        binding.btnLocalSignup.setOnClickListener {
            openLocalSignup()
        }

        binding.btnSclSignup.setOnClickListener {
            openSclSignup()
         }


        binding.btnForgotPass.setOnClickListener {
            openForgotPass()
        }

    }

    private fun checkLocalOrSchool() {
        if(MyPreference.shared.getBool(MyPreference.isSchoolSelected) == false){
            binding.imageView.setImageResource(R.drawable.btn_local_selected)
        }
    }

    private fun openStartPage(){
        val browserIntent = Intent(this, StartPageActivity::class.java)
        startActivity(browserIntent)
    }

    fun openHomePage(){
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun openForgotPass(){
        val intent = Intent(this, ForgotPassActivity::class.java)
        startActivity(intent)
    }

    private fun openPricingPage(){
        val intent = Intent(this, PricingActivity::class.java)
        startActivity(intent)
    }

    private fun openSclSignup(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.schoolSignupLink))
        startActivity(browserIntent)
    }

    private fun openLocalSignup(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.businessSignupLink))
        startActivity(browserIntent)
    }


    private fun login(email: String, password: String) {

        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            val resultList = Retriever().loginAdmin(email, password)
            //Log.e("---APiResponse---", resultList.toString())
            binding.loadingPB.visibility = View.GONE
            MyPreference.shared.saveInt(MyPreference.userId, resultList.user_id)
            MyPreference.shared.saveBool(MyPreference.isLoggedIn, true)
            openHomePage()
        }
    }

    private fun checkValidation(): Boolean {

        if (binding.etUsername.text.isEmpty()) {
            showAlert("Username is required field")
            return false
        }

        else if (binding.etPassword.text.isEmpty()) {
            showAlert("Password is required field")
            return false
        }

        return true
    }

    private fun showAlert(msg: String){
        AlertDialog.Builder(this)
            .setMessage(msg)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .show()
    }

}