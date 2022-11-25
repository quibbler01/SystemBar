package cn.quibbler.systembar

import android.app.Application
import android.database.ContentObserver
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings

/**
 * Huawei Emui 3 status bar listener
 */
object EMUI3NavigationBarObserver : ContentObserver(Handler(Looper.getMainLooper())) {

    private var mCallbacks: ArrayList<ImmersionCallback> = ArrayList()

    private var mApplication: Application? = null

    private var mIsRegister: Boolean = false

    fun register(application: Application?) {
        mApplication = application
        mApplication?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && it.contentResolver != null && !mIsRegister) {
                val uri = Settings.System.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_EMUI)
                if (uri != null) {
                    it.contentResolver?.registerContentObserver(uri, true, this)
                    mIsRegister = true
                }
            }
        }
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        mApplication?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && it.contentResolver != null && mCallbacks.isNotEmpty()) {
                val type = Settings.System.getInt(it.contentResolver, IMMERSION_NAVIGATION_BAR_MODE_EMUI)
                var navigationBarType = if (type == 1) {
                    NavigationBarType.GESTURES
                } else {
                    NavigationBarType.CLASSIC
                }
                for (callback in mCallbacks) {
                    callback.onNavigationBarChange(type == 0, navigationBarType)
                }
            }
        }
    }

    fun addOnNavigationBarListener(callback: ImmersionCallback?) {
        callback?.let {
            if (!mCallbacks.contains(it)) {
                mCallbacks.add(it)
            }
        }
    }

    fun removeOnNavigationBarListener(callback: ImmersionCallback?) {
        callback?.let {
            mCallbacks.remove(it)
        }
    }

}