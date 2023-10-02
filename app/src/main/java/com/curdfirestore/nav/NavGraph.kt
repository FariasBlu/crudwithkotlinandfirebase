package com.curdfirestore.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.curdfirestore.screen.AddDataScreen
import com.curdfirestore.screen.GetDataScreen
import com.curdfirestore.screen.ListDataScreen
import com.curdfirestore.screen.MainScreen
import com.curdfirestore.util.SharedViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screens.MainScreen.route
    ) {
        // main screen
        composable(
            route = Screens.MainScreen.route
        ) {
            MainScreen(
                navController = navController,
            )
        }
        // get data screen
        composable(
            route = Screens.GetDataScreen.route
        ) {
            GetDataScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
            )
        }
        // add data screen
        composable(
            route = Screens.AddDataScreen.route
        ) {
            AddDataScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = Screens.ListDataScreen.route
        ) {
            ListDataScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = "${Screens.GetDataScreen.route}/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            GetDataScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
            )
        }



    }
}