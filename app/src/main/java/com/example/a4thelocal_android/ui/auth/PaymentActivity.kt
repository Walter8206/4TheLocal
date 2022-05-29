package com.example.a4thelocal_android.ui.auth

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a4thelocal_android.api.data.CouponCodeResponseItem
import com.example.a4thelocal_android.api.services.Retriever
import com.example.a4thelocal_android.api.services.RetrieverWithToken
import com.example.a4thelocal_android.api.services.StripeRetriever
import com.example.a4thelocal_android.databinding.ActivityPaymentBinding
import com.example.a4thelocal_android.ui.adapter.CouponItemAdapter
import com.example.a4thelocal_android.ui.adapter.OrderItemAdapter
import com.example.a4thelocal_android.ui.home.MainActivity
import com.example.a4thelocal_android.utills.Constants
import com.example.a4thelocal_android.utills.MyPreference
import kotlinx.coroutines.*

class PaymentActivity : AppCompatActivity() {

    private var _binding: ActivityPaymentBinding? =null
    private val binding get() = _binding!!
    private var dateTextWatcher: TextWatcher? = null

    val lineItemList = ArrayList<Map<String, String>>()
    val couponLineItemList = ArrayList<Map<String, String>>()
    val couponItemList = ArrayList<CouponCodeResponseItem>()
    var cAdapter: CouponItemAdapter? = null
    var totalPrice = 0.0

    // Handle exceptions if any
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
       exception.message?.let { showError(it) }
        binding.loadingPB.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.paymentBackBtnId.setOnClickListener {
            closePage()
        }

        binding.purchaseBtnId.setOnClickListener {
            if(checkValidation()) {
                stripePayment()
            }
        }

        binding.btnApplyCoupon.setOnClickListener {
            with(binding){
                val codeList = couponItemList.filter { it.code ==  etCoupon.text.toString()}
                if(codeList.count() > 0){
                    showAlert(Constants.couponMsg)
                }
                else if(!etCoupon.text.isEmpty()){
                    applyCoupon(etCoupon.text.toString())
                }
            }

        }

        var mAdapter = OrderItemAdapter(
            applicationContext,
            SubscriptionState.selectedProducts
        )

        binding.rcvOrders.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mAdapter
        }


        cAdapter = CouponItemAdapter(
            applicationContext,
            couponItemList
        )

        binding.rcvCoupon.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = cAdapter
        }



        with(binding) {
            dateTextWatcher = etDate.doAfterTextChanged {
                val str = it.toString()
                if (str.count() == 2){
                    etDate.setText("$str/")
                    etDate.setSelection(3)
                }
            }
        }

        mAdapter.deletedItem.observe(this, Observer {
            if(SubscriptionState.selectedProducts.size == 1){
                showError("Do not delete all item.")
                return@Observer
            }
            SubscriptionState.selectedProducts.remove(it)
            mAdapter.notifyDataSetChanged()
            setTotalPrice()
        })

        cAdapter!!.deletedItem.observe(this, Observer {
            couponItemList.remove(it)
            cAdapter?.notifyDataSetChanged()
            setTotalPrice()
        })

        setTotalPrice()
        setCharities()


        //setFields()
    }

    fun setFields(){
        binding.etCardNum.setText("4242424242424242")
        binding.etDate.setText("08/28")
        binding.etCvc.setText("123")
    }

    private fun setCharities(){
        for(charity in SubscriptionState.selectedCharities){
            val coupon = CouponCodeResponseItem(
                null,
                "0.0",
                charity.slug,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null)

            couponItemList.add(coupon)
        }

        cAdapter?.notifyDataSetChanged()
    }

    private fun setTotalPrice(){
        totalPrice = 0.0
        lineItemList.clear()
        for(product in SubscriptionState.selectedProducts){
            val lineItem = mapOf(
                "product_id" to  product.id,
                "quantity" to 2,
                "tax_class" to  "",
                "subtotal" to  product.price,
                "subtotal_tax" to  "0.00",
                "total" to  product.price,
                "total_tax" to  "0.00",
                "sku" to  "",
                "price" to  product.price
            )

            totalPrice += product.price.toDouble()
            lineItemList.add(lineItem as Map<String, String>)
        }

        binding.tvSubtotal.text = "$"+totalPrice.toString()
        if(MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true){
            binding.processingFee.visibility = View.VISIBLE
            totalPrice += 1.5
        }
        binding.tvTotal.text = "$"+totalPrice.toString()
    }

    private fun closePage(){
        onBackPressed()
    }

    private fun stripePayment() {

        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {

            val params = mapOf(
                "type" to "card",
                "card[number]" to binding.etCardNum.text.toString(),
                "card[exp_month]" to "08",
                "card[exp_year]" to "28",
                "card[cvc]" to binding.etCvc.text.toString()
            )

            //4
            val resultList = StripeRetriever().stripePayment(params)
            Log.e("---APiResponse---", resultList.toString())
            binding.loadingPB.visibility = View.GONE


            if(MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true){
                sclSubscription(resultList.id)
            }
            else{
                localSubscription(resultList.id)
            }
        }
    }


    private fun  sclSubscription(paymentMethodId: String){

        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE

        val boosters = arrayOf("Cricket team", "Football Team")
        val students = arrayOf("student1", "student2")

        val feeLineItem = mapOf(
            "name" to  "Processing Fee",
            "tax_class" to  "0",
            "tax_status" to  "taxable",
            "amount" to  "1.5",
            "total" to  "1.50",
            "total_tax" to  "0.00"
        )

        val feeLineItemList = listOf(feeLineItem)



        for(coupon in couponItemList){
            val couponLineItem = mapOf(
                "code" to  coupon.code,
                "discount" to coupon.amount,
                "discount_tax" to  "0"
            )

            couponLineItemList.add(couponLineItem as Map<String, String>)
        }

        for(charity in SubscriptionState.selectedCharities){
            val couponLineItem = mapOf(
                "code" to  charity.slug,
                "discount" to "0.0",
                "discount_tax" to  "0"
            )

            couponLineItemList.add(couponLineItem)
        }



        val params = mapOf(
            "email" to SubscriptionState.billing_email,
            "first_name" to SubscriptionState.billing_firstName,
            "last_name" to SubscriptionState.billing_lastName,
            "username" to SubscriptionState.billing_email,
            "password" to SubscriptionState.billing_password,
            "billing_first_name" to SubscriptionState.billing_firstName,
            "billing_last_name" to SubscriptionState.billing_lastName,
            "billing_company" to SubscriptionState.billing_companyName,
            "billing_address_1" to SubscriptionState.billing_street_address,
            "billing_address_2" to SubscriptionState.billing_street_address,
            "billing_city" to SubscriptionState.billing_city,
            "billing_state" to SubscriptionState.billing_state,
            "billing_postcode" to SubscriptionState.billing_zip,
            "billing_country" to SubscriptionState.billing_county,
            "billing_email" to SubscriptionState.billing_email,
            "billing_phone" to SubscriptionState.billing_phone,
            "billing_period" to SubscriptionState.billing_period,
            "billing_interval" to SubscriptionState.billing_interval,
            "customer_note" to SubscriptionState.billing_note,
            "payment_method" to paymentMethodId,
            "default_payment_method" to paymentMethodId,
            "total_amount" to "${totalPrice.toInt() * 100}",
            "state" to SubscriptionState.boosterState,
            "county" to SubscriptionState.boosterCounty,
            "school" to SubscriptionState.boosterSchool,
            "booster" to boosters,
            "students" to students,
            "line_items" to lineItemList,
            "fee_lines" to feeLineItemList,
            "coupon_lines" to couponLineItemList
        )

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            val resultList = Retriever().sclSubscription(params as Map<String, String>)
            Log.e("---APiResponse---", resultList.toString())
            binding.loadingPB.visibility = View.GONE

            resultList.message.let {
                it?.let { it1 -> showError(it1) }
            }

            if (resultList.creating_subscription != null){
                showSuccessAlert(Constants.sclCreateMsg)
            }

        }
    }


    private fun  localSubscription(paymentMethodId: String){

        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE


        val billing = mapOf(
            "first_name" to SubscriptionState.billing_firstName,
            "last_name" to SubscriptionState.billing_lastName,
            "company" to SubscriptionState.billing_companyName,
            "address_1" to SubscriptionState.billing_street_address,
            "address_2" to SubscriptionState.billing_street_address,
            "city" to SubscriptionState.billing_city,
            "state" to SubscriptionState.billing_state,
            "postcode" to SubscriptionState.billing_zip,
            "country" to SubscriptionState.billing_county,
            "email" to SubscriptionState.billing_email,
            "phone" to SubscriptionState.billing_phone
        )

        val shipping = mapOf(
            "first_name" to SubscriptionState.billing_firstName,
            "last_name" to SubscriptionState.billing_lastName,
            "company" to SubscriptionState.billing_companyName,
            "address_1" to SubscriptionState.billing_street_address,
            "address_2" to SubscriptionState.billing_street_address,
            "city" to SubscriptionState.billing_city,
            "state" to SubscriptionState.billing_state,
            "postcode" to SubscriptionState.billing_zip,
            "country" to SubscriptionState.billing_county
        )

        val stripePaymentIntent = mapOf(
            "total_amount" to "${totalPrice.toInt() * 100}",
        )

        val stripeCustomer = mapOf(
            "payment_method" to paymentMethodId,
            "name" to SubscriptionState.billing_firstName,
            "phone" to SubscriptionState.billing_phone,
            "email" to SubscriptionState.billing_email,
            "invoice_settings[default_payment_method]" to paymentMethodId
        )

        for(coupon in couponItemList){
            val couponLineItem = mapOf(
                "code" to  coupon.code,
                "discount" to coupon.amount,
                "discount_tax" to  "0"
            )

            couponLineItemList.add(couponLineItem as Map<String, String>)
        }



        val params = mapOf(
            "email" to SubscriptionState.billing_email,
            "first_name" to SubscriptionState.billing_firstName,
            "last_name" to SubscriptionState.billing_lastName,
            "username" to SubscriptionState.billing_email,
            "password" to SubscriptionState.billing_password,

            "billing" to billing,

            "billing_interval" to SubscriptionState.billing_interval,
            "billing_period" to SubscriptionState.billing_period,

            "shipping" to shipping,
            "shipping_lines" to "",

            "stripe_customer" to stripeCustomer,
            "stripe_payment_intent" to stripePaymentIntent,

            "customer_note" to SubscriptionState.billing_note,
            "line_items" to lineItemList,
            "coupon_lines" to couponLineItemList
        )

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            val resultList = Retriever().localSubscription(params as Map<String, String>)
            Log.e("---APiResponse---", resultList.toString())
            binding.loadingPB.visibility = View.GONE

            resultList.message.let {
                it?.let { it1 -> showError(it1) }
            }

            if (resultList.creating_subscription != null){
                showSuccessAlert(Constants.localCreateMsg)
            }

        }
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

    private fun applyCoupon( code: String) {

        // below line is for displaying our progress bar.
        binding.loadingPB.visibility = View.VISIBLE

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            val resultList = RetrieverWithToken().applyCoupon(code)
            Log.e("---APiResponse---", resultList.toString())
            binding.loadingPB.visibility = View.GONE
            couponItemList.addAll(resultList)
            cAdapter?.notifyDataSetChanged()

            for(coupon in resultList){
                totalPrice -= coupon.amount!!.toDouble()
            }

            binding.tvTotal.text = "$"+totalPrice.toString()
        }
    }

    private fun openHomePage(){
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showError(msg: String){
        AlertDialog.Builder(this).setTitle("Error")
            .setMessage(msg)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    private fun checkValidation(): Boolean {

        if (binding.etCardNum.text.isEmpty()) {
            showAlert("Card number is required field")
            return false
        }

        else if (binding.etDate.text.isEmpty()) {
            showAlert("Expire date is required field")
            return false
        }

        else if (binding.etCvc.text.isEmpty()) {
            showAlert("CVC is required field")
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

    private fun showSuccessAlert(msg: String){
        AlertDialog.Builder(this)
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok){ _, _ ->
                login(SubscriptionState.billing_email, SubscriptionState.billing_password)
            }
            .show()
    }
}
