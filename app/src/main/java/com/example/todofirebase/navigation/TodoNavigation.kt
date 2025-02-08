package com.example.todofirebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todofirebase.screen.HomeScreen
import com.example.todofirebase.screen.InsertTodoScreen
import com.example.todofirebase.screen.SplashScreen

@Composable
fun TodoNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationScreen.SplashScreen.route){

        composable(route = "splash"){
            SplashScreen(navController = navController)
        }
        composable(route = "home"){
            HomeScreen(navController = navController)
        }
        composable(route = "insert_todo"+"/{id}"){
            val id = it.arguments?.getString("id")
            InsertTodoScreen(navController = navController, id = id)
        }
    }
}