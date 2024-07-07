package com.example.musicappui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.Card
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SubscriptionView(){
    Column(modifier=Modifier.height(200.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = "Manange Subscription", color = Color.Black)
        Card(modifier=Modifier.padding(8.dp), elevation = 4.dp) {
            Column(modifier=Modifier.padding(8.dp)) {
                Column() {
                    Text(text = "Musical", color = Color.Black)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement =Arrangement.SpaceBetween) {
                        Text(text = "Free Tier", color = Color.Black)
                        TextButton(onClick = {  }) {
                            Row {
                                Text(text = "See All Plans" , color = colorResource(id = R.color.purple_500))
                                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "See All Plans", tint = Color.Black)
                            }

                        }

                    }

                }
                Divider(thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp))
                Row(modifier = Modifier.padding(vertical = 16.dp)) {
                    Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Get a Plan", modifier = Modifier.padding(end =4.dp), tint = Color.Black)
                    Text(text = "Get a Plan", color = Color.White)
            }

                
            }
        }

    }
}