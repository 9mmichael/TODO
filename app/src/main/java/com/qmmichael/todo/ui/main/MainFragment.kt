package com.qmmichael.todo.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.qmmichael.todo.R

/**
 * Created by 9mmichael on 2019/02/16.
 */

class MainFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.main_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    val mainRecyclerView: RecyclerView? = view?.findViewById(R.id.main_recycler_view)
    val mainEditText: EditText? = view?.findViewById(R.id.edit_text)

    refreshTodoItem(mainRecyclerView)

    mainRecyclerView?.let {

      it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    mainEditText?.let { editText ->
      editText.setOnKeyListener(View.OnKeyListener(fun (view: View, keyCode: Int, event: KeyEvent) : Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && editText.text.toString() != "") {
          setTodoItem(editText)
          editText.text.clear()
          refreshTodoItem(mainRecyclerView)
          return true
        }
        return false
      })
      )

    }
  }

  fun setTodoItem(editText: EditText) {
    val text = editText.text.toString()
    activity?.let {
      val textItems = it.getSharedPreferences("item_text", Context.MODE_PRIVATE)
          .getStringSet("item_text", mutableSetOf())

      textItems?.add(text)

      it.getSharedPreferences("item_text", Context.MODE_PRIVATE)
          .edit()
          .putStringSet("item_text", textItems)
          .apply()
    }
  }

  fun refreshTodoItem(recyclerView: RecyclerView?) {
    activity?.let {
      val textItems = it.getSharedPreferences("item_text", Context.MODE_PRIVATE)
          .getStringSet("item_text", mutableSetOf())?.toMutableList()

      recyclerView?.let { recyclerView ->
            textItems?.let { textItems ->
              recyclerView.adapter = MainAdapter(textItems)
            }
      }
    }
  }
}


class MainAdapter(private val itemList: MutableList<String>) : RecyclerView.Adapter<MainViewHolder>() {
  override fun onCreateViewHolder(
    p0: ViewGroup,
    p1: Int
  ): MainViewHolder {
    val layoutInflater = LayoutInflater.from(p0.context)
    return MainViewHolder(layoutInflater.inflate(R.layout.main_list_item, p0, false))
  }

  override fun onBindViewHolder(
    p0: MainViewHolder,
    p1: Int
  ) {
    p0.itemText.text = itemList.get(p1)
  }
  override fun getItemCount(): Int {
    return itemList.count()
  }
}

class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  val itemCheckBox = view.findViewById<CheckBox>(R.id.item_check_box)
  val itemText = view.findViewById<TextView>(R.id.item_text_view)

}
