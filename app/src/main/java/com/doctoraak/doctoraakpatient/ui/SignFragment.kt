package com.doctoraak.doctoraakpatient.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.FragmentSignBinding
import com.doctoraak.doctoraakpatient.ui.signUp.SignUpActivity

class SignFragment : Fragment()
{

    companion object {
        @JvmStatic
        fun newInstance() = SignFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val binding = DataBindingUtil.inflate<FragmentSignBinding>(inflater
            , R.layout.fragment_sign, container, false)

        binding.btnSignUp.setOnClickListener { signUpClick() }
        binding.btnLogin.setOnClickListener { logInClick() }

        return binding.getRoot()
    }

    fun signUpClick()
    {
        startActivity(Intent(context, SignUpActivity::class.java))
        activity?.finish()
    }

    fun logInClick()
    {
        startActivity(Intent(context, SignInActivity::class.java))
        activity?.finish()
    }




}
