package com.example.todofirebase.navigation

sealed class NavigationScreen(val route:String) {
    object SplashScreen:NavigationScreen(
        route = "splash"
    )
    object HomeScreen:NavigationScreen(
        route = "home"
    )
    object InsertTodoScreen:NavigationScreen(
        route = "insert_todo"
    )
}