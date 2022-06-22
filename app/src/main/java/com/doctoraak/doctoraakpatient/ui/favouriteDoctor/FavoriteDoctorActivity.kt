package com.doctoraak.doctoraakpatient.ui.favouriteDoctor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.adapters.FavouriteDoctorAdapter
import com.doctoraak.doctoraakpatient.databinding.ActivityFavoriteDoctorBinding
import com.doctoraak.doctoraakpatient.model.Clinic
import com.doctoraak.doctoraakpatient.model.ClinicsResponse
import com.doctoraak.doctoraakpatient.model.FavDoctorResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.bookDoctor.BookDoctorActivity
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.getMsg

class FavoriteDoctorActivity : BaseActivity()
{
    private var deletedItemPosition = -1
    private lateinit var binding: ActivityFavoriteDoctorBinding
    private lateinit var adapter : FavouriteDoctorAdapter

    private val viewModel: FavoriteDoctorViewModel by lazy {
        ViewModelProviders.of(this).get(
            FavoriteDoctorViewModel::class.java
        )
    }

    private val DELEED_ITEM_POS = "DELEED_ITEM_POS"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(DELEED_ITEM_POS , deletedItemPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        if (savedInstanceState != null) {
            deletedItemPosition = savedInstanceState.getInt(DELEED_ITEM_POS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_doctor)
        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user.insuranceId==1){
            logo.visibility= View.VISIBLE
        }
        else{
            logo.visibility= View.GONE
        }

        setRecyclerviewLinearLayout(binding.rvFavDoctors)
        adapter = FavouriteDoctorAdapter(ArrayList<Clinic>() , this)
        binding.rvFavDoctors.adapter = adapter

        observeData()
        getFavuriteDoctors()

        adapter.setOnItemClickUnfavListener(object : FavouriteDoctorAdapter.ClickUnfavDoctor {
            override fun onClick(model: Clinic, position: Int, aView: View) {
                if (Utils.checkInternetConnection(this@FavoriteDoctorActivity,binding.clFavDoctor)) {
                    viewModel.favDoctor(Utils.getUserId() , model.doctor_id , Utils.getApiToken())
                    deletedItemPosition = position
                }
            }
        })

        adapter.setOnItemClickListener(object : FavouriteDoctorAdapter.ClickListenerView {
            override fun onClick(model: Clinic, aView: View) {
                val intent = Intent(this@FavoriteDoctorActivity
                    , BookDoctorActivity::class.java)
                intent.putExtra(getString(com.doctoraak.doctoraakpatient.R.string.clinic_item_key) , Utils.convertObjectToJson(model))
                startActivity(intent)
            }
        })

        binding.srlRefresh.setOnRefreshListener {
            getFavuriteDoctors()
            binding.srlRefresh.isRefreshing = false
        }
    }

    private fun getFavuriteDoctors() {
        if (Utils.checkInternetConnection(this , binding.clFavDoctor)) {
            viewModel.getFavouriteDoctors(Utils.getUserId() , Utils.getApiToken())
        }
    }

    fun deleteDoctor(pos : Int){
        if (pos != -1) {
            adapter.deleteItem(pos)
            adapter.notifyItemRemoved(pos)
            adapter.notifyItemRangeChanged(pos , adapter.itemCount - pos)
            deletedItemPosition = -1
        }
    }

    private fun observeData() {

        viewModel.favResposne.observe(this, object : Observer<ClinicsResponse> {
            override fun onChanged(t: ClinicsResponse?) {
                if (t!!.data.size == 0){
                    showSnackbar(binding.clFavDoctor , getString(R.string.you_dont_have_fav_docs))
                    binding.emptyList.visibility = View.VISIBLE
                }
                binding.loading!!.visibility = View.INVISIBLE
                adapter.setData(t.data)
            }
        })

        viewModel.favDoctorResposne.observe(this, object : Observer<FavDoctorResponse> {
            override fun onChanged(t: FavDoctorResponse?) {
                deleteDoctor(deletedItemPosition)
                binding.loading!!.visibility = View.INVISIBLE
                showSnackbar(binding.clFavDoctor , t!!.getMsg())
            }
        })

        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {

                if (t!!)
                    binding.loading.visibility = View.VISIBLE
                else
                    binding.loading.visibility = View.INVISIBLE
            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {

                if (!t.isNullOrEmpty()) {
                    showSnackbar(binding.clFavDoctor , t)
                    binding.loading.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {

                if (t!! != 0) {
                    showSnackbar(binding.clFavDoctor , getString(t))
                    binding.loading.visibility = View.INVISIBLE
                }
            }
        })

    }
}
