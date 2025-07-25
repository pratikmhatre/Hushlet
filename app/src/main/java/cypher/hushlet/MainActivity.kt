package cypher.hushlet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cypher.hushlet.core.theme.HushletTheme
import cypher.hushlet.features.Screens
import cypher.hushlet.features.add_credentials.ui.AddEditCredentialScreen
import cypher.hushlet.features.dashboard.ui.DashboardScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            HushletTheme {
                HushletNavHost(
                    modifier = Modifier
                        .fillMaxSize(),
                    navHostController = navController
                )
            }
        }
    }
}


@Composable
fun HushletNavHost(modifier: Modifier = Modifier, navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        modifier = modifier,
        startDestination = Screens.DashboardScreen.routeName
    ) {
        composable(route = Screens.DashboardScreen.routeName) { DashboardScreen() }
    }
}