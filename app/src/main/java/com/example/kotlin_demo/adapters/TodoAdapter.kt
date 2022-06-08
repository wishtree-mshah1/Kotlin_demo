package com.example.kotlin_demo.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.activities.TodoActivity
import com.example.kotlin_demo.data.TodoData
import com.google.type.Color

class TodoAdapter(val context: Context, val listner: TodoActivity) : RecyclerView.Adapter<TodoAdapter.ViewHolder>()  {

    var allNotes = ArrayList<TodoData>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view = ViewHolder(inflater.inflate(R.layout.todo_items, parent, false))

        view.delete.setOnClickListener(){
            listner.setClick(allNotes[view.adapterPosition])
        }
        view.edit.setOnClickListener(){
            listner.onItemClick(allNotes[view.adapterPosition])
        }


        return view
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = allNotes[position]

        holder.title.text = ItemsViewModel.title
        holder.desc.text = ItemsViewModel.desc
        holder.time.text = ItemsViewModel.time
        holder.date.text = ItemsViewModel.date
        if (ItemsViewModel.color.toString() == "Blue") {
            holder.card.setBackgroundResource(R.color.blue)
        }
        else if (ItemsViewModel.color.toString() == "Black") {
            holder.card.setBackgroundResource(R.color.black)
        }
        else if (ItemsViewModel.color.toString() == "Yellow") {
            holder.card.setBackgroundResource(R.color.yellow)
        }
        else if (ItemsViewModel.color.toString() == "Pink") {
            holder.card.setBackgroundResource(R.color.themecolor)
        }
        else if (ItemsViewModel.color.toString() == "Red") {
            holder.card.setBackgroundResource(R.color.red)
        }

    }

    override fun getItemCount(): Int {
        return allNotes.size
    }
    fun updateTodoData(todoDataList: List<TodoData>){
        allNotes.clear()
        allNotes.addAll(todoDataList)

        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var title: TextView = view.findViewById(R.id.title)
        var desc: TextView = view.findViewById(R.id.desc)
        var time: TextView = view.findViewById(R.id.time)
        var date: TextView = view.findViewById(R.id.date)
        var delete: ImageButton = view.findViewById(R.id.delete)
        var edit: ImageButton = view.findViewById(R.id.edit)
        var card: CardView = view.findViewById(R.id.cardView)
    }
}