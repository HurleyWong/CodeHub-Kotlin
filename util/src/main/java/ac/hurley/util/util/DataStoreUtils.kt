package ac.hurley.util.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

/**
 * DataStore 工具类
 */
object DataStoreUtils {

    private lateinit var dataStore: DataStore<Preferences>
    private const val preferenceName = "CodeHubDataStore"

    /**
     * 初始化
     */
    fun init(context: Context) {
        dataStore = context.createDataStore(preferenceName)
    }

    @Suppress("UNCHECKED_CAST")
    fun <U> getSyncData(key: String, default: U): U {
        val res = when (default) {
            is Long -> readLongData(key, default)
            is String -> readStringData(key, default)
            is Int -> readIntData(key, default)
            is Boolean -> readBooleanData(key, default)
            is Float -> readFloatData(key, default)
            else -> throw IllegalArgumentException("This type cannot be saved into DataStore")
        }
        return res as U
    }

    fun <U> putSyncData(key: String, value: U) {
        when (value) {
            is Long -> saveSyncLongData(key, value)
            is String -> saveSyncStringData(key, value)
            is Int -> saveSyncIntData(key, value)
            is Boolean -> saveSyncBooleanData(key, value)
            is Float -> saveSyncFloatData(key, value)
            else -> throw IllegalArgumentException("This type cannot be saved into DataStore")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <U> getData(key: String, default: U): Flow<U> {
        val data = when (default) {
            is Long -> readLongFlow(key, default)
            is String -> readStringFlow(key, default)
            is Int -> readIntFlow(key, default)
            is Boolean -> readBooleanFlow(key, default)
            is Float -> readFloatFlow(key, default)
            else -> throw IllegalArgumentException("This type cannot be saved into DataStore")
        }
        return data as Flow<U>
    }

    suspend fun <U> putData(key: String, value: U) {
        when (value) {
            is Long -> saveLongData(key, value)
            is String -> saveStringData(key, value)
            is Int -> saveIntData(key, value)
            is Boolean -> saveBooleanData(key, value)
            is Float -> saveFloatData(key, value)
            else -> throw IllegalArgumentException("This type cannot be saved into DataStore")
        }
    }

    fun readIntData(key: String, default: Int = 0): Int {
        var value = 0
        runBlocking {
            dataStore.data.first {
                value = it[preferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readFloatData(key: String, default: Float = 0f): Float {
        var value = 0f
        runBlocking {
            dataStore.data.first {
                value = it[preferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readLongData(key: String, default: Long = 0L): Long {
        var value = 0L
        runBlocking {
            dataStore.data.first {
                value = it[preferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readStringData(key: String, default: String = ""): String {
        var value = ""
        runBlocking {
            dataStore.data.first {
                value = it[preferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readBooleanData(key: String, default: Boolean = false): Boolean {
        var value = false
        runBlocking {
            dataStore.data.first {
                value = it[preferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readBooleanFlow(key: String, default: Boolean = false): Flow<Boolean> =
        dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { it[preferencesKey(key)] ?: default }

    fun readIntFlow(key: String, default: Int = 0): Flow<Int> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferencesKey(key)] ?: default
            }

    fun readStringFlow(key: String, default: String = ""): Flow<String> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferencesKey(key)] ?: default
            }

    fun readFloatFlow(key: String, default: Float = 0f): Flow<Float> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferencesKey(key)] ?: default
            }

    fun readLongFlow(key: String, default: Long = 0L): Flow<Long> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[preferencesKey(key)] ?: default
            }

    suspend fun saveBooleanData(key: String, value: Boolean) {
        dataStore.edit { mutablePreferences -> mutablePreferences[preferencesKey(key)] = value }
    }

    suspend fun saveLongData(key: String, value: Long) {
        dataStore.edit { mutablePreferences -> mutablePreferences[preferencesKey(key)] = value }
    }

    suspend fun saveFloatData(key: String, value: Float) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    suspend fun saveIntData(key: String, value: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    suspend fun saveStringData(key: String, value: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[preferencesKey(key)] = value
        }
    }

    fun saveSyncBooleanData(key: String, value: Boolean) =
        runBlocking { saveBooleanData(key, value) }

    fun saveSyncLongData(key: String, value: Long) = runBlocking { saveLongData(key, value) }

    fun saveSyncFloatData(key: String, value: Float) = runBlocking { saveFloatData(key, value) }

    fun saveSyncIntData(key: String, value: Int) = runBlocking { saveIntData(key, value) }

    fun saveSyncStringData(key: String, value: String) = runBlocking { saveStringData(key, value) }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    fun clearSync() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }
}