package kz.app.todo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kz.app.todo.models.Task

class EditTaskDialogFragment : DialogFragment() {

    interface EditTaskListener {
        fun onTaskEdited(updatedTask: Task)
    }

    private lateinit var listener: EditTaskListener
    private lateinit var task: Task

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditTaskListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement EditTaskListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_edit_task, null)

        val titleEditText: EditText = view.findViewById(R.id.edit_task_title)
        val descriptionEditText: EditText = view.findViewById(R.id.edit_task_description)

        titleEditText.setText(task.title)
        descriptionEditText.setText(task.description)

        builder.setView(view)
            .setTitle("Edit Task Dialog")
            .setPositiveButton("Save") { dialog, _ ->
                if (descriptionEditText.text.toString().isBlank()) {
                    Toast.makeText(
                        this.context,
                        "Description can't not be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val updatedTask = task.copy(
                        title = titleEditText.text.toString().trim(),
                        description = descriptionEditText.text.toString().trim()
                    )
                    listener.onTaskEdited(updatedTask)
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        return builder.create()
    }

    companion object {
        fun newInstance(task: Task): EditTaskDialogFragment {
            val fragment = EditTaskDialogFragment()
            val args = Bundle().apply {
                putLong("task_id", task.id)
                putString("task_title", task.title)
                putString("task_description", task.description)
                putBoolean("task_completed", task.isCompleted)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            task = Task(
                id = it.getLong("task_id"),
                title = it.getString("task_title", ""),
                description = it.getString("task_description", ""),
                isCompleted = it.getBoolean("task_completed")
            )
        }
    }
}
