package com.doctoraak.doctoraakpatient.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
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
import com.doctoraak.doctoraakpatient.ui.myOrders.MyOrdersActivity
import com.doctoraak.doctoraakpatient.ui.notification.NotificationActivity
import com.doctoraak.doctoraakpatient.ui.payment.PaymentActivity
import com.doctoraak.doctoraakpatient.ui.paymentDetails.PaymentDetailsActivity
import com.doctoraak.doctoraakpatient.ui.profile.ProfileActivity
import com.doctoraak.doctoraakpatient.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.main_header.view.*


class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val rotateOpen:Animation by lazy {AnimationUtils.loadAnimation(requireContext(),R.anim.contactus_rotate_open_anim)}
    private val rotateClose:Animation by lazy {AnimationUtils.loadAnimation(requireContext(),R.anim.contactus_rotate_close_anim)}
    private val rotateOpenLeft:Animation by lazy {AnimationUtils.loadAnimation(requireContext(),R.anim.contactus_from_left_anim)}
    private val rotateCloseRight:Animation by lazy {AnimationUtils.loadAnimation(requireContext(),R.anim.contactus_from_right_anim)}
    private var clicked = false



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        if (SessionManager.isLogIn()) {
            sendUpdatedToken()

        }

        observeData()
        setListeners()
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


        binding.itCvMyorders.setOnClickListener {
            if (SessionManager.isLogIn()) {
                launchIntent(requireActivity(), MyOrdersActivity::class.java)
            } else {
                showLoginFirstDialog(getString(R.string.login_first))
            }
        }

        binding.tvPayment.setOnClickListener {
            if (SessionManager.isLogIn()) {
                startActivity(Intent(requireActivity(), PaymentActivity::class.java))
            } else {
                showLoginFirstDialog(getString(R.string.login_first))
            }
        }

        binding.tvDetails.setOnClickListener {
            startActivity(Intent(requireActivity(), PaymentDetailsActivity::class.java))
        }
        binding.toolBar.iv_notification.setOnClickListener {
            if (SessionManager.isLogIn()) {
                launchIntent(requireActivity(), NotificationActivity::class.java)
            } else {
                showLoginFirstDialog(getString(R.string.login_first))
            }
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
        binding.cvUserInformation.visibility=View.VISIBLE
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

    private fun  setListeners(){
        binding.btnContactus.setOnClickListener {
            setVisibility(clicked)
            setAnimation(clicked)
            clicked = !clicked
        }

        val contactInfo = Utils.getContactInfo()
        binding.ivMessanger.setOnClickListener { Utils.openMessanger(requireContext(), "2088455828039695") }
        binding.ivWhatsapp.setOnClickListener {
            Utils.openWhatsApp(requireActivity(),
                "2" + contactInfo.data[3])
        }
        binding.ivPhone.setOnClickListener { Utils.openDialer(requireContext(), contactInfo.data[3]) }
        binding.ivEmail.setOnClickListener { Utils.openEmail(requireContext(), contactInfo.data[2]) }
        binding.ivFacebook.setOnClickListener {
            Utils.openFacebookPage(requireContext(), contactInfo.data[0], "2088455828039695")
        }
        binding.ivWebsite.setOnClickListener { Utils.openWebsite(requireContext(), contactInfo.data[1]) }
    }

    private fun setAnimation(clicked:Boolean) {
        if (!clicked){
            binding.ivEmail.visibility = View.VISIBLE
            binding.ivFacebook.visibility = View.VISIBLE
            binding.ivMessanger.visibility = View.VISIBLE
            binding.ivPhone.visibility = View.VISIBLE
            binding.ivWebsite.visibility = View.VISIBLE
            binding.ivWhatsapp.visibility = View.VISIBLE
        }
        else{
            binding.ivEmail.visibility = View.INVISIBLE
            binding.ivFacebook.visibility = View.INVISIBLE
            binding.ivMessanger.visibility = View.INVISIBLE
            binding.ivPhone.visibility = View.INVISIBLE
            binding.ivWebsite.visibility = View.INVISIBLE
            binding.ivWhatsapp.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(clicked:Boolean) {
        if(!clicked){
            binding.ivEmail.startAnimation(rotateOpenLeft)
            binding.ivFacebook.startAnimation(rotateOpenLeft)
            binding.ivMessanger.startAnimation(rotateOpenLeft)
            binding.ivPhone.startAnimation(rotateOpenLeft)
            binding.ivWebsite.startAnimation(rotateOpenLeft)
            binding.ivWhatsapp.startAnimation(rotateOpenLeft)
            binding.btnContactus.startAnimation(rotateOpen)

        }
        else{
            binding.ivEmail.startAnimation(rotateCloseRight)
            binding.ivFacebook.startAnimation(rotateCloseRight)
            binding.ivMessanger.startAnimation(rotateCloseRight)
            binding.ivPhone.startAnimation(rotateCloseRight)
            binding.ivWebsite.startAnimation(rotateCloseRight)
            binding.ivWhatsapp.startAnimation(rotateCloseRight)
            binding.btnContactus.startAnimation(rotateClose)
        }
    }


}