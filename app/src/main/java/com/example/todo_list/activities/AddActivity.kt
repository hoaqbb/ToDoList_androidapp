package com.example.todo_list.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todo_list.MainActivity
import com.example.todo_list.adapter.ToDoAdapter
import com.example.todo_list.databinding.ActivityAddBinding
import com.example.todo_list.db.DbHelper
import com.example.todo_list.model.Task

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.btnSave.setOnClickListener{
            if(binding.txtInput.text.toString().trim().isNotEmpty()){
                val db = DbHelper(this, null)
                val task = Task(
                    null,
                    binding.txtInput.text.toString(),
                    0
                )
                val rs = db.addTask(task)
                if(rs<1){
                    Toast.makeText(this,
                        " Cannot add task!",
                        Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this,
                        " Task is added!",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.txtInput.text.clear()
                }
            }else {
                    Toast.makeText(this,
                        " Cannot add task!",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }



