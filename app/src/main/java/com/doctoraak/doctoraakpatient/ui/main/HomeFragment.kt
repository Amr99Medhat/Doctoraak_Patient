package com.doctoraak.doctoraakpatient.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.FragmentHomeBinding
import com.doctoraak.doctoraakpatient.model.FirebaseTokenResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.repository.remote.ApiOthers
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener
import com.doctoraak.doctoraakpatient.ui.BaseFragment
import com.doctoraak.doctoraakpatient.ui.favouriteDoctor.FavoriteDoctorActivity
import com.doctoraak.doctoraakpatient.ui.findServcie.FindServiceActivity
import com.doctoraak.doctoraakpatient.ui.paymentDetails.PaymentDetailsActivity
import com.doctoraak.doctoraakpatient.ui.profile.ProfileActivity
import com.doctoraak.doctoraakpatient.utils.Constants
import com.doctoraak.doctoraakpatient.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId



class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

         binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (SessionManager.isLogIn()) {
            sendUpdatedToken()
        }
//        val logo = requireActivity().findViewById<ImageView>(R.id.iv_oncare_logo)
//        val user = SessionManager.returnUserInfo()
//        if (user != null) {
//            if (user.insurance!!.id == 1) {
//                logo.visibility = View.VISIBLE
//                if (user.patient_name == "" || user.phone2 == "") {
//                    val intent = Intent(requireContext(), ProfileActivity::class.java)
//                    intent.putExtra(Constants.MISSING_DATA, true)
//                    startActivity(intent)
//                   requireActivity().finish()
//                }
//            }
//        }

        observeData()

        binding.itCvFindService.setOnClickListener {
            launchIntent(requireActivity(), FindServiceActivity::class.java)
        }

        binding.itCvFavoriteDoctor.setOnClickListener {
            if (SessionManager.isLogIn()) {
                launchIntent(requireActivity(), FavoriteDoctorActivity::class.java)
            } else {
                showLoginFirstDialog(getString(R.string.login_first))
            }
        }

        binding.tvDetails.setOnClickListener {
            val intent = Intent(requireContext(), PaymentDetailsActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun sendUpdatedToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result?.token
                ApiOthers.updateFirebaseToken(token!!,
                    Utils.getUserId(),
                    "PATIENT",
                    Utils.getApiToken(),
                    object : BaseResponseListener<FirebaseTokenResponse> {
                        override fun onSuccess(model: FirebaseTokenResponse) {}

                        override fun onLoading() {}

                        override fun onErrorMsg(errorMsg: String) {}

                        override fun onError(errorMsgId: Int) {}
                    })
            })
    }

    override fun onResume() {
        super.onResume()
        if (SessionManager.isLogIn()) {
            setImageAndUsername()
            if (Utils.checkInternetConnection(requireActivity(), binding.nestedScrollView)) {
                //get favourite list ids and save it to shared preference and later
                //when patient search for doctors check for every doctor id if found
                //in this list and based on this, mark this doctor as favourite or not
                viewModel.getFavouriteDoctors(Utils.getUserId(), Utils.getApiToken())

            }
        }

        val logo = requireActivity().findViewById<ImageView>(R.id.iv_oncare_logo)

        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
                if (user.patient_name == "" || user.phone2 == "") {
                    startActivity(Intent(requireContext(), ProfileActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

    }

    private fun setImageAndUsername() {
        Glide.with(this).load(Utils.getUser().photo).placeholder(R.drawable.ic_face)
            .error(R.drawable.ic_face).into(binding.ivUserImage)
        binding.tvUserName.text =  Utils.getUserName()
    }

    private fun observeData() {

        viewModel.favResposne.observe(requireActivity()
        ) { t -> //save ids to sharedpref
            Utils.saveFavDoctorsIds(t!!)
        }

        viewModel.isLoading.observe(requireActivity()
        ) { }

        viewModel.errorMsg.observe(requireActivity()
        ) { }

        viewModel.errorInt.observe(requireActivity()) { }

    }


}