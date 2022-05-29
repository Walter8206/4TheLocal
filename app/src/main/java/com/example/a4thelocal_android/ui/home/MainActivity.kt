package com.example.a4thelocal_android.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.api.services.Retriever
import com.example.a4thelocal_android.databinding.ActivityMainBinding
import com.example.a4thelocal_android.ui.auth.PricingActivity
import com.example.a4thelocal_android.utills.Constants
import com.example.a4thelocal_android.utills.MyPreference
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? =null
    private val binding get() = _binding!!
    private var profileImg: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(MyPreference.shared.getBool(MyPreference.isLoggedIn) == true){
            if(MyPreference.shared.getBool(MyPreference.isSchoolSelected) == true){
                Constants.setSclConfig()
            }
            else{
                Constants.setLocalConfig()
            }
            loginAdmin()
        }

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_location, R.id.navigation_account))
        binding.navView.setupWithNavController(navController)

        val mDrawerLayout = findViewById<DrawerLayout>(R.id.my_drawer_layout)

        binding.btnMenu.setOnClickListener {
            mDrawerLayout.openDrawer(Gravity.LEFT)
        }

        binding.btnAnnual.setOnClickListener {
            val intent = Intent(this, PricingActivity::class.java)
            startActivity(intent)
        }

        binding.btnGift.setOnClickListener {
            val intent = Intent(this, PricingActivity::class.java)
            startActivity(intent)
        }

        binding.btnPrivacy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_URL + Constants.privacyLink))
            startActivity(browserIntent)
        }

        binding.btnSupport.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_URL + Constants.supportLink))
            startActivity(browserIntent)
        }

        binding.btnInstagram.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.instagramLink))
            startActivity(browserIntent)
        }

        binding.btnFacebook.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.facebookLink))
            startActivity(browserIntent)
        }
    }

    fun gotoLocationTab(){
        binding.navView.selectedItemId = R.id.navigation_location
    }

    fun gotoAccTab(){
        binding.navView.selectedItemId = R.id.navigation_account
    }

    fun openImagePicker(imgProfile: ImageView){
        profileImg = imgProfile
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
            profileImg?.setImageURI(uri)
            val userId = MyPreference.shared.getInt(MyPreference.userId)
            MyPreference.shared.saveImgString(userId.toString(), uri.path)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
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
        }
    }
}