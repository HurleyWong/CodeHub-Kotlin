package ac.hurley.model

import ac.hurley.model.room.dao.*
import ac.hurley.model.room.entity.*
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        Classify::class, Article::class, HotKey::class, Banner::class, Almanac::class
    ], version = 3
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun classifyDao(): ClassifyDao

    abstract fun articleDao(): ArticleDao

    abstract fun hotKeyDao(): HotKeyDao

    abstract fun bannerDao(): BannerDao

    abstract fun almanacDao(): AlmanacDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            /**
             * 双重检验的单例模式
             */
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "codehub_database",
                ).addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}


val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        // 创建新的表，如果不存在 Banner
        database.execSQL(
            "create table if not exists `banner` " +
                    "(`uid` integer primary key autoincrement not null," +
                    "`desc` text not null, " +
                    "`id` integer not null, " +
                    "`imagePath` text not null, " +
                    "`isVisible` integer not null," +
                    "`order` integer not null, " +
                    "`title` text not null, " +
                    "`type` integer not null, " +
                    "`url` text not null, " +
                    "`file_path` text not null)"
        )
    }
}