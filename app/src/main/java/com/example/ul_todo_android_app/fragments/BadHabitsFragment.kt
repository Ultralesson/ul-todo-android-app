package com.example.ul_todo_android_app.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.ul_todo_android_app.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

data class BadHabit(
    val name: String,
    val description: String
)

val badHabits = listOf(
    BadHabit(
        "Smoking",
        "Quit smoking to improve your health and well-being."
    ),
    BadHabit(
        "Excessive Junk Food",
        "Reduce the consumption of junk food for better nutrition."
    ),
    BadHabit(
        "Procrastination",
        "Overcome procrastination to boost productivity and reduce stress."
    ),
    BadHabit(
        "Excessive Alcohol Consumption",
        "Reduce alcohol consumption to promote better physical and mental health."
    ),
    BadHabit(
        "Binge-Watching TV Shows",
        "Limit excessive TV watching to free up time for more productive activities."
    ),
    BadHabit(
        "Negative Self-Talk",
        "Replace self-criticism with self-compassion and positive self-talk."
    ),
    BadHabit(
        "Overworking",
        "Avoid overworking to prevent burnout and maintain a healthy work-life balance."
    ),
    BadHabit(
        "Gossiping",
        "Refrain from engaging in gossip to foster positive relationships and communication."
    ),
    BadHabit(
        "Ignoring Physical Health",
        "Address health issues promptly and prioritize regular check-ups."
    ),
    BadHabit(
        "Nail Biting",
        "Break the habit of nail biting to maintain healthy nails and hygiene."
    ),
    BadHabit(
        "Excessive Sugary Drinks",
        "Reduce sugary drink intake to support a healthier diet."
    ),
    BadHabit(
        "Impulse Buying",
        "Control impulse buying to manage your finances more effectively."
    ),
    BadHabit(
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

        val gridView: GridView? = view?.findViewById(R.id.gridView)
        val adapter = BadHabitsAdapter(requireContext(), badHabits)
        gridView?.adapter = adapter

        gridView?.setOnItemClickListener { parent, view, position, id ->
            // Get the habit selected
            val habit = badHabits[position]

            // Show habit details in a dialog
            showHabitDetailsDialog(habit)
        }

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
    private fun showHabitDetailsDialog(habit: BadHabit) {
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

class BadHabitsAdapter(private val context: Context, private val habits: List<BadHabit>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return habits.size
    }

    override fun getItem(position: Int): Any {
        return habits[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val habit = habits[position]
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.activity_habit_item, parent, false)

        val habitName: TextView = view.findViewById(R.id.textHabitName)
        val habitDescription: TextView = view.findViewById(R.id.textHabitDescription)

        habitName.text = habit.name
        habitDescription.text = habit.description

        return view
    }
}
