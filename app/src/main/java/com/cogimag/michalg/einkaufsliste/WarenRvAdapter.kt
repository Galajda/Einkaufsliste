package com.cogimag.michalg.einkaufsliste

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.laden_rv_item_layout.view.*
import kotlinx.android.synthetic.main.waren_rv_item_layout.view.*

class WarenRvAdapter(val dataset:ArrayList<WarenModell>, val clickListener: View.OnClickListener):
    RecyclerView.Adapter<WarenRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarenRvAdapter.ViewHolder {
        val warenRvLayout:WarenRvItemLayout = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.waren_rv_item_layout, parent, false)
            as WarenRvItemLayout
        return WarenRvAdapter.ViewHolder(warenRvLayout)
    }

    override fun onBindViewHolder(holder: WarenRvAdapter.ViewHolder, position: Int) {
        val ware:WarenModell = dataset[position]
        holder.itemView.waren_rv_layout_vordergrund_list_position.text = position.toString()
        holder.itemView.waren_rv_layout_vordergrund_item_db_id.text = ware.id.toString()
        holder.itemView.waren_rv_layout_vordergrund_item_name.text = ware.name
        holder.itemView.waren_rv_layout_vordergrund.setTag(R.id.waren_rv_layout_vordergrund_tag_list_position, position)
        holder.itemView.waren_rv_layout_vordergrund.setTag(R.id.waren_rv_layout_vordergrund_tag_db_id, ware.id)

        holder.itemView.waren_rv_layout_hintergrund_btn_bearbeiten.setTag(R.id.waren_rv_layout_hintergrund_btn_bearbeiten_tag_list_position, position)
        holder.itemView.waren_rv_layout_hintergrund_btn_bearbeiten.setTag(R.id.waren_rv_layout_hintergrund_btn_bearbeiten_tag_waren_id, ware.id)

        holder.itemView.waren_rv_layout_hintergrund_btn_loeschen.setTag(R.id.waren_rv_layout_hintergrund_btn_loeschen_tag_list_position, position)
        holder.itemView.waren_rv_layout_hintergrund_btn_loeschen.setTag(R.id.waren_rv_layout_hintergrund_btn_loeschen_tag_waren_id, ware.id)

        (0 until holder.itemView.waren_rv_layout_hintergrund.childCount).forEach { i ->
            holder.itemView.waren_rv_layout_hintergrund.getChildAt(i).setOnClickListener(clickListener)
        }
        holder.itemView.waren_rv_layout_vordergrund.setOnClickListener(clickListener)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ViewHolder: RecyclerView.ViewHolder {
        constructor(baseItemView: WarenRvItemLayout):super(baseItemView)
        //this syntax works. Others raise ambiguous reference error or
        //do not give access to view ids.
    }
}