package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.gson.Gson
import androidx.compose.material3.TextField as TextField

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                ScaffoldExample()
            }
        }
    }
}


@Composable
fun AppointmentCard(
    name: String,
    position: String,
    photoLink: String,
    date: String,
    time: String
) {
    Column(
        modifier = Modifier
            .background(
                color = Color(0xFF4894FE),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(all = 20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.woman),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Spacer(
                    modifier = Modifier.width(12.dp)
                )
                Column {
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Text(
                        text = "Стоматолог",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFCBE1FF)
                    )
                }
            }

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_forward_ios_24),
                tint = Color.White,
                contentDescription = null,
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.15f))
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row {

            Row {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_calendar_month_24),
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(
                modifier = Modifier.width(34.dp)
            )

            Row {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_punch_clock_24),
                    tint = Color.White,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal
                )
            }

        }

    }
}

data class AppoinmentCardModel(
    val name: String,
    val position: String,
    val photoLink: String,
    val date: String,
    val time: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldExample() {


    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("main", Context.MODE_PRIVATE)
    }

    val appointmentDataList = remember { mutableStateListOf<AppoinmentCardModel>() }

    LaunchedEffect(Unit) {
        val jsonData =
            sharedPreferences.getString("cardModels", null)
                ?: return@LaunchedEffect
        val gson = Gson()
        val data = gson.fromJson(
            jsonData,
            Array<AppoinmentCardModel>::class.java
        ).toList()
        appointmentDataList.addAll(data)
    }

    val addAppointmentBottomSheetVisible = remember{
        mutableStateOf(false)
    }

    if (addAppointmentBottomSheetVisible.value) {
        ModalBottomSheet(
            onDismissRequest = { addAppointmentBottomSheetVisible.value = false },
            containerColor = Color.White,
            content = {

                val inputSurname = remember { mutableStateOf("") }
                val inputPosition = remember { mutableStateOf("") }
                val inputDate = remember { mutableStateOf("") }
                val inputTime = remember { mutableStateOf("") }

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = inputSurname.value,
                    label = { Text(text = "Имя и фамилия") },
                    onValueChange = {newText -> inputSurname.value = newText},
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF4894FE).copy(0.2f),
                        unfocusedContainerColor = Color(0xFF4894FE).copy(0.2f),
                        disabledContainerColor = Color(0xFF4894FE).copy(0.2f),
                        errorContainerColor = Color(0xFF4894FE).copy(0.2f),
                    )
                )
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = inputPosition.value,
                    label = { Text(text = "Должность") },
                    onValueChange = {newText -> inputPosition.value = newText},
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF4894FE).copy(0.2f),
                        unfocusedContainerColor = Color(0xFF4894FE).copy(0.2f),
                        disabledContainerColor = Color(0xFF4894FE).copy(0.2f),
                        errorContainerColor = Color(0xFF4894FE).copy(0.2f),
                    )
                )
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = inputDate.value,
                    label = { Text(text = "Дата записи") },
                    onValueChange = {newText -> inputDate.value = newText},
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF4894FE).copy(0.2f),
                        unfocusedContainerColor = Color(0xFF4894FE).copy(0.2f),
                        disabledContainerColor = Color(0xFF4894FE).copy(0.2f),
                        errorContainerColor = Color(0xFF4894FE).copy(0.2f),
                    )
                )
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    value = inputTime.value,
                    label = { Text(text = "Время записи") },
                    onValueChange = {newText -> inputTime.value = newText},
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF4894FE).copy(0.2f),
                        unfocusedContainerColor = Color(0xFF4894FE).copy(0.2f),
                        disabledContainerColor = Color(0xFF4894FE).copy(0.2f),
                        errorContainerColor = Color(0xFF4894FE).copy(0.2f),
                    )
                )
                Spacer(
                    modifier = Modifier.height(40.dp)
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 80.dp,
                            end = 80.dp,
                            bottom = 56.dp
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4894FE)
                    ),
                    onClick = {
                            val gson = Gson()
                        val cardModel = AppoinmentCardModel(
                            name = inputSurname.value,
                            photoLink = "",
                            position = inputPosition.value,
                            date = inputDate.value,
                            time = inputTime.value
                        )
                        val currentValueJson = sharedPreferences.getString(
                            "cardModels",
                            null
                        )
                        val listToWrite = if (currentValueJson == null) {
                            arrayOf(cardModel)
                        } else {
                            val mutableList = gson.fromJson(
                                currentValueJson,
                                Array<AppoinmentCardModel>::class.java
                            ).toMutableList()
                            mutableList.add(cardModel)
                            mutableList.toTypedArray()
                        }
                        val jsonOutput = gson.toJson(listToWrite)
                        sharedPreferences.edit().putString("cardModels", jsonOutput).apply()

                        appointmentDataList.add(cardModel)
                        addAppointmentBottomSheetVisible.value = false
                        inputSurname.value = ""
                        inputPosition.value = ""
                        inputDate.value = ""
                        inputTime.value = ""



                    }
                ) {
                    Text(text = "Добавить")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Запись к врачу",
                        color = Color(0xFFFFFFFF)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF4894FE)
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                containerColor = Color(0xFFECE6F),
                onClick = { addAppointmentBottomSheetVisible.value = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            contentPadding = PaddingValues(all = 30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
            ) {

            items(appointmentDataList) { data ->
                AppointmentCard(
                    name = data.name,
                    position = data.position,
                    photoLink = data.photoLink,
                    date = data.date,
                    time = data.time
                )
            }

        }

    }

}


@Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MyApplicationTheme {
            AppointmentCard(name = "Алина", position = "Ортопед", photoLink = "", date = "11 октября", time = "1:00")
        }
    }



/** @Composable
fun AppointmentCard (name: String, date: String, time: String) {
Column(
Modifier
.background(
Color(0xFF4894FE),
shape = RoundedCornerShape(12.dp)
)
.padding(vertical = 20.dp)
) {

Row { //верхняя строчка
Row {
Image(
bitmap = ImageBitmap.imageResource(R.drawable.woman),
contentDescription = "Woman"
)

Column(
) {
Text(
text = name,
color = Color.White,
fontSize = 20.sp
)

Text(
text = "Стоматолог",
color = Color.Blue,
fontSize = 10.sp,
)
}
}
Icon(
Icons.Rounded.KeyboardArrowRight,
contentDescription = "Стрелка",
)
}

Box(Modifier.size(20.dp)) {
Image(
bitmap = ImageBitmap.imageResource(R.drawable.img),
contentDescription = "Line"
)
}

Row {
//нижняя строчка

Row {
Icon(
Icons.Rounded.DateRange,
contentDescription = "Календарь"
)

Text(
text = date,
color = Color.White,
fontSize = 8.sp
)
}

Row {
Icon(
Icons.Rounded.AddCircle,
contentDescription = "Часы",
)

Text(
text = time,
color = Color.White,
fontSize = 8.sp
)
}
}
}
}
 */