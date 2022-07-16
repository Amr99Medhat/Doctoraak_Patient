package com.doctoraak.doctoraakpatient.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.RadiologyWorkingHoursAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityRadiologyItemBinding
import com.doctoraak.doctoraakpatient.model.Radiology
import com.doctoraak.doctoraakpatient.model.RadiologyWorkingHour
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.radiologyOrder.RadiologyOrderActivity
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.toBoolean
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class RadiologyItemActivity : BaseActivity() {

    private lateinit var binding: ActivityRadiologyItemBinding
    private lateinit var radiology: Radiology
    private lateinit var adapter: RadiologyWorkingHoursAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_radiology_item)
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        setRecyclerviewLinearLayout(binding.rvWorkingDate)

        radiology = Utils.convertJsonToObject(intent.getStringExtra(getString(R.string.radiology_item_key)) ?:"")

        val list = getActiveWorkingHours(radiology.working_hours)
        adapter = RadiologyWorkingHoursAdapter(list, this)
        binding.rvWorkingDate.adapter = adapter

        InitializUI()
        binding.btnBook.setOnClickListener {
            val intent = Intent(this, RadiologyOrderActivity::class.java)
            intent.putExtra(getString(R.string.radiology_id_key) , radiology.id)
            startActivity(intent)
        }
        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
            }
        }

    }

    private fun getActiveWorkingHours(workingHours: ArrayList<RadiologyWorkingHour>) : ArrayList<RadiologyWorkingHour> {
        val list = ArrayList<RadiologyWorkingHour>()
        for (item in workingHours)
            if (item.active.toBoolean())
                list.add(item)
        return list
    }

    private fun InitializUI() {
        Glide.with(this).load(radiology.photo).error(R.drawable.ic_radiology)
            .placeholder(R.drawable.ic_radiology)
            .into(binding.ivPhoto)

        binding.tvName.text =
            Utils.getTextForAppLanguage(radiology.name, radiology.name_ar, radiology.name_fr)
        binding.tvAddress.text = Utils.getAddress(radiology.area, radiology.city)

        binding.ivAddressMap.setOnClickListener {
            showMapDialog(radiology)
        }

    }

    private fun showMapDialog(model: Radiology) {
        var gm : GoogleMap
        val builder = AlertDialog.Builder(this)

        val inflater = this.getLayoutInflater()
        val mView = inflater.inflate(R.layout.show_address_dialog, null)

        val mapview : MapView = mView.findViewById<MapView>(R.id.mapView)
        val addressText = mView.findViewById<TextView>(R.id.tv_address_map)
        val googleMapApp = mView.findViewById<ImageView>(R.id.iv_google_map)
        googleMapApp.setOnClickListener { Utils.gotoMapDirection(this
            , model.latt.toDouble(),model.lang.toDouble()) }

        addressText.text = ""

        builder.setView(mView)
        val alertDialog = builder.create()

        MapsInitializer.initialize(this@RadiologyItemActivity)
        mapview.onCreate(alertDialog.onSaveInstanceState())
        mapview.onResume()

        mapview.getMapAsync(OnMapReadyCallback { googleMap ->
            gm = googleMap
            gm.setMinZoomPreference(10f)
            val location = LatLng(model.latt.toDouble(), model.lang.toDouble())
            val cameraPosition = CameraPosition.Builder().target(location).zoom(12f).build()
            googleMap.addMarker(MarkerOptions().position(location))
            gm.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        })

        alertDialog.show()
    }

}
