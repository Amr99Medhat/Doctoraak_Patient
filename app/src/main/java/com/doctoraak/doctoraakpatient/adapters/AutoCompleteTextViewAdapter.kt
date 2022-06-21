package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import com.doctoraak.doctoraakpatient.utils.Utils
import java.util.*
import kotlin.collections.ArrayList

class AutoCompleteTextViewAdapter<T : AutoCompleteBase>
    (context: Context, @LayoutRes private val layoutResource: Int, private val models: ArrayList<T>):
    BaseAdapter(), Filterable {

    private var mModels: List<T> = models
    private var tempModels = models

    override fun getCount(): Int {
        return mModels.size
    }

    /** don't dall this method to get Item At position  */
    override fun getItem(position: Int): Any {
        val item = mModels[position]
        return item.name // Utils.getTextForAppLanguage(item.name, item.nameAr, item.nameFr)
    }

    fun getItemAt(position: Int) = mModels.get(position)


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(parent.context).inflate(layoutResource
            , parent, false) as TextView
        val item = mModels[position]
        view.text = Utils.getTextForAppLanguage(item.name, item.nameAr, item.nameFr)
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {

                mModels = if (filterResults.values == null)
                    arrayListOf()
                else
                    filterResults.values as List<T>

                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults
            {
                val queryString = charSequence?.toString()?.trim()

                Log.d("saif", "performFiltering: mainList= ${tempModels.size}")
                val filterResults = FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    tempModels
                else
                {
                    val lis = arrayListOf<T>()
                    tempModels.forEach {
                        if (filterCondition(it, queryString))
                        {
                            Log.d("saif", "performFiltering: filterNmae= ${it.name}      que=$queryString")
                            lis.add(it)
                        }
                    }
//                    val lis=  tempModels.filter {
//                        Log.d("saif", "performFiltering: name= ${it.name}   && nameAr= ${it.nameAr}  && nameFr= ${it.nameFr}")
//                        Log.d("saif", "performFiltering: name= ${it.name.contains(queryString)} ")
//
//                        filterCondition(it, queryString)
//                    }

                    Log.d("saif", "performFiltering: size= ${lis.size}")
                    lis
                }
                return filterResults
            }

            fun filterCondition(obj: T, query: String)
                    = ( (obj.name.contains(query, true))
                    /*|| (obj.nameAr.isNotEmpty() && obj.nameAr.contains(query))
                    || (obj.nameFr.isNotEmpty() && obj.nameFr.contains(query, true))*/)
        }
    }
}

interface AutoCompleteBase {
    var name: String
    var nameAr: String
    var nameFr: String
}