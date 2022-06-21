package com.doctoraak.doctoraakpatient.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.doctoraak.doctoraakpatient.ui.myOrders.DoctorOrdersFragment
import com.doctoraak.doctoraakpatient.ui.myOrders.LabOrdersFragment
import com.doctoraak.doctoraakpatient.ui.myOrders.PharmacyOrdersFragment
import com.doctoraak.doctoraakpatient.ui.myOrders.RadiologyOrdersFragment

class MyOrdersPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
    override fun getItem(position: Int): Fragment
    {
        return when (position)
        {
            0 -> DoctorOrdersFragment.newInstance()
            1 -> PharmacyOrdersFragment.newInstance()
            2 -> LabOrdersFragment.newInstance()
            3 -> RadiologyOrdersFragment.newInstance()
            else -> Fragment()
        }
    }


    override fun getCount() = 4

}