package com.example.mywearosapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ListAdapter(context: Context, dataArgs: ArrayList<String>) :
    RecyclerView.Adapter<ListAdapter.RecyclerViewHolder?>() {
    private var dataSource = ArrayList<String>()

    private val drawableIcon: String? = null
    private val context: Context

    init {
        this.context = context
        dataSource = dataArgs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return RecyclerViewHolder(view)
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var menuContainer: RelativeLayout
        var menuItem: TextView

        init {
            menuContainer = view.findViewById(R.id.menu_container)
            menuItem = view.findViewById(R.id.menu_item)
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.menuItem.text = dataSource[position]
    }

    override fun getItemCount() = dataSource.size

}