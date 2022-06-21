package com.doctoraak.doctoraakpatient.ui.myOrders

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.ReservationsAdapter
import com.doctoraak.doctoraakpatient.databinding.MyOrdersPagerItemBinding
import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.model.RatingResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.Reservation
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.Utils.Companion.showSnackbar
import com.doctoraak.doctoraakpatient.utils.getMsg
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DoctorOrdersFragment : Fragment()
{
    private lateinit var adapter: ReservationsAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(MyOrdersViewModel::class.java) }

    private lateinit var binding: MyOrdersPagerItemBinding
    private var doctorRate = -1
    private var orderPosition = -1

    companion object {
        @JvmStatic
        fun newInstance() = DoctorOrdersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context)
            , R.layout.my_orders_pager_item, container, false)

        with(binding.rvMyOrders)
        {
            layoutManager = LinearLayoutManager(context)
            this@DoctorOrdersFragment.adapter = ReservationsAdapter(arrayListOf(), context)
            adapter = this@DoctorOrdersFragment.adapter
        }

        observeData()

        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            getAllOrders()
        }

        adapter.setOnItemMapListener(object : ReservationsAdapter.ClickItemMap {
            override fun onClick(model: Reservation, position: Int, aView: View) {
                showMapDialog(model)
            }
        })

        adapter.setOnItemRateListener(object : ReservationsAdapter.ClickItemRate {
            override fun onClick(model: Reservation, position: Int, aView: View) {
                showRateDialog(model)
            }
        })

        adapter.setOnItemCancelOrderListener(object : ReservationsAdapter.ClickCancelOrder {
            override fun onClick(model: Reservation, position: Int, aView: View) {
                orderPosition = position
                cancelOrderDialog(model)
            }
        })

        return binding.root
    }

    val getAllOrders = { viewModel.getReservations(Utils.getUserId(), Utils.getUser().apiToken
        , {if (it) {
            binding.loading.visibility = View.VISIBLE
        }else{
            binding.loading.visibility = View.INVISIBLE
        }}
        , {if (!it.isNullOrEmpty()){
            showSnackbar(binding.viewGroup , it)
            binding.loading.visibility = View.INVISIBLE
        }}, {if (it != 0){
            showSnackbar(binding.viewGroup , getString(it))
            binding.loading.visibility = View.INVISIBLE
        }})
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.doctorReservation.value == null) {
            getAllOrders()
        }else{
            adapter.setData(viewModel.doctorReservation.value!!.data)
        }
    }


    private fun showRateDialog(model: Reservation) {
        val builder = AlertDialog.Builder(activity!!)

        val inflater = this.getLayoutInflater()
        val mView = inflater.inflate(R.layout.rate_dialog, null)
        val ratingBar =
            mView.findViewById<AppCompatRatingBar>(R.id.rb_rate)
        val ok = mView.findViewById<TextView>(R.id.ok)
        val cancel = mView.findViewById<TextView>(R.id.cancel)
        builder.setView(mView)
        val alertDialog = builder.create()
        ok.setOnClickListener {
            doctorRate = ratingBar.rating.toInt()
            if (doctorRate != -1 && doctorRate != 0) {
                if (Utils.checkInternetConnection(activity!!,binding.viewGroup )) {
                    binding.loading.visibility = View.VISIBLE
                    viewModel.rateDoctor(Utils.getUserId(), model.clinic.doctor_id, doctorRate
                        , Utils.getApiToken(), "PATIENT"
                        , {if (it) {
                            binding.loading.visibility = View.VISIBLE
                        }else{
                            binding.loading.visibility = View.INVISIBLE
                        }}
                        , {if (!it.isNullOrEmpty()){
                            showSnackbar(binding.viewGroup , it)
                            binding.loading.visibility = View.INVISIBLE
                        }}, {if (it != 0){
                            showSnackbar(binding.viewGroup , getString(it))
                            binding.loading.visibility = View.INVISIBLE
                        }}
                    )
                }
            } else {
                showSnackbar(binding.viewGroup , getString(R.string.set_rate_for_the_docor_msg))
            }
            alertDialog.dismiss()
        }

        cancel.setOnClickListener { alertDialog.cancel() }
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun cancelOrderDialog(model: Reservation) {
        val builder = AlertDialog.Builder(activity!!)

        val inflater = this.getLayoutInflater()
        val mView = inflater.inflate(R.layout.cancel_order_dialog, null)
        val ok = mView.findViewById<TextView>(R.id.ok)
        val cancel = mView.findViewById<TextView>(R.id.cancel)
        val notes = mView.findViewById<TextView>(R.id.et_notes)
        builder.setView(mView)
        val alertDialog = builder.create()
        ok.setOnClickListener {
            if (notes.text.isNotEmpty()){
                if (Utils.checkInternetConnection(activity!!,binding.viewGroup)){
                    viewModel.cancelOrder(Utils.getApiToken() , Utils.getUserId(),model.id
                        ,"DOCTOR", notes.text.toString()
                        , {if (it) {
                            binding.loading.visibility = View.VISIBLE
                        }else{
                            binding.loading.visibility = View.INVISIBLE
                        }}
                        , {if (!it.isNullOrEmpty()){
                            showSnackbar(binding.viewGroup , it)
                            binding.loading.visibility = View.INVISIBLE
                        }}, {if (it != 0){
                            showSnackbar(binding.viewGroup , getString(it))
                            binding.loading.visibility = View.INVISIBLE
                        }})
                }
            }else{
                showSnackbar(binding.viewGroup , getString(R.string.enter_your_notes_mandotry))
            }
            alertDialog.dismiss()
        }

        cancel.setOnClickListener { alertDialog.dismiss() }
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun showMapDialog(model: Reservation) {
        var gm: GoogleMap
        val builder = AlertDialog.Builder(activity!!)

        val inflater = this.getLayoutInflater()
        val mView =
            inflater.inflate(com.doctoraak.doctoraakpatient.R.layout.show_address_dialog, null)

        val mapview: MapView =
            mView.findViewById<MapView>(com.doctoraak.doctoraakpatient.R.id.mapView)
        val addressText =
            mView.findViewById<TextView>(com.doctoraak.doctoraakpatient.R.id.tv_address_map)
        val googleMapApp =
            mView.findViewById<ImageView>(com.doctoraak.doctoraakpatient.R.id.iv_google_map)
        googleMapApp.setOnClickListener { Utils.gotoMapDirection(activity!!
            , model.clinic.latt.toDouble(),model.clinic.lang.toDouble()) }
        addressText.text = Utils.getAddress(model.clinic.area.toInt(), model.clinic.city.toInt())

        builder.setView(mView)
        val alertDialog = builder.create()

        MapsInitializer.initialize(activity!!)
        mapview.onCreate(alertDialog.onSaveInstanceState())
        mapview.onResume()

        mapview.getMapAsync(OnMapReadyCallback { googleMap ->
            gm = googleMap
            val location = LatLng(model.clinic.latt.toDouble(), model.clinic.lang.toDouble())
            val cameraPosition = CameraPosition.Builder().target(location).zoom(10f).build()
            googleMap.addMarker(MarkerOptions().position(location))
            gm.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        })

        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun observeData()
    {
        viewModel.cancelOrderResponse.observe(this, object : Observer<BaseResponse?> {
            override fun onChanged(t: BaseResponse?) {
                binding.loading.visibility = View.INVISIBLE
                if (orderPosition != -1){
                    adapter.deleteListItem(orderPosition)
                    orderPosition = -1
                }
                showSnackbar(binding.viewGroup , t!!.getMsg())
            }
        })

        viewModel.rateDoctorResposne.observe(this, object : Observer<RatingResponse> {
            override fun onChanged(t: RatingResponse?) {
                doctorRate = -1
                showSnackbar(binding.viewGroup ,t!!.getMsg())
                binding.loading.visibility = View.INVISIBLE
            }
        })

        viewModel.doctorReservation.observe(this, Observer {
            if (it.data.size > 0) {
                adapter.setData(it.data)
                binding.emptyList.visibility = View.INVISIBLE
            }else{
                binding.emptyList.visibility = View.VISIBLE
            }
            binding.loading.visibility = View.INVISIBLE
        })
    }




}