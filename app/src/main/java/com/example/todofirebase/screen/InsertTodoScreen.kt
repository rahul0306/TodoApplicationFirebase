package com.example.todofirebase.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todofirebase.model.Todo
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InsertTodoScreen(
    modifier: Modifier = Modifier,
    id: String?,
    navController: NavController
) {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }

    val todos = FirebaseFirestore.getInstance()

    val todoDb = todos.collection("todo")


    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (id != "defaultId") {
            todoDb.document(id.toString()).get().addOnSuccessListener {
                val data = it.toObject(Todo::class.java)
                title.value = data!!.title
                description.value = data.description
            }
        }
    }
    Scaffold(
        floatingActionButton =
        {
            FloatingActionButton(
                onClick = {
                    if (title.value.isEmpty() && description.value.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Title and Description cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val myTodoId = if (id != "defaultId") {
                            id.toString()
                        } else {
                            todoDb.document().id
                        }
                        val todo = Todo(
                            id = myTodoId,
                            title = title.value,
                            description = description.value
                        )
                        todoDb.document(myTodoId).set(todo).addOnCompleteListener {
                            if (it.isSuccessful) {
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(corner = CornerSize(15.dp))
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Done")
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
                text = "Insert Todo",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = modifier.padding(10.dp)
            )
            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it },
                label = {
                    Text(text = "Enter Title")
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(corner = CornerSize(7.dp))
            )
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = {
                    Text(text = "Enter Description")
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(corner = CornerSize(7.dp))
            )
        }
    }
}