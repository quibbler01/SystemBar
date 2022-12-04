package cn.quibbler.systembar

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

/**
 * Android 4.4 and above immersive and bar management
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class ImmersionBar : ImmersionCallback {

    private val mActivity: Activity
    private var mSupportFragment: Fragment? = null
    private var mFragment: android.app.Fragment? = null
    private var mDialog: Dialog? = null
    private var mWindow: Window? = null
    private var mDecorView: ViewGroup? = null
    private var mContentView: ViewGroup? = null
    private var mParentBar: ImmersionBar? = null

    /**
     * 是否是在Fragment里使用
     */
    private var mIsFragment = false

    /**
     * 是否是DialogFragment
     */
    private var mIsDialogFragment = false

    /**
     * 是否是在Dialog里使用
     */
    private var mIsDialog = false

    /**
     * 用户配置的bar参数
     */
    private var mBarParams: BarParams? = null

    /**
     * 系统bar相关信息
     */
    private var mBarConfig: BarConfig? = null

    /**
     * 导航栏的高度，适配Emui3系统有用
     */
    private var mNavigationBarHeight = 0

    /**
     * 导航栏的宽度，适配Emui3系统有用
     */
    private var mNavigationBarWidth = 0

    /**
     * ActionBar的高度
     */
    private var mActionBarHeight = 0

    /**
     * 软键盘监听相关
     */
    private var mFitsKeyboard: FitsKeyboard? = null

    /**
     * 用户使用tag增加的bar参数的集合
     */
    private val mTagMap: Map<String, BarParams> = HashMap()

    /**
     * 当前顶部布局和状态栏重叠是以哪种方式适配的
     */
    private var mFitsStatusBarType = FLAG_FITS_DEFAULT

    /**
     * 是否已经调用过init()方法了
     */
    private var mInitialized = false

    /**
     * ActionBar是否是在LOLLIPOP下设备使用
     */
    private var mIsActionBarBelowLOLLIPOP = false

    private var mKeyboardTempEnable = false

    private val mPaddingLeft: Int = 0
    private var mPaddingTop: Int = 0
    private var mPaddingRight: Int = 0
    private var mPaddingBottom: Int = 0

    /**
     * It can be called successfully after initialization through the above configuration
     */
    fun init() {

    }

    /**
     * Initialize in Activity.
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     */
    constructor(activity: Activity) {
        this.mActivity = activity
        initCommonParameter(mActivity.window)
    }

    /**
     * Initialize in Fragment.
     * Instantiates a new Immersion bar.
     *
     * @param fragment the fragment
     */
    constructor(fragment: Fragment) {
        mIsFragment = true
        mActivity = fragment.requireActivity()
        mSupportFragment = fragment
        checkInitWithActivity()
        initCommonParameter(mActivity.window)
    }

    constructor(fragment: android.app.Fragment) {
        mIsFragment = true
        mActivity = fragment.activity
        mFragment = fragment
        checkInitWithActivity()
        initCommonParameter(mActivity.window)
    }

    /**
     * Used in dialog fragment
     * Instantiates a new Immersion bar.
     *
     * @param dialogFragment the dialog fragment
     */
    constructor(dialogFragment: DialogFragment) {
        mIsDialog = true
        mIsDialogFragment = true
        mActivity = dialogFragment.requireActivity()
        mSupportFragment = dialogFragment
        mDialog = dialogFragment.dialog
        checkInitWithActivity()
        initCommonParameter(mDialog?.window)
    }

    /**
     * Used in dialog fragment
     * Instantiates a new Immersion bar.
     *
     * @param dialogFragment the dialog fragment
     */
    constructor(dialogFragment: android.app.DialogFragment) {
        mIsDialog = true
        mIsDialogFragment = true
        mActivity = dialogFragment.activity
        mFragment = dialogFragment
        mDialog = dialogFragment.dialog
        checkInitWithActivity()
        initCommonParameter(mDialog?.window)
    }

    /**
     * Initialize in Dialog
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     * @param dialog   the dialog
     */
    constructor(activity: Activity, dialog: Dialog) {
        mIsDialog = true
        mActivity = activity
        mDialog = dialog
        checkInitWithActivity()
        initCommonParameter(mDialog?.window)
    }

    private fun checkInitWithActivity() {
        if (mParentBar == null) {
            mParentBar = with(mActivity)
        }
        mParentBar?.apply {
            if (!mInitialized) init()
        }
    }

    private fun initCommonParameter(window: Window?) {
        mWindow = window
        mBarParams = BarParams()
        mDecorView = mWindow?.decorView as ViewGroup?
        mContentView = mDecorView?.findViewById(android.R.id.content)
    }

    /**
     * Transparent status bar, transparent by default.
     *
     * @return the immersion bar
     */
    fun transparentStatusBar(): ImmersionBar {
        mBarParams?.statusBarColor = Color.TRANSPARENT
        return this
    }

    /**
     * Transparent navigation bar, default black.
     *
     * @return the immersion bar
     */
    fun transparentNavigationBar(): ImmersionBar {
        mBarParams?.navigationBarColor = Color.TRANSPARENT
        mBarParams?.fullScreen = true
        return this
    }

    /**
     * Transparent status bar and navigation bar.
     *
     * @return the immersion bar.
     */
    fun transparentBar(): ImmersionBar {
        mBarParams?.statusBarColor = Color.TRANSPARENT
        mBarParams?.navigationBarColor = Color.TRANSPARENT
        mBarParams?.fullScreen = true
        return this
    }

    /**
     * Status bar color.
     *
     * @param statusBarColor Status bar colors, resource files（R.color.xxx）
     * @return the immersion bar.
     */
    fun statusBarColor(@ColorRes statusBarColor: Int): ImmersionBar {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor))
    }

    /**
     * Status bar color
     *
     * @param statusBarColor Status bar colors, resource files（R.color.xxx）
     * @param alpha          the alpha  transparency
     * @return the immersion bar
     */
    fun statusBarColor(@ColorRes statusBarColor: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): ImmersionBar {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor))
    }

    /**
     * Status bar color
     *
     * @param statusBarColor          Status bar colors, resource files（R.color.xxx）
     * @param statusBarColorTransform the status bar color transform Status bar transformed color
     * @param alpha                   the alpha  transparency
     * @return the immersion bar
     */
    fun statusBarColor(statusBarColor: Int, statusBarColorTransform: Int, alpha: Float): ImmersionBar {
        return this.statusBarColorInt(
            ContextCompat.getColor(mActivity, statusBarColor),
            ContextCompat.getColor(mActivity, statusBarColorTransform),
            alpha
        )
    }

    /**
     * Status bar color.
     * Status bar color int immersion bar.
     *
     * @param statusBarColor the status bar color
     * @return the immersion bar
     */
    fun statusBarColor(statusBarColor: String): ImmersionBar {
        return this.statusBarColorInt(Color.parseColor(statusBarColor))
    }

    /**
     * Status bar color
     *
     * @param statusBarColor Status bar color
     * @param alpha          the alpha  transparency
     * @return the immersion bar
     */
    fun statusBarColor(statusBarColor: String, @FloatRange(from = 0.0, to = 1.0) alpha: Float): ImmersionBar {
        return this.statusBarColorInt(Color.parseColor(statusBarColor), alpha)
    }

    /**
     * Status bar color
     *
     * @param statusBarColor          Status bar color
     * @param statusBarColorTransform the status bar color transform Status bar transformed color
     * @param alpha                   the alpha  transparency
     * @return the immersion bar
     */
    fun statusBarColor(statusBarColor: String, statusBarColorTransform: String, @FloatRange(from = 0.0, to = 1.0) alpha: Float): ImmersionBar {
        return this.statusBarColorInt(
            Color.parseColor(statusBarColor),
            Color.parseColor(statusBarColorTransform),
            alpha
        )
    }

    /**
     * Status bar color
     *
     * @param statusBarColor Status bar colors, resource files（R.color.xxx）
     * @return the immersion bar
     */
    fun statusBarColorInt(@ColorInt statusBarColor: Int): ImmersionBar {
        mBarParams?.statusBarColor = statusBarColor
        return this
    }

    /**
     * Status bar color
     *
     * @param statusBarColor Status bar colors, resource files（R.color.xxx）
     * @param alpha          the alpha  transparency
     * @return the immersion bar
     */
    fun statusBarColorInt(@ColorInt statusBarColor: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): ImmersionBar {
        mBarParams?.statusBarColor = statusBarColor
        mBarParams?.statusBarAlpha = alpha
        return this
    }

    /**
     * Status bar color
     *
     * @param statusBarColor          Status bar colors, resource files（R.color.xxx）
     * @param statusBarColorTransform the status bar color transform Status bar transformed color
     * @param alpha                   the alpha  transparency
     * @return the immersion bar
     */
    fun statusBarColorInt(
        @ColorInt statusBarColor: Int,
        @ColorInt statusBarColorTransform: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ): ImmersionBar {
        mBarParams?.statusBarColor = statusBarColor
        mBarParams?.statusBarColorTransform = statusBarColorTransform
        mBarParams?.statusBarAlpha = alpha
        return this
    }

    /**
     * Navigation Bar Color
     *
     * @param navigationBarColor the navigation bar color Navigation Bar Color
     * @return the immersion bar
     */
    fun navigationBarColor(@ColorRes navigationBarColor: Int): ImmersionBar {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor))
    }

    /**
     * Navigation Bar Color
     *
     * @param navigationBarColor the navigation bar color Navigation Bar Color
     * @param navigationAlpha    the navigation alpha transparency
     * @return the immersion bar
     */
    fun navigationBarColor(@ColorRes navigationBarColor: Int, @FloatRange(from = 0.0, to = 1.0) navigationAlpha: Float): ImmersionBar {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor), navigationAlpha)
    }

    /**
     * Navigation Bar Color
     *
     * @param navigationBarColor          the navigation bar color Navigation Bar Color
     * @param navigationBarColorTransform the navigation bar color transform  Color of navigation bar after color change
     * @param navigationAlpha             the navigation alpha  transparency
     * @return the immersion bar
     */
    fun navigationBarColor(
        @ColorRes navigationBarColor: Int,
        @ColorRes navigationBarColorTransform: Int,
        @FloatRange(from = 0.0, to = 1.0) navigationAlpha: Float
    ): ImmersionBar {
        return this.navigationBarColorInt(
            ContextCompat.getColor(mActivity, navigationBarColor),
            ContextCompat.getColor(mActivity, navigationBarColorTransform), navigationAlpha
        )
    }

    /**
     * Navigation Bar Color
     *
     * @param navigationBarColor the navigation bar color Navigation Bar Color
     * @return the immersion bar
     */
    fun navigationBarColor(navigationBarColor: String): ImmersionBar {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor))
    }

    /**
     * Navigation Bar Color
     *
     * @param navigationBarColor the navigation bar color Navigation Bar Color
     * @param navigationAlpha    the navigation alpha transparency
     * @return the immersion bar
     */
    fun navigationBarColor(navigationBarColor: String, @FloatRange(from = 0.0, to = 1.0) navigationAlpha: Float): ImmersionBar {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor), navigationAlpha);
    }

    /**
     * Navigation Bar Color
     *
     * @param navigationBarColor          the navigation bar color Navigation Bar Color
     * @param navigationBarColorTransform the navigation bar color transform  Color of navigation bar after color change
     * @param navigationAlpha             the navigation alpha  transparency
     * @return the immersion bar
     */
    fun navigationBarColor(
        navigationBarColor: String,
        navigationBarColorTransform: String,
        @FloatRange(from = 0.0, to = 1.0) navigationAlpha: Float
    ): ImmersionBar {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor), Color.parseColor(navigationBarColorTransform), navigationAlpha)
    }

    /**
     * Navigation Bar Color
     *
     * @param navigationBarColor the navigation bar color Navigation Bar Color
     * @return the immersion bar
     */
    fun navigationBarColorInt(@ColorInt navigationBarColor: Int): ImmersionBar {
        mBarParams?.navigationBarColor = navigationBarColor
        return this
    }

    /**
     * Navigation Bar Color
     *
     * @param navigationBarColor the navigation bar color Navigation Bar Color
     * @param navigationAlpha    the navigation alpha transparency
     * @return the immersion bar
     */
    fun navigationBarColorInt(@ColorInt navigationBarColor: Int, @FloatRange(from = 0.0, to = 1.0) navigationAlpha: Float): ImmersionBar {
        mBarParams?.navigationBarColor = navigationBarColor
        mBarParams?.navigationBarAlpha = navigationAlpha
        return this
    }

    /**
     * Navigation Bar Color
     *
     * @param navigationBarColor          the navigation bar color Navigation Bar Color
     * @param navigationBarColorTransform the navigation bar color transform  Color of navigation bar after color change
     * @param navigationAlpha             the navigation alpha  transparency
     * @return the immersion bar
     */
    fun navigationBarColorInt(
        @ColorInt navigationBarColor: Int,
        @ColorInt navigationBarColorTransform: Int,
        @FloatRange(from = 0.0, to = 1.0) navigationAlpha: Float
    ): ImmersionBar {
        mBarParams?.navigationBarColor = navigationBarColor
        mBarParams?.navigationBarColorTransform = navigationBarColorTransform
        mBarParams?.navigationBarAlpha = navigationAlpha
        return this
    }

    /**
     * Status bar and navigation bar colors.
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    fun barColor(@ColorRes barColor: Int): ImmersionBar {
        return this.barColorInt(ContextCompat.getColor(mActivity, barColor))
    }

    /**
     * Status bar and navigation bar colors
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    fun barColor(@ColorRes barColor: Int, @FloatRange(from = 0.0, to = 1.0) barAlpha: Float): ImmersionBar {
        return this.barColorInt(ContextCompat.getColor(mActivity, barColor), barAlpha)
    }

    /**
     * Status bar and navigation bar colors
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    fun barColor(
        @ColorRes barColor: Int,
        @ColorRes barColorTransform: Int,
        @FloatRange(from = 0.0, to = 1.0) barAlpha: Float
    ): ImmersionBar {
        return this.barColorInt(
            ContextCompat.getColor(mActivity, barColor),
            ContextCompat.getColor(mActivity, barColorTransform), barAlpha
        )
    }

    /**
     * Status bar and navigation bar colors
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    fun barColor(barColor: String): ImmersionBar {
        return this.barColorInt(Color.parseColor(barColor))
    }

    /**
     * Status bar and navigation bar colors
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    fun barColor(barColor: String, @FloatRange(from = 0.0, to = 1.0) barAlpha: Float): ImmersionBar {
        return this.barColorInt(Color.parseColor(barColor), barAlpha)
    }

    /**
     * Status bar and navigation bar colors
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    fun barColor(barColor: String, barColorTransform: String, @FloatRange(from = 0.0, to = 1.0) barAlpha: Float): ImmersionBar {
        return this.barColorInt(Color.parseColor(barColor), Color.parseColor(barColorTransform), barAlpha)
    }

    /**
     * Status bar and navigation bar colors
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    fun barColorInt(@ColorInt barColor: Int): ImmersionBar {
        mBarParams?.statusBarColor = barColor
        mBarParams?.navigationBarColor = barColor
        return this
    }

    /**
     * Status bar and navigation bar colors
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    fun barColorInt(@ColorInt barColor: Int, @FloatRange(from = 0.0, to = 1.0) barAlpha: Float): ImmersionBar {
        mBarParams?.statusBarColor = barColor
        mBarParams?.navigationBarColor = barColor
        mBarParams?.statusBarAlpha = barAlpha
        mBarParams?.navigationBarAlpha = barAlpha
        return this
    }

    /**
     * Status bar and navigation bar colors
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    fun barColorInt(
        @ColorInt barColor: Int,
        @ColorInt barColorTransform: Int,
        @FloatRange(from = 0.0, to = 1.0) barAlpha: Float
    ): ImmersionBar {
        mBarParams?.statusBarColor = barColor
        mBarParams?.navigationBarColor = barColor

        mBarParams?.statusBarColorTransform = barColorTransform
        mBarParams?.navigationBarColorTransform = barColorTransform

        mBarParams?.statusBarAlpha = barAlpha
        mBarParams?.navigationBarAlpha = barAlpha
        return this
    }

    override fun onNavigationBarChange(show: Boolean, type: NavigationBarType) {

    }

    override fun run() {

    }

}