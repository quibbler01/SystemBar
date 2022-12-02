package cn.quibbler.systembar

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Flyme OS Modify Status Bar Font Color Tool Class
 */
private val mSetStatusBarColorIcon: Method? by lazy {
    try {
        Activity::class.java.getMethod("setStatusBarDarkIcon", Int::class.javaPrimitiveType)
    } catch (ignored: NoSuchMethodException) {
        null
    }
}

private val mSetStatusBarDarkIcon: Method? by lazy {
    try {
        Activity::class.java.getMethod("setStatusBarDarkIcon", Boolean::class.javaPrimitiveType)
    } catch (ignored: NoSuchMethodException) {
        null
    }
}

private val mStatusBarColorFiled: Field? by lazy {
    try {
        WindowManager.LayoutParams::class.java.getField("statusBarColor")
    } catch (ignored: NoSuchMethodException) {
        null
    }
}

private val SYSTEM_UI_FLAG_LIGHT_STATUS_BAR: Int by lazy {
    try {
        val field = View::class.java.getField("SYSTEM_UI_FLAG_LIGHT_STATUS_BAR")
        field.getInt(null)
    } catch (ignored: NoSuchMethodException) {
        0
    } catch (ignored: IllegalAccessException) {
        0
    }
}

/**
 * Judge whether the color is black
 *
 * @param color
 * @param level
 * @return boolean boolean
 */
fun isBlackColor(color: Int, level: Int): Boolean {
    val grey = toGrey(color)
    return grey < level
}

/**
 * Convert color to grayscale value
 *
 * @param rgb colour
 * @return the int
 * @return　Gray value
 */
fun toGrey(rgb: Int): Int {
    val blue = rgb and 0x000000FF
    val green = rgb and 0x0000FF00 shr 8
    val red = rgb and 0x00FF0000 shr 16
    return (red * 38 + green * 75 + blue * 15) shr 7
}

/**
 * Set status bar font icon color
 *
 * @param activity Current activity
 * @param color    colour
 */
fun setStatusBarDarkIcon(activity: Activity, color: Int) {
    if (mSetStatusBarColorIcon != null) {
        try {
            mSetStatusBarColorIcon?.invoke(activity, color)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    } else {
        val whiteColor: Boolean = isBlackColor(color, 50)
        if (mStatusBarColorFiled != null) {
            setStatusBarDarkIcon(activity, whiteColor, whiteColor)
            setStatusBarDarkIcon(activity.window, color)
        } else {
            setStatusBarDarkIcon(activity, whiteColor)
        }
    }
}

/**
 * Set status bar font icon color (only for full screen non activity)
 *
 * @param window current window
 * @param color  colour
 */
fun setStatusBarDarkIcon(window: Window, color: Int) {
    try {
        setStatusBarColor(window, color)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            setStatusBarDarkIcon(window.decorView, true)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Set status bar font icon color
 *
 * @param activity Current activity
 * @param dark     Whether dark color true is dark color false is white
 */
fun setStatusBarDarkIcon(activity: Activity, dark: Boolean) {
    setStatusBarDarkIcon(activity, dark, true)
}

private fun changeMeizuFlag(winParams: WindowManager.LayoutParams, flagName: String, on: Boolean): Boolean {
    try {
        val f = winParams.javaClass.getDeclaredField(flagName)
        f.isAccessible = true
        val bits = f.getInt(winParams)
        val f2 = winParams.javaClass.getDeclaredField("meizuFlags")
        f2.isAccessible = true
        var meizuFlags = f2.getInt(winParams)
        val oldFlags = meizuFlags
        meizuFlags = if (on) {
            meizuFlags or bits
        } else {
            meizuFlags and bits.inv()
        }
        if (oldFlags != meizuFlags) {
            f2.setInt(winParams, meizuFlags)
            return true
        }
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return false
}

/**
 * Set status bar color
 *
 * @param view
 * @param dark
 */
private fun setStatusBarDarkIcon(view: View, dark: Boolean) {
    val oldVis = view.systemUiVisibility
    var newVis = oldVis
    newVis = if (dark) {
        newVis or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        newVis and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
    if (newVis != oldVis) {
        view.systemUiVisibility = newVis
    }
}

/**
 * Set status bar color
 *
 * @param window
 * @param color
 */
private fun setStatusBarColor(window: Window, color: Int) {
    val winParams = window.attributes
    mStatusBarColorFiled?.let {
        try {
            val oldColor: Int = it.getInt(winParams)
            if (oldColor != color) {
                it.set(winParams, color)
                window.attributes = winParams
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}

/**
 * Set status bar font icon color (only for full screen non activity)
 *
 * @param window current window
 * @param dark   Whether dark color true is dark color false is white
 */
fun setStatusBarDarkIcon(window: Window, dark: Boolean) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        changeMeizuFlag(window.attributes, "MEIZU_FLAG_DARK_STATUS_BAR_ICON", dark)
    } else {
        val decorView = window.decorView
        setStatusBarDarkIcon(decorView, dark)
        setStatusBarColor(window, 0)
    }
}

private fun setStatusBarDarkIcon(activity: Activity, dark: Boolean, flag: Boolean) {
    if (mSetStatusBarDarkIcon != null) {
        try {
            mSetStatusBarDarkIcon?.invoke(activity, dark)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    } else {
        if (flag) {
            setStatusBarDarkIcon(activity.window, dark)
        }
    }
}

fun setMIUIBarDark(window: Window?, key: String, dark: Boolean) {
    window?.let {
        val clazz = it.javaClass
        try {
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField(key)
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            if (dark) {
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
        } catch (ignored: Exception) {
        }
    }
}





