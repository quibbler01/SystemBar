package cn.quibbler.systembar

import android.content.Context
import android.os.Build
import android.provider.Settings

/**
 * Get information about the full screen
 *
 * @param context Context
 * @return GestureBean
 */
fun getGestureBean(context: Context?): GestureBean {
    val gestureBean = GestureBean()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context?.contentResolver != null) {
        val contentResolver = context.contentResolver
        var navigationBarType = NavigationBarType.UNKNOWN
        var type = -1
        var isGesture = false
        var checkNavigation = false
        if (isHuaWei || isEMUI()) {
            if (isEMUI3_x() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                type = Settings.System.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_EMUI)
            } else {
                type = Settings.System.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_EMUI)
            }
            if (type == 0) {
                navigationBarType = NavigationBarType.CLASSIC
                isGesture = false
            } else if (type == 1) {
                navigationBarType = NavigationBarType.GESTURES
                isGesture = true
            }
        } else if (isXiaoMi || isMIUI()) {
            type = Settings.Global.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_MIUI)
            if (type == 0) {
                navigationBarType = NavigationBarType.CLASSIC
                isGesture = false
            } else if (type == -1) {
                navigationBarType = NavigationBarType.GESTURES
                isGesture = true
                val i = Settings.Global.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_MIUI_HIDE)
                checkNavigation = i != 1
            }
        } else if (isVivo || isFuntouchOrOriginOs()) {
            type = Settings.Secure.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_VIVO, -1)
            if (type == 0) {
                navigationBarType = NavigationBarType.CLASSIC
                isGesture = false
            } else if (type == 1) {
                navigationBarType = NavigationBarType.GESTURES_THREE_STAGE
                isGesture = true
            } else if (type == 2) {
                navigationBarType = NavigationBarType.GESTURES
                isGesture = true
            }
        } else if (isOppo || isColorOs()) {
            type = Settings.Secure.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_OPPO, -1)
            if (type == 0) {
                navigationBarType = NavigationBarType.CLASSIC
                isGesture = false
            } else if (type == 1 || type == 2 || type == 3) {
                navigationBarType = NavigationBarType.GESTURES
                isGesture = true
            }
        } else if (isSamsung) {
            type = Settings.Global.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG, -1)
            if (type != -1) {
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC
                    isGesture = false
                } else if (type == 1) {
                    isGesture = true
                    val gestureType = Settings.Global.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE_TYPE, 1)
                    navigationBarType = if (gestureType == 1) {
                        NavigationBarType.GESTURES
                    } else {
                        NavigationBarType.GESTURES_THREE_STAGE
                    }
                    val hide = Settings.Global.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE, 1)
                    checkNavigation = hide == 1
                }
            } else {
                type = Settings.Global.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_OLD, -1)
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC
                    isGesture = false
                } else if (type == 1) {
                    navigationBarType = NavigationBarType.GESTURES
                    isGesture = true
                }
            }
        }
        if (type == -1) {
            type = Settings.Secure.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_DEFAULT, -1)
            if (type == 0) {
                navigationBarType = NavigationBarType.CLASSIC
                isGesture = false
            } else if (type == 1) {
                navigationBarType = NavigationBarType.DOUBLE
                isGesture = false
            } else if (type == 2) {
                navigationBarType = NavigationBarType.GESTURES
                isGesture = true
                checkNavigation = true
            }
        }
        gestureBean.isGesture = isGesture
        gestureBean.checkNavigation = checkNavigation
        gestureBean.type = navigationBarType
    }
    return gestureBean
}

class GestureBean(
    /**
     * Whether there is gesture operation
     */
    var isGesture: Boolean = false,

    /**
     * The height of the navigation bar needs to be verified. The model to be checked, Xiaomi, Samsung, native
     */
    var checkNavigation: Boolean = false,

    /**
     * Gesture type
     */
    var type: NavigationBarType = NavigationBarType.UNKNOWN
) {

    override fun toString(): String {
        return "GestureBean{isGesture=$isGesture, checkNavigation=$checkNavigation, type=$type}"
    }

}