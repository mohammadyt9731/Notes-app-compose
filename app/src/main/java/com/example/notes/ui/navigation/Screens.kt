package com.example.notes.ui.navigation

sealed class Screens(val route: String) {
    object Notes : Screens("notes")
    object Note : Screens("note")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}