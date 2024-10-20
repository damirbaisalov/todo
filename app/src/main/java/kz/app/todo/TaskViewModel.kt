package kz.app.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.app.todo.models.Task

class TaskViewModel : ViewModel() {

    val tasks: MutableLiveData<MutableList<Task>> = TaskRepository.tasks

    init {
        addSampleTasks()
    }

    fun addTask(title: String, description: String) {
        TaskRepository.addTask(Task(title = title, description = description))
    }

    fun updateTask(task: Task) {
        TaskRepository.updateTask(task)
    }

    fun deleteTask(taskId: Long) {
        TaskRepository.deleteTask(taskId)
    }

    fun clear() {
        TaskRepository.clear()
    }

    private fun addSampleTasks() {
        addTask("taskTitle", "Description 1")
        addTask("taskTitle", "Description 2")
    }
}