package kz.app.todo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kz.app.todo.models.OnTaskClickType
import kz.app.todo.models.Task

class MainActivity : AppCompatActivity(), AddTaskFragment.AddTaskListener,
    ConfirmDeleteDialogFragment.ConfirmDeleteListener, EditTaskDialogFragment.EditTaskListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var addTaskButton: ImageView
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addTaskButton = findViewById(R.id.task_add)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TaskAdapter(emptyList(), ::performClick)

        recyclerView.adapter = adapter

        viewModel.tasks.observe(this) { tasks ->
            adapter.updateTasks(tasks)
        }

        addTaskButton.setOnClickListener {
            openAddTaskFragment()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    changeMainActivityViewVisibilityState(View.VISIBLE)
                } else {
                    isEnabled = false
                    onBackPressed()
                }
            }
        })
    }

    private fun openAddTaskFragment() {
        changeMainActivityViewVisibilityState(View.GONE)
        val fragment: Fragment = AddTaskFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun changeMainActivityViewVisibilityState(visibility: Int) {
        recyclerView.visibility = visibility
        addTaskButton.visibility = visibility
    }

    override fun onTaskAdded(title: String, description: String) {
        viewModel.addTask(title, description)
        changeMainActivityViewVisibilityState(View.VISIBLE)
    }

    override fun onDeleteConfirmed(taskId: Long) {
        viewModel.deleteTask(taskId)
    }

    override fun onTaskEdited(updatedTask: Task) {
        viewModel.updateTask(updatedTask)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }

    private fun showConfirmDeleteDialog(task: Task) {
        val dialogFragment = ConfirmDeleteDialogFragment.newInstance(task.id, task.title)
        dialogFragment.show(supportFragmentManager, "ConfirmDeleteDialog")
    }

    private fun performClick(onTaskClickType: OnTaskClickType) {
        when (onTaskClickType) {
            is OnTaskClickType.OnDeleteClick -> showConfirmDeleteDialog(onTaskClickType.task)
            is OnTaskClickType.OnUpdateClick -> viewModel.updateTask(onTaskClickType.task)
            is OnTaskClickType.OnTaskClick -> showEditTaskDialog(onTaskClickType.task)
        }
    }

    private fun showEditTaskDialog(task: Task) {
        val dialog = EditTaskDialogFragment.newInstance(task)
        dialog.show(supportFragmentManager, "EditTaskDialog")
    }
}