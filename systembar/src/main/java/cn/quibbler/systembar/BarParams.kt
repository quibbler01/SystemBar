package cn.quibbler.systembar

import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

/**
 * Immersion parameter information
 */
class BarParams : Cloneable {

    /**
     * The Status bar color.
     */
    @ColorInt
    var statusBarColor = Color.TRANSPARENT

    /**
     * The Navigation bar color.
     */
    @ColorInt
    var navigationBarColor = Color.BLACK

    /**
     * The Default navigation bar color.
     */
    var defaultNavigationBarColor = Color.BLACK

    /**
     * The Status bar alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var statusBarAlpha = 0f

    @FloatRange(from = 0.0, to = 1.0)
    var statusBarTempAlpha = 0.0f

    /**
     * The Navigation bar alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var navigationBarAlpha = 0.0f

    @FloatRange(from = 0.0, to = 1.0)
    var navigationBarTempAlpha = 0.0f

    /**
     * Full screen display if there is a navigation bar.
     * The Full screen.
     */
    var fullScreen = false

    /**
     * Whether the navigation bar is hidden.
     */
    var hideNavigationBar = false

    /**
     * The Bar hide.
     */
    var barHide = BarHide.FLAG_SHOW_BAR

    /**
     * Status bar font dark and light flag bit.
     */
    var statusBarDarkFont = false

    /**
     * Navigation bar icon dark and light flag bit.
     */
    var navigationBarDarkIcon = false

    /**
     * The Auto status bar dark mode enable.
     */
    var autoStatusBarDarkModeEnable = false

    /**
     * Whether to enable automatic adjustment of dark mode and light mode
     * according to NavigationBar color
     */
    var autoNavigationBarDarkModeEnable = false

    /**
     * The Auto status bar dark mode alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var autoStatusBarDarkModeAlpha = 0f

    /**
     * The Auto navigation bar dark mode alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var autoNavigationBarDarkModeAlpha = 0.0f

    /**
     * Whether the status bar color can be modified.
     */
    var statusBarColorEnabled = true

    /**
     * Status bar transformed color.
     */
    @ColorInt
    var statusBarColorTransform = Color.BLACK

    /**
     * The transformed color of the navigation bar.
     */
    @ColorInt
    var navigationBarColorTransform = Color.BLACK

    /**
     * Support view color change.
     */
    var viewMap: Map<View, Map<Int, Int>> = HashMap()

    /**
     * The View alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var viewAlpha = 0.0f

    /**
     * The Status bar color content view.
     */
    @ColorInt
    var contentColor = Color.TRANSPARENT

    /**
     * The Status bar color content view transform.
     */
    @ColorInt
    var contentColorTransform = Color.BLACK

    /**
     * The Status bar content view alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var contentAlpha = 0f

    /**
     * Solve the problem that the title bar overlaps the status bar
     */
    var fits = false

    /**
     * Solve the problem that the title bar overlaps the status bar.
     * The Title bar view.
     */
    var titleBarView: View? = null

    /**
     * The Status bar view by height.
     */
    var statusBarView: View? = null

    /**
     * Whether the overlapping problem of title bar and status bar can be solved.
     */
    var fitsLayoutOverlapEnable = true

    /**
     * The Flyme os status bar font color.
     */
    @ColorInt
    var flymeOSStatusBarFontColor = 0

    @ColorInt
    var flymeOSStatusBarFontTempColor = 0

    /**
     * The Is support action bar.
     */
    var isSupportActionBar = false

    /**
     * The Keyboard enable.
     */
    var keyboardEnable = false

    /**
     * The Keyboard mode.
     */
    var keyboardMode = (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
            or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    /**
     * Whether the navigation bar color can be modified
     * The Navigation bar enable.
     */
    var navigationBarEnable = true

    /**
     * Can I modify the navigation bar colors of 4.4 mobile phones and Huawei emui 3.1
     * The Navigation bar with kitkat enable.
     */
    var navigationBarWithKitkatEnable = true

    /**
     * Can the navigation bar of the emui 3 series mobile phone be modified
     * The Navigation bar with emui 3 enable.
     */
    var navigationBarWithEMUI3Enable = true

    /**
     * Whether immersive
     * The Init enable.
     */
    var barEnable = true

    /**
     * Soft keyboard monitoring class
     * The On keyboard listener.
     */
    var onKeyboardListener: OnKeyboardListener? = null

    /**
     * Navigation bar shows hidden monitoring
     */
    var onNavigationBarListener: OnNavigationBarListener? = null

    /**
     * Horizontal and vertical monitoring
     */
    var onBarListener: OnBarListener? = null

    override fun clone(): BarParams {
        return super.clone() as BarParams
    }

}