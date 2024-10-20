package kz.app.todo.models

import kz.app.todo.TaskRepository

data class Task(
    val id: Long = TaskRepository.nextId,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)
