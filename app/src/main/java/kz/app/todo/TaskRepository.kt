package kz.app.todo

import kz.app.todo.models.Task

import androidx.lifecycle.MutableLiveData

object TaskRepository {

    private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val tasks: MutableLiveData<MutableList<Task>> = _tasks

    var nextId = 0L

    fun addTask(task: Task) {
        _tasks.value?.add(task.copy(id = nextId++))
        _tasks.postValue(_tasks.value)
    }

    fun updateTask(updatedTask: Task) {
        _tasks.value?.indexOfFirst { it.id == updatedTask.id }?.let { index ->
            if (index != -1) {
                _tasks.value?.set(index, updatedTask)
                _tasks.postValue(_tasks.value)
            }
        }
    }

    fun deleteTask(taskId: Long) {
        _tasks.value?.removeIf { it.id == taskId }
        _tasks.postValue(_tasks.value)
    }

    fun clear() {
        _tasks.value?.clear()
        _tasks.postValue(_tasks.value)
    }
}

