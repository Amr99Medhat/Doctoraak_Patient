package com.doctoraak.doctoraakpatient.ui.splash

import android.animation.Animator
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.scaleMatrix
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.ActivitySplashBinding
import com.doctoraak.doctoraakpatient.model.*
import com.doctoraak.doctoraakpatient.repository.local.SessionManager
import com.doctoraak.doctoraakpatient.ui.main.MainActivity
import com.doctoraak.doctoraakpatient.utils.Utils

class SplashActivity : AppCompatActivity() {

    private var noOfResponses = 0
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this).get(
            SplashViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.changeLanguage(this, Utils.getAppLanguage())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        observeData()

        if (Utils.checkInternetConnection(this@SplashActivity, binding.viewGroup)) {
            viewModel.getAreas()
            viewModel.getCities()
            viewModel.getMedicines()
            viewModel.getMedicinesType()
            viewModel.getRays()
            viewModel.getAnaylsis()
            viewModel.getAllLabs()
            viewModel.getAllRadiologies()
            viewModel.getContactInfo()
            viewModel.getInsurrance()
        }
        try {
            startAnimateSplashScreen()
        } catch (e: Exception) {
            Log.e("saif", "onCreate: ", e)
        }
    }

    private fun observeData() {

        viewModel.insurranceResponse.observe(this, object : Observer<InsuranceResponse> {
            override fun onChanged(t: InsuranceResponse?) {
                noOfResponses += 1
                SessionManager.saveInsurrance(Utils.convertObjectToJson(t))
                checkAndProcess()
            }
        })

        viewModel.contactInfoResponse.observe(this, object : Observer<ContactUsResponse> {
            override fun onChanged(t: ContactUsResponse?) {
                noOfResponses += 1
                SessionManager.saveContactInfo(Utils.convertObjectToJson(t))
                checkAndProcess()
            }
        })

        viewModel.allRadiologiesResponse.observe(this, object : Observer<AllRadiologiesResponse> {
            override fun onChanged(t: AllRadiologiesResponse?) {
                noOfResponses += 1
                SessionManager.saveAllRadiologies(Utils.convertObjectToJson(t))
                checkAndProcess()
            }
        })

        viewModel.allLabsResponse.observe(this, object : Observer<AllLabsResponse> {
            override fun onChanged(t: AllLabsResponse?) {
                noOfResponses += 1
                SessionManager.saveAllLabs(Utils.convertObjectToJson(t))
                checkAndProcess()
            }
        })

        viewModel.areasResponse.observe(this, object : Observer<AreaResponse> {
            override fun onChanged(t: AreaResponse?) {
                noOfResponses += 1
                SessionManager.saveAreas(Utils.convertObjectToJson(t))
                checkAndProcess()
            }
        })

        viewModel.raysResponse.observe(this, object : Observer<RaysResponse> {
            override fun onChanged(t: RaysResponse?) {
                noOfResponses += 1
                SessionManager.saveRays(Utils.convertObjectToJson(t))
                checkAndProcess()
            }
        })

        viewModel.anaylsisResponse.observe(this, object : Observer<AnalysisResponse> {
            override fun onChanged(t: AnalysisResponse?) {
                noOfResponses += 1
                SessionManager.saveAnaysis(Utils.convertObjectToJson(t))
                checkAndProcess()
            }
        })

        viewModel.citiesResponse.observe(this, object : Observer<CityResponse> {
            override fun onChanged(t: CityResponse?) {
                noOfResponses += 1
                SessionManager.saveCities(Utils.convertObjectToJson(t))
                checkAndProcess()
            }
        })

        viewModel.mediResponse.observe(this, object : Observer<MedicinesResponse> {
            override fun onChanged(t: MedicinesResponse?) {
                noOfResponses += 1
                SessionManager.saveMedicines(Utils.convertObjectToJson(t))
                checkAndProcess()
            }
        })

        viewModel.mediTypeResponse.observe(this, object : Observer<MedicinesTypeResponse> {
            override fun onChanged(t: MedicinesTypeResponse?) {
                noOfResponses += 1
                SessionManager.saveMedicinesType(Utils.convertObjectToJson(t))
                checkAndProcess()
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

    private fun checkAndProcess() {
        if (noOfResponses == 10){
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun startAnimateSplashScreen()
    {
        val heartAnimatable = binding.ivHeartbeat.drawable as Animatable
        heartAnimatable.start()

        animate(binding.ivLogo)
        animate(binding.ivCareLogo)
    }

    private fun animate(it: View, xCor :Float = -450f, scaleDownUp: Boolean = true)
    {
        it.animate().apply {
            startDelay = 500
            duration = 1000
            rotationYBy(180f)
//            translationXBy(xCor)
//            scaleX(if (scaleDownUp) 0.5f else 1f)
//            scaleY(if (scaleDownUp) 0.5f else 1f)

            setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    animate(it)//, xCor * -1, !scaleDownUp)
                }
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
            })
        }.start()
    }


    override fun onPause() {
        super.onPause()

        binding.ivHeartbeat.clearAnimation()
        binding.ivLogo.clearAnimation()
    }


}
