package com.example.kotlin_demo.adapters

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_demo.data.MyDataItem
import com.example.kotlin_demo.R


class MyAdapter(val context: Context): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    var allData = ArrayList<MyDataItem>()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var userId: TextView = itemView.findViewById(R.id.userId)
        var title: TextView = itemView.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =  ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_items, parent,false))
        return itemView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.userId.text = allData[holder.adapterPosition].userId.toString()
            holder.title.text = allData[holder.adapterPosition].title
        }
        catch (e:Exception){
            println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            println(e)

        }

        /*lateinit var apiDataViewModels: ApiDataViewModels
        apiDataViewModels.insertData(MyDataItem(userList[position].body.toString(),userList[position].id.toInt(),userList[position].title.toString(),userList[position].id.toInt()))
*/
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // If android version below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    override fun getItemCount(): Int {

        return allData.size
    }
    fun updateData(apiDataList: List<MyDataItem>){
        allData.clear()
        allData.addAll(apiDataList)

        notifyDataSetChanged()
    }
}
