package com.company.screens

import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.company.authtest.R
import com.company.authtest.viewmodel.KaKaoAuthViewModel
import com.company.authtest.viewmodel.networkViewModel
data class BottomNavItem (
    val title : String,
    val selectedIcon : Int,
    val unselectedIcon : Int
)
sealed class Screen (val route : String) {
    object MainScreen : Screen("홈")
    object LoginScreen : Screen("로그인")
    object ProductScreen : Screen("상품")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BottomNav() {
    val navController = rememberNavController()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val bottomNavVisibleRoutes =
        listOf(
            Screen.MainScreen.route , Screen.ProductScreen.route,
            )

    val items = listOf(

        BottomNavItem(
            title = "상품",
            selectedIcon = R.drawable.ic_launcher_background,
            unselectedIcon = R.drawable.ic_launcher_background,

            ),

        BottomNavItem(
            title = "홈",
            selectedIcon = R.drawable.ic_launcher_background,
            unselectedIcon = R.drawable.ic_launcher_background,
        ),

        BottomNavItem(
            title = "예비1",
            selectedIcon = R.drawable.ic_launcher_background,
            unselectedIcon = R.drawable.ic_launcher_background,
        ),
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(1)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (currentRoute in bottomNavVisibleRoutes) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    items.forEachIndexed { index, bottomNavItem ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(bottomNavItem.title) {
                                    launchSingleTop = true
                                }
                            },
                            label = {
                                Text(
                                    text = bottomNavItem.title,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            alwaysShowLabel = true,
                            icon = {
                                Image(
                                    painter = painterResource(id = bottomNavItem.selectedIcon),
                                    contentDescription = "test",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(MaterialTheme.colorScheme.background)
                        )
                    }
                }
            }

        }
    ) {
        innerPadding ->
        NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
            composable(route = Screen.MainScreen.route) {
                val viewModel: KaKaoAuthViewModel = viewModel()

                LoginScreen(viewModel , navController)
            }

//            composable(route = Screen.AlarmScreen.route) {
//                AlarmScreen(navController)
//            }

            composable(route = Screen.ProductScreen.route) {
                ProductScreen(innerPadding)
            }

//            composable(route = Screen.RoutineScreen.route) {
//                val viewModel = hiltViewModel<RoutineViewModel>()
//                val state by viewModel.state.collectAsState()
//                RoutineScreen(state = state, onEvent = viewModel::onEvent)
//            }
        }
    }
}

