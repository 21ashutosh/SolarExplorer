package com.example.solarexplorer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.solarexplorer.data.datastore.DataStoreManager
import com.example.solarexplorer.ui.HomeScreen
//import com.example.solarexplorer.ui.PlanetDetailScreen
import com.example.solarexplorer.ui.PlanetViewModelFactory
import com.example.solarexplorer.ui.QuizScreen
import com.example.solarexplorer.ui.ResultScreen
import com.example.solarexplorer.ui.TourScreen
import com.example.solarexplorer.viewmodel.PlanetViewModel
import com.example.solarexplorer.ui.theme.SolarExplorerTheme

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope

// 🔥 ADDED
import com.example.solarexplorer.viewmodel.QuizViewModel
import com.example.solarexplorer.viewmodel.QuizViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.solarexplorer.ui.PlanetDetailScreen



// 🔥 ADDED FOR THEME
//import com.example.solarexplorer.ui.theme.SolarExplorerTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)     // ✅ must be first

        // --- SPLASH SCREEN ---
        val splashScreen = installSplashScreen()

        var keepSplashOn = true

        splashScreen.setKeepOnScreenCondition { keepSplashOn }

        lifecycleScope.launch {
            delay(600) // simulate initialization load
            keepSplashOn = false
        }

        // --- CREATE DATASTORE + FACTORIES ---
        val dataStoreManager = DataStoreManager(applicationContext)
        val planetFactory = PlanetViewModelFactory(dataStoreManager)
        val quizFactory = QuizViewModelFactory(applicationContext)

        // --- SET CONTENT: only ONCE ---
        setContent {

            // 🔥 Wrap everything into your custom theme
            SolarExplorerTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {

                    // ---------------- HOME SCREEN ----------------
                    composable("home") {
                        val planetVM: PlanetViewModel = viewModel(factory = planetFactory)
                        val quizVM: QuizViewModel = viewModel(factory = quizFactory)

                        val planets by planetVM.planets.collectAsState()

                        HomeScreen(
                            planets = planets,
                            onPlanetClick = { planetName ->
                                navController.navigate("detail/${Uri.encode(planetName)}")
                            }
                        )
                    }

                    // ---------------- PLANET DETAIL SCREEN ----------------
                    composable(
                        "detail/{planetName}",
                        arguments = listOf(navArgument("planetName") { type = NavType.StringType })
                    ) { backStackEntry ->

                        val planetVM: PlanetViewModel = viewModel(factory = planetFactory)
                        val quizVM: QuizViewModel = viewModel(factory = quizFactory)

                        val planetName = backStackEntry.arguments?.getString("planetName") ?: ""
                        val planets by planetVM.planets.collectAsState()
                        val planet = planets.find { it.name == planetName }

                        PlanetDetailScreen(
                            planet = planet,
                            onBack = { navController.popBackStack() },
                            onQuizClick = { name ->
                                navController.navigate("quiz/${Uri.encode(name)}")
                            },
                            vm = quizVM
                        )
                    }

                    // ---------------- QUIZ SCREEN ----------------
                    composable(
                        "quiz/{planetName}",
                        arguments = listOf(navArgument("planetName") { type = NavType.StringType })
                    ) { backEntry ->

                        val planetVM: PlanetViewModel = viewModel(factory = planetFactory)
                        val quizVM: QuizViewModel = viewModel(factory = quizFactory)

                        val planetName = backEntry.arguments?.getString("planetName") ?: ""
                        val planets by planetVM.planets.collectAsState()
                        val planet = planets.find { it.name == planetName }

                        if (planet != null) {
                            val questions = planetVM.getQuizForPlanet(planet)

                            QuizScreen(
                                questions = questions,
                                onFinish = { score, max ->
                                    quizVM.updateHighScore(planetName, score)
                                    navController.navigate("result/$score/$max")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }

                    // ---------------- RESULT SCREEN ----------------
                    composable(
                        "result/{score}/{max}",
                        arguments = listOf(
                            navArgument("score") { type = NavType.IntType },
                            navArgument("max") { type = NavType.IntType }
                        )
                    ) { backEntry ->

                        val score = backEntry.arguments?.getInt("score") ?: 0
                        val max = backEntry.arguments?.getInt("max") ?: 0

                        ResultScreen(
                            score = score,
                            total = max,
                            onRetry = { navController.popBackStack() },
                            onBackHome = { navController.popBackStack("home", inclusive = false) }
                        )
                    }

                    // ---------------- TOUR SCREEN ----------------
                    composable("tour") {
                        val planetVM: PlanetViewModel = viewModel(factory = planetFactory)
                        val planets by planetVM.planets.collectAsState()

                        TourScreen(
                            planets = planets,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
