package com.doctoraak.doctoraakpatient.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.R
import com.doctoraak.doctoraakpatient.databinding.LoadingItemBinding
import com.doctoraak.doctoraakpatient.adapters.PagingHolder
import com.doctoraak.doctoraakpatient.model.Lab
import com.doctoraak.doctoraakpatient.utils.PageStatus
import kotlin.reflect.KClass

/**
 * this Class used as pagination Adapter.
* @param T type of arrayList
**/
abstract class PagingAdapter<T>
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    protected var list: ArrayList<T> = ArrayList()
    private var pageStatus: PageStatus = PageStatus.NEW_PAGE

    fun isPageLoading() = pageStatus==PageStatus.LOADING

    fun isEndOfList() = pageStatus==PageStatus.END_LIST

    override fun getItemViewType(position: Int) = if (pageStatus == PageStatus.NEW_PAGE || position != list.size) /* Last Item */
        PageStatus.NEW_PAGE.value
    else pageStatus.value

    override fun getItemCount(): Int  {
        Log.d("saif", "list size=${list.size}")
        return if (pageStatus == PageStatus.NEW_PAGE) list.size else list.size+1
    }

    /**
     * @return Loading ViewHolder as a SUPER return, so you must return the super when want to use the LoadingViewHolder.
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        return LoadingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context)
            , R.layout.loading_item, parent, false))
    }

//    abstract fun onCreateViewHolder(): RecyclerView.ViewHolder

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        if (holder is PagingHolder<*>)
            (holder as PagingHolder<Any>).bind(list[position] as Any)
        else if (holder is LoadingViewHolder)
            holder.bindData(pageStatus == PageStatus.LOADING, pageStatus == PageStatus.END_LIST)
    }

    /**
     * @param data if data was null so end of list.
     * */
    fun setData(data: ArrayList<T>?)
    {
        if (data != null && data.size > 0)
        {
            pageStatus = PageStatus.NEW_PAGE
            val listSize = list.size
            Log.d("saif", "listSize=${list.size}")
            list.addAll(data)
            Log.d("saif", "listSize=${list.size}")

            if (listSize == 0)
                notifyDataSetChanged()
            else
                notifyItemRangeInserted(listSize+1, data.size)
        }
        else
        {
            pageStatus = PageStatus.END_LIST
            notifyItemInserted(list.size+1)
        }
    }

    fun startPageLoading()
    {
        pageStatus = PageStatus.LOADING
        notifyItemChanged(list.size+1)
    }

    fun clearData()
    {
        list.clear()
        notifyDataSetChanged()
    }

}

class LoadingViewHolder(val binding: LoadingItemBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bindData(isLoading: Boolean, isEndOfList: Boolean = false) = with(binding)
    {
        pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        tvText.visibility = if (isEndOfList) View.VISIBLE else View.GONE
    }
}