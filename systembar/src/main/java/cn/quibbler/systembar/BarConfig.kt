package cn.quibbler.systembar

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import kotlin.math.min

class BarConfig(activity: Activity) {

    companion object {

        fun getInternalDimensionSize(context: Context, key: String): Int {
            val result = 0
            try {
                val resourceId = Resources.getSystem().getIdentifier(key, "dimen", "android")
                if (resourceId > 0) {
                    val sizeOne = context.resources.getDimensionPixelSize(resourceId)
                    val sizeTwo = Resources.getSystem().getDimensionPixelSize(resourceId)

                    if (sizeTwo >= sizeOne && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                                key != IMMERSION_STATUS_BAR_HEIGHT)
                    ) {
                        return sizeTwo
                    } else {
                        val densityOne = context.resources.displayMetrics.density
                        val densityTwo = Resources.getSystem().displayMetrics.density
                        val f = sizeOne * densityTwo / densityOne
                        return (if (f >= 0) (f + 0.5f) else (f - 0.5f)).toInt()
                    }
                }
            } catch (ignored: Resources.NotFoundException) {
                return 0
            }
            return result
        }

        fun getNavigationBarHeightInternal(context: Context): Int {
            val key: String = if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                IMMERSION_NAVIGATION_BAR_HEIGHT
            } else {
                IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE
            }
            return getInternalDimensionSize(context, key)
        }

        fun getNavigationBarWidthInternal(context: Context): Int = getInternalDimensionSize(context, IMMERSION_NAVIGATION_BAR_WIDTH)

    }

    val mStatusBarHeight: Int
    val mActionBarHeight: Int
    val mHasNavigationBar: Boolean
    val mNavigationBarHeight: Int
    val mNavigationBarWidth: Int
    val mInPortrait: Boolean
    val mSmallestWidthDp: Float

    init {
        val res = activity.resources
        mInPortrait = res.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        mSmallestWidthDp = getSmallestWidthDp(activity)
        mStatusBarHeight = getInternalDimensionSize(activity, IMMERSION_STATUS_BAR_HEIGHT)
        mActionBarHeight = getActionBarHeight(activity)
        mNavigationBarHeight = getNavigationBarHeight(activity)
        mNavigationBarWidth = getNavigationBarWidth(activity)
        mHasNavigationBar = (mNavigationBarHeight > 0)
    }

    private fun getActionBarHeight(activity: Activity): Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            val actionBar: View? = activity.window.findViewById(R.id.action_bar_container)
            actionBar?.let {
                result = it.measuredHeight
            }
            if (result == 0) {
                val tv = TypedValue()
                activity.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
                result = TypedValue.complexToDimensionPixelSize(tv.data, activity.resources.displayMetrics)
            }
        }
        return result
    }

    private fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar(context as Activity)) {
                return getNavigationBarHeightInternal(context)
            }
        }
        return result
    }

    private fun getNavigationBarWidth(context: Context): Int {
        var result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar(context as Activity)) {
                return getInternalDimensionSize(context, IMMERSION_NAVIGATION_BAR_WIDTH)
            }
        }
        return result
    }

    private fun hasNavBar(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val gestureBean = getGestureBean(activity)
            if (!gestureBean.checkNavigation && gestureBean.isGesture) {
                return false
            }
        }

        //Other mobile phones judge whether the real height of the screen is the same as the display height
        val windowManager = activity.windowManager
        val d = windowManager.defaultDisplay

        val realDisplayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics)
        }

        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels
        val displayMetrics = DisplayMetrics()
        d.getRealMetrics(displayMetrics)

        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0
    }

    private fun getSmallestWidthDp(activity: Activity): Float {
        val metric = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.windowManager.defaultDisplay.getRealMetrics(metric)
        } else {
            activity.windowManager.defaultDisplay.getMetrics(metric)
        }
        val widthDp = metric.widthPixels / metric.density
        val heightDp = metric.heightPixels / metric.density
        return min(widthDp, heightDp)
    }

    /**
     * Should a navigation bar appear at the bottom of the screen in the current
     * device configuration? A navigation bar may appear on the right side of
     * the screen in certain configurations.
     *
     * @return True if navigation should appear at the bottom of the screen, False otherwise.
     */
    fun isNavigationAtBottom(): Boolean {
        return mSmallestWidthDp >= 600 || mInPortrait
    }

    /**
     * Get the height of the system navigation bar.
     *
     * @return The height of the navigation bar (in pixels). If the device does not have soft navigation keys, this will always return 0.
     */
    fun getNavigationBarHeight() = mNavigationBarHeight

    /**
     * Get the width of the system navigation bar when it is placed vertically on the screen.
     *
     * @return The width of the navigation bar (in pixels). If the device does not have soft navigation keys, this will always return 0.
     */
    fun getNavigationBarWidth() = mNavigationBarWidth

    /**
     * Get the height of the system status bar.
     *
     * @return The height of the status bar (in pixels).
     */
    fun getStatusBarHeight() = mStatusBarHeight

}