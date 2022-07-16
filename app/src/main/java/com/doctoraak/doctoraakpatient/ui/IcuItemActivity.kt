package com.doctoraak.doctoraakpatient.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.ActivityIcuItemBinding
import com.doctoraak.doctoraakpatient.model.Icu
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.utils.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class IcuItemActivity : BaseActivity(){

    val colors = arrayOf(R.drawable.circle_text_shape_color1,
        R.drawable.circle_text_shape_color3 ,R.drawable.circle_text_shape_color2
        ,R.drawable.circle_text_shape_color4)

    lateinit var googleMap : GoogleMap

    private lateinit var icu : Icu
    private lateinit var binding : ActivityIcuItemBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_icu_item)
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        val json = intent.getStringExtra(getString(R.string.icu_item_key)) ?: ""
        icu = Utils.convertJsonToObject(json)
        binding.mapView.onCreate(savedInstanceState)
        setUpMap()

        binding.tvName.text = Utils.getTextForAppLanguage(icu.name,icu.name_ar,icu.name_fr)
        try { binding.rbRate.rating = (icu.rate).toFloat() } catch (e: Exception) { }
        binding.tvDescription.text = Utils.getTextForAppLanguage(icu.description
            ,icu.description_ar,icu.description_fr)

        binding.tvAvailableSeat.text = (icu.bed_number).toString()

        val city = Utils.getCities().data[icu.city]
        val area = Utils.getArea().data[icu.area]

        binding.tvCity.text = Utils.getTextForAppLanguage(city.name,city.name_ar,city.name_fr)
        binding.tvArea.text = Utils.getTextForAppLanguage(area.name,area.name_ar,area.name_fr)

        binding.ivImage.text = Utils.getTextForAppLanguage(icu.name,icu.name_ar,icu.name_fr)
            .substring(0 , 1).toUpperCase(Locale.US)
        binding.ivImage.setBackgroundResource(colors[(0..3).random()])

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpMap() {

        binding.mapView.onResume()

        try {
            MapsInitializer.initialize(this.getApplicationContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }


        binding.mapView.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap

            val location = LatLng(icu.lat, icu.lng)
            googleMap.addMarker(MarkerOptions().position(location)
                .title(Utils.getTextForAppLanguage(icu.name,icu.name_ar,icu.name_fr)))
            googleMap.isIndoorEnabled = true
            val uiSettings = mMap.uiSettings
            uiSettings.isIndoorLevelPickerEnabled = true
            uiSettings.isMapToolbarEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isZoomControlsEnabled = true

            val cameraPosition = CameraPosition.Builder().target(location).zoom(12f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        })

    }


    public override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    public override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}
