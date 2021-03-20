package ac.hurley.codehub_kotlin.compose.repository

import ac.hurley.model.AppDatabase
import android.app.Application

abstract class ArticleRepository(application: Application) {

    private val projectClassifyDao = AppDatabase.getDatabase(application).projectClassifyDao()
    private val articleListDao = AppDatabase.getDatabase(application).articleDao()
}