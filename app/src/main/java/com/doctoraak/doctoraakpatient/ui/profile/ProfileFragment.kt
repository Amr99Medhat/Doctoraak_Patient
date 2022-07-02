package com.doctoraak.doctoraakpatient.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.FragmentProfileBinding
import java.util.*
import kotlin.collections.ArrayList


private var name = ""
private var photo = ""
private var gender = ""
private var patinet_name = ""
private var phone2 = ""
private val arr = ArrayList<String>()


class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mFragmentProfileBinding: FragmentProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        arr.add(resources.getString(R.string.male))
        arr.add(resources.getString(R.string.female))
        // Inflate the layout for this fragment
        val t=inflater.inflate(R.layout.fragment_profile, container, false)
        val spinner = t.findViewById<Spinner>(R.id.spinner)
        spinner?.adapter = activity?.applicationContext?.let { ArrayAdapter(it, R.layout.item_spinner, arr) } as SpinnerAdapter

        return t
        //return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


}