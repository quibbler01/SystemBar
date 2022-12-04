package cn.quibbler.systembar

import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.os.Build
import android.view.Surface
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlin.math.acos

class ImmersionDelegate : Runnable {

    private var mImmersionBar: ImmersionBar? = null
    private var mBarProperties: BarProperties? = null
    private var mOnBarListener: OnBarListener? = null
    private var mNotchHeight = 0

    constructor(a: Any) {
        if (a is Activity) {
            if (mImmersionBar == null) {
                mImmersionBar = ImmersionBar(a as Activity)
            }
        } else if (a is Fragment) {
            if (mImmersionBar == null) {
                if (a is DialogFragment) {
                    mImmersionBar = ImmersionBar(a as DialogFragment)
                } else {
                    mImmersionBar = ImmersionBar(a as Fragment)
                }
            }
        } else if (a is android.app.Fragment) {
            if (mImmersionBar == null) {
                if (a is android.app.DialogFragment) {
                    mImmersionBar = ImmersionBar(a as android.app.DialogFragment)
                } else {
                    mImmersionBar = ImmersionBar(a as android.app.Fragment)
                }
            }
        }
    }

    constructor(activity: Activity, dialog: Dialog) {
        if (mImmersionBar == null) {
            mImmersionBar = ImmersionBar(activity, dialog)
        }
    }

    fun get(): ImmersionBar? = mImmersionBar

    fun onActivityCreated(configuration: Configuration) {
        barChanged(configuration)
    }

    fun onResume() {
        mImmersionBar?.onResume()
    }

    fun onDestroy() {
        mBarProperties = null
        mOnBarListener = null
        if (mImmersionBar != null) {
            mImmersionBar.onDestroy()
            mImmersionBar = null
        }
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        mImmersionBar?.let {
            it.onConfigurationChanged(newConfig)
            barChanged(newConfig)
        }
    }

    private fun barChanged(configuration: Configuration) {
        mImmersionBar?.let {
            if (it.initialized() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mOnBarListener = it.getBarParams().onBarListener
                mOnBarListener?.let { l ->
                    val activity: Activity? = it.getActivity()
                    if (mBarProperties == null) {
                        mBarProperties = BarProperties()
                    }
                    mBarProperties?.portrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
                    val rotation = activity?.windowManager?.defaultDisplay?.rotation
                    if (rotation == Surface.ROTATION_90) {
                        mBarProperties?.landscapeLeft = true
                        mBarProperties?.landscapeRight = false
                    } else if (rotation == Surface.ROTATION_270) {
                        mBarProperties?.landscapeLeft = false
                        mBarProperties?.landscapeRight = true
                    } else {
                        mBarProperties?.landscapeLeft = false
                        mBarProperties?.landscapeRight = false
                    }
                    activity.window.decorView.post(this)
                }
            }
        }
    }

    override fun run() {
        mImmersionBar?.let {
            if (it.getActivity() != null) {
                val activity: Activity = it.getActivity() ?: return
                val barConfig = BarConfig(activity)
                mBarProperties?.statusBarHeight = barConfig.mStatusBarHeight
                mBarProperties?.hasNavigationBar = barConfig.mHasNavigationBar
                mBarProperties?.navigationBarHeight = barConfig.mNavigationBarHeight
                mBarProperties?.navigationBarWidth = barConfig.mNavigationBarWidth
                mBarProperties?.actionBarHeight = barConfig.mActionBarHeight
                val notchScreen = hasNotchScreen(activity)
                mBarProperties?.notchScreen = notchScreen
                if (notchScreen && mNotchHeight == 0) {
                    mNotchHeight = getNotchHeight(activity)
                    mBarProperties?.notchHeight = mNotchHeight
                }
                mOnBarListener?.onBarChange(mBarProperties)
            }
        }
    }

}