package com.example.a4thelocal_android.ui.accounts

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.a4thelocal_android.api.services.RetrieverWithToken
import com.example.a4thelocal_android.databinding.ActivityPasswordChangeBinding
import com.example.a4thelocal_android.utills.MyPreference
import kotlinx.coroutines.*

class PasswordChangeActivity : AppCompatActivity() {

    private var _binding: ActivityPasswordChangeBinding? =null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPasswordChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userInfo = AccountsFragment.userInfoResponse

        binding.acntDtlsBackBtnId.setOnClickListener {
            closePage()
        }

        binding.btnSave.setOnClickListener {
            changePassword()
        }

        binding.tvName.setText(userInfo?.first_name + " " + userInfo?.last_name)
        binding.tvEmail.setText(userInfo?.email)
    }

    private fun closePage(){
        onBackPressed()
    }

    private fun changePassword(){

        binding.loadingPB.visibility = View.VISIBLE
        val mainActivityJob = Job()
        val errorHandler = CoroutineExceptionHandler { _, exception ->
            AlertDialog.Builder(this).setTitle("Error")
                .setMessage(exception.message)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
            exception.printStackTrace()
            binding.loadingPB.visibility = View.GONE
        }

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4

            val userId = MyPreference.shared.getInt(MyPreference.userId)
            val resultList = RetrieverWithToken().changePassword(userId,
                binding.tvName.text.toString(), binding.tvEmail.text.toString(), binding.tvPassword.text.toString())
            binding.loadingPB.visibility = View.GONE
            Log.e("--States---", resultList.toString())
            finish()
        }
    }
}