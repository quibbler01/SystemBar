package cn.quibbler.systembar

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils
import kotlin.NumberFormatException

/**
 * Mobile phone system judgment
 */

private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
private const val KEY_EMUI_VERSION_NAME = "ro.build.version.emui"
private const val KEY_DISPLAY = "ro.build.display.id"


/**
 * Is xiao mi boolean.
 */
val isXiaoMi: Boolean = Build.MANUFACTURER.contains("xiaomi", true)

/**
 * Is hua wei boolean.
 */
val isHuaWei: Boolean = Build.MANUFACTURER.contains("huawei", true)

/**
 * Is oppo boolean.
 */
val isOppo: Boolean = Build.MANUFACTURER.contains("oppo", true)

/**
 * Is vivo boolean.
 */
val isVivo: Boolean = Build.MANUFACTURER.contains("vivo", true)

/**
 * Is samsung boolean.
 */
val isSamsung: Boolean = Build.MANUFACTURER.contains("samsung", true)

/**
 * Is a Lenovo phone.
 */
val isLenovo: Boolean = Build.MANUFACTURER.contains("lenovo", true)

/**
 * Is it Meizu.
 */
val isMeizu: Boolean = Build.MANUFACTURER.contains("meizu", true)

/**
 * Is miui boolean.
 */
fun isMIUI(): Boolean {
    val property = getSystemProperty(KEY_MIUI_VERSION_NAME)
    return !TextUtils.isEmpty(property)
}

/**
 * Is miui 6 later boolean.
 *
 * @return the boolean
 */
fun isMIUI6Later(): Boolean {
    val version = getMIUIVersion()
    if (version.isNotEmpty()) {
        try {
            val num = version.substring(1).toInt()
            return num >= 6
        } catch (e: NumberFormatException) {
            return false
        }
    } else {
        return false
    }
}

/**
 * Gets miui version.
 *
 * @return the miui version
 */
fun getMIUIVersion(): String {
    return if (isMIUI()) getSystemProperty(KEY_MIUI_VERSION_NAME) else ""
}

/**
 * Is emui boolean.
 *
 * @return the boolean
 */
fun isEMUI(): Boolean {
    val property = getSystemProperty(KEY_EMUI_VERSION_NAME)
    return !TextUtils.isEmpty(property)
}

/**
 * Gets emui version.
 *
 * @return the emui version
 */
fun getEMUIVersion(): String {
    return if (isEMUI()) getSystemProperty(KEY_EMUI_VERSION_NAME) else ""
}

/**
 * Is emui 3 1 boolean.
 *
 * @return the boolean
 */
fun isEMUI3_1(): Boolean {
    val property = getEMUIVersion()
    if ("EmotionUI 3" == property || property.contains("EmotionUI_3.1", true)) {
        return true
    }
    return false
}

/**
 * Is emui 3 1 boolean.
 *
 * @return the boolean
 */
fun isEMUI3_0(): Boolean {
    val property = getEMUIVersion()
    return property.contains("EmotionUI_3.0", true)
}

/**
 * Is emui 3 x boolean.
 *
 * @return the boolean
 */
fun isEMUI3_x(): Boolean {
    return isEMUI3_0() || isEMUI3_1()
}

/**
 * Judge whether it is Color Os
 *
 * @return the boolean
 */
fun isColorOs(): Boolean {
    val property = getSystemProperty("ro.build.version.opporom")
    return !TextUtils.isEmpty(property)
}

/**
 * Judge whether it is Funtouch Os or Origin Os
 *
 * @return the boolean
 */
fun isFuntouchOrOriginOs(): Boolean {
    val property = getSystemProperty("ro.vivo.os.version")
    return !TextUtils.isEmpty(property)
}

/**
 * Is flyme os boolean.
 *
 * @return the boolean
 */
fun isFlymeOS(): Boolean {
    return getFlymeOSFlag().contains("flyme", true)
}

/**
 * Is flyme os 4 later boolean.
 *
 * @return the boolean
 */
fun isFlymeOS4Later(): Boolean {
    val version = getFlymeOSVersion()
    if (version.isNotEmpty()) {
        try {
            val num: Int = if (version.contains("os", true)) {
                version.substring(9, 10).toInt()
            } else {
                version.substring(6, 7).toInt()
            }
            return num >= 4
        } catch (e: NumberFormatException) {
            return false
        }
    }
    return false
}

/**
 * Is flyme os 5 boolean.
 *
 * @return the boolean
 */
fun isFlymeOS5(): Boolean {
    val version = getFlymeOSVersion()
    if (version.isNotEmpty()) {
        try {
            val num = if (version.contains("os", true)) {
                version.substring(9, 10).toInt()
            } else {
                version.substring(6, 7).toInt()
            }
            return num == 5
        } catch (e: NumberFormatException) {
            return false
        }
    }
    return false
}

/**
 * Gets flyme os version.
 *
 * @return the flyme os version
 */
fun getFlymeOSVersion(): String {
    return if (isFlymeOS()) getSystemProperty(KEY_DISPLAY) else ""
}

private fun getFlymeOSFlag(): String = getSystemProperty(KEY_DISPLAY)

@SuppressLint("PrivateApi")
private fun getSystemProperty(key: String): String {
    try {
        val clz = Class.forName("android.os.SystemProperties")
        val method = clz.getMethod("get", String::class.java, String::class.java)
        return method.invoke(clz, key, "") as String
    } catch (ignored: Exception) {
    }
    return ""
}
