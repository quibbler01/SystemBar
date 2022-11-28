package cn.quibbler.systembar

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.TypedValue
import android.view.DisplayCutout
import android.view.View
import java.lang.reflect.InvocationTargetException


/**
 * System Properties
 * The constant SYSTEM_PROPERTIES.
 */
private const val SYSTEM_PROPERTIES = "android.os.SystemProperties"

/**
 * Millet fringe
 * The constant NOTCH_XIAO_MI.
 */
private const val NOTCH_XIAO_MI = "ro.miui.notch"

/**
 * Huawei Liu Hai
 * The constant NOTCH_HUA_WEI.
 */
private const val NOTCH_HUA_WEI = "com.huawei.android.util.HwNotchSizeUtil"

/**
 * VIVO Liu Hai
 * The constant NOTCH_VIVO.
 */
private const val NOTCH_VIVO = "android.util.FtFeature"

/**
 * OPPO Liu Hai
 * The constant NOTCH_OPPO.
 */
private const val NOTCH_OPPO = "com.oppo.feature.screen.heteromorphism"

/**
 * Lenovo bangs
 * The Notch lenovo.
 */
private const val NOTCH_LENOVO = "config_screen_has_notch"

private const val NOTCH_MEIZU = "flyme.config.FlymeFeature"

/**
 * Has notch screen boolean.
 *
 * @param activity the activity
 * @return the boolean
 */
fun hasNotchScreen(activity: Activity?): Boolean {
    activity?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return hasNotchAtAndroidP(it)
        } else {
            return hasNotchAtXiaoMi(it) ||
                    hasNotchAtHuaWei(it) ||
                    hasNotchAtOPPO(it) ||
                    hasNotchAtVIVO(it) ||
                    hasNotchAtLenovo(it) ||
                    hasNotchAtMeiZu();
        }
    }
    return false
}

/**
 * Has notch screen boolean.
 *
 * @param view the view
 * @return the boolean
 */
fun hasNotchScreen(view: View?): Boolean {
    view?.let {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            hasNotchAtAndroidP(it)
        } else {
            hasNotchAtXiaoMi(it.context) ||
                    hasNotchAtHuaWei(it.context) ||
                    hasNotchAtOPPO(it.context) ||
                    hasNotchAtVIVO(it.context)
        }
    }
    return false
}

/**
 * Has notch at android p boolean.
 *
 * @param view the view
 * @return the boolean
 */
private fun hasNotchAtAndroidP(view: View): Boolean = getDisplayCutout(view) != null

/**
 * Android P 刘海屏判断
 * Has notch at android p boolean.
 *
 * @param activity the activity
 * @return the boolean
 */
private fun hasNotchAtAndroidP(activity: Activity): Boolean = getDisplayCutout(activity) != null

/**
 * Gets display cutout.
 *
 * @param activity the activity
 * @return the display cutout
 */
private fun getDisplayCutout(activity: Activity): DisplayCutout? = getDisplayCutout(activity.window.decorView)

private fun getDisplayCutout(view: View?): DisplayCutout? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        if (view != null) {
            val windowInsets = view.rootWindowInsets
            if (windowInsets != null) {
                return windowInsets.displayCutout
            }
        }
    }
    return null
}

/**
 * Has notch at xiao mi int.
 *
 * @param context the context
 * @return the int
 */
private fun hasNotchAtXiaoMi(context: Context): Boolean {
    var result = 0
    if (isXiaoMi) {
        try {
            val classLoader = context.classLoader
            val aClass = classLoader.loadClass(SYSTEM_PROPERTIES)
            val method = aClass.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            val o = method.invoke(aClass, NOTCH_XIAO_MI, 0)
            if (o != null) {
                result = o as Int
            }
        } catch (ignored: NoSuchMethodException) {
        } catch (ignored: IllegalAccessException) {
        } catch (ignored: InvocationTargetException) {
        } catch (ignored: ClassNotFoundException) {
        }
    }
    return result != -1
}

/**
 * Has notch at hua wei boolean.
 *
 * @param context the context
 * @return the boolean
 */
private fun hasNotchAtHuaWei(context: Context): Boolean {
    var result = false
    if (isHuaWei) {
        try {
            val classLoader = context.classLoader
            val aClass = classLoader.loadClass(NOTCH_HUA_WEI)
            val get = aClass.getMethod("hasNotchInScreen")
            result = get.invoke(aClass) as Boolean
        } catch (ignored: ClassNotFoundException) {
        } catch (ignored: NoSuchMethodException) {
        } catch (ignored: Exception) {
        }
    }
    return result
}

/**
 * Has notch at vivo boolean.
 *
 * @param context the context
 * @return the boolean
 */
private fun hasNotchAtVIVO(context: Context): Boolean {
    var result = false
    if (isVivo) {
        try {
            val classLoader = context.classLoader
            val aClass = classLoader.loadClass(NOTCH_VIVO)
            val method = aClass.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            result = method.invoke(aClass, 0x00000020) as Boolean
        } catch (ignored: ClassNotFoundException) {
        } catch (ignored: NoSuchMethodException) {
        } catch (ignored: java.lang.Exception) {
        }
    }
    return result
}

/**
 * Has notch at oppo boolean.
 *
 * @param context the context
 * @return the boolean
 */
private fun hasNotchAtOPPO(context: Context): Boolean {
    return if (isOppo) {
        try {
            context.packageManager.hasSystemFeature(NOTCH_OPPO)
        } catch (ignored: java.lang.Exception) {
            false
        }
    } else false
}

/**
 * Has notch at lenovo boolean.
 *
 * @param context the context
 * @return the boolean
 */
private fun hasNotchAtLenovo(context: Context): Boolean {
    if (isLenovo) {
        val resourceId = context.resources.getIdentifier(NOTCH_LENOVO, "bool", "android")
        if (resourceId > 0) {
            return context.resources.getBoolean(resourceId)
        }
    }
    return false
}

/**
 * Has notch at meizu boolean.
 *
 * @return the boolean
 */
private fun hasNotchAtMeiZu(): Boolean {
    return if (isMeizu) {
        try {
            val clazz = Class.forName(NOTCH_MEIZU)
            val field = clazz.getDeclaredField("IS_FRINGE_DEVICE")
            field[null] as Boolean
        } catch (e: java.lang.Exception) {
            false
        }
    } else false
}

/**
 * Notch height int.
 *
 * @param activity the activity
 * @return the int
 */
fun getNotchHeight(activity: Activity): Int {
    if (!hasNotchScreen(activity)) {
        return 0
    }
    var notchHeight: Int = 0
    val statusBarHeight: Int = ImmersionBar.getStatusBarHeight(activity)
    val displayCutout: DisplayCutout? = getDisplayCutout(activity)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && displayCutout != null) {
        notchHeight = if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            displayCutout.safeInsetTop
        } else {
            if (displayCutout.safeInsetLeft == 0) {
                displayCutout.safeInsetRight
            } else {
                displayCutout.safeInsetLeft
            }
        }
    } else {
        if (hasNotchAtXiaoMi(activity)) {
            notchHeight = getXiaoMiNotchHeight(activity)
        }
        if (hasNotchAtHuaWei(activity)) {
            notchHeight = getHuaWeiNotchSize(activity).get(1)
        }
        if (hasNotchAtVIVO(activity)) {
            notchHeight = dp2px(activity, 32)
            if (notchHeight < statusBarHeight) {
                notchHeight = statusBarHeight
            }
        }
        if (hasNotchAtOPPO(activity)) {
            notchHeight = 80
            if (notchHeight < statusBarHeight) {
                notchHeight = statusBarHeight
            }
        }
        if (hasNotchAtLenovo(activity)) {
            notchHeight = getLenovoNotchHeight(activity)
        }
        if (hasNotchAtMeiZu()) {
            notchHeight = getMeizuNotchHeight(activity)
        }
    }
    return notchHeight
}

/**
 * Gets notch height.
 *
 * @param activity the activity
 * @param callback the callback
 */
fun getNotchHeight(activity: Activity, callback: NotchCallback?) {
    activity.window.decorView.post {
        callback?.onNotchHeight(getNotchHeight(activity))
    }
}

/**
 * Gets xiao mi notch height.
 *
 * @param context the context
 * @return the xiao mi notch height
 */
private fun getXiaoMiNotchHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("notch_height", "dimen", "android")
    return if (resourceId > 0) {
        context.resources.getDimensionPixelSize(resourceId)
    } else 0
}

/**
 * Get hua wei notch size int [ ].
 *
 * @param context the context
 * @return the int [ ]
 */
private fun getHuaWeiNotchSize(context: Context): IntArray {
    val ret = intArrayOf(0, 0)
    try {
        val cl = context.classLoader
        val clazz = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
        val get = clazz.getMethod("getNotchSize")
        return get.invoke(clazz) as IntArray
    } catch (ignored: ClassNotFoundException) {
    } catch (ignored: NoSuchMethodException) {
    } catch (ignored: Exception) {
    }
    return ret
}

/**
 * Gets lenovo notch height.
 *
 * @param context the context
 * @return the lenovo notch height
 */
private fun getLenovoNotchHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("notch_h", "dimen", "android")
    return if (resourceId > 0) {
        context.resources.getDimensionPixelSize(resourceId)
    } else 0
}

/**
 * Gets meizu notch height.
 *
 * @param context the context
 * @return the meizu notch height
 */
private fun getMeizuNotchHeight(context: Context): Int {
    var notchHeight = 0
    val resourceId = context.resources.getIdentifier("fringe_height", "dimen", "android")
    if (resourceId > 0) {
        notchHeight = context.resources.getDimensionPixelSize(resourceId)
    }
    return notchHeight
}

private fun dp2px(context: Context, dpValue: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue.toFloat(), context.resources.displayMetrics).toInt()
}
