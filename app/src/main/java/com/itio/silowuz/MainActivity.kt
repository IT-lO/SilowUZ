package com.itio.silowuz

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itio.silowuz.`interface`.IconResource
import com.itio.silowuz.screen.ExerciseScreen
import com.itio.silowuz.screen.HomeScreen
import com.itio.silowuz.screen.LoginScreen
import com.itio.silowuz.screen.ProfileScreen
import com.itio.silowuz.ui.theme.SilowUZTheme
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SilowUZTheme {
                MainRoot()
            }
        }
    }
}
@Composable
fun MainRoot(){
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    if(!isLoggedIn){
        LoginScreen(onLoginSuccess = {isLoggedIn = true} )
    }
    else{
        SilowUZApp(onLogout = {isLoggedIn = false})
    }
}
@Composable
fun SilowUZApp( onLogout: () -> Unit) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        when(val iconRes = destination.icon){
                            is IconResource.Vector -> {
                                Icon(
                                    imageVector = iconRes.imageVector,
                                    contentDescription = stringResource(destination.labelId)
                                )
                            }
                            is IconResource.Drawable -> {
                                Icon(
                                    painter = painterResource(id = iconRes.resId),
                                    contentDescription = stringResource(destination.labelId)
                                )
                            }
                        }
                    },
                    label = { Text(stringResource(destination.labelId)) },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when(currentDestination){
                AppDestinations.HOME -> HomeScreen(innerPadding)
                AppDestinations.PLANS -> ExerciseScreen(innerPadding)
                AppDestinations.PROFILE -> ProfileScreen(innerPadding, onLogout)
            }
        }
    }
}
enum class AppDestinations(
    val labelId: Int,
    val icon: IconResource
) {
    HOME(R.string.home, IconResource.Vector(Icons.Default.Home)),
    PLANS(R.string.plans, IconResource.Drawable(R.drawable.exercise_ico)),
    PROFILE(R.string.profile, IconResource.Vector(Icons.Default.AccountBox))
}