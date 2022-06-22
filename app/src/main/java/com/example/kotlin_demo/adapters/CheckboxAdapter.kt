package com.example.kotlin_demo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.activities.TodoAddActivity
import com.example.kotlin_demo.data.Temp
import com.example.kotlin_demo.viewmodels.TaskViewModels


class CheckboxAdapter(val context: Context, val listner: TodoAddActivity) : RecyclerView.Adapter<CheckboxAdapter.ViewHolder>(){

    var allTask = ArrayList<Temp>()
    var id = 0
    private lateinit var taskViewModels: TaskViewModels


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckboxAdapter.ViewHolder {
        lateinit var viewHolder: CheckboxAdapter.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        val viewItem: View = inflater.inflate(R.layout.checkbox_task, parent, false)
        viewHolder = ViewHolder(viewItem)


        viewHolder.des_title.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                println("presssssss")
                var title = viewHolder.des_title.text
                var check_status = true
                if (viewHolder.checkbox_check.isSelected){
                    check_status = true
                }
                else{
                    check_status = false
                }
                listner.setClick(title,check_status)
            }
            false
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CheckboxAdapter.ViewHolder, position: Int) {
        val Itemofcheck = allTask[position]

        holder.des_title.setText(Itemofcheck.title)


        var check_status = Itemofcheck.isselected

        if (check_status == true){
            holder.checkbox_check.setChecked(true)
        }
        else{
            holder.checkbox_check.setChecked(false)
        }
    }

    override fun getItemCount(): Int {
        return this.allTask.size

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var des_title: EditText = view.findViewById(R.id.checkbox_edt)
        var checkbox_check : CheckBox = view.findViewById(R.id.checkbox_check)

    }
    fun updateTempData(temp: List<Temp>){
        allTask.clear()
        allTask.addAll(temp)

        notifyDataSetChanged()
    }



}