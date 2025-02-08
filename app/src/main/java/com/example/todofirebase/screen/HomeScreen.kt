package com.example.todofirebase.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todofirebase.model.Todo
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val todos = FirebaseFirestore.getInstance()

    val todosDb = todos.collection("todo")
    val todoList = remember {
        mutableStateListOf<Todo>()
    }
    val loading = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        todosDb.addSnapshotListener { value, error ->
            if (error != null) {
                loading.value = false
            } else {
                val data = value?.toObjects(Todo::class.java)
                todoList.clear()
                data?.let { todoList.addAll(it) }
                loading.value = true
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(route = "insert_todo"+"/defaultId")
                },
                shape = RoundedCornerShape(corner = CornerSize(15.dp))
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Note")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Notes",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            if (loading.value) {
                LazyColumn {
                    items(todoList) { todo ->
                        Todo(
                            todo = todo,
                            todoDb = todosDb,
                            navController = navController
                        )
                    }
                }
            } else {
                Box(modifier = modifier.align(Alignment.CenterHorizontally)) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun Todo(
    modifier: Modifier = Modifier,
    todo: Todo,
    todoDb: CollectionReference,
    navController: NavController
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                navController.navigate(route = "insert_todo"+"/${todo.id}")
            },
        shape = RoundedCornerShape(corner = CornerSize(7.dp))
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = todo.title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = {
                    todoDb.document(todo.id).delete()
                }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete ")
                }
            }
            Spacer(modifier = modifier.height(5.dp))
            Text(
                text = todo.description,
                style = TextStyle(
                    fontWeight = FontWeight.W200,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Serif
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}