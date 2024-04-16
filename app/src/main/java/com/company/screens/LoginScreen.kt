package com.company.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.company.authtest.viewmodel.KaKaoAuthViewModel
import com.company.authtest.viewmodel.networkViewModel

@Composable
fun LoginScreen(viewModel: KaKaoAuthViewModel , navController: NavController) {
    val networkViewModel : networkViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    val isLoggedIn = viewModel.isLoggedIn.collectAsState().value
    val userInfo = viewModel.userInfoList.collectAsState()
    LaunchedEffect(isLoggedIn) {
        Log.d("datastore" , isLoggedIn.toString())
        if (isLoggedIn) {
            navController.navigate(Screen.ProductScreen.route) {
                popUpTo(Screen.LoginScreen.route) { inclusive = true }
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        Button(onClick = {
            viewModel.kakaoLogin()
        }) {
            Text("카카오 로그인")
        }

        Button(onClick = { /*TODO*/ }) {
            Text("카카오 로그아웃")
        }

        Text(text = "카카오 로그인 여부", textAlign = TextAlign.Center, fontSize = 20.sp)

        if (userInfo.value.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            Text(userInfo.value[0])
            Spacer(Modifier.height(10.dp))
            Text(userInfo.value[1])
            Spacer(Modifier.height(10.dp))
            Text(userInfo.value[2])
            Spacer(Modifier.height(10.dp))
            Text(userInfo.value[3])
        }

    }
}
