package com.example.a4thelocal_android.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.databinding.ActivityBillingDetailsBinding
import com.example.a4thelocal_android.databinding.ActivityBoosterPageBinding
import com.example.a4thelocal_android.databinding.ActivityLoginBinding
import com.example.a4thelocal_android.utills.MyPreference
import com.github.dhaval2404.imagepicker.ImagePicker

class BillingDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityBillingDetailsBinding? =null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBillingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setFields()

        binding.backImgBtnId.setOnClickListener {
            closePage()
        }

        binding.billingContinueBtnId.setOnClickListener {
            if(checkValidation()) {
                openPaymentActivity()
            }
        }

        binding.imgUpload.setOnClickListener {
            openImagePicker()
        }

//        Log.e("---State1----", SubscriptionState.boosterStudents.toString())
//        Log.e("---State2----", SubscriptionState.boosters.toString())
    }


    fun openImagePicker(){
        ImagePicker.with(this)
            //.crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            binding.uploadedImg?.setImageURI(uri)
            val userId = MyPreference.shared.getInt(MyPreference.userId)
            MyPreference.shared.saveImgString(userId.toString(), uri.path)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun closePage(){
       onBackPressed()
    }

    private fun openPaymentActivity(){
        SubscriptionState.billing_firstName = binding.etFirstName.text.toString()
        SubscriptionState.billing_lastName = binding.etLastName.text.toString()
        SubscriptionState.billing_companyName = binding.etCompany.text.toString()
        SubscriptionState.billing_county = binding.etCounty.text.toString()
        SubscriptionState.billing_street_address = binding.etAddress.text.toString()
        SubscriptionState.billing_city = binding.etCity.text.toString()
        SubscriptionState.billing_zip = binding.etZip.text.toString()
        SubscriptionState.billing_state = binding.etState.text.toString()
        SubscriptionState.billing_phone = binding.etPhone.text.toString()
        SubscriptionState.billing_email = binding.etEmail.text.toString()
        SubscriptionState.billing_password = binding.etPassword.text.toString()
        SubscriptionState.billing_note = binding.etNote.text.toString()

        val intent = Intent(this, PaymentActivity::class.java)
        startActivity(intent)
    }


    private fun checkValidation(): Boolean{

        if (binding.etFirstName.text.isEmpty()){
            showAlert("First name is required field")
            return false
        }
        else if (binding.etLastName.text.isEmpty()){
            showAlert("Last name is required field")
            return false
        }
        else if (binding.etCounty.text.isEmpty()){
            showAlert("County is required field")
            return false
        }
        else if (binding.etAddress.text.isEmpty()){
            showAlert("Address is required field")
            return false
        }
        else if (binding.etCity.text.isEmpty()){
            showAlert("City is required field")
            return false
        }
        else if (binding.etState.text.isEmpty()){
            showAlert("State is required field")
            return false
        }
        else if (binding.etZip.text.isEmpty()){
            showAlert("Zip is required field")
            return false
        }
        else if (binding.etPhone.text.isEmpty()){
            showAlert("Phone is required field")
            return false
        }
        else if (binding.etEmail.text.isEmpty()){
            showAlert("Email is required field")
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


    private fun setFields(){
        binding.etFirstName.setText("Nill")
        binding.etLastName.setText("Jhons")
        binding.etCompany.setText("M company")
        binding.etCounty.setText("Queen")
        binding.etAddress.setText("test address")
        binding.etCity.setText("test city")
        binding.etState.setText("test state")
        binding.etZip.setText("test zip")
        binding.etPhone.setText("123456789")
        binding.etEmail.setText("usr1@school.com")
        binding.etPassword.setText("123321")
        binding.etNote.setText("test note")
    }
}