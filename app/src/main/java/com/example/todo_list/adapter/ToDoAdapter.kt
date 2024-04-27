package com.example.todo_list.adapter

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list.R
import com.example.todo_list.model.Task
import kotlinx.android.synthetic.main.todo_layout.view.*


class ToDoAdapter (private val list: List<Task>, private val listener: RecyclerViewEvent): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){

    inner class ToDoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_layout, parent, false)
        return ToDoViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.itemView.apply {
            checkBox.text = list[position].title
            if (list[position].status > 0) {
                view.checkBox.isChecked = true
            }
//            checkBox.isChecked = list[position].status > 0
            checkBox.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked){
                    listener.onItemClick(position)
                } else{
                    listener.onItemClick(position)
                }
            }

            btnDelete.setOnClickListener{
                listener.onButtonDeleteClick(position)
            }

            btnEdit.setOnClickListener{
                listener.onButtonEdit(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    //interface on click event
    interface RecyclerViewEvent {
        fun onItemClick(position: Int)
        fun onButtonDeleteClick(position: Int)
        fun onButtonEdit(position: Int)
    }
}