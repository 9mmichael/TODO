package com.qmmichael.todo.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.qmmichael.todo.R
import com.qmmichael.todo.R.layout

/**
 * Created by 9mmichael on 2019/02/16.
 */

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.main_activity)

    val fragment = MainFragment()
    supportFragmentManager.beginTransaction()
        .replace(R.id.containerLayout, fragment).commit()
  }
}
