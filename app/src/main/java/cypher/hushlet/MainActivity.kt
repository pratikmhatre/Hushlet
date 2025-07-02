package cypher.hushlet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cypher.hushlet.ui.screes.AddNewCredential
import cypher.hushlet.features.add_credentials.ui.AddEditCredentialScreen
import cypher.hushlet.ui.screes.Pager
import cypher.hushlet.ui.screes.PagerScreen
import cypher.hushlet.core.theme.HushletTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            HushletTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    ) { innerPadding ->
                    HushletNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        navController
                    )
                }
            }
        }
    }
}





@Composable
fun HushletNavHost(modifier: Modifier = Modifier, navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        modifier = modifier,
        startDestination = Pager.route
    ) {
        composable(route = Pager.route) { PagerScreen() }
        composable(route = AddNewCredential.route) { AddEditCredentialScreen() }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HushletTheme {
        Greeting("Android")
    }
}