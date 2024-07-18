package com.example.jennyserviceapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jennyserviceapp.ui.screens.message.MessageScreen
import com.example.jennyserviceapp.ui.screens.notification.NotificationScreen
import com.example.jennyserviceapp.ui.screens.orders.CustomerOrdersScreen
import com.example.jennyserviceapp.ui.screens.profile.AccountViewModel
import com.example.jennyserviceapp.ui.screens.profile.AccountsScreen
import com.example.jennyserviceapp.ui.screens.profile.login.LoginScreen
import com.example.jennyserviceapp.ui.screens.promotions.PromotionScreen
import com.example.jennyserviceapp.ui.screens.splash.SplashScreen
import com.example.jennyserviceapp.ui.screens.uploadfeed.FeedScreen
import com.example.jennyserviceapp.ui.screens.uploadproducts.ProductScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceNavHost(
    appState: StoreAppState,
    allScreen: List<ServiceDestinations>,
    currentScreen: ServiceDestinations,
    navigateScreens: (ServiceDestinations) -> Unit,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Transparent,
        drawerContent = {
            ModalDrawerSheet {
                allScreen.forEach {
                    NavigationDrawerItem(
                        label = {
                            Text(text = it.route)
                        },
                        selected = currentScreen.route == it.route,
                        onClick = {
                            navigateScreens(it)
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = it.icon,
                                contentDescription = it.route
                            )
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                            .width(dimensionResource(R.dimen.dp_200))
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.menu)
                            )
                        }
                    },
                    title = {
                        Text(text = currentScreen.route)
                    }
                )
            }
        ) {
            NavHost(
                navController = appState.navController,
                startDestination = Orders.route,
                modifier = Modifier
                    .padding(it)
            ) {
                composable(Orders.route) {
                    CustomerOrdersScreen()
                }
                composable(HomeUpload.route) {
                    ProductScreen()
                }
                composable(UploadFeed.route) {
                    FeedScreen()
                }
                composable(Message.route) {
                    MessageScreen()
                }
                composable(SendNotification.route) {
                    NotificationScreen()
                }
                composable(Promotions.route) {
                    PromotionScreen()
                }
                composable(Accounts.route) {
                    AccountsScreen(
                        restartApp = { route ->
                            appState.clearAndNavigate(route.route)
                        }
                    )
                }
                composable(Login.route) {
                    LoginScreen(
                        navigateAndPopup = { route, popUp ->
                            appState.navigateAndPopUp(route.route, popUp.route)
                        }
                    )
                }
                composable(Splash.route) {
                    SplashScreen(
                        openAndPopUp =  { route, popUp ->
                            appState.navigateAndPopUp(route.route, popUp.route)
                        }
                    )
                }
            }
        }
    }
}