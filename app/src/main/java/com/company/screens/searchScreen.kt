package com.company.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.company.authtest.viewmodel.productviewmodel



//@Composable
//fun MainScreen() {
//    val productviewmodel: productviewmodel = viewModel()
//    val categories = productviewmodel.thingOn.collectAsState()
//
//    LazyColumn(
//        contentPadding = PaddingValues(8.dp),
//        verticalArrangement = Arrangement.SpaceAround,
//    ) {
//        items(categories.value.distinct()) {
//            CategoryItem(category = it, {})
//        }
//
//    }
//}
//
//@Composable
//fun CategoryItem(category: String, onClick: (category: String) -> Unit) {
//    Box(
//        modifier = Modifier
//            .padding(4.dp)
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(8.dp))
//            .border(1.dp, Color(0xFFEEEEEE)),
//        contentAlignment = Alignment.BottomCenter
//    ) {
//        Text(
//            text = category,
//            fontSize = 18.sp,
//            color = Color.Black,
//            modifier = Modifier.padding(0.dp, 8.dp),
//        )
//    }
//}