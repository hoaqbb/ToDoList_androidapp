package com.example.todo_list


import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list.activities.AddActivity
import com.example.todo_list.activities.EditActivity
import com.example.todo_list.adapter.ToDoAdapter
import com.example.todo_list.databinding.ActivityMainBinding
import com.example.todo_list.db.DbHelper
import com.example.todo_list.model.Task

class MainActivity : AppCompatActivity(), ToDoAdapter.RecyclerViewEvent {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DbHelper
    private lateinit var list: MutableList<Task>
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: ToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        db = DbHelper(this, null)
        recyclerview = binding.recyclerview

        getTasks()
        addTask()
    }

    //read all tasks
    private fun getTasks(){
        list = db.readTask().toMutableList()
        adapter = ToDoAdapter(list, this, this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    //On click button add task
    private fun addTask(){
        binding.btnAddTask.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
}

    //on click task update status
    override fun onItemClick(position: Int,  isChecked: Boolean) {
        val task = list[position]
        task.id?.let {
            val newStatus = if (isChecked) 1 else 0
            db.updateStatus(it, newStatus)
            task.status = newStatus // Cập nhật trạng thái trong danh sách
        }
    }

    override fun onMenuMaskComplete(position: Int) {
        val task = list[position]
        if(task.status==0){
            task.id?.let {
                db.updateStatus(it, 1)
                task.status = 1 // Cập nhật trạng thái trong danh sách
                adapter.notifyItemChanged(position)
            }
        }
    }

    //button edit task
    override fun onMenuEdit(position: Int) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("id", list[position].id)
        startActivity(intent)
    }

    //on click button delete
    override fun onMenuDelete(position: Int) {
        val  taskId = list[position].id
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Warning")
        alertDialogBuilder.setMessage("Are you sure you want to delete this task?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            taskId?.let { it1 -> db.deleteTask(it1) }
            getTasks()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}




