package cypher.hushlet.ui.screes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun PagerScreen() {
    val navController = rememberNavController()
    Scaffold(bottomBar = {
        BottomNavBar(
            modifier = Modifier,
            onDashboardClicked = { navController.switchToDashboard() },
            onSettingsClicked = { navController.switchToSettings() }
        )
    }) { innerPadding ->
        PagerNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), navController = navController
        )
    }
}

@Composable
fun PagerNavHost(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Dashboard.route,
        modifier = modifier
    ) {
        composable(route = Dashboard.route) {
            DashboardScreen()
        }
        composable(route = Settings.route) {
            SettingsScreen()
        }
    }
}

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    onDashboardClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    BottomAppBar(modifier = modifier) {
        NavigationBarItem(
            onClick = onDashboardClicked,
            selected = false,
            icon = { Icon(Icons.Default.Lock, contentDescription = "") },
            label = { Text("Dashboard") })
        NavigationBarItem(
            onClick = onSettingsClicked,
            selected = false,
            icon = { Icon(Icons.Default.Settings, contentDescription = "") },
            label = { Text("Settings") })
    }

}

private fun NavHostController.switchToDashboard() = this.navigateSingleTop(Dashboard.route)

private fun NavHostController.switchToSettings() = this.navigateSingleTop(Settings.route)


private fun NavHostController.navigateSingleTop(route: String) {
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(Dashboard.route) { saveState = true }
    }
}