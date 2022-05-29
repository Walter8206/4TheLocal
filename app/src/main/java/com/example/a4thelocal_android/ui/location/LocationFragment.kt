package com.example.a4thelocal_android.ui.location

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a4thelocal_android.R
import com.example.a4thelocal_android.api.data.LocationResponseItem
import com.example.a4thelocal_android.api.services.RetrieverWithToken
import com.example.a4thelocal_android.databinding.FragmentLocationBinding
import com.example.a4thelocal_android.ui.adapter.LocationAdapter
import com.example.a4thelocal_android.ui.home.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.*


class LocationFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private var locationList = ArrayList<LocationResponseItem>()
    private var tempLocationList = ArrayList<LocationResponseItem>()
    var lAdapter: LocationAdapter? = null

    private var searchTextWatcher: TextWatcher? = null
    private var businessList = ArrayList<String>()
    private var cityList = ArrayList<String>()

    private var selectedBusiness = ""
    private var selectedCity = ""
    private var isReset = false

    private lateinit var mMap: GoogleMap

    companion object {
        var selectedItem: LocationResponseItem? = null
    }

    val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLocationBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    private fun initView() {
        loadLocation()

        with(binding) {

            searchTextWatcher = etSearch.doAfterTextChanged {

                val searchTxt = it

                if (searchTxt!!.count() > 0) {
                    locationList = locationList.filter { it ->
                        it.title.rendered.toLowerCase()
                            .startsWith(searchTxt.toString().toLowerCase())
                    } as ArrayList<LocationResponseItem>
                } else {
                    locationList = tempLocationList
                }

                setListData()
            }

            btnFilter.setOnClickListener {
                showFilterDialog()
            }

            btnSort.setOnClickListener {
                showSortDialog()
            }
        }

        binding.btnList.setOnClickListener {
            binding.mapContainer.visibility = View.GONE
        }

        binding.btnMap.setOnClickListener {
            binding.mapContainer.visibility = View.VISIBLE
        }


        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    private fun loadLocation() {
        locationList.clear()
        tempLocationList.clear()
        for (nums in 1..5) {
            getAllLocations(nums)
        }
    }


    private fun getAllLocations(pageNo: Int) {

        //1 Create a Coroutine scope using a job to be able to cancel when needed
        val mainActivityJob = Job()

        //2 Handle exceptions if any

        binding.loadingPB.visibility = View.VISIBLE
        //3 the Coroutine runs using the Main (UI) dispatcher
        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            //4
            val resultList = RetrieverWithToken().getAllLocations(pageNo)
            Log.e("---APiResponse---", resultList.toString())
            //locationList = resultList
            //tempLocationList = resultList
            locationList.addAll(resultList)
            tempLocationList.addAll(resultList)

            if (pageNo == 2) {
                binding.loadingPB.visibility = View.GONE
            }

            setListData()

            for (item in resultList) {
                if (!cityList.contains(item.acf.city)) {
                    cityList.add(item.acf.city)
                }

                for (bItem in item.acf.business_type) {
                    if (!businessList.contains(bItem)) {
                        businessList.add(bItem)
                    }
                }
            }

            onMapReady(mMap)
            Log.e("--Cities--", cityList.toString())
        }
    }

    private fun setListData() {
        onMapReady(mMap)
        lAdapter = activity?.let {
            LocationAdapter(
                it.applicationContext,
                locationList
            )
        }

        lAdapter?.detailItem?.observe(viewLifecycleOwner, Observer {
            openDetailPage(it)
        })

        binding.rcvLocation.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = lAdapter
        }
    }


    private fun showFilterDialog() {
        val dialog = BottomSheetDialog(activity as MainActivity)
        dialog.setContentView(R.layout.bottomsheet_filter)

        val btnBusiness = dialog.findViewById<Button>(R.id.btn_all_businesses)
        val btnCities = dialog.findViewById<Button>(R.id.btn_all_cities)
        val btnApply = dialog.findViewById<Button>(R.id.btn_apply)
        val btnreset = dialog.findViewById<Button>(R.id.btn_reset)

        btnBusiness?.setOnClickListener {
            showBusinessListAlert("All Business types", businessList, btnBusiness)
        }

        btnCities?.setOnClickListener {
            showCityListAlert("All Cities", cityList, btnCities)
        }

        btnApply?.setOnClickListener {
            locationList = tempLocationList
            if (isReset) {
                isReset = false
                loadLocation()
            } else {
                if (selectedBusiness.count() > 0) {
                    filterWithBusiness(selectedBusiness)
                }

                if (selectedCity.count() > 0) {
                    filterWithCity(selectedCity)
                }
            }
        }

        btnreset?.setOnClickListener {
            btnBusiness?.text = "All Business types"
            btnCities?.text = "All Cities"
            isReset = true
        }

        dialog.show()
    }


    private fun showSortDialog() {
        val dialog = BottomSheetDialog(activity as MainActivity)
        dialog.setContentView(R.layout.bottomsheet_sort)

        val btnNew = dialog.findViewById<Button>(R.id.btn_new_listing)
        val btnAsc = dialog.findViewById<Button>(R.id.btn_asc)
        val btnDsc = dialog.findViewById<Button>(R.id.btn_dsc)

        btnNew?.setOnClickListener {
            btnNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_radio_check, 0)
            btnAsc?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_radio_uncheck, 0)
            btnDsc?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_radio_uncheck, 0)
            sortNewList()
        }

        btnAsc?.setOnClickListener {
            btnAsc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_radio_check, 0)
            btnNew?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_radio_uncheck, 0)
            btnDsc?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_radio_uncheck, 0)
            sortAsc()
        }

        btnDsc?.setOnClickListener {
            btnDsc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_radio_check, 0)
            btnAsc?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_radio_uncheck, 0)
            btnNew?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_radio_uncheck, 0)
            sortDsc()
        }

        dialog.show()
    }


    private fun sortNewList() {
        loadLocation()
    }

    private fun sortAsc() {
        locationList.sortBy { it.title.rendered }
        setListData()
    }

    private fun sortDsc() {
        locationList.sortByDescending { it.title.rendered }
        setListData()
    }

    private fun showCityListAlert(title: String, list: ArrayList<String>, btn: Button) {
        val builderSingle = AlertDialog.Builder(activity as MainActivity)
        builderSingle.setTitle(title)

        val arrayAdapter =
            ArrayAdapter<String>(activity as MainActivity, android.R.layout.select_dialog_item)
        arrayAdapter.addAll(list)

        builderSingle.setAdapter(
            arrayAdapter
        ) { dialog, which ->
            val strName = arrayAdapter.getItem(which)
            btn.text = strName
            selectedCity = strName.toString()
        }
        builderSingle.show()
    }

    private fun showBusinessListAlert(title: String, list: ArrayList<String>, btn: Button) {
        val builderSingle = AlertDialog.Builder(activity as MainActivity)
        builderSingle.setTitle(title)

        val arrayAdapter =
            ArrayAdapter<String>(activity as MainActivity, android.R.layout.select_dialog_item)
        arrayAdapter.addAll(list)

        builderSingle.setAdapter(
            arrayAdapter
        ) { dialog, which ->
            val strName = arrayAdapter.getItem(which)
            btn.text = strName
            selectedBusiness = strName.toString()
        }
        builderSingle.show()
    }


    private fun filterWithCity(cityName: String) {
        locationList = locationList.filter { it ->
            it.acf.city == cityName
        } as ArrayList<LocationResponseItem>

        setListData()
    }

    private fun filterWithBusiness(type: String) {
        locationList = locationList.filter { it ->
            it.acf.business_type.contains(type)
        } as ArrayList<LocationResponseItem>

        setListData()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.let {
            it.clear()
        }

        for (item in locationList) {
            val lat = item.acf.map_location.lat
            val lng = item.acf.map_location.lng
            val address = item.acf.map_location.address
            val sydney = LatLng(lat, lng)
            val mTitle = Html.fromHtml(item.title.rendered)
            mMap.addMarker(
                MarkerOptions().position(sydney).title(mTitle.toString()).snippet(address)
                    .icon(context?.let {
                        BitmapFromVector(it, R.drawable.marker)
                    })
            )
            mMap.animateCamera(CameraUpdateFactory.zoomTo(25.0f))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


            // add onclick listener for marker info on map
            mMap.setOnInfoWindowClickListener {

                val marker: Marker = it
                for (item in locationList) {
                    if (Html.fromHtml(marker.title).toString() == mTitle.toString()) {
                        selectedItem = item
                    }
                }

                val intent = Intent(activity, LocationDetailsActivity::class.java)
                startActivity(intent)
            }

        }

    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun openDetailPage(locationResponseItem: LocationResponseItem) {
        selectedItem = locationResponseItem
        val intent = Intent(activity, LocationDetailsActivity::class.java)
        startActivity(intent)
    }
}