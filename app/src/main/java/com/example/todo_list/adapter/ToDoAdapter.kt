package com.example.todo_list.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.CheckBox
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list.R
import com.example.todo_list.model.Task
import kotlinx.android.synthetic.main.todo_layout.view.*


class ToDoAdapter (private val list: MutableList<Task>, private val context: Context, private val listener: RecyclerViewEvent): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){

    class ToDoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_layout, parent, false)
        return ToDoViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val currentItem = list[position]

        holder.checkBox.text = currentItem.title
        if(currentItem.status>0){
            holder.checkBox.isChecked = true
        }
        holder.checkBox.setOnCheckedChangeListener{ _, isChecked ->
            listener.onItemClick(position, isChecked)
        }
        holder.checkBox.setOnLongClickListener {
            val popupMenu = PopupMenu(context ,holder.checkBox)
            popupMenu.inflate(R.menu.menu_holder)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.mnuEdit -> {
                        listener.onMenuEdit(position)
                        true
                    }
                    R.id.mnuComplete -> {
                        listener.onMenuMaskComplete(position)
                        true
                    }
                    R.id.mnuDelete -> {
                        listener.onMenuDelete(position)
                        notifyDataSetChanged()
                        true
                    }
                    else -> {false}
                }
            }
            popupMenu.show()
            true
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }



    //interface on click event
    interface RecyclerViewEvent {
        fun onItemClick(position: Int, isChecked: Boolean)
        fun onMenuDelete(position: Int)
        fun onMenuEdit(position: Int)
        fun onMenuMaskComplete(position: Int)

    }
}