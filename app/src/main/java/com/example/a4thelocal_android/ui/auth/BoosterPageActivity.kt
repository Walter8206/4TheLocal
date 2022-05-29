package com.example.a4thelocal_android.ui.auth

import android.app.ActionBar
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.a4thelocal_android.databinding.ActivityBoosterPageBinding
import android.widget.*
import com.example.a4thelocal_android.R
import android.widget.CheckBox
import com.example.a4thelocal_android.api.data.*
import com.example.a4thelocal_android.api.services.RetrieverWithToken
import kotlinx.coroutines.*


class BoosterPageActivity : AppCompatActivity() {

    private var _binding: ActivityBoosterPageBinding? =null
    private val binding get() = _binding!!

    private var stateList: ArrayList<StateItem>? = null
    var stateNames = arrayListOf<String>()

    private var countiesResponse: CountiesResponse? = null
    var countyNames = arrayListOf<String>()

    private var schoolResponse:  SchoolsResponse? = null
    var schoolsNames = arrayListOf<String>()

    private var boosterResponse:  BoostersResponse? = null
    var boosterNames = arrayListOf<String>()

    private var studentsResponse:  StudentsResponse? = null
    var studentNames = arrayListOf<String>()

    var selectedStateCode = ""
    var selectedCountry = ""
    var selectedSchool = ""
    var selectedBooster = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBoosterPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        getAllStates()
    }


    private fun initView(){
        binding.bkBtnId.setOnClickListener {
            onBackPressed()
        }

        binding.continueBtnId.setOnClickListener {
            openBillingPage()
        }
    }

    private fun openBillingPage(){
        val intent = Intent(this, BillingDetailsActivity::class.java)
        startActivity(intent)
    }

    private fun getAllStates(){
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
            val resultList = RetrieverWithToken().getAllStates()
            stateList = resultList
            stateNames.clear()
            for(state in resultList){
                stateNames.add(state.state_name)
            }

            setStateSpinner()
            binding.loadingPB.visibility = View.GONE
            //Log.e("--States---", resultList.toString())
        }
    }


    private fun getAllCounty(stateCode: String){
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
            val resultList = RetrieverWithToken().getAllCounty(stateCode)
            countiesResponse = resultList
            countyNames.clear()
            for(county in countiesResponse!!.counties){
                countyNames.add(county)
            }

            setCountySpinner()
            binding.loadingPB.visibility = View.GONE
            //Log.e("--Counties---", resultList.toString())
        }
    }


    private fun getAllSchool(stateCode: String, county: String){
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
            val resultList = RetrieverWithToken().getAllSchool(stateCode, county)
            schoolResponse = resultList
            schoolsNames.clear()
            schoolResponse.let {
                for(school in it!!){
                    schoolsNames.add(school.school_name)
                }
            }


            setSchoolSpinner()
            binding.loadingPB.visibility = View.GONE
            //Log.e("--Schools---", resultList.toString())
        }
    }


    private fun getAllBooster(stateCode: String, county: String, schoolName: String){
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
            val resultList = RetrieverWithToken().getAllBooster(stateCode, county, schoolName)
            boosterResponse = resultList

            boosterResponse.let {
                boosterNames.clear()
                for(booster in it!!){
                    boosterNames.add(booster.booster)
                }
            }

            boostersCheckbox()
            binding.loadingPB.visibility = View.GONE
            //Log.e("--Schools---", resultList.toString())
        }
    }


    private fun getAllStudents(stateCode: String, county: String, schoolName: String, booster: String){
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
            val resultList = RetrieverWithToken().getAllStudent(stateCode, county, schoolName, booster)
            studentsResponse = resultList

            studentNames.clear()
            for(stdList in  studentsResponse!![0].students){
                studentNames.add(stdList.first_name + " " + stdList.last_name)
            }

            studentsCheckbox()
            binding.loadingPB.visibility = View.GONE
        }
    }


    private fun setStateSpinner(){
        binding.spnState.let {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, stateNames)
            it.adapter = adapter

            it.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long,
                ) {
                    selectedStateCode = stateList?.get(position)!!.state_code
                    SubscriptionState.boosterState = stateList?.get(position)!!.state_name
                    getAllCounty(stateList?.get(position)!!.state_code)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun setCountySpinner(){
        binding.spnCounty.let {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, countyNames)
            it.adapter = adapter

            it.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long,
                ) {
                    selectedCountry = countyNames[position]
                    SubscriptionState.boosterCounty = countyNames[position]
                    countiesResponse?.let { it1 -> getAllSchool(it1?.state_code,
                        countyNames[position]) }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun setSchoolSpinner(){
        binding.spnSchool.let {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, schoolsNames)
            it.adapter = adapter

            it.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long,
                ) {
                    schoolResponse.let {
                        selectedSchool = schoolsNames[position]
                        SubscriptionState.boosterSchool = schoolsNames[position]
                        getAllBooster(it?.get(position)!!.state, it.get(position).county_name, it.get(position).school_name)
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }


    private fun boostersCheckbox(){

        var Array_Count = 0
        var Str_Array: ArrayList<String> = boosterNames

        Array_Count = Str_Array.size

        val my_layout = findViewById<View>(R.id.cbx_view) as LinearLayout

        for (i in 0 until Array_Count) {
            val row = TableRow(this)
            row.id = i
            row.layoutParams = ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, 100)
            val checkBox = CheckBox(this)
            checkBox.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked) {
                    SubscriptionState.boosters.add(boosterNames[i])
                    getAllStudents(selectedStateCode, selectedCountry, selectedSchool, boosterNames[i])
                }
                else{
                    SubscriptionState.boosters.remove(boosterNames[i])
                }
            }
            checkBox.setButtonDrawable(R.drawable.drawable_checkbox)
            checkBox.id = i
            checkBox.text = " "+Str_Array[i]
            row.addView(checkBox)
            my_layout.addView(row)
        }
    }



    private fun studentsCheckbox(){

        var Array_Count = 0
        var Str_Array: ArrayList<String> = studentNames

        Array_Count = Str_Array.size

        val my_layout = findViewById<View>(R.id.cbx_students) as LinearLayout

        for (i in 0 until Array_Count) {
            val row = TableRow(this)
            row.id = i
            row.layoutParams = ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, 100)
            val checkBox = CheckBox(this)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) {
                    SubscriptionState.boosterStudents.add(studentNames[i])
                }
                else{
                    SubscriptionState.boosterStudents.remove(studentNames[i])
                }
            }
            checkBox.setButtonDrawable(R.drawable.drawable_checkbox)
            checkBox.id = i
            checkBox.text = " "+Str_Array[i]
            row.addView(checkBox)
            my_layout.addView(row)
        }
    }
}