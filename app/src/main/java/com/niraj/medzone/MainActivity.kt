package com.niraj.medzone

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.MedZoneTheme
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.niraj.medzone.UIComponents.PostCard
import com.niraj.medzone.data.Post
import com.niraj.medzone.data.rememberPostState
import com.niraj.medzone.destinations.FindScreenDestination
import com.niraj.medzone.destinations.PostScreenDestination
import com.niraj.medzone.destinations.WritePostScreenDestination
import com.niraj.medzone.ui.theme.montserratFamily
import com.niraj.medzone.utils.stringToList
import com.niraj.medzone.viewModel.mainViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency


class MainActivity : ComponentActivity() {
    private val viewModel: mainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel = ViewModelProvider(this).get(mainViewModel::class.java)
        val lifeCycleOwner = this
        setContent {
            MedZoneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root, dependenciesContainerBuilder = {
                        dependency(mainViewModel)
                        dependency(lifeCycleOwner)
                    })
                }
            }
        }
    }
}

@Destination(start = true)
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    mainViewModel: mainViewModel
){

    Surface (
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ){
        val state = rememberWebViewState("https://0x0.st/o50s.svg")
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
        ) {
            Text(
                text = "MEDZONE",
                fontFamily = montserratFamily,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Find the best doctor available in your locality",
                fontFamily = montserratFamily,
                modifier = Modifier.fillMaxWidth(0.6f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(id = R.drawable.cardiologist_transparent),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .weight(0.4f)
                    .clip(shape = RoundedCornerShape(38.dp)),
            )

            // To use animated SVG
//            WebView(state = state, modifier = Modifier.fillMaxWidth(0.8f).weight(0.4f).clip(shape = RoundedCornerShape(38.dp)))
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Find your issue",
                fontFamily = montserratFamily,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "No need to change doctor every week, write down your symptoms and get started!",
                fontFamily = montserratFamily,
                modifier = Modifier.fillMaxWidth(0.6f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    navigator.navigate(FindScreenDestination())
                },
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .shadow(
                        elevation = 8.dp,
                        spotColor = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    ),

                ) {
                Text(
                    text = "Get Started",
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Button(
                onClick = { navigator.navigate(WritePostScreenDestination()) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = "Write post",
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritePostScreen(
    navigator: DestinationsNavigator,
    mainViewModel: mainViewModel,
    lifecycleOwner: LifecycleOwner
){


    Surface (
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ){
        val localFocusManager = LocalFocusManager.current
        val context = LocalContext.current

        val post = rememberPostState(UserName = "",
            DoctorName = "",
            Symptoms = mutableListOf(),
            Description = "",
            Age = 0,
            Address = "",
            Gender = "",
            Contact = "",
            Relief = 0f
        )
        val currentSymptom = remember  {
            mutableStateOf("")
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
        ) {
            Text(
                text = "MEDZONE",
                fontFamily = montserratFamily,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                item {
                    Text(
                        text = "Help others by writing your experience with certain symptoms and doctors :)",
                        fontFamily = montserratFamily,
//                        color = Color(0xFF6B6B6B),
                        modifier = Modifier.fillMaxWidth(0.8f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Image(
                        painter = painterResource(id = R.drawable.help_out),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .weight(0.4f)
                            .clip(shape = RoundedCornerShape(38.dp)),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row (
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ){
                        TextField(
                            value = post.UserName,
                            onValueChange = {
                                post.UserName = it
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color(0xFFF14351)
                            ),
                            label = {
                                Text(text = "User Name")
                            },
                            modifier = Modifier.weight(0.7f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = {
                                localFocusManager.moveFocus(FocusDirection.Next)
                            })
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        TextField(
                            value = if(post.Age != 0) post.Age.toString() else "",
                            onValueChange = {
                                if((it.length > 0)){
                                    if( (it.isDigitsOnly()) and (it.toInt() < 120)){
                                        post.Age = it.toInt()
                                    }
                                }else{
                                    post.Age = 0
                                }
                            },
                            label = {
                                Text(text = "Age")
                            },
                            modifier = Modifier.weight(0.35f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions(onNext = {
                                localFocusManager.moveFocus(FocusDirection.Next)
                            })
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row (
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ){
                        TextField(
                            value = currentSymptom.value,
                            onValueChange = { it ->
                                currentSymptom.value = it
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color(0xFFF14351)
                            ),
                            label = {
                                Text(text = "Symptoms")
                            },
                            supportingText = {
                                Text("Please seperate your symptoms by comma",
                                    fontSize = 7.sp,
                                    maxLines = 1
                                )
                            },
                            modifier = Modifier.weight(0.7f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = {
                                localFocusManager.moveFocus(FocusDirection.Next)
                            })
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        TextField(
                            value = post.Gender,
                            onValueChange = {
                                post.Gender = it
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color(0xFFF14351)
                            ),
                            label = {
                                Text(text = "Gender")
                            },
                            supportingText = {
                                Text("")
                            },
                            modifier = Modifier.weight(0.35f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = {
                                localFocusManager.moveFocus(FocusDirection.Down)
                            })
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = post.DoctorName,
                        onValueChange = {
                            post.DoctorName = it
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color(0xFFF14351)
                        ),
                        label = {
                            Text(text = "Doctor's Name")
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            localFocusManager.moveFocus(FocusDirection.Next)
                        })
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // Relief Row
                    Row(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Relief :",
                            style = MaterialTheme.typography.labelMedium,
                            fontFamily = montserratFamily
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Slider(
                            value = post.Relief,
                            onValueChange = {
                                post.Relief = it
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFf14351)
                            ),
                            modifier = Modifier.weight(0.8f)
                        )
                        AnimatedVisibility(post.Relief != 0f){
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${(post.Relief * 100).toInt()}%", /* TODO: make sure to multiply the relief by 100 after everything's done*/
                                style = MaterialTheme.typography.labelMedium,
                                fontFamily = montserratFamily
                            )
                        }
                    }
                    TextField(
                        value = post.Address,
                        onValueChange = {
                            post.Address = it
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color(0xFFF14351)
                        ),
                        label = {
                            Text(text = "Doctor's Address")
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            localFocusManager.moveFocus(FocusDirection.Next)
                        })
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = post.Contact,
                        onValueChange = {
                            post.Contact = it
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color(0xFFF14351)
                        ),
                        label = {
                            Text(text = "Doctor's Contact")
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Phone),
                        keyboardActions = KeyboardActions(onNext = {
                            localFocusManager.moveFocus(FocusDirection.Next)
                        })
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = post.Description,
                        onValueChange = {
                            post.Description = it
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color(0xFFF14351)
                        ),
                        label = {
                            Text(text = "Description")
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                        keyboardActions = KeyboardActions(onGo = {
                            val sPost = Post(
                                UserName = post.UserName,
                                DoctorName = post.DoctorName,
                                Symptoms = stringToList(currentSymptom.value),
                                Description = post.Description,
                                Age = post.Age,
                                Address = post.Address,
                                Gender = post.Gender,
                                Contact = post.Contact,
                                Relief = (post.Relief * 100).toInt(),
                                Matched = 0,
                                Distance = 0.0
                            )
                            mainViewModel.writePost(sPost)
                            Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show()
                            localFocusManager.clearFocus()
                        })
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        onClick = {
                            Log.d("JSON", "ONE")
                            val sPost = Post(
                                UserName = post.UserName,
                                DoctorName = post.DoctorName,
                                Symptoms = stringToList(currentSymptom.value),
                                Description = post.Description,
                                Age = post.Age,
                                Address = post.Address,
                                Gender = post.Gender,
                                Contact = post.Contact,
                                Relief = (post.Relief * 100).toInt(),
                                Matched = 0,
                                Distance = 0.0
                            )
                            mainViewModel.writePost(sPost)
                            Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .shadow(
                                elevation = 8.dp,
                                spotColor = MaterialTheme.colorScheme.primary,
                                ambientColor = Color.Blue,
                                shape = RoundedCornerShape(corner = CornerSize(50.dp))
                            ),
                    ) {
                        Text(
                            text = "Save",
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Button(
                        onClick = { navigator.popBackStack() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Text(
                            text = "Back",
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Destination
@Composable
fun PostScreen(
    navigator : DestinationsNavigator,
    mainViewModel: mainViewModel,
    lifecycleOwner: LifecycleOwner
){


    val isLoaded = remember {
        mutableStateOf(false)
    }
    mainViewModel.loaded.observe(lifecycleOwner){
        Log.d("JSON", "Loaded $it")
        isLoaded.value = it
    }

    Surface (
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp)
        ){
            Text(
                text = "MEDZONE",
                fontFamily = montserratFamily,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "These are the posts we were able to find based on your symptoms and location",
                fontFamily = montserratFamily,
                color = Color(0xFF6B6B6B),
                modifier = Modifier.fillMaxWidth(0.6f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            DropDownMenu(mainViewModel){
                isLoaded.value = false
            }
            if(!isLoaded.value) {
                CircularProgressIndicator()
            }
            if(isLoaded.value) {
                Spacer(modifier = Modifier.height(20.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxHeight()
                ){
                    items(
                        count = mainViewModel.postList.Post.size
                    ){ item ->
                        Spacer(modifier = Modifier.height(20.dp))
                        PostCard(
                            post = mainViewModel.postList.Post[item]
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun DropDownMenu(
    mainViewModel: mainViewModel,
    refreshLayout: () -> Unit
){

    val isDropDownOpen = remember {
        mutableStateOf(false)
    }
    val currentSelectedItem = remember {
        mutableStateOf("Symptoms")
    }

    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sort by : ",
                    fontFamily = montserratFamily,
                    color = Color(0xFF6B6B6B),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(5.dp))
                Button(
                    onClick = {
                        isDropDownOpen.value = !isDropDownOpen.value
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .shadow(
                            elevation = 8.dp,
                            spotColor = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(CornerSize(50.dp))
                        )
                    ,
                ) {
                    Text(
                        text = currentSelectedItem.value,
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 10.sp),
                        modifier = Modifier.padding(start = 6.dp, bottom = 2.dp)
                    )
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }

            DropdownMenu(
                expanded = isDropDownOpen.value,
                onDismissRequest = {
                    /*TODO : Sort the List*/
                    isDropDownOpen.value = !isDropDownOpen.value
                },
                modifier = Modifier.width(150.dp)
            ) {
                DropdownMenuItem(text = { Text(text = "Symptoms") },
                    onClick = {
                        refreshLayout()
                        currentSelectedItem.value = "Symptoms"
                        isDropDownOpen.value = !isDropDownOpen.value
                        mainViewModel.sortList("Symptoms")
                    }
                )
                DropdownMenuItem(text = { Text(text = "Distance") },
                    onClick = { /*TODO*/
                        refreshLayout()
                        currentSelectedItem.value = "Distance"
                        isDropDownOpen.value = !isDropDownOpen.value
                        mainViewModel.sortList("Distance")

                    }
                )
                DropdownMenuItem(text = { Text(text = "Relief") },
                    onClick = { /*TODO*/
                        refreshLayout()
                        currentSelectedItem.value = "Relief"
                        isDropDownOpen.value = !isDropDownOpen.value
                        mainViewModel.sortList("Relief")
                    }
                )
            }
        }
    }
}

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindScreen(
    navigator : DestinationsNavigator,
    mainViewModel: mainViewModel
){

    Surface (
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ){
        val symptoms = remember {
            mutableStateOf("")
        }
        val address = remember {
            mutableStateOf("")
        }

        val localFocusManager = LocalFocusManager.current

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
        ) {
            Text(
                text = "MEDZONE",
                fontFamily = montserratFamily,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Please fill out this form with your symptoms and address to find out similar diagnosis around your locality",
                fontFamily = montserratFamily,
                color = Color(0xFF6B6B6B),
                modifier = Modifier.fillMaxWidth(0.6f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Image(
                painter = painterResource(id = R.drawable.forensic_medicine),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .weight(0.4f)
                    .clip(shape = RoundedCornerShape(38.dp)),
            )
            Spacer(modifier = Modifier.height(40.dp))
            TextField(
                value = symptoms.value,
                onValueChange = {
                    symptoms.value = it
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color(0xFFF14351)
                ),
                label = {
                    Text(text = "Symptoms")
                },
                supportingText = {
                    Text(text = "Please separate your symptoms by comma")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    localFocusManager.moveFocus(FocusDirection.Down)
                })
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = address.value,
                onValueChange = {
                    address.value = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color(0xFFF14351)
                ),
                modifier = Modifier.fillMaxWidth(0.8f),
                label = { Text(text = "Address")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    mainViewModel.getPosts(
                        stringToList(symptoms.value), address.value
                    )
                    navigator.navigate(PostScreenDestination())
                })
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { /*TODO*/
                    mainViewModel.getPosts(
                        stringToList(symptoms.value), address.value
                    )
                    navigator.navigate(PostScreenDestination())
                },
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .shadow(
                        elevation = 8.dp,
                        spotColor = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    )
            ) {
                Text(
                    text = "Search",
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Button(
                onClick = {
                    navigator.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = "Back",
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

