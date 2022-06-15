package com.example.kotlin_demo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.R
import com.example.kotlin_demo.activities.TodoActivity
import com.example.kotlin_demo.data.TodoData
import com.example.kotlin_demo.viewmodels.TodoDataViewModels

class TodoAdapter(val context: Context, val listner: TodoActivity,private val showMenuDelete: (Boolean) -> Unit) : RecyclerView.Adapter<TodoAdapter.ViewHolder>()  {

    private var isEnable = false
    private var itemSelectedList = mutableListOf<Int>()
    private var itemSelectedList1 = mutableListOf<Int>()
    private lateinit var todoDataViewModels: TodoDataViewModels
    var allNotes = ArrayList<TodoData>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view = ViewHolder(inflater.inflate(R.layout.todo_items1, parent, false))

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

        var id = ItemsViewModel.id
        holder.title.text = ItemsViewModel.title
        holder.desc.text = ItemsViewModel.desc
        holder.time.text = ItemsViewModel.time
        holder.date.text = ItemsViewModel.date
        holder.iv.visibility = View.GONE
        holder.alarm_time.text = ItemsViewModel.hour+" : "+ItemsViewModel.min

        if (ItemsViewModel.hour.toInt() != 0){
            holder.alarm_img.setVisibility(VISIBLE)
        }
        else{
            holder.alarm_img.setVisibility(INVISIBLE)
            holder.alarm_time.setVisibility(INVISIBLE)

        }
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

        holder.card.setOnLongClickListener{
            selectItem(holder,ItemsViewModel,position)
            holder.iv.visibility = View.VISIBLE
            holder.card.setBackgroundResource(R.color.grey)
            true
        }
        holder.card.setOnClickListener{


            if (itemSelectedList.contains(position)){

                itemSelectedList.removeAt(position)
                itemSelectedList1.removeAt(ItemsViewModel.id.toInt())
                holder.iv.visibility = View.GONE
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
                ItemsViewModel.selected = false

                if (itemSelectedList.isEmpty()){
                    showMenuDelete(false)
                    isEnable = false
                }
            }
            else if(isEnable){
                selectItem(holder,ItemsViewModel,position)
            }
        }

    }

    private fun selectItem(holder: TodoAdapter.ViewHolder, itemsViewModel: TodoData, position: Int) {
        isEnable = true
        itemSelectedList.add(position)
        val ItemsViewModel = allNotes[position]
        println("holder.itemId.toInt()")
        println(ItemsViewModel.id)
        itemSelectedList1.add(ItemsViewModel.id.toInt())
        itemsViewModel.selected = true
        holder.card.visibility = View.VISIBLE
        showMenuDelete(true)
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }
    fun setClick(todoData: TodoData) {
        todoDataViewModels.deleteData(todoData)
        println("sdhfhsdfsdfg////")
    }

    fun deleteSelectedItems(){
        if (itemSelectedList.isNotEmpty()){
            println("itemSelectedList")
            println(itemSelectedList1)

            allNotes.removeAll{item -> item.selected}
            //todoDataViewModels.deleteData()
            isEnable = false
            itemSelectedList.clear()
        }
        notifyDataSetChanged()
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
        var alarm_img: ImageView = view.findViewById(R.id.alarm_img)
        var alarm_time: TextView = view.findViewById(R.id.alarm_time)
        var iv: ImageView = view.findViewById(R.id.ImageView)

    }
}