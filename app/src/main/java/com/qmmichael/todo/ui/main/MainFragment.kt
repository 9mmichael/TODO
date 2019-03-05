package com.qmmichael.todo.ui.main

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.qmmichael.todo.R
import com.qmmichael.todo.data.model.TodoItem
import com.qmmichael.todo.util.FrequentRegax
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONArray
import org.json.JSONException

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
    val mutableList : MutableList<TodoItem> = loadList()

    mainRecyclerView?.let {
      it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    mainRecyclerView?.adapter = MainAdapter(mutableList)

    mainEditText?.let { editText ->
      editText.setOnKeyListener(View.OnKeyListener(fun (view: View, keyCode: Int, event: KeyEvent) : Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && editText.text.toString() != "") {
          if (!FrequentRegax.DESCRIPTION.isMatched(editText.text.toString())) {
            mutableList.add(TodoItem(false, editText.text.toString()))
            saveList(mutableList, context)
            mainRecyclerView?.adapter = MainAdapter(loadList())
          }
          editText.text.clear()
          return true
        }
        return false
      })
      )
    }
  }

  fun saveList(mutableList: MutableList<TodoItem>, context: Context?) {
    val type = Types.newParameterizedType(MutableList::class.java, TodoItem::class.java)
    val jsonAdapter = Moshi.Builder().build().adapter<MutableList<TodoItem>>(type)
    context?.let {
      PreferenceManager.getDefaultSharedPreferences(it)
          .edit()
          .putString("todo_list", jsonAdapter.toJson(mutableList))
          .apply()
    }
  }

  fun loadList() : MutableList<TodoItem> {
    val mutableList : MutableList<TodoItem> = mutableListOf()
    val listJson = PreferenceManager.getDefaultSharedPreferences(context)
        .getString("todo_list", "")
    if (listJson == "[]" || FrequentRegax.INCLUDE_NULL.isMatched(listJson)) {
      return initializeList()
    }
    else {
      try {
        val jsonArray = JSONArray(listJson)
        val type = Types.newParameterizedType(MutableList::class.java, TodoItem::class.java)
        val jsonAdapter = Moshi.Builder().build().adapter<MutableList<TodoItem>>(type)
        val todoItemList = jsonAdapter.fromJson(listJson)
        listJson?.let {
          todoItemList?.let {
            for (i in 0..(jsonArray.length()-1)) {
              mutableList.add(TodoItem(it[i].isChecked, it[i].itemText))
            }
          }
        }
      }
      catch (e: JSONException) {
      }
    }
    return mutableList
  }

  private fun initializeList() : MutableList<TodoItem> {
    val mutableList : MutableList<TodoItem> = mutableListOf()
    saveList(mutableList, context)
    return mutableList
  }

  companion object {
    fun newInstance() = MainFragment()
  }
}

class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  val itemCheckBox = view.findViewById<CheckBox>(R.id.item_check_box)
  val itemText = view.findViewById<TextView>(R.id.item_text_view)
  val itemRemoveButton = view.findViewById<ImageButton>(R.id.item_remove_image_button)

}
