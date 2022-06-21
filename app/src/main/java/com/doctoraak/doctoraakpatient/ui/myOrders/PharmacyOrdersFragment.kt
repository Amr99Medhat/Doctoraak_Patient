package com.doctoraak.doctoraakpatient.ui.myOrders

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.PharmacyOrdersAdapter
import com.doctoraak.doctoraakpatient.databinding.MyOrdersPagerItemBinding
import com.doctoraak.doctoraakpatient.model.BaseResponse
import com.doctoraak.doctoraakpatient.model.userOrdersModels.PharmacyOrders
import com.doctoraak.doctoraakpatient.ui.ViewPhotoFragment
import com.doctoraak.doctoraakpatient.utils.OrderType
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.Utils.Companion.showSnackbar
import com.doctoraak.doctoraakpatient.utils.getMsg

class PharmacyOrdersFragment : Fragment()
{
    private lateinit var adapter: PharmacyOrdersAdapter
    private val viewModel by lazy { ViewModelProviders.of(activity!!).get(MyOrdersViewModel::class.java) }
    private lateinit var binding: MyOrdersPagerItemBinding
    private var orderPosition = -1
    companion object {
        @JvmStatic
        fun newInstance() = PharmacyOrdersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context)
            , R.layout.my_orders_pager_item, container, false)

        with(binding.rvMyOrders)
        {
            layoutManager = LinearLayoutManager(context)
            this@PharmacyOrdersFragment.adapter = PharmacyOrdersAdapter(arrayListOf(), context)
            adapter = this@PharmacyOrdersFragment.adapter
        }

        observeData()

        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            getAllOrders()
        }



        adapter.setOnItemCancelOrderListener(object : PharmacyOrdersAdapter.ClickCancelOrder {
            override fun onClick(model: PharmacyOrders, position: Int, aView: View) {
                orderPosition = position
                cancelOrderDialog(model)
            }
        })

        adapter.setOnItemViewPhotoListener(object : PharmacyOrdersAdapter.ClickViewPhoto {
            override fun onClick(url: String) {
                val dialog = ViewPhotoFragment.newInstance(url , OrderType.PHARMACY.toString())
                dialog.show(activity!!.supportFragmentManager , "tag")
            }
        })
        return binding.root
    }
    val getAllOrders = { viewModel.getMyOrdersPharmacy(Utils.getUserId(), Utils.getUser().apiToken
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
        }}) }

    private fun cancelOrderDialog(model: PharmacyOrders) {
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
                        ,"PHARMACY",notes.text.toString()
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

    private fun observeData()
    {
        viewModel.pharmacyReservation.observe(this, Observer {
            if (it.data.size > 0) {
                adapter.setData(it.data)
                binding.emptyList.visibility = View.INVISIBLE
            }else{
                binding.emptyList.visibility = View.VISIBLE
            }
            binding.loading.visibility = View.INVISIBLE
        })

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
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.pharmacyReservation.value == null) {
            getAllOrders()
        } else {
            adapter.setData(viewModel.pharmacyReservation.value!!.data)
        }
    }

}