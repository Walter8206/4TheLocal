package com.example.a4thelocal_android.ui.auth

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.a4thelocal_android.api.data.AllProductResponse
import com.example.a4thelocal_android.api.data.AllProductResponseItem
import com.example.a4thelocal_android.api.services.RetrieverWithToken
import com.example.a4thelocal_android.databinding.ActivityPricingBinding
import com.example.a4thelocal_android.utills.Constants
import com.example.a4thelocal_android.utills.MyPreference
import kotlinx.coroutines.*
import java.lang.Exception

class PricingActivity : AppCompatActivity() {

    private var _binding: ActivityPricingBinding? = null
    private val binding get() = _binding!!
    var resultList: AllProductResponse? = null
    var selectedProduct: AllProductResponseItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPricingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initView()

        if (MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true) {
            getAllSclProducts()
        } else {
            getAllLocalProducts()
        }
    }

    fun initView() {

        Log.d("resultList", "resultList: " + resultList);

       // println(SubscriptionState.selectedProducts)

        binding.annualPriceContainer.visibility = View.GONE
        binding.giftPriceContainer.visibility = View.GONE
        binding.partTimePriceContainer.visibility = View.GONE
        binding.moreContainer.visibility = View.GONE
        binding.btnAnnualSignup.isEnabled = false

        binding.leftPriceBtn.isSelected = false
        binding.middlePriceBtn.isSelected = false
        binding.rightPriceBtn.isSelected = false

        binding.leftPriceBtn.setOnClickListener {
            binding.leftPriceBtn.isSelected = true
            binding.middlePriceBtn.isSelected = false
            binding.rightPriceBtn.isSelected = false
            binding.btnAnnualSignup.isEnabled = true

            try {
                selectedProduct = resultList?.single { it.name == Constants.annually_local ||  it.name == Constants.annual_scl}
            } catch (e: Exception) {
                Log.e("resultList", "error: " + e.message);
            }

        }

        binding.middlePriceBtn.setOnClickListener {
            binding.middlePriceBtn.isSelected = true
            binding.leftPriceBtn.isSelected = false
            binding.rightPriceBtn.isSelected = false
            binding.btnAnnualSignup.isEnabled = true

            Log.d("resultList", "it.price: " + binding.tvPriceM.text.toString());


            try {
                selectedProduct = resultList?.single { it.name == Constants.quarterly_local }
            } catch (e: Exception) {
                Log.d("resultList", "error: " + e.message);
            }

            Log.d("resultList", "selectedProduct: " + selectedProduct);
        }

        binding.rightPriceBtn.setOnClickListener {
            binding.rightPriceBtn.isSelected = true
            binding.leftPriceBtn.isSelected = false
            binding.middlePriceBtn.isSelected = false
            binding.btnAnnualSignup.isEnabled = true

            Log.d("resultList", "it.price: " + binding.tvPriceR.text.toString());

            try {
                selectedProduct = resultList?.single { it.name == Constants.monthly_local }
            } catch (e: Exception) {
                Log.d("resultList", "error: " + e.message);
            }

            Log.d("resultList", "selectedProduct: " + selectedProduct);

        }

        binding.btnAnnualSignup.setOnClickListener {
            if (MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true) {
                selectedProduct?.let { it1 -> SubscriptionState.selectedProducts.add(it1) }
                openBoosterPage()
            } else {
                selectedProduct?.let { it1 -> SubscriptionState.selectedProducts.add(it1) }
                Log.e("----selectedProduct", SubscriptionState.selectedProducts.toString())
                openCharitySelectPage()
            }
        }

        binding.bkBtnId.setOnClickListener {
            closePage()
        }

        binding.btnFaq.setOnClickListener {
            openFaq()
        }

        binding.button3.setOnClickListener {
            try {
                selectedProduct = resultList?.single { it.name == Constants.membership_local }
            } catch (e: Exception) {
                Log.d("resultList", "error: " + e.message);
            }

            selectedProduct?.let { it1 -> SubscriptionState.selectedProducts.add(it1) }
            if (MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true) {
                openBoosterPage()
            }
            else {
                openBillingPage()
            }
        }

        binding.button5.setOnClickListener {
            try {
                selectedProduct = resultList?.single { it.name == Constants.part_time_local }
            } catch (e: Exception) {
                Log.d("resultList", "error: " + e.message);
            }

            selectedProduct?.let { it1 -> SubscriptionState.selectedProducts.add(it1) }
            if (MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true) {
                openBoosterPage()
            }
            else {
                openBillingPage()
            }
        }

        binding.button6.setOnClickListener {
            //openCharitySearchPage()
        }
    }

    private fun openCharitySelectPage() {
        val intent = Intent(this, CharitySelectPageActivity::class.java)
        startActivity(intent)
    }


    private fun openCharitySearchPage() {
        val intent = Intent(this, CharitySearchPageActivity::class.java)
        startActivity(intent)
    }



    private fun openFaq() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_URL + "/faq-page/"))
        startActivity(browserIntent)
    }

    private fun closePage() {
        onBackPressed()
    }

    fun openBillingPage() {
        val intent = Intent(this, BillingDetailsActivity::class.java)
        startActivity(intent)
    }

    fun openBoosterPage() {
        val intent = Intent(this, BoosterPageActivity::class.java)
        startActivity(intent)
    }


    private fun getAllLocalProducts() {

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()
        binding.loadingPB.visibility = View.VISIBLE

        //2 Handle exceptions if any
        val errorHandler = CoroutineExceptionHandler { _, exception ->
            AlertDialog.Builder(this).setTitle("Error")
                .setMessage(exception.message)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
            exception.printStackTrace()
        }

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {

            //4
            resultList = RetrieverWithToken().getProducts()
            Log.d("resultList", "resultList 2: " + resultList);


            val leftPrice = resultList?.filter { it.name == Constants.annually_local }?.single()
            leftPrice.let {
                binding.annualPriceContainer.visibility = View.VISIBLE
                binding.leftPriceBtn.visibility = View.VISIBLE
                binding.tvPriceLeft.text = "$" + it?.price
            }

            val middlePrice = resultList?.filter { it.name == Constants.quarterly_local }?.single()
            middlePrice.let {
                binding.annualPriceContainer.visibility = View.VISIBLE
                binding.middlePriceBtn.visibility = View.VISIBLE
                binding.tvPriceM.text = "$" + it?.price
            }

            val rightPrice = resultList?.filter { it.name == Constants.monthly_local }?.single()
            rightPrice.let {
                binding.annualPriceContainer.visibility = View.VISIBLE
                binding.rightPriceBtn.visibility = View.VISIBLE
                binding.tvPriceR.text = "$" + it?.price
            }


            val giftPrice = resultList?.filter { it.name == Constants.membership_local }?.single()
            giftPrice.let {
                binding.giftPriceContainer.visibility = View.VISIBLE
                binding.tvGiftPrice.text = "$" + it?.price
            }

            val ptPrice = resultList?.filter { it.name == Constants.part_time_local }?.single()
            ptPrice.let {
                binding.partTimePriceContainer.visibility = View.VISIBLE
                binding.tvPtPrice.text = "$" + it?.price
            }


            binding.moreContainer.visibility = View.VISIBLE
            binding.loadingPB.visibility = View.GONE
        }
    }


    private fun getAllSclProducts() {
        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()
        binding.loadingPB.visibility = View.VISIBLE
        //2 Handle exceptions if any
        val errorHandler = CoroutineExceptionHandler { _, exception ->
            AlertDialog.Builder(this).setTitle("Error")
                .setMessage(exception.message)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
            exception.printStackTrace()
        }

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            resultList = RetrieverWithToken().getProducts()
            binding.loadingPB.visibility = View.GONE

            val left2Price = resultList?.filter { it.name == Constants.annual_scl }?.single()
            left2Price.let {
                binding.annualPriceContainer.visibility = View.VISIBLE
                binding.leftPriceBtn.visibility = View.VISIBLE
                binding.tvPriceLeft.text = "$" + it?.price
            }


            val giftPrice = resultList?.filter { it.name == "Annual Membership Gift Certificate" }
            if (giftPrice?.count()!! > 0) {
                giftPrice.single().let {
                    binding.giftPriceContainer.visibility = View.VISIBLE
                    binding.tvGiftPrice.text = "$" + it.price
                }
            }
        }
    }
}