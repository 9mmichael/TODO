package com.qmmichael.todo.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qmmichael.todo.R
import com.qmmichael.todo.data.model.TodoItem

class MainAdapter(private val itemList: MutableList<TodoItem>) : RecyclerView.Adapter<MainViewHolder>() {
  override fun onCreateViewHolder(
    parent: ViewGroup,
    position: Int
  ): MainViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val mView = MainViewHolder(layoutInflater.inflate(R.layout.main_list_item, parent, false))

    mView.itemCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
      if (isChecked) {
        mView.itemRemoveButton.visibility = View.VISIBLE
        mView.itemRemoveButton.setOnClickListener {
          removeListItem(itemList, mView.layoutPosition)
          val fragment = MainFragment.newInstance()
          fragment.saveList(itemList, parent.context)
        }
      }
      else {
        mView.itemRemoveButton.visibility = View.INVISIBLE
      }
    }

    return mView
  }

  override fun onBindViewHolder(
    holder: MainViewHolder,
    position: Int
  ) {
    holder.itemCheckBox.isChecked = itemList.get(position).isChecked
    holder.itemText.text = itemList.get(position).itemText
  }

  override fun getItemCount(): Int {
      return itemList.count()
  }

  fun removeListItem(mutableList: MutableList<TodoItem>, index: Int) {
    mutableList.removeAt(index)
    notifyItemRemoved(index)
  }
}
