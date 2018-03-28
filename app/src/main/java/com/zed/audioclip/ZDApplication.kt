package com.zed.audioclip

import android.support.multidex.MultiDexApplication
import com.zed.common.util.LogUtils
import com.zed.common.util.Utils
import com.zed.core.CoreController

/**
 * @author zd
 * @package
 * @fileName com.zed.audioclip.ZDApplication
 * @date on 2017/12/12 0012 10:57
 * @org 湖北博娱天成科技有限公司
 * @describe TODO
 * @email 1053834336@qq.com
 */
class ZDApplication : MultiDexApplication() {
    private var fc: CoreController? = null
    private val TAG = javaClass.simpleName

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }

    /**
     * 获取处理
     */
    fun getCoreController(): CoreController {
        try {
            if (fc == null)
                fc = CoreController(applicationContext)
        } catch (e: Exception) {
            LogUtils.e(TAG, e.localizedMessage)
            return getCoreController()
        }
        return fc as CoreController
    }
}