package com.example.advancedandroidassignment1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.advancedandroidassignment1.ui.theme.AssignmentTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssignmentTheme {
                AppScreen(dataStoreManager = DataStoreManager(applicationContext))
            }
        }
    }
}

@Composable
fun AppScreen(dataStoreManager: DataStoreManager) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Observe stored data
    val storedData by dataStoreManager.loadData.collectAsState(initial = Triple("", "", ""))

    // Local state variables
    var id by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var courseName by remember { mutableStateOf("") }
    var footerText by remember { mutableStateOf("") }

    // Update UI when data is loaded
    LaunchedEffect(storedData) {
        id = storedData.first
        username = storedData.second
        courseName = storedData.third
        Log.d("DataStore", "Loaded Data - ID: $id, Username: $username, Course: $courseName")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(32.dp) // Increased padding for spacing
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Input fields
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomTextField(value = id, label = "ID", onValueChange = { id = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(value = username, label = "Username", onValueChange = { username = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextField(value = courseName, label = "Course Name", onValueChange = { courseName = it })
                    Spacer(modifier = Modifier.height(32.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    dataStoreManager.loadData.collect { storedValues ->
                                        id = storedValues.first
                                        username = storedValues.second
                                        courseName = storedValues.third
                                        footerText = "$username\n$id"
                                        Log.d("DataStore", "Data Loaded - ID: $id, Username: $username, Course: $courseName")
                                    }
                                }
                            },
                            modifier = Modifier
                                .height(100.dp)
                                .width(160.dp)
                        ) {
                            Text("Load", fontSize = 20.sp)
                        }

                        Button(
                            onClick = {
                                scope.launch {
                                    dataStoreManager.saveData(id, username, courseName)
                                    Toast.makeText(context, "Data Stored Successfully", Toast.LENGTH_SHORT).show()
                                    Log.d("DataStore", "Data Stored - ID: $id, Username: $username, Course: $courseName")
                                }
                            },
                            modifier = Modifier
                                .height(80.dp)
                                .width(140.dp)
                        ) {
                            Text("Store", fontSize = 20.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                dataStoreManager.clearData()
                                id = ""
                                username = ""
                                courseName = ""
                                footerText = ""
                                Toast.makeText(context, "Data Cleared", Toast.LENGTH_SHORT).show()
                                Log.d("DataStore", "Data Cleared")
                            }
                        },
                        modifier = Modifier
                            .height(80.dp)
                            .width(140.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Reset", color = Color.White, fontSize = 20.sp)
                    }
                }

                // Footer section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = footerText,
                        style = TextStyle(fontSize = 24.sp, color = Color.Black)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Aditya Janjanam\n301357523",
                        style = TextStyle(fontSize = 18.sp, color = Color.Gray)
                    )
                }
            }
        }
    )
}

@Composable
fun CustomTextField(value: String, label: String, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 4.dp)
            .background(Color(0xFFF0F0F0), shape = MaterialTheme.shapes.medium)
            .padding(horizontal = 12.dp),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = label,
                        style = TextStyle(fontSize = 20.sp, color = Color.Gray)
                    )
                }
                innerTextField()
            }
        },
        textStyle = TextStyle(fontSize = 20.sp, color = Color.Black)
    )
}
