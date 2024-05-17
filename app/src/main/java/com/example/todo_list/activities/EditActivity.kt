package com.example.todo_list.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.example.todo_list.MainActivity
import com.example.todo_list.databinding.ActivityAddBinding
import com.example.todo_list.db.DbHelper

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val db = DbHelper(this, null)
        val id = intent.getIntExtra("id",-1)
        val task = db.getTaskById(id)
        binding.txtInput.text = Editable.Factory.getInstance().newEditable(task?.title)

        binding.btnSave.setOnClickListener {
            if(binding.txtInput.text.toString().trim().isNotEmpty()){

                val newTitle = binding.txtInput.text.toString()

                val rs = db.editTask(id, newTitle)
                if(rs<1){
                    Toast.makeText(this,
                        " Cannot edit task!",
                        Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this,
                        " Task is edited!",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.txtInput.text.clear()
                }
            }else {
                Toast.makeText(this,
                    " Cannot edit task!",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}