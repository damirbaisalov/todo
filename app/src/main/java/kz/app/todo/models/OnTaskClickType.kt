package kz.app.todo.models

sealed class OnTaskClickType {

    class OnTaskClick(val task: Task): OnTaskClickType()
    class OnUpdateClick(val task: Task): OnTaskClickType()
    class OnDeleteClick(val task: Task): OnTaskClickType()
}