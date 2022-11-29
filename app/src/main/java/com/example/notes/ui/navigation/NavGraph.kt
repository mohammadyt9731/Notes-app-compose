package com.example.notes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notes.ui.screens.AddNoteScreen
import com.example.notes.ui.screens.NotesScreen

const val IS_TASK_KEY = "is_task_key"
const val ID_KEY = "id_key"

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screens.Notes.route)
    {
        composable(route = Screens.Notes.route) {
            NotesScreen(navController)
        }
        composable(route = "${Screens.Note.route}/{${IS_TASK_KEY}}/{${ID_KEY}}",
            arguments = listOf(
                navArgument(name = IS_TASK_KEY) {
                    type = NavType.BoolType
                },
                navArgument(
                    name = ID_KEY,
                ) {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            AddNoteScreen(
                navController = navController,
                isTask = entry.arguments?.getBoolean(IS_TASK_KEY),
                id = entry.arguments?.getInt(ID_KEY) ?: -1
            )
        }
    }
}