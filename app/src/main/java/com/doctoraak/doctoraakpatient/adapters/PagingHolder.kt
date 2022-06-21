package com.doctoraak.doctoraakpatient.adapters

/**
 ** @param T type of arrayList
 * */

interface PagingHolder<T> where T: Any{
    fun bind(item: T)
}