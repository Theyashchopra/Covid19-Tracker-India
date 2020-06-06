package com.example.covid19trackerindia.ui.india

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19trackerindia.R
import kotlinx.android.synthetic.main.states_card.view.*

class StateAdapter(var mStateList:ArrayList<StateItem>) : RecyclerView.Adapter<StateAdapter.StateViewHolder>() {
     class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mStateName:TextView=itemView.statename;
        val mConfirmedState:TextView=itemView.stateconfirmed
        val mActiveState:TextView=itemView.stateactive
        val mDeathState:TextView=itemView.statedeath
        val mNextImage:ImageView=itemView.iv
        val mCardll:LinearLayout=itemView.statecardll
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val v:View=LayoutInflater.from(parent.context).inflate(R.layout.states_card,parent,false)
        return StateViewHolder(v)
    }
    override fun getItemCount(): Int {
        return  mStateList.size;
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        val currentItem:StateItem= mStateList[position]
        holder.mStateName.setText(currentItem.name)
        holder.mConfirmedState.setText(currentItem.confirmedCases)
        holder.mActiveState.setText(currentItem.activeCases)
        holder.mDeathState.setText(currentItem.death)
        if (currentItem.name.equals("India") )
            holder.mNextImage.isGone;
        holder.mCardll.setOnClickListener{view->
            val arg= bundleOf("name" to currentItem.name)
            Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_districtFragment,arg)
        }
    }
}