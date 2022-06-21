package com.doctoraak.doctoraakpatient.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.ActivityIncubationItemBinding
import com.doctoraak.doctoraakpatient.model.Incubation
import com.doctoraak.doctoraakpatient.utils.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class IncubationItemActivity : BaseActivity()
{

    val colors = arrayOf(R.drawable.circle_text_shape_color1,
        R.drawable.circle_text_shape_color3 ,R.drawable.circle_text_shape_color2
        ,R.drawable.circle_text_shape_color4)
    lateinit var googleMap : GoogleMap
    private lateinit var incubationItem : Incubation
    private lateinit var binding : ActivityIncubationItemBinding


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_incubation_item)

        val json = intent.getStringExtra(getString(R.string.incubation_item_key))
        incubationItem = Utils.convertJsonToObject(json!!)

        binding.mapView.onCreate(savedInstanceState)
        setUpMap()

        binding.tvName.text = Utils.getTextForAppLanguage(incubationItem.name
            ,incubationItem.name_ar,incubationItem.name_fr)

        try { binding.rbRate.rating = (incubationItem.rate).toFloat()
        } catch (e: Exception) {}
        binding.tvDescription.text = Utils.getTextForAppLanguage(incubationItem.description
            ,incubationItem.description_ar,incubationItem.description_fr)

        binding.tvAvailableSeat.text = (incubationItem.bed_number).toString()

        val city = Utils.getCities().data[incubationItem.city]
        val area = Utils.getArea().data[incubationItem.area]
        binding.tvCity.text = Utils.getTextForAppLanguage(city.name,city.name_ar,city.name_fr)
        binding.tvArea.text = Utils.getTextForAppLanguage(area.name,area.name_ar,area.name_fr)


        binding.ivImage.text = Utils.getTextForAppLanguage(incubationItem.name
            ,incubationItem.name_ar,incubationItem.name_fr)
            .substring(0 , 1).toUpperCase(Locale.US)
        binding.ivImage.setBackgroundResource(colors[(0..3).random()])
    }


    private fun setUpMap() {

        binding.mapView.onResume()

        try {
            MapsInitializer.initialize(this.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.mapView.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap

            val location = LatLng(incubationItem.lat, incubationItem.lng)
            googleMap.addMarker(
                MarkerOptions().position(location)
                    .title(Utils.getTextForAppLanguage
                        (incubationItem.name,incubationItem.name_ar,incubationItem.name_fr)))
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
