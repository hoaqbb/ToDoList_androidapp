package com.example.todo_list


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_list.activities.AddActivity
import com.example.todo_list.activities.EditActivity
import com.example.todo_list.adapter.ToDoAdapter
import com.example.todo_list.databinding.ActivityMainBinding
import com.example.todo_list.db.DbHelper
import com.example.todo_list.model.Task
import kotlinx.android.synthetic.main.todo_layout.*

class MainActivity : AppCompatActivity(), ToDoAdapter.RecyclerViewEvent {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DbHelper
    private lateinit var list: List<Task>
    private lateinit var adapter: ToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        getTasks()
        addTask()
    }

    //read all tasks
    private fun getTasks(){
        db = DbHelper(this, null)
        list = db.readTask()
        adapter = ToDoAdapter(list, this)

        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(
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
    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(position: Int) {
        val task = list[position]
        if(checkBox.isChecked){
            task.id?.let { it1 -> db.updateStatus(it1, 1) }
            getTasks()

        } else if (!checkBox.isChecked){
            task.id?.let { it1 -> db.updateStatus(it1, 0) }
            getTasks()
        }
    }

    //button edit task
    override fun onButtonEdit(position: Int) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("id", list[position].id)
        startActivity(intent)
//        finish()
    }

    //on click button delete
    @SuppressLint("NotifyDataSetChanged")
    override fun onButtonDeleteClick(position: Int) {
        val  taskId = list[position].id
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Warning")
        alertDialogBuilder.setMessage("Are you sure you want to delete this task?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            taskId?.let { it1 -> db.deleteTask(it1) }
            dialog.dismiss()
            getTasks()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}




