package ac.hurley.codehub_kotlin.compose

import ac.hurley.codehub_kotlin.compose.MainNav.ARTICLE_ROUTE_URL
import ac.hurley.codehub_kotlin.compose.common.article.ArticlePage
import ac.hurley.codehub_kotlin.compose.page.AboutPage
import ac.hurley.codehub_kotlin.compose.page.LoginPage
import ac.hurley.codehub_kotlin.compose.page.SearchPage
import ac.hurley.codehub_kotlin.compose.theme.Theme
import ac.hurley.codehub_kotlin.compose.theme.Theme2
import ac.hurley.codehub_kotlin.compose.viewmodel.ThemeViewModel
import ac.hurley.model.room.entity.Article
import ac.hurley.util.util.getHtmlText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.google.gson.Gson

object MainNav {
    const val HOME_PAGE_ROUTE = "homepage_route"
    const val ARTICLE_ROUTE = "article_route"
    const val ARTICLE_ROUTE_URL = "article_route_url"
    const val LOGIN_ROUTE = "login_route"
    const val SEARCH_ROUTE = "search_route"
    const val ABOUT_ROUTE = "about_route"
}

@Composable
fun NavGraphPage() {
    val viewModel: ThemeViewModel = viewModel()
    val theme by viewModel.theme.observeAsState()
    if (theme == false) {
        Theme {
            NavGraph(viewModel)
        }
    } else {
        Theme2 {
            NavGraph(viewModel)
        }
    }
}

@Composable
fun NavGraph(viewModel: ThemeViewModel, startDestination: String = MainNav.HOME_PAGE_ROUTE) {
    val navController = rememberNavController()
    val actions = remember(navController) {
        MainActions(navController)
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable(MainNav.HOME_PAGE_ROUTE) {
            Main(actions, viewModel)
        }
        composable(MainNav.LOGIN_ROUTE) {
            LoginPage(actions)
        }
        composable(MainNav.SEARCH_ROUTE) {
            SearchPage(actions)
        }
        composable(MainNav.ABOUT_ROUTE) {
            AboutPage(actions)
        }
        composable(
            "${MainNav.ARTICLE_ROUTE}/{$ARTICLE_ROUTE_URL}", arguments = listOf(navArgument(
                ARTICLE_ROUTE_URL
            ) {
                type = NavType.StringType
            })
        ) {
            backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val parcelable = arguments.getString(ARTICLE_ROUTE_URL)
            val fromJson = Gson().fromJson(parcelable, Article::class.java)
            ArticlePage(article = fromJson, onBack = actions.upPress)
        }
    }
}

class MainActions(navController: NavHostController) {
    val homePage: () -> Unit = {
        navController.navigate(MainNav.HOME_PAGE_ROUTE)
    }
    val login: () -> Unit = {
        navController.navigate(MainNav.LOGIN_ROUTE)
    }
    val article: (Article) -> Unit = { article ->
        article.desc = ""
        article.title = getHtmlText(article.title)
        val gson = Gson().toJson(article).trim()
        navController.navigate("${MainNav.ARTICLE_ROUTE}/$gson")
    }
    val search: () -> Unit = {
        navController.navigate(MainNav.SEARCH_ROUTE)
    }
    val about: () -> Unit = {
        navController.navigate(MainNav.ABOUT_ROUTE)
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}