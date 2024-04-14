package com.company.authtest

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.company.authtest.ui.theme.AuthTestTheme

class MainActivity : ComponentActivity() {
    private val kaKaoAuthViewModel : KaKaoAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KakoLoginView(kaKaoAuthViewModel)
                }
            }
        }
    }
}

@Composable
fun KakoLoginView(viewModel: KaKaoAuthViewModel) {

    val isLoggedIn = viewModel.isLoggedIn.collectAsState()
    val userInfo = viewModel.userInfoList.collectAsState()
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

