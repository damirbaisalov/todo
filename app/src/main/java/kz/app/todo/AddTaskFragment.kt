package kz.app.todo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class AddTaskFragment : Fragment() {

    interface AddTaskListener {
        fun onTaskAdded(title: String, description: String)
    }

    private lateinit var listener: AddTaskListener

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addButton: Button
    private lateinit var viewModel: TaskViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddTaskListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement AddTaskListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleEditText = view.findViewById(R.id.title_edit_text)
        descriptionEditText = view.findViewById(R.id.description_edit_text)
        addButton = view.findViewById(R.id.add_button)

        viewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        addButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()
            if (description.isNotBlank()) {
                listener.onTaskAdded(title, description)
                closeFragment()
            } else {
                Toast.makeText(
                    this.context,
                    "Enter description please!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun closeFragment() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}
