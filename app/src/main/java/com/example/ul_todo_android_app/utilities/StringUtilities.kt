package com.example.ul_todo_android_app.utilities

class StringUtilities {

    companion object {
        fun areStringsMatched(string1: String?, string2: String?): Boolean {
            return string1 != null && string2 != null && string1 == string2
        }
    }
}