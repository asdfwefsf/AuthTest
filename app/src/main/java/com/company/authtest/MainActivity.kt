package com.company.authtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.company.authtest.ui.theme.AuthTestTheme
import com.company.authtest.viewmodel.KaKaoAuthViewModel
import com.company.authtest.viewmodel.networkViewModel
import com.company.screens.BottomNav
import com.company.screens.LoginScreen


class MainActivity : ComponentActivity() {
    private val kaKaoAuthViewModel : KaKaoAuthViewModel by viewModels()
    private val networkViewModel : networkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    KakoLoginView(kaKaoAuthViewModel)
                    BottomNav()
                }
            }
        }
    }
}

//@Composable
//fun KakoLoginView(viewModel: KaKaoAuthViewModel) {
//
//    val isLoggedIn = viewModel.isLoggedIn.collectAsState()
//    val userInfo = viewModel.userInfoList.collectAsState()
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(20.dp)
//    ) {
//        Spacer(Modifier.height(10.dp))
//
//        Button(onClick = {
//            viewModel.kakaoLogin()
//        }) {
//            Text("카카오 로그인")
//        }
//
//        Button(onClick = { /*TODO*/ }) {
//            Text("카카오 로그아웃")
//        }
//
//        Text(text = "카카오 로그인 여부", textAlign = TextAlign.Center, fontSize = 20.sp)
//
//        if (userInfo.value.isNotEmpty()) {
//            Spacer(Modifier.height(10.dp))
//            Text(userInfo.value[0])
//            Spacer(Modifier.height(10.dp))
//            Text(userInfo.value[1])
//            Spacer(Modifier.height(10.dp))
//            Text(userInfo.value[2])
//            Spacer(Modifier.height(10.dp))
//            Text(userInfo.value[3])
//        }
//
//    }
//}

