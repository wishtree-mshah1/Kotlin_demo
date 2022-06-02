package com.example.kotlin_demo.adapters

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.data.MyDataItem


class MyAdapter(val context: Context) : RecyclerView.Adapter<MyAdapter.BaseViewHolder>() {
    var allData = ArrayList<MyDataItem>()

    open class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }
    class ViewHolder(itemView: View): BaseViewHolder(itemView) {
        var userId: TextView = itemView.findViewById(com.example.kotlin_demo.R.id.userId)
        var title: TextView = itemView.findViewById(com.example.kotlin_demo.R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        lateinit var viewHolder: BaseViewHolder
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> {
                val viewItem: View = inflater.inflate(com.example.kotlin_demo.R.layout.row_items, parent, false)
                viewHolder = ViewHolder(viewItem)
            }
            LOADING -> {
                val viewLoading: View = inflater.inflate(com.example.kotlin_demo.R.layout.row_progress, parent, false)
                viewHolder = LoadingViewHolder(viewLoading)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        when (getItemViewType(position)) {
            ITEM -> {
                val itemHolder =  holder as ViewHolder
                itemHolder.userId.text = allData[holder.adapterPosition].id.toString()
                itemHolder.title.text = allData[holder.adapterPosition].body
            }
            LOADING -> {
                println("loading")
            }
        }
    }

    override fun getItemCount(): Int {
        return this.allData.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == this.allData!!.size) LOADING else ITEM
    }

    fun updateData(apiDataList: List<MyDataItem>){
        allData.clear()
        allData.addAll(apiDataList)

        notifyDataSetChanged()
    }
    fun getItem(position: Int): Int {
        return allData.size
    }

    @SuppressLint("ResourceType")
    inner class LoadingViewHolder(itemView: View) : BaseViewHolder(itemView) {

        init {

        }
    }

    companion object {
        private const val LOADING = 0
        private const val ITEM = 1
    }


}