package com.example.ul_todo_android_app.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ul_todo_android_app.R
import com.example.ul_todo_android_app.adapters.HabitsAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

data class Habit(
    val name: String,
    val description: String
)

val badHabits = listOf(
    Habit(
        "Smoking",
        "Quit smoking to improve your health and well-being."
    ),
    Habit(
        "Excessive Junk Food",
        "Reduce the consumption of junk food for better nutrition."
    ),
    Habit(
        "Procrastination",
        "Overcome procrastination to boost productivity and reduce stress."
    ),
    Habit(
        "Excessive Alcohol Consumption",
        "Reduce alcohol consumption to promote better physical and mental health."
    ),
    Habit(
        "Binge-Watching TV Shows",
        "Limit excessive TV watching to free up time for more productive activities."
    ),
    Habit(
        "Negative Self-Talk",
        "Replace self-criticism with self-compassion and positive self-talk."
    ),
    Habit(
        "Overworking",
        "Avoid overworking to prevent burnout and maintain a healthy work-life balance."
    ),
    Habit(
        "Gossiping",
        "Refrain from engaging in gossip to foster positive relationships and communication."
    ),
    Habit(
        "Ignoring Physical Health",
        "Address health issues promptly and prioritize regular check-ups."
    ),
    Habit(
        "Nail Biting",
        "Break the habit of nail biting to maintain healthy nails and hygiene."
    ),
    Habit(
        "Excessive Sugary Drinks",
        "Reduce sugary drink intake to support a healthier diet."
    ),
    Habit(
        "Impulse Buying",
        "Control impulse buying to manage your finances more effectively."
    ),
    Habit(
        "Excessive Social Media Usage",
        "Limit time spent on social media to reduce distractions and improve focus."
    )
)

class BadHabitsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bad_habits, container, false)

        val badHabitsRecyclerView: RecyclerView? = view?.findViewById(R.id.badHabitsRecyclerView)
        badHabitsRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)

        val adapter = HabitsAdapter(requireContext(), badHabits)
        // Set the click listener for the adapter
        adapter.setOnItemClickListener(object : HabitsAdapter.OnItemClickListener {
            override fun onItemClick(habit: Habit) {
                showHabitDetailsDialog(habit)
            }
        })

        badHabitsRecyclerView?.adapter = adapter

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BadHabitsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("MissingInflatedId")
    private fun showHabitDetailsDialog(habit: Habit) {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val dialogView = layoutInflater.inflate(R.layout.activity_habit_dialog, null)
        dialogBuilder.setView(dialogView)

        // Initialize dialog views
        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val dialogDescription = dialogView.findViewById<TextView>(R.id.dialogDescription)

        // Set habit details in the dialog
        dialogTitle.text = habit.name
        dialogDescription.text = habit.description

        // Create and show the dialog
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}