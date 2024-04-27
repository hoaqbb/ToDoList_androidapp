package com.example.todo_list.model

data class Task(
    var id: Int? = null,
    var title: String,
    var status: Int
)