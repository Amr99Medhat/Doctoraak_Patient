package com.doctoraak.doctoraakpatient.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.doctoraak.doctoraakpatient.adapters.PagingAdapter

class PagingRecyclerView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs)
{
    var onStartLoadingAction: (()->Boolean)? = null


    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        if (adapter is PagingAdapter<*>)
        {
            with(adapter as PagingAdapter<*>)
            {
                if (!canScrollVertically(1) && state == SCROLL_STATE_IDLE && !isPageLoading() && !isEndOfList())
                {
                    Log.d("saif", "onScrollStateChanged startLoading")
                    val isLoading = onStartLoadingAction?.invoke()
                    if (isLoading == true)
                    {
                        startPageLoading()
                        smoothScrollToPosition(itemCount-1)
                    }
                }
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?)
    {
        if (adapter !is PagingAdapter<*>?)
            Log.e("saif", "This adapter (${adapter?.javaClass?.name}) must extend from PagingAdapter Class to implement Paging and its ViewHolder Must Implement PagingHolder.")

        super.setAdapter(adapter)
    }


}