package com.doctoraak.doctoraakpatient.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.ActivityMainBinding
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.BaseActivity
import com.doctoraak.doctoraakpatient.ui.SettingsFragment
import com.doctoraak.doctoraakpatient.ui.profile.ProfileFragment


class MainActivity : BaseActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupBottomNavigation()

        val user = SessionManager.returnUserInfo()
        if (user != null) {
            if (user.insurance!!.id == 1) {
                if (user.patient_name == "" || user.phone2 == "") {
                    replaceFragment(ProfileFragment.newInstance())
                } else {
                    replaceFragment(HomeFragment.newInstance())
                }
            }
        }


    }


//    private fun showContactUsDialog() {
//        val contactInfo = Utils.getContactInfo()
//        val builder = AlertDialog.Builder(
//            this@MainActivity)
//
//        val inflater = this@MainActivity.getLayoutInflater()
//        val mView = inflater.inflate(R.layout.contactus_dialog, null)
//        val messenger = mView.findViewById(R.id.iv_messenger)
//                as ImageView
//        val whatsapp = mView.findViewById(R.id.iv_whatsapp)
//                as ImageView
//        val phone = mView.findViewById(R.id.iv_call)
//                as ImageView
//        val email = mView.findViewById(R.id.iv_email)
//                as ImageView
//        val facebook = mView.findViewById(R.id.iv_facebook)
//                as ImageView
//        val website = mView.findViewById(R.id.iv_website)
//                as ImageView
//
//        messenger.setOnClickListener { Utils.openMessanger(this@MainActivity, "2088455828039695") }
//        whatsapp.setOnClickListener {
//            Utils.openWhatsApp(this@MainActivity,
//                "2" + contactInfo.data[3])
//        }
//        phone.setOnClickListener { Utils.openDialer(this@MainActivity, contactInfo.data[3]) }
//        email.setOnClickListener { Utils.openEmail(this@MainActivity, contactInfo.data[2]) }
//        facebook.setOnClickListener {
//            Utils.openFacebookPage(this@MainActivity, contactInfo.data[0], "2088455828039695")
//        }
//        website.setOnClickListener { Utils.openWebsite(this@MainActivity, contactInfo.data[1]) }
//
//        builder.setView(mView)
//        val alertDialog = builder.create()
//        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        alertDialog.show()
//    }


//    private fun setImageAndUsername() {
//        val image = binding.navView.getHeaderView(0)
//            .findViewById<CircleImageView>(R.id.iv_profile)
//
//        Glide.with(this).load(Utils.getUser().photo).placeholder(R.drawable.ic_face)
//            .error(R.drawable.ic_face).into(image)
//        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_name)
//            .text = Utils.getUserName()
//    }
//
//    private fun logout() {
//        SessionManager.signOut()
//        startActivity(Intent(this, SignInActivity::class.java))
//        finish()
//    }
//
//    private fun selectLanguage() {
//        val options = arrayOf<CharSequence>(
//            getString(R.string.english), getString(R.string.arabic), getString(R.string.cancel)
//        )
//
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle(getString(R.string.choose_langaue))
//
//        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
//            if (options[item].equals(getString(R.string.english))) {
//                SessionManager.saveLang("en")
//                changelayoutDirection("en")
//                Utils.changeLanguage(this, "en")
//                recreate()
//            } else if (options[item].equals(getString(R.string.arabic))) {
//                SessionManager.saveLang("ar")
//                changelayoutDirection("ar")
//                Utils.changeLanguage(this, "ar")
//                recreate()
//            } else {
//                dialog.dismiss()
//            }
//        })
//        builder.show()
//    }
//    override fun onBackPressed() {
//        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
//            binding.drawer.closeDrawer(GravityCompat.START)
//        } else
//            super.onBackPressed()
//    }
//


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentView, fragment)
        fragmentTransaction.commit()
        if (fragmentManager.fragments.size > 0) {

        }
    }

    private fun setupBottomNavigation() {
//        binding.navView.selectedItemId = R.id.navigation_Home
        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_Home -> {
                    replaceFragment(HomeFragment.newInstance())
                }

                R.id.navigation_Profile -> {
                    replaceFragment(ProfileFragment.newInstance())
                }
                R.id.navigation_Settings -> {
                    replaceFragment(SettingsFragment.newInstance())
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onResume() {
        super.onResume()
        //binding.navView.selectedItemId = R.id.navigation_Home

    }
}
