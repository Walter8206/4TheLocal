package com.example.a4thelocal_android.ui.accounts

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.api.services.Retriever
import com.example.a4thelocal_android.api.services.RetrieverWithToken
import com.example.a4thelocal_android.databinding.ActivityAddressBinding
import com.example.a4thelocal_android.databinding.ActivityOrderListBinding
import com.example.a4thelocal_android.utills.MyPreference
import kotlinx.coroutines.*

class OrderListActivity : AppCompatActivity() {

    private var _binding: ActivityOrderListBinding? =null
    private val binding get() = _binding!!


    // Handle exceptions if any
    val errorHandler = CoroutineExceptionHandler { _, exception ->
        AlertDialog.Builder(this).setTitle("Error")
            .setMessage(exception.message)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()

        binding.loadingPB.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtnId.setOnClickListener {
            finish()
        }

        getOrderList()
    }


    private fun getOrderList() {

        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            val resultList = RetrieverWithToken().getOrderList()
            Log.e("---APiResponse---", resultList.toString())
            binding.loadingPB.visibility = View.GONE

            binding.tvOrderId.text = "Order Id: "+resultList.orders.first().id.toString()
            binding.tvOrderStatus.text = "Status: "+resultList.orders.first().status
        }
    }
}