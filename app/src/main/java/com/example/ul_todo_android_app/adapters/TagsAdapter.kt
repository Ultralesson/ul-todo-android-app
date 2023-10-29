package com.example.ul_todo_android_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ul_todo_android_app.R
import com.google.android.material.snackbar.Snackbar

class TagsAdapter(
    private val tagsList: List<String>,
    private val context: Context,
    private val clickListener: OnClickListener? = null,
    private val layoutResourceId: Int? = null
) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    interface OnClickListener {
        fun onCloseTagButtonClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(layoutResourceId ?: R.layout.activity_tags, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tagsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTag.text = tagsList[position]

        holder.imgCloseNewTask?.setOnClickListener {
            clickListener?.onCloseTagButtonClick(position)
        }

        holder.itemView.setOnClickListener {
            Snackbar.make(
                holder.itemView,
                "${holder.txtTag.text} is clicked",
                Snackbar.LENGTH_LONG
            ).setAction(
                "Ok"
            ) {
                // If the Ok button is pressed, perform the action
            }.show()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTag: TextView = itemView.findViewById(R.id.txtTag)
        val imgCloseNewTask: ImageView? = itemView.findViewById(R.id.imgRemoveTag)
    }
}