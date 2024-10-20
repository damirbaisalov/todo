package kz.app.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.app.todo.models.OnTaskClickType
import kz.app.todo.models.Task

class TaskAdapter(
    private var tasks: List<Task>,
    private val performClick: (OnTaskClickType) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.task_title)
        val description: TextView = view.findViewById(R.id.task_description)
        val completedCheckBox: CheckBox = view.findViewById(R.id.completed_checkbox)
        val deleteButton: ImageView = view.findViewById(R.id.task_delete)
        val leftTextBlock: LinearLayout = view.findViewById(R.id.left_texts_block)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.description.text = task.description
        holder.completedCheckBox.isChecked = task.isCompleted
        holder.deleteButton.setImageResource(R.drawable.ic_delete_24)

        val titleVisibility = if (holder.title.text.isEmpty()) View.GONE else View.VISIBLE
        holder.title.visibility = titleVisibility

        if (task.isCompleted) {
            holder.title.paint.isStrikeThruText = true
            holder.description.paint.isStrikeThruText = true
        } else {
            holder.title.paint.isStrikeThruText = false
            holder.description.paint.isStrikeThruText = false
        }

        holder.leftTextBlock.setOnClickListener {
            performClick(OnTaskClickType.OnTaskClick(task))
        }

        holder.completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val updatedTask = task.copy(isCompleted = isChecked)
            performClick(OnTaskClickType.OnUpdateClick(updatedTask))
        }

        holder.deleteButton.setOnClickListener {
            performClick(OnTaskClickType.OnDeleteClick(task))
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
