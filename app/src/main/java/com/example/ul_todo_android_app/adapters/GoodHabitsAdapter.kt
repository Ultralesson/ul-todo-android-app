package com.example.ul_todo_android_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ul_todo_android_app.R
import com.example.ul_todo_android_app.fragments.GoodHabit

class GoodHabitsAdapter(private val context: Context, private val habits: List<GoodHabit>) :
    RecyclerView.Adapter<GoodHabitsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(habit: GoodHabit)
    }

    private var itemClickListener: OnItemClickListener? = null

    // Set the click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.activity_habit_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = habits[position]
        holder.bind(habit)

        // Set click listener for the item view
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(habit)
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize your views here
        private val habitName: TextView = itemView.findViewById(R.id.textHabitName)
        private val habitDescription: TextView = itemView.findViewById(R.id.textHabitDescription)

        fun bind(habit: GoodHabit) {
            // Bind data to views here
            habitName.text = habit.name
            habitDescription.text = habit.description
        }
    }
}
