package com.qmmichael.todo.util

import java.util.regex.Pattern

enum class FrequentRegax(val value: String) {
  DESCRIPTION("\n|\\s"),
  INCLUDE_NULL(".*null.*");

  fun isMatched(value: String?): Boolean {
    return Pattern.compile(this.value, Pattern.DOTALL).matcher(value).matches()
  }
}
