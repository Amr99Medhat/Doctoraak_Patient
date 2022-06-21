package com.doctoraak.doctoraakpatient.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.LabWorkingHoursAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityLapItemBinding
import com.doctoraak.doctoraakpatient.model.Lab
import com.doctoraak.doctoraakpatient.model.LabWorkingHour
import com.doctoraak.doctoraakpatient.ui.labOrder.LabOrderActivity
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.toBoolean
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LapItemActivity : BaseActivity()
{

    private lateinit var binding : ActivityLapItemBinding
    private lateinit var lab: Lab
    private lateinit var adapter : LabWorkingHoursAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_lap_item)

        setRecyclerviewLinearLayout(binding.rvWorkingDate)
        addListDivider(binding.rvWorkingDate)

        lab = Utils.convertJsonToObject(intent.getStringExtra(getString(R.string.lab_item))!!)

        val list = getActiveWorkingHours(lab.working_hours)
        adapter = LabWorkingHoursAdapter(list , this)
        binding.rvWorkingDate.adapter = adapter
        InitializUI()
        binding.btnBook.setOnClickListener {
            val intent = Intent(this  , LabOrderActivity::class.java)
            intent.putExtra(getString(R.string.lab_id_key) , lab.id)
            startActivity(intent)
        }
    }

    private fun getActiveWorkingHours(workingHours: ArrayList<LabWorkingHour>)
            : ArrayList<LabWorkingHour> {
        val list = ArrayList<LabWorkingHour>()
        for (item in workingHours)
            if (item.active.toBoolean())
                list.add(item)
        return list
    }

    private fun InitializUI() {
        Glide.with(this).load(lab.photo).error(R.drawable.ic_lab).placeholder(R.drawable.ic_lab)
            .into(binding.ivPhoto)

        binding.tvName.text = Utils.getTextForAppLanguage(lab.name,lab.nameAr,lab.nameFr)
        binding.tvAddress.text = Utils.getAddress(lab.area , lab.city)

        binding.ivAddressMap.setOnClickListener {
            showMapDialog(lab)
        }

    }

    private fun showMapDialog(model: Lab) {
        var gm : GoogleMap
        val builder = AlertDialog.Builder(this)

        val inflater = this.getLayoutInflater()
        val mView = inflater.inflate(com.doctoraak.doctoraakpatient.R.layout.show_address_dialog, null)

        val mapview : MapView = mView.findViewById<MapView>(com.doctoraak.doctoraakpatient.R.id.mapView)
        val addressText = mView.findViewById<TextView>(com.doctoraak.doctoraakpatient.R.id.tv_address_map)
        val googleMapApp =
            mView.findViewById<ImageView>(com.doctoraak.doctoraakpatient.R.id.iv_google_map)
        googleMapApp.setOnClickListener { Utils.gotoMapDirection(this
            , model.latt.toDouble(),model.lang.toDouble()) }

        addressText.text = ""

        builder.setView(mView)
        val alertDialog = builder.create()

        MapsInitializer.initialize(this@LapItemActivity)
        mapview.onCreate(alertDialog.onSaveInstanceState())
        mapview.onResume()

        mapview.getMapAsync(OnMapReadyCallback { googleMap ->
            gm = googleMap
            val location = LatLng(model.latt.toDouble(), model.lang.toDouble())
            val cameraPosition = CameraPosition.Builder().target(location).zoom(12f).build()
            googleMap.addMarker(MarkerOptions().position(location))
            gm.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        })

        alertDialog.show()
    }
}
