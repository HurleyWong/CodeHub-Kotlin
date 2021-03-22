package ac.hurley.codehub_kotlin.compose

import ac.hurley.util.AppContext
import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.tencent.smtt.sdk.QbSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.initialize(applicationContext)
        initData()
    }

    private fun initData() {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            initQbSDK()
        }
    }

    private fun initQbSDK() {
        // 腾讯 X5 内核初始化接口
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(p0: Boolean) {
                LogUtils.e("onViewInitFinished is $p0")
            }

            override fun onCoreInitFinished() {
                LogUtils.e("onCoreInitFinished")
            }
        })
    }
}