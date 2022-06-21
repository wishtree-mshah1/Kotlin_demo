package com.example.kotlin_demo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.data.Temp

class CheckboxAdapter(val context: Context) : RecyclerView.Adapter<CheckboxAdapter.ViewHolder>(){

    var allTask = ArrayList<Temp>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckboxAdapter.ViewHolder {
        lateinit var viewHolder: CheckboxAdapter.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        val viewItem: View = inflater.inflate(R.layout.checkbox_task, parent, false)
        viewHolder = ViewHolder(viewItem)

        return viewHolder
    }

    override fun onBindViewHolder(holder: CheckboxAdapter.ViewHolder, position: Int) {
        val Itemofcheck = allTask[position]

        holder.des_title.setText(Itemofcheck.title)
        holder.checkbox_check.setChecked(true)
//        var check_status = Itemofcheck.isselected
//
//        if (check_status == true){
//            holder.checkbox_check.setChecked(true)
//        }
//        else{
//            holder.checkbox_check.setChecked(false)
//        }
    }

    override fun getItemCount(): Int {
        return this.allTask.size

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var des_title: EditText = view.findViewById(R.id.checkbox_edt)
        var checkbox_check : CheckBox = view.findViewById(R.id.checkbox_check)

    }



}