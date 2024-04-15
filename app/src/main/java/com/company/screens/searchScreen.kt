package com.company.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.company.authtest.model.Product
import com.company.authtest.viewmodel.networkViewModel
import com.company.authtest.viewmodel.productviewmodel


@Composable
fun MainScreen() {
//    val networkViewModel : networkViewModel by viewModel()
//    val thingOnViewModel: ThingOnViewModel = hiltViewModel()
    val networkViewModel: networkViewModel = viewModel()
    val categories = networkViewModel.thingOn.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
//        categories.value.forEach {
        items(10) {
//            CategoryItem(category = , onClick = )
            CategoryItem(Image = categories.value.image, Name = categories.value.name, onClick = {} )
//            CategoryItem(category = it, {})
        }
//            CategoryItem(category = it, onClick = {})
//        }
//        items(categories.value.distinct()) {
//            CategoryItem(category = it, {})
//        }



    }
}

@Composable
fun CategoryItem(Image: String, Name : String, onClick: (category: String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFEEEEEE)),
//        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = Name,
            fontSize = 18.sp,
            color = Color.Black,
//            modifier = Modifier.padding(0.dp, 8.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = Image,
            fontSize = 18.sp,
            color = Color.Black,
//            modifier = Modifier.padding(0.dp, 8.dp),
        )
    }
}