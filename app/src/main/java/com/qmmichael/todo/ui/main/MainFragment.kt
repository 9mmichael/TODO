package com.qmmichael.todo.ui.main

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
import android.widget.ImageButton
import android.widget.TextView
import com.qmmichael.todo.R
import com.qmmichael.todo.data.model.TodoItem
import com.qmmichael.todo.util.FrequentRegax

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
    val mutableList =  MutableList(1,{ TodoItem(false, "hoge") })

    mainRecyclerView?.let {
      it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    mutableList.let {
      mainRecyclerView?.adapter = MainAdapter(it)
    }

    mainEditText?.let { editText ->
      editText.setOnKeyListener(View.OnKeyListener(fun (view: View, keyCode: Int, event: KeyEvent) : Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && editText.text.toString() != "") {
          mutableList.let { mutableList ->
            if (!FrequentRegax.DESCRIPTION.isMatched(editText.text.toString())) {
              mutableList.add(TodoItem(false, editText.text.toString()))
            }
          }
          editText.text.clear()
          return true
        }
        return false
      })
      )
    }
  }
}

class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  val itemCheckBox = view.findViewById<CheckBox>(R.id.item_check_box)
  val itemText = view.findViewById<TextView>(R.id.item_text_view)
  val itemRemoveButton = view.findViewById<ImageButton>(R.id.item_remove_image_button)

}
