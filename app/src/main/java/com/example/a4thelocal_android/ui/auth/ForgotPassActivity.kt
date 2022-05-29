package com.example.a4thelocal_android.ui.auth

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.api.services.Retriever
import com.example.a4thelocal_android.api.services.RetrieverWithToken
import com.example.a4thelocal_android.databinding.ActivityForgotPassBinding
import com.example.a4thelocal_android.databinding.ActivityLoginBinding
import com.example.a4thelocal_android.utills.MyPreference
import kotlinx.coroutines.*

class ForgotPassActivity : AppCompatActivity() {

    private var _binding: ActivityForgotPassBinding? =null
    private val binding get() = _binding!!

    val errorHandler = CoroutineExceptionHandler { _, exception ->

        exception.printStackTrace()
        AlertDialog.Builder(this).setTitle("Error")
            .setMessage(exception.message)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()

        binding.loadingPB.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bkBtn.setOnClickListener {
            finish()
        }

        binding.btnSend.setOnClickListener {
            forgotPassword()
        }
    }


    private fun forgotPassword(){

        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            val resultList = RetrieverWithToken().forgotPassword(binding.etEmail.text.toString())
            //Log.e("---APiResponse---", resultList.toString())
            binding.loadingPB.visibility = View.GONE
            showAlert(resultList.message)
            //openHomePage()
        }
    }

    private fun showAlert(msg: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage(msg)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            MyPreference.shared.clearAll()
            finish()
        }
        builder.show()
    }
}