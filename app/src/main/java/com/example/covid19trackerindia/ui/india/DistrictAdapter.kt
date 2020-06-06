package com.example.covid19trackerindia.ui.india

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19trackerindia.R
import kotlinx.android.synthetic.main.districts_card.view.*

class DistrictAdapter(var mDistrictList: ArrayList<DistrictItem>): RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder>() {
    class DistrictViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mDistrictName=itemView.districtname
        val mActiveDistrict=itemView.districtactive
        val mConfirmedDistrict=itemView.districtconfirmed
        val mDeathDistrict=itemView.districtdeath
        val mDistrictLL=itemView.districtcardll
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
        val v:View= LayoutInflater.from(parent.context).inflate(R.layout.districts_card,parent,false)
        return DistrictViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mDistrictList.size;
    }

    override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
        val currentItem=mDistrictList[position]
        holder.mDistrictName.setText(currentItem.dname)
        holder.mDistrictName.setTextColor(currentItem.zone)
        holder.mConfirmedDistrict.setText(currentItem.dconfirmed)
        holder.mActiveDistrict.setText(currentItem.dactive)
        holder.mDeathDistrict.setText(currentItem.ddeadths)
    }
}