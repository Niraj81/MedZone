package com.niraj.medzone.UIComponents

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.niraj.medzone.R
import com.niraj.medzone.data.Post


fun darkIt(value: Float) : Float {
        return if(value.isNaN()) 0f
        else value*0.8f
}

@Composable
fun PostCard(
    post: Post,
    bgColor: Long,
    modifier: Modifier = Modifier,
){
    val descriptionOpen = remember  {
        mutableStateOf(false)
    }
    val uriHandler = LocalUriHandler.current
    val ctx = LocalContext.current
    Surface(
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),
        modifier = modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.50f)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(corner = CornerSize(12.dp)),
//                spotColor = MaterialTheme.colorScheme.secondary,
                clip = true
            )
            .clickable(
                onClick = {
                    descriptionOpen.value = !descriptionOpen.value
                }
            ),
        color = MaterialTheme.colorScheme.secondary,
    ) {

        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Row {
                Column() {
                    Text(
                        "Dr. Rajendra Jaiswal",
                        modifier = Modifier.fillMaxWidth(0.8f),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Headache, Nausea, Morning Sickness, blah, blah, blah",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.fillMaxWidth(0.01f))
                        Text(
                            text = post.Address,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            maxLines = 1
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(0.2f))
                ReliefCircle(relief = post.Relief)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Line()
            Spacer(modifier = Modifier.height(5.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_car),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.weight(0.01f))
                Text(
                    text = post.Distance.toString() + " km away",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(0.2f))
                Text(
                    text = "~ Niraj Patidar",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Light
                )
            }
            AnimatedVisibility(visible = descriptionOpen.value) {
                Column() {
                    Spacer(modifier = Modifier.height(5.dp))
                    Row() {
                        Text(
                            text = "Age : ${post.Age}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Gender : ${post.Gender}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Description : ${post.Description}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(){
                        // Call Button
                        Button(
                            onClick = {
                                val uri = Uri.parse("tel://${post.Contact}")
                                uriHandler.openUri(uri.toString())
                            },
                            modifier = Modifier.weight(0.5f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Phone, null)
                            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                            Text(
                                text = "Call",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        // Open in map button
                        Button(
                            onClick = {
                                val uri = Uri.parse("geo:0,0?q=${post.Address}")
//                                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
//                                mapIntent.setPackage("com.google.android.apps.maps")
//                                startActivity(mapIntent)
                                uriHandler.openUri(uri.toString())
                            },
                            modifier = Modifier.weight(0.5f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Icon(imageVector = Icons.Default.LocationOn, null)
                            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                            Text(
                                text = "Locate",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun Line(){
    var color = MaterialTheme.colorScheme.onSecondary
    Canvas(modifier = Modifier.fillMaxWidth(1f)){
        drawLine(
            color = color,
            start = Offset.Zero,
            end = Offset(size.width, 0f)
        )
    }
}

@Composable
fun ReliefCircle(relief : Int){
    val tertColor = MaterialTheme.colorScheme.tertiaryContainer
    Box(
        contentAlignment = Alignment.Center
    ){
        Canvas(
            modifier = Modifier
                .height(30.dp)
                .width(30.dp),
        ){
            drawArc(
                color = tertColor,
                startAngle = -90f,
                sweepAngle = (relief.toFloat()/100) * 347f,
                true,
                style = Stroke(
                    width = 19f
                )
            )
            drawCircle(
                color = Color.White,
                radius = 37f,
            )

        }
        Text(
            text = "$relief%",
            style = MaterialTheme.typography.displaySmall,
            fontSize = 8.sp,
            color = Color.Black
        )
    }
}


@Preview()
@Composable
fun PostPreview() {
    val post = Post(
        Address = "Indore, Madhya Pradesh",
        Age = 20,
        Description = "Lorel ipsum, dahi bada, mauj masti",
        Distance = 263.0,
        DoctorName = "Niraj Jain",
        Gender = "Male",
        Matched = 3,
        Contact = "8827920256",
        Relief = 70,
        Symptoms = listOf(
            "Nausea",
            "Headache",
            "Pale Skin"
        ),
        UserName = "Niraj Patidar"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val colours = listOf<Long>(
            0xFFFFEBE5,
            0xFFfdf1dc,
            0xFFcfecff
        )
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                count = 3
            ){ item->
                Spacer(modifier = Modifier.height(25.dp))
                PostCard(post = post, colours[item])
            }
        }
    }
}
