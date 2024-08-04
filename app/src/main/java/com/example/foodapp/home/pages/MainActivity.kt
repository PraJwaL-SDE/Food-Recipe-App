package com.example.foodapp.home.pages

import Meal
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import com.example.foodapp.network.ApiClient
import com.example.foodapp.ui.theme.FoodappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        Home()
                    }
                }
            }
        }
    }
}

@Composable
fun ElevatedCardExample(mealId: String) {
    val context = LocalContext.current
    var meal by remember { mutableStateOf<Meal?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    // Load meal data when the composable is first used
    LaunchedEffect(mealId) {
        ApiClient().getData(context) { data ->
            meal = data
        }
    }



    ElevatedCard(
        onClick = {showDialog = true},
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .size(width = 190.dp, height = 350.dp)
            .padding(7.dp)
    ) {
        Column {
            meal?.let {
                Box(modifier = Modifier.size(height = 200.dp, width = 200.dp)) {
                    SubcomposeAsyncImage(
                        model = it.strMealThumb,
                        contentDescription = null,
                        loading = { CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)) }
                    )
                }
                Text(
                    text = it.strMeal,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            } ?: run {
                // Optionally, show a loading indicator or placeholder
                CircularProgressIndicator(modifier = Modifier
                    .fillMaxSize()
                    .align(alignment = Alignment.CenterHorizontally))
            }
        }
    }
    if(showDialog == true){
        Dialog(onDismissRequest = {
            showDialog = false
        }, properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
        ) {
            // Draw a rectangle shape with rounded corners inside the dialog

            Box (
                modifier = Modifier
                    .fillMaxSize(),

                contentAlignment = Alignment.Center
            ){

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        ,
                    shape = RoundedCornerShape(16.dp),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item {
                            Box(modifier = Modifier.width(width = 500.dp)) {
                                SubcomposeAsyncImage(
                                    model = meal!!.strMealThumb,
                                    contentDescription = null,
                                    modifier = Modifier.align(alignment =  Alignment.Center),
                                    loading = {
                                        CircularProgressIndicator(
                                            modifier = Modifier.align(
                                                Alignment.Center
                                            )
                                        )
                                    }
                                )
                            }
                        }
                        item{
                            Text(
                                text = meal!!.strMeal,
                                modifier = Modifier.padding(4.dp),
                            )
                        }
                        item{
                            Text(
                                text = meal!!.strInstructions,
                                modifier = Modifier.padding(4.dp),
                            )
                        }
                        item{
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                TextButton(
                                    onClick = { showDialog = false },
                                    modifier = Modifier.padding(8.dp),
                                ) {
                                    Text("Dismiss")
                                }
                                TextButton(
                                    onClick = { },
                                    modifier = Modifier.padding(8.dp),
                                ) {
                                    Text("Confirm")
                                }
                            }
                        }



                    }
                }
            }


        }
    }

}


@Composable
fun Home() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Replace with a list of IDs or relevant data
        val mealIds = List(100) { "sampleMealId" }

        mealIds.chunked(2).forEach { chunk ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                chunk.forEach { mealId ->
                    ElevatedCardExample(mealId)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodappTheme {
        Home()
    }
}
