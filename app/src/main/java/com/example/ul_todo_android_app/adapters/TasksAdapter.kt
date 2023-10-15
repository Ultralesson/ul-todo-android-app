package com.example.ul_todo_android_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ul_todo_android_app.R
import com.example.ul_todo_android_app.constants.SessionStoreTexts
import com.example.ul_todo_android_app.database.crud.TasksCRUDRepo
import com.example.ul_todo_android_app.database.entities.TasksEntity
import com.example.ul_todo_android_app.utilities.DateUtilities
import com.example.ul_todo_android_app.utilities.SessionData
import java.util.*

class TasksAdapter(
    private val tasksList: List<TasksEntity>,
    private val context: Context,
    private val clickListener: OnClickListener? = null,
    private val longClickListener: OnItemLongClickListener? = null
) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    interface OnClickListener {

        fun onProgressButtonClick(position: Int, taskValue: TasksEntity)
        fun onDoneButtonClick(position: Int)
        fun onEditButtonClick(position: Int, task: TasksEntity)
        fun onDeleteButtonClick(position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int, task: TasksEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_to_do_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        // Implement the logic to handle item movement here
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(tasksList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(tasksList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = tasksList[position]
        val userEmail = SessionData(context).getFromSession(
            SessionStoreTexts.LOGGED_IN_EMAIL
        )

        holder.txtTask.text = currentItem.task
        holder.txtDescription.text = currentItem.description
        holder.imgProgress.setImageResource(R.drawable.ic_progress)
        holder.imgDone.setImageResource(R.drawable.ic_done)
        holder.imgEdit.setImageResource(R.drawable.ic_edit)
        holder.imgDelete.setImageResource(R.drawable.ic_delete)

        if (currentItem.inProgress) {
            holder.imgProgress.visibility = View.GONE
            holder.imgDone.visibility = View.VISIBLE
            holder.txtCompletionDate.text =
                DateUtilities.getDueDateMessage(currentItem.completionDate)
        }

        if (currentItem.done) {
            holder.eleActions.visibility = View.GONE
            holder.eleDone.visibility = View.VISIBLE
            holder.txtCompletionDate.text = DateUtilities.getCurrentDate()
        }

        holder.tagsRecyclerView.adapter = TagsAdapter(
            currentItem.tags, context, null, R.layout.activity_tag_on_home_screen
        )

        holder.imgProgress.setOnClickListener {
            holder.imgProgress.visibility = View.GONE
            holder.imgDone.visibility = View.VISIBLE
            holder.txtCompletionDate.text =
                DateUtilities.getDueDateMessage(currentItem.completionDate)

            clickListener?.onProgressButtonClick(position, currentItem)
        }

        holder.imgEdit.setOnClickListener {
            TasksCRUDRepo(context).getTask(currentItem.userRefEmail, currentItem.task) {
                clickListener?.onEditButtonClick(position, it)
            }
        }

        holder.imgDone.setOnClickListener {
            holder.eleActions.visibility = View.GONE
            holder.eleDone.visibility = View.VISIBLE
            holder.txtCompletionDate.text = DateUtilities.getCurrentDate()
            TasksCRUDRepo(context).updateTaskDoneStatus(
                email = userEmail, task = currentItem.task, true
            )
            TasksCRUDRepo(context).updateTaskInProgressStatus(
                email = userEmail, task = currentItem.task, false
            ) {}

            clickListener?.onDoneButtonClick(position)
        }

        holder.imgDelete.setOnClickListener {
            TasksCRUDRepo(context).deleteTask(email = userEmail, task = currentItem.task)
            clickListener?.onDeleteButtonClick(position)
        }

        holder.imgDelete.setOnLongClickListener {
            // When a long-press occurs, invoke the longClickListener passing the position
            longClickListener?.onItemLongClick(position, currentItem)
            true // Return true to indicate that the long-click event is consumed
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTask: TextView = itemView.findViewById(R.id.txtTask)
        val txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
        val txtCompletionDate: TextView = itemView.findViewById(R.id.txtCompletionDate)
        val tagsRecyclerView: RecyclerView = itemView.findViewById(R.id.tagRecyclerView)
        val imgProgress: ImageView = itemView.findViewById(R.id.imgProgress)
        val imgDone: ImageView = itemView.findViewById(R.id.imgDone)
        val imgEdit: ImageView = itemView.findViewById(R.id.imgEdit)
        val imgDelete: ImageView = itemView.findViewById(R.id.imgDelete)
        val eleActions: LinearLayout = itemView.findViewById(R.id.eleActions)
        val eleDone: LinearLayout = itemView.findViewById(R.id.eleDone)
    }
}

class DragAndDropTasksCallback(private val adapter: TasksAdapter) : ItemTouchHelper.Callback() {
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false // Disable item swipe
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition

        // Check bounds to avoid IndexOutOfBoundsException
        if (fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION) {
            // Swap the items in your data source (tasksList) and notify the adapter
            adapter.onItemMove(fromPosition, toPosition)
            return true
        }
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Handle swipe actions if needed (e.g., delete)
    }
}
