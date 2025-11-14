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
import com.example.solarexplorer.ui.PlanetDetailScreen
import com.example.solarexplorer.ui.PlanetViewModelFactory
import com.example.solarexplorer.ui.QuizScreen
import com.example.solarexplorer.ui.ResultScreen
import com.example.solarexplorer.ui.TourScreen
import com.example.solarexplorer.viewmodel.PlanetViewModel

// 🔥 ADDED
import com.example.solarexplorer.viewmodel.QuizViewModel
import com.example.solarexplorer.viewmodel.QuizViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create DataStore + ViewModel Factory
        val dataStoreManager = DataStoreManager(applicationContext)
        val factoryInstance = PlanetViewModelFactory(dataStoreManager)

        // 🔥 ADDED — Create QuizViewModel Factory
        val quizFactory = QuizViewModelFactory(applicationContext)

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {

                // ---------------- HOME SCREEN ----------------
                composable("home") {
                    val vm: PlanetViewModel = viewModel(factory = factoryInstance)

                    // 🔥 ADDED — Create QuizViewModel
                    val quizVM: QuizViewModel = viewModel(factory = quizFactory)

                    val planets by vm.planets.collectAsState()

                    HomeScreen(
                        planets = planets,
                        onPlanetClick = { planetName ->
                            navController.navigate("detail/${Uri.encode(planetName)}")
                        }
                    )
                }

                // ---------------- PLANET DETAIL SCREEN ----------------
                composable(
                    route = "detail/{planetName}",
                    arguments = listOf(
                        navArgument("planetName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->

                    val vm: PlanetViewModel = viewModel(factory = factoryInstance)

                    // 🔥 ADDED
                    val quizVM: QuizViewModel = viewModel(factory = quizFactory)

                    val planetName = backStackEntry.arguments?.getString("planetName") ?: ""
                    val planets by vm.planets.collectAsState()
                    val planet = planets.find { it.name == planetName }

                    PlanetDetailScreen(
                        planet = planet,
                        onBack = { navController.popBackStack() },
                        onQuizClick = { name ->
                            navController.navigate("quiz/${Uri.encode(name)}")
                        },
                        vm = quizVM   // 🔥 ADDED
                    )
                }

                // ---------------- QUIZ SCREEN ----------------
                composable(
                    route = "quiz/{planetName}",
                    arguments = listOf(
                        navArgument("planetName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->

                    val vm: PlanetViewModel = viewModel(factory = factoryInstance)

                    // 🔥 ADDED — Use QuizViewModel here
                    val quizVM: QuizViewModel = viewModel(factory = quizFactory)

                    val planetName = backStackEntry.arguments?.getString("planetName") ?: ""
                    val planets by vm.planets.collectAsState()
                    val planet = planets.find { it.name == planetName }

                    if (planet != null) {
                        val questions = vm.getQuizForPlanet(planet)

                        QuizScreen(
                            questions = questions,
                            onFinish = { score, max ->
                                quizVM.updateHighScore(planetName, score)  // 🔥 UPDATED
                                navController.navigate("result/$score/$max")
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                // ---------------- RESULT SCREEN ----------------
                composable(
                    route = "result/{score}/{max}",
                    arguments = listOf(
                        navArgument("score") { type = NavType.IntType },
                        navArgument("max") { type = NavType.IntType }
                    )
                ) { backStackEntry ->

                    val score = backStackEntry.arguments?.getInt("score") ?: 0
                    val max = backStackEntry.arguments?.getInt("max") ?: 0

                    ResultScreen(
                        score = score,
                        total = max,
                        onRetry = { navController.popBackStack() },
                        onBackHome = {
                            navController.popBackStack("home", inclusive = false)
                        }
                    )
                }

                // ---------------- TOUR SCREEN ----------------
                composable("tour") {
                    val vm: PlanetViewModel = viewModel(factory = factoryInstance)
                    val planets by vm.planets.collectAsState()

                    TourScreen(
                        planets = planets,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
