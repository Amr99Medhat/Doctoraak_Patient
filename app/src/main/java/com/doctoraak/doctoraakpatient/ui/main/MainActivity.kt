package com.doctoraak.doctoraakpatient.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.customView.SweetDialog
import com.doctoraak.doctoraakpatient.databinding.ActivityMainBinding
import com.doctoraak.doctoraakpatient.model.ClinicsResponse
import com.doctoraak.doctoraakpatient.model.FirebaseTokenResponse
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.repository.remote.ApiOthers
import com.doctoraak.doctoraakpatient.repository.remote.BaseResponseListener
import com.doctoraak.doctoraakpatient.ui.AnalyticActivity
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.SignInActivity
import com.doctoraak.doctoraakpatient.ui.favouriteDoctor.FavoriteDoctorActivity
import com.doctoraak.doctoraakpatient.ui.findServcie.FindServiceActivity
import com.doctoraak.doctoraakpatient.ui.myOrders.MyOrdersActivity
import com.doctoraak.doctoraakpatient.ui.notification.NotificationActivity
import com.doctoraak.doctoraakpatient.ui.payment.PaymentActivity
import com.doctoraak.doctoraakpatient.ui.paymentDetails.PaymentDetailsActivity
import com.doctoraak.doctoraakpatient.ui.profile.ProfileActivity
import com.doctoraak.doctoraakpatient.utils.Constants
import com.doctoraak.doctoraakpatient.utils.Utils
import com.doctoraak.doctoraakpatient.utils.hide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.main_header.*
import kotlinx.android.synthetic.main.main_header.view.*

class MainActivity : BaseActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (SessionManager.isLogIn()) {
            sendUpdatedToken()
        }

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)
        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
                if (user.patient_name == "" || user.phone2 == "") {
                    val intent = Intent(applicationContext, ProfileActivity::class.java)
                    intent.putExtra(Constants.MISSING_DATA, true)
                    startActivity(intent)
                    finish()
                }
            }
        }



        setupDrawer()
        observeData()
        binding.mainContent.itCvFindService.setOnClickListener {
            launchIntent(this, FindServiceActivity::class.java)
        }

        binding.mainContent.itCvFavoriteDoctor.setOnClickListener {
            if (SessionManager.isLogIn()) {
                launchIntent(this, FavoriteDoctorActivity::class.java)
            } else {
                showLoginFirstDialog(getString(R.string.login_first))
            }
        }

        binding.mainContent.itCvMyorders.setOnClickListener {
            if (SessionManager.isLogIn()) {
                launchIntent(this, MyOrdersActivity::class.java)
            } else {
                showLoginFirstDialog(getString(R.string.login_first))
            }
        }

        binding.mainContent.tvPayment.setOnClickListener {
            if (SessionManager.isLogIn()) {
                startActivity(Intent(this@MainActivity, PaymentActivity::class.java))
            } else {
                showLoginFirstDialog(getString(R.string.login_first))
            }
        }

        binding.mainContent.tvDetails.setOnClickListener {
            startActivity(Intent(this@MainActivity, PaymentDetailsActivity::class.java))
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


    private fun setupDrawer() {
        if (!SessionManager.isLogIn()) {
            binding.tvLogout.hide()
        }

        binding.mainContent.toolBar.iv_notification.setOnClickListener {
            if (SessionManager.isLogIn()) {
                launchIntent(this, NotificationActivity::class.java)
            } else {
                showLoginFirstDialog(getString(R.string.login_first))
            }
        }

        binding.mainContent.toolBar.iv_menu.setOnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        }

        binding.tvLogout.setOnClickListener {
            binding.drawer.closeDrawer(GravityCompat.START)

            showWarringDialog()
        }
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, binding.drawer, R.string.open, R.string.close
        )

        binding.drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.navView.setNavigationItemSelectedListener { menuItem ->

            binding.drawer.closeDrawers()

            when (menuItem.itemId) {

                R.id.nav_profile -> {
                    if (SessionManager.isLogIn()) {
                        launchIntent(this, ProfileActivity::class.java)
                    } else {
                        showLoginFirstDialog(getString(R.string.login_first))
                    }
                }
                /*R.id.nav_my_orders -> {
                    launchIntent(this, MyOrdersActivity::class.java)
                }*/
                R.id.nav_notifications -> {
                    if (SessionManager.isLogIn()) {
                        launchIntent(this, NotificationActivity::class.java)
                    } else {
                        showLoginFirstDialog(getString(R.string.login_first))
                    }
                }

                R.id.nav_contact_us -> {

                    showContactUsDialog()
                }
                R.id.nav_language -> {
                    binding.drawer.closeDrawer(GravityCompat.START)
                    selectLanguage()

                }
                R.id.nav_analytics -> {
                    if (SessionManager.isLogIn()) {
                        launchIntent(this, AnalyticActivity::class.java)
                    } else {
                        showLoginFirstDialog(getString(R.string.login_first))
                    }
                }
                /*  R.id.nav_log_out -> {
                      showWarringDialog()
                  }*/

            }

            true
        }

    }

    private fun showContactUsDialog() {
        val contactInfo = Utils.getContactInfo()
        val builder = AlertDialog.Builder(
            this@MainActivity)

        val inflater = this@MainActivity.getLayoutInflater()
        val mView = inflater.inflate(R.layout.contactus_dialog, null)
        val messenger = mView.findViewById(R.id.iv_messenger)
                as ImageView
        val whatsapp = mView.findViewById(R.id.iv_whatsapp)
                as ImageView
        val phone = mView.findViewById(R.id.iv_call)
                as ImageView
        val email = mView.findViewById(R.id.iv_email)
                as ImageView
        val facebook = mView.findViewById(R.id.iv_facebook)
                as ImageView
        val website = mView.findViewById(R.id.iv_website)
                as ImageView

        messenger.setOnClickListener { Utils.openMessanger(this@MainActivity, "2088455828039695") }
        whatsapp.setOnClickListener {
            Utils.openWhatsApp(this@MainActivity,
                "2" + contactInfo.data[3])
        }
        phone.setOnClickListener { Utils.openDialer(this@MainActivity, contactInfo.data[3]) }
        email.setOnClickListener { Utils.openEmail(this@MainActivity, contactInfo.data[2]) }
        facebook.setOnClickListener {
            Utils.openFacebookPage(this@MainActivity, contactInfo.data[0], "2088455828039695")
        }
        website.setOnClickListener { Utils.openWebsite(this@MainActivity, contactInfo.data[1]) }

        builder.setView(mView)
        val alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (SessionManager.isLogIn()) {
            setImageAndUsername()
            if (Utils.checkInternetConnection(this, binding.drawer)) {
                //get favourite list ids and save it to shaerdpref and later
                //when patient search for doctors check for every doctor id if found
                //in this list and based on this, mark this doctor as favourite or not
                viewModel.getFavouriteDoctors(Utils.getUserId(), Utils.getApiToken())

            }
        }

        val logo = findViewById<ImageView>(R.id.iv_oncare_logo)

        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                logo.visibility = View.VISIBLE
                if (user.patient_name == "" || user.phone2 == "") {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    finish()
                }
            }
        }

    }

    private fun setImageAndUsername() {
        val image = binding.navView.getHeaderView(0)
            .findViewById<CircleImageView>(R.id.iv_profile)

        Glide.with(this).load(Utils.getUser().photo).placeholder(R.drawable.ic_face)
            .error(R.drawable.ic_face).into(image)
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_name)
            .text = Utils.getUserName()
    }

    private fun logout() {
        SessionManager.signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun selectLanguage() {
        val options = arrayOf<CharSequence>(
            getString(R.string.english), getString(R.string.arabic), getString(R.string.cancel)
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_langaue))

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item].equals(getString(R.string.english))) {
                SessionManager.saveLang("en")
                changelayoutDirection("en")
                Utils.changeLanguage(this, "en")
                recreate()
            } else if (options[item].equals(getString(R.string.arabic))) {
                SessionManager.saveLang("ar")
                changelayoutDirection("ar")
                Utils.changeLanguage(this, "ar")
                recreate()
            } else {
                dialog.dismiss()
            }
        })
        builder.show()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawer.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }

    private fun observeData() {

        viewModel.favResposne.observe(this, object : Observer<ClinicsResponse> {
            override fun onChanged(t: ClinicsResponse?) {
                //save ids to sharedpref
                Utils.saveFavDoctorsIds(t!!)
            }
        })

        viewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
            }
        })

        viewModel.errorMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
            }
        })

        viewModel.errorInt.observe(this, object : Observer<Int> {
            override fun onChanged(t: Int?) {
            }
        })

    }

    fun showWarringDialog() {
        val sd = SweetDialog.newInstance(
            this,
            com.doctoraak.doctoraakpatient.customView.DialogType.WARNING
        )
        sd.show()
        sd.setMessage(getString(R.string.are_you_sure_to_logout))
        sd.setTitle(getString(R.string.warning))
        sd.setCancelClickListener(View.OnClickListener {
            sd.dismiss()
        })
        sd.setOkClickListener(View.OnClickListener {
            logout()
            sd.dismiss()
        }
        )
    }
}
