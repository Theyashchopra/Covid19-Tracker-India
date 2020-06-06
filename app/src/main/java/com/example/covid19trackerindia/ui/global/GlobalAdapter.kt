package com.example.covid19trackerindia.ui.global

import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19trackerindia.R
import kotlinx.android.synthetic.main.global_card.view.*

class GlobalAdapter(var mGlobalList:ArrayList<GlobalItem>) : RecyclerView.Adapter<GlobalAdapter.GlobalViewHolder>() {
    class GlobalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mGlobalName=itemView.globalname
        val mActiveGlobal=itemView.globalactive
        val mConfirmedGlobal=itemView.globalconfirmed
        val mDeathGlobal=itemView.globaldeath
        val mGlobalLL=itemView.globalcardll
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalViewHolder {
        val v:View= LayoutInflater.from(parent.context).inflate(R.layout.global_card,parent,false)
        return GlobalViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mGlobalList.size
    }

    override fun onBindViewHolder(holder: GlobalViewHolder, position: Int) {
        val currentItem=mGlobalList[position]
        holder.mGlobalName.setText(currentItem.cName)
        holder.mConfirmedGlobal.setText(currentItem.cConfirm)
        holder.mActiveGlobal.setText(currentItem.cRecovered)
        holder.mDeathGlobal.setText(currentItem.cDeaths)
        holder.mGlobalLL.setOnClickListener { view->
            val arg= bundleOf("slug" to currentItem.cSlug,"name" to currentItem.cName)
            Navigation.findNavController(view).navigate(R.id.action_navigation_dashboard_to_countryDetailsFragment,arg)

        }
    }
}