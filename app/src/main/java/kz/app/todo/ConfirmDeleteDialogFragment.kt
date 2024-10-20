package kz.app.todo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ConfirmDeleteDialogFragment : DialogFragment() {

    interface ConfirmDeleteListener {
        fun onDeleteConfirmed(taskId: Long)
    }

    private lateinit var clickListener: ConfirmDeleteListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ConfirmDeleteListener) {
            clickListener = context
        } else {
            throw RuntimeException("$context must implement ConfirmDeleteListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val taskId = arguments?.getLong(TASK_TO_DELETE_ID) ?: 0L
        val taskTitle = arguments?.getString(TASK_TO_DELETE_TITLE) ?: ""
        return AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Do you want to delete task with title -> $taskTitle?")
            .setPositiveButton("Delete") { _, _ -> clickListener.onDeleteConfirmed(taskId) }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()
    }

    companion object {
        const val TASK_TO_DELETE_ID = "taskToDeleteId"
        const val TASK_TO_DELETE_TITLE = "taskToDeleteTitle"

        fun newInstance(taskId: Long, taskTitle: String): ConfirmDeleteDialogFragment {
            val fragment = ConfirmDeleteDialogFragment()
            val args = Bundle().apply {
                putLong(TASK_TO_DELETE_ID, taskId)
                putString(TASK_TO_DELETE_TITLE, taskTitle)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
