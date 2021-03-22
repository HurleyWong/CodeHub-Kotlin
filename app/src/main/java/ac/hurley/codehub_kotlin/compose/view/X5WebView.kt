package ac.hurley.codehub_kotlin.compose.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.tencent.smtt.sdk.*
import ac.hurley.codehub_kotlin.R
import ac.hurley.util.util.showToast
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class X5WebView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    private var progressBar: ProgressBar? = null

    fun setShowProgress(showProgress: Boolean) {
        progressBar?.isVisible = showProgress
    }

    init {
        onCreate()
    }

    fun onCreate() {
        // 水平不显示小方块
        isHorizontalScrollBarEnabled = false
        // 垂直不显示小方块
        isVerticalScrollBarEnabled = false

        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        progressBar?.max = 100
        progressBar?.progressDrawable = this.resources.getDrawable(R.drawable.color_progress_bar)
        addView(progressBar, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6))

        initWebViewSettings()
    }

    fun onStart() {}

    fun onStop() {}

    // WebView 的基本设置
    private fun initWebViewSettings() {
        setBackgroundColor(resources.getColor(R.color.white))
        webViewClient = client
        webChromeClient = chromeClient
        setDownloadListener(downloadListener)
        isClickable = true
        setOnTouchListener { _: View?, _: MotionEvent? -> false }
        val webSetting = settings
        webSetting.javaScriptEnabled = true
        webSetting.builtInZoomControls = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.domStorageEnabled = true
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        webSetting.setAppCacheEnabled(true)
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)
        // 支持多窗体
        webSetting.setSupportMultipleWindows(false)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack()) {
            // 返回 WebView 的上一页面
            goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private val chromeClient: WebChromeClient = object : WebChromeClient() {

        // 接受网页的标题
        override fun onReceivedTitle(view: WebView, title: String) {
            if (TextUtils.isEmpty(title)) {
                return
            }
        }

        // 监听进度条的变化
        override fun onProgressChanged(view: WebView, newProgressBar: Int) {
            progressBar!!.progress = newProgressBar
            if (progressBar != null && newProgressBar != 100) {
                // 如果 WebView 没有加载完成，就显示自定义视图
                progressBar!!.visibility = VISIBLE
            } else if (progressBar != null) {
                // 如果 WebView 加载完成，就隐藏进度条，显示 WebView
                progressBar?.visibility = GONE
            }
        }
    }

    private val client: WebViewClient = object : WebViewClient() {
        // 当页面加载完成
        override fun onPageFinished(webview: WebView, url: String) {
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            val endCookie = cookieManager.getCookie(url)
            CookieManager.getInstance().flush()
            super.onPageFinished(webview, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // 返回 false 调用系统浏览器或者第三方浏览器
            return if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                false
            } else {
                // 返回 true 的时候用 WebView 打开
                try {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    view.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    showToast(R.string.no_app)
                }
                true
            }
        }

        override fun onLoadResource(webView: WebView, s: String) {
            super.onLoadResource(webView, s)
            val reUrl = webView.url + ""
            val urlList: MutableList<String> = ArrayList()
            urlList.add(reUrl)
            val newList: MutableList<String> = ArrayList()
            for (url in urlList) {
                if (!newList.contains(url)) {
                    newList.add(url)
                }
            }
        }
    }

    private val downloadListener =
        DownloadListener { url: String?, _: String?, _: String?, _: String?, _: Long ->
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(intent)
        }
}