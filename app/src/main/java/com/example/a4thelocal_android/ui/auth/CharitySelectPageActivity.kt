package com.example.a4thelocal_android.ui.auth

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a4thelocal_android.api.data.Charity
import com.example.a4thelocal_android.api.data.CharityItem
import com.example.a4thelocal_android.api.services.RetrieverWithToken
import com.example.a4thelocal_android.databinding.ActivityCharitySelectPageBinding
import com.example.a4thelocal_android.ui.adapter.CharityAdapter
import kotlinx.coroutines.*

class CharitySelectPageActivity : AppCompatActivity() {

    private var _binding: ActivityCharitySelectPageBinding? =null
    private val binding get() = _binding!!

    private var charityResponse:  Charity? = null
    var charitysNames = arrayListOf<String>()
    var charitysDesc = arrayListOf<String>()
    private var charityList = ArrayList<CharityItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCharitySelectPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCharityList()

        //setFields()
        binding.backImgBtnId.setOnClickListener {
            closePage()
        }


    }

    private fun closePage(){
        onBackPressed()
    }


    private fun getCharityList(){
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
            val resultList = RetrieverWithToken().getCharities()
            charityResponse = resultList
            charitysNames.clear()
            charitysDesc.clear()
            charityList.clear()
            charityResponse.let {
                for(charity in it!!){
                    charitysNames.add(charity.title.rendered)
                    charitysDesc.add(charity.acf.description)
                    charityList.add(charity)
                    Log.d("sizeA", "charityList: "+charityList)
                }
            }


            setRecyclerView()
            binding.loadingPB.visibility = View.GONE
            //Log.e("--Schools---", resultList.toString())
        }
    }

    private fun setRecyclerView() {

        var mAdapter = CharityAdapter(
            applicationContext,
            charityList
        )

        binding.rcvCharity.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mAdapter
        }

    }


}