package cn.quibbler.systembar

import android.app.Application
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings

/**
 * Display and hide of navigation bar.
 * Currently, Huawei, Xiaomi, VOVO and Android 10 mobile phones with navigation bar are supported
 */
object NavigationBarObserver : ContentObserver(Handler(Looper.getMainLooper())) {

    private val mListeners = ArrayList<OnNavigationBarListener>()

    private var mApplication: Application? = null

    private var mIsRegister = false

    fun register(application: Application) {
        mApplication = application
        mApplication?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && it.contentResolver != null && !mIsRegister) {
                var uri: Uri? = null
                var uri1: Uri? = null
                var uri2: Uri? = null
                if (isHuaWei || isEMUI()) {
                    if (isEMUI3_x() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        uri = Settings.System.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_EMUI)
                    } else {
                        uri = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_EMUI)
                    }
                } else if (isXiaoMi || isMIUI()) {
                    uri = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_MIUI);
                    uri1 = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_MIUI_HIDE)
                } else if (isVivo || isFuntouchOrOriginOs()) {
                    uri = Settings.Secure.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_VIVO)
                } else if (isOppo || isColorOs()) {
                    uri = Settings.Secure.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_OPPO)
                } else if (isSamsung) {
                    val i = Settings.Global.getInt(it.contentResolver, IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_OLD, -1)
                    if (i == -1) {
                        uri = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG);
                        uri1 = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE_TYPE);
                        uri2 = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE)
                    } else {
                        uri = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_OLD)
                    }
                } else {
                    uri = Settings.Secure.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_DEFAULT)
                }
                if (uri != null) {
                    it.contentResolver.registerContentObserver(uri, true, this)
                    mIsRegister = true
                }
                if (uri1 != null) {
                    it.contentResolver.registerContentObserver(uri1, true, this)
                }
                if (uri2 != null) {
                    it.contentResolver.registerContentObserver(uri2, true, this)
                }
            }
        }
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        mListeners.let {
            if (mListeners.isNotEmpty()) {
                val bean: GestureBean = getGestureBean(mApplication)
                var show: Boolean = false
                if (bean.isGesture) {
                    if (bean.checkNavigation && mApplication != null) {
                        val navigationBarHeight = BarConfig.getNavigationBarHeightInternal(mApplication!!)
                        show = navigationBarHeight > 0
                    } else {
                        show = false
                    }
                } else {
                    show = true
                }
                for (onNavigationBarListener in mListeners) {
                    onNavigationBarListener.onNavigationBarChange(show, bean.type)
                }
            }
        }
    }

    fun addOnNavigationBarListener(listener: OnNavigationBarListener?) {
        listener?.let {
            if (!mListeners.contains(it)) {
                mListeners.add(it)
            }
        }
    }

    fun removeOnNavigationBarListener(listener: OnNavigationBarListener?) {
        listener?.let {
            mListeners.remove(it)
        }
    }

}