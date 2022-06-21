package com.doctoraak.doctoraakpatient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.model.Category
import com.doctoraak.doctoraakpatient.utils.Utils
import kotlinx.android.synthetic.main.categoty_item.view.*

class DoctorCategoryAdapter(val categories: ArrayList<Category>, val context: Context) :
    RecyclerView.Adapter<DoctorCategoryAdapter.MyViewHolder>(), Filterable
{
    private val categoriesMain: ArrayList<Category> = arrayListOf()
    lateinit var mClickListener: ClickListener

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(model: Category, aView: View)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.categoty_item, parent, false)
        )
    }

    override fun getItemCount() = categories.size

    fun clearData(){
        if (categories.size > 0){
            categories.clear()
            categoriesMain.clear()
        }
    }

    fun setData(data : ArrayList<Category>){
        clearData()
        categories.addAll(data)
        categoriesMain.addAll(data)
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cate  = categories[position]

        holder.text.text =Utils.getTextForAppLanguage(cate.name,cate.name_ar,cate.name_fr )

        Glide.with(context).load(cate.url)
            .placeholder(R.drawable.ic_cate_temp)
            .error(R.drawable.ic_cate_temp)
            .into(holder.image)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
        , View.OnClickListener {
        override fun onClick(v: View?) {
             mClickListener.onClick(categories[adapterPosition] , v!!)
        }

        val image = view.imageView
        val text = view.textView

        init {
            itemView.setOnClickListener(this)
        }
    }

    override fun getFilter(): Filter  = CategoryFilter()

    inner class CategoryFilter: Filter()
    {
        override fun performFiltering(text: CharSequence?): FilterResults
        {
            val res = FilterResults()
            res.values = if (text.isNullOrEmpty())
                categoriesMain
            else
                categoriesMain.filter {
                    it.name.contains(text, true) || it.name_ar.contains(text, true) || it.name_fr.contains(text, true)
                }

            return res
        }

        override fun publishResults(p0: CharSequence?, result: FilterResults?)
        {
            if (result != null)
            {
                categories.clear()
                categories.addAll(result.values as List<Category>)
                notifyDataSetChanged()
            }
        }

    }
}