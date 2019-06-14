package com.cogimag.michalg.einkaufsliste


import android.support.v7.widget.RecyclerView
import android.view.View.OnClickListener
import android.view.ViewGroup
import kotlinx.android.synthetic.main.laden_rv_item_layout.view.*

class LadenRvAdapter(val dataset:ArrayList<LadenModell>, val clickListener: OnClickListener):
    RecyclerView.Adapter<LadenRvAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LadenRvAdapter.ViewHolder {
        val ladenRvLayout:LadenRvItemLayout= android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.laden_rv_item_layout, parent, false)
                as LadenRvItemLayout
        //learn more about casting
        return LadenRvAdapter.ViewHolder(ladenRvLayout)

    }



    override fun onBindViewHolder(holder: LadenRvAdapter.ViewHolder, position: Int) {
        //call super?
        val laden:LadenModell= dataset[position]
        holder.itemView.laden_rv_layout_vordergrund_list_position.text = position.toString()
        holder.itemView.laden_rv_layout_vordergrund_item_name.text = laden.name
        holder.itemView.laden_rv_layout_vordergrund_item_db_id.text = laden.id.toString()
        holder.itemView.laden_rv_layout_hintergrund_btn_loeschen.setTag(R.id.laden_rv_layout_hintergrund_btn_loeschen_tag_list_position, position)
        holder.itemView.laden_rv_layout_hintergrund_btn_loeschen.setTag(R.id.laden_rv_layout_hintergrund_btn_loeschen_tag_laden_id, laden.id)
        holder.itemView.laden_rv_layout_hintergrund_btn_bearbeiten.setTag(R.id.laden_rv_layout_hintergrund_btn_bearbeiten_tag_list_position, position)
        holder.itemView.laden_rv_layout_hintergrund_btn_bearbeiten.setTag(R.id.laden_rv_layout_hintergrund_btn_bearbeiten_tag_laden_id, laden.id)
        //assign listener to individual buttons, not to the hintergrund as a whole
        //find a way to assign listener to hintergrund for simplicity, then get the child in the click listener code
        (0 until holder.itemView.laden_rv_layout_hintergrund.childCount).forEach { i ->
            holder.itemView.laden_rv_layout_hintergrund.getChildAt(i).setOnClickListener(clickListener)
        }
    }


    override fun getItemCount(): Int {
        return dataset.size
    }

    class ViewHolder: RecyclerView.ViewHolder {
        constructor(baseItemView: LadenRvItemLayout):super(baseItemView)
        //this syntax works. Others raise ambiguous reference error or
        //do not give access to view ids.
    }
}