package ac.hurley.net.download

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.OutputStream

class DownloadInfo(val ops: OutputStream?, val file: File? = null, val uri: Uri? = null)

/**
 * Download 相关方法的具体实现
 */
class DownloadBuild(private val context: Context) : IDownloadBuild() {
    override fun getContext(): Context = context
}

/**
 * Download 相关方法的抽象类
 */
abstract class IDownloadBuild {
    open fun getFileName(): String? = null
    open fun getUri(contentType: String): Uri? = null
    open fun getDownloadFile(): File? = null
    abstract fun getContext(): Context
}

/**
 * Download 的状态
 */
sealed class DownloadStatus {
    class DownloadProcess(val currentLength: Long, val length: Long, val process: Float) :
        DownloadStatus()

    class DownloadError(val t: Throwable) : DownloadStatus()

    class DownloadSuccess(val uri: Uri, val file: File?) : DownloadStatus()
}