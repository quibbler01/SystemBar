package cn.quibbler.systembar

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.view.View
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

    /**
     * The final color of the status bar according to transparency
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    fun statusBarColorTransform(@ColorRes statusBarColorTransform: Int): ImmersionBar {
        return this.statusBarColorTransformInt(ContextCompat.getColor(mActivity,statusBarColorTransform))
    }

    /**
     * The final color of the status bar according to transparency
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    fun statusBarColorTransform(statusBarColorTransform:String):ImmersionBar{
        return this.statusBarColorTransformInt(Color.parseColor(statusBarColorTransform));
    }

    /**
     * The final color of the status bar according to transparency
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    fun statusBarColorTransformInt(@ColorInt statusBarColorTransform:Int):ImmersionBar{
        mBarParams?.statusBarColorTransform = statusBarColorTransform
        return this
    }

    /**
     * The final color of the navigation bar according to transparency
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    fun navigationBarColorTransform(@ColorRes navigationBarColorTransform:Int):ImmersionBar{
        return this.navigationBarColorTransformInt(ContextCompat.getColor(mActivity, navigationBarColorTransform));
    }

    /**
     * The final color of the navigation bar according to transparency
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    fun navigationBarColorTransform(navigationBarColorTransform:String):ImmersionBar{
        return this.navigationBarColorTransformInt(Color.parseColor(navigationBarColorTransform));
    }

    /**
     * The final color of the navigation bar according to transparency
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    fun navigationBarColorTransformInt(@ColorInt navigationBarColorTransform: Int): ImmersionBar {
        mBarParams?.navigationBarColorTransform = navigationBarColorTransform
        return this
    }

    /**
     * The final color of the status bar and navigation bar according to transparency
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    fun barColorTransform(@ColorRes barColorTransform:Int):ImmersionBar{
        return this.barColorTransformInt(ContextCompat.getColor(mActivity, barColorTransform));
    }

    /**
     * The final color of the status bar and navigation bar according to transparency
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    fun barColorTransform(barColorTransform:String):ImmersionBar{
        return this.barColorTransformInt(Color.parseColor(barColorTransform));
    }

    /**
     * The final color of the status bar and navigation bar according to transparency
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    fun barColorTransformInt(@ColorInt barColorTransform:Int):ImmersionBar{
        mBarParams?.statusBarColorTransform = barColorTransform
        mBarParams?.navigationBarColorTransform = barColorTransform
        return this
    }

    /**
     * Add color transformation supports View
     *
     * @param view the view
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(view:View):ImmersionBar{
        return this.addViewSupportTransformColorInt(view, mBarParams?.statusBarColorTransform)
    }

    /**
     * Add Color Transformation Support View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(view:View,@ColorRes viewColorAfterTransform:Int):ImmersionBar{
        return this.addViewSupportTransformColorInt(view, ContextCompat.getColor(mActivity, viewColorAfterTransform))
    }

    /**
     * Add Color Transformation Support View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(
        view: View?, @ColorRes viewColorBeforeTransform: Int,
        @ColorRes viewColorAfterTransform: Int
    ): ImmersionBar? {
        return this.addViewSupportTransformColorInt(
            view,
            ContextCompat.getColor(mActivity, viewColorBeforeTransform),
            ContextCompat.getColor(mActivity, viewColorAfterTransform)
        )
    }

    /**
     * Add Color Transformation Support View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(view: View?, viewColorAfterTransform: String?): ImmersionBar? {
        return this.addViewSupportTransformColorInt(view, Color.parseColor(viewColorAfterTransform))
    }

    /**
     * Add Color Transformation Support View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(
        view: View?, viewColorBeforeTransform: String?,
        viewColorAfterTransform: String?
    ): ImmersionBar? {
        return this.addViewSupportTransformColorInt(
            view,
            Color.parseColor(viewColorBeforeTransform),
            Color.parseColor(viewColorAfterTransform)
        )
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColorInt(view: View?, @ColorInt viewColorAfterTransform: Int): ImmersionBar? {
        requireNotNull(view) { "View parameter cannot be empty" }
        val map: MutableMap<Int, Int> = java.util.HashMap()
        map[mBarParams!!.statusBarColor] = viewColorAfterTransform
        mBarParams!!.viewMap[view] = map
        return this
    }


    /**
     * Add 颜色变换支持View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColorInt(
        view: View?, @ColorInt viewColorBeforeTransform: Int,
        @ColorInt viewColorAfterTransform: Int
    ): ImmersionBar? {
        requireNotNull(view) { "View参数不能为空" }
        val map: MutableMap<Int, Int> = java.util.HashMap()
        map[viewColorBeforeTransform] = viewColorAfterTransform
        mBarParams!!.viewMap[view] = map
        return this
    }

    /**
     * view透明度
     * View alpha immersion bar.
     *
     * @param viewAlpha the view alpha
     * @return the immersion bar
     */
    fun viewAlpha(@FloatRange(from = 0.0, to = 1.0) viewAlpha: Float): ImmersionBar? {
        mBarParams!!.viewAlpha = viewAlpha
        return this
    }

    /**
     * Remove support view immersion bar.
     *
     * @param view the view
     * @return the immersion bar
     */
    fun removeSupportView(view: View?): ImmersionBar? {
        requireNotNull(view) { "View parameter cannot be empty" }
        val map = mBarParams!!.viewMap[view]
        if (map != null && map.size != 0) {
            mBarParams!!.viewMap.remove(view)
        }
        return this
    }

    /**
     * Remove support all view immersion bar.
     *
     * @return the immersion bar
     */
    fun removeSupportAllView(): ImmersionBar? {
        if (mBarParams!!.viewMap.size() !== 0) {
            mBarParams!!.viewMap.clear()
        }
        return this
    }

    /**
     * 有导航栏的情况下，Activity是否全屏显示
     *
     * @param isFullScreen the is full screen
     * @return the immersion bar
     */
    fun fullScreen(isFullScreen: Boolean): ImmersionBar? {
        mBarParams!!.fullScreen = isFullScreen
        return this
    }

    /**
     * 状态栏透明度
     *
     * @param statusAlpha the status alpha
     * @return the immersion bar
     */
    fun statusBarAlpha(@FloatRange(from = 0.0, to = 1.0) statusAlpha: Float): ImmersionBar? {
        mBarParams!!.statusBarAlpha = statusAlpha
        mBarParams!!.statusBarTempAlpha = statusAlpha
        return this
    }

    /**
     * 导航栏透明度
     *
     * @param navigationAlpha the navigation alpha
     * @return the immersion bar
     */
    fun navigationBarAlpha(@FloatRange(from = 0.0, to = 1.0) navigationAlpha: Float): ImmersionBar? {
        mBarParams!!.navigationBarAlpha = navigationAlpha
        mBarParams!!.navigationBarTempAlpha = navigationAlpha
        return this
    }

    /**
     * 状态栏和导航栏透明度
     *
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    fun barAlpha(@FloatRange(from = 0.0, to = 1.0) barAlpha: Float): ImmersionBar? {
        mBarParams!!.statusBarAlpha = barAlpha
        mBarParams!!.statusBarTempAlpha = barAlpha
        mBarParams!!.navigationBarAlpha = barAlpha
        mBarParams!!.navigationBarTempAlpha = barAlpha
        return this
    }

    /**
     * 是否启用 自动根据StatusBar和NavigationBar颜色调整深色模式与亮色模式
     *
     * @param isEnable true启用 默认false
     * @return the immersion bar
     */
    fun autoDarkModeEnable(isEnable: Boolean): ImmersionBar? {
        return autoDarkModeEnable(isEnable, 0.2f)
    }

    /**
     * 是否启用自动根据StatusBar和NavigationBar颜色调整深色模式与亮色模式
     * Auto dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    fun autoDarkModeEnable(isEnable: Boolean, @FloatRange(from = 0.0, to = 1.0) autoDarkModeAlpha: Float): ImmersionBar? {
        mBarParams!!.autoStatusBarDarkModeEnable = isEnable
        mBarParams!!.autoStatusBarDarkModeAlpha = autoDarkModeAlpha
        mBarParams!!.autoNavigationBarDarkModeEnable = isEnable
        mBarParams!!.autoNavigationBarDarkModeAlpha = autoDarkModeAlpha
        return this
    }

    /**
     * 是否启用自动根据StatusBar颜色调整深色模式与亮色模式
     * Auto status bar dark mode enable immersion bar.
     *
     * @param isEnable the is enable
     * @return the immersion bar
     */
    fun autoStatusBarDarkModeEnable(isEnable: Boolean): ImmersionBar? {
        return autoStatusBarDarkModeEnable(isEnable, 0.2f)
    }

    /**
     * 是否启用自动根据StatusBar颜色调整深色模式与亮色模式
     * Auto status bar dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    fun autoStatusBarDarkModeEnable(isEnable: Boolean, @FloatRange(from = 0.0, to = 1.0) autoDarkModeAlpha: Float): ImmersionBar? {
        mBarParams!!.autoStatusBarDarkModeEnable = isEnable
        mBarParams!!.autoStatusBarDarkModeAlpha = autoDarkModeAlpha
        return this
    }

    /**
     * 是否启用自动根据StatusBar颜色调整深色模式与亮色模式
     * Auto navigation bar dark mode enable immersion bar.
     *
     * @param isEnable the is enable
     * @return the immersion bar
     */
    fun autoNavigationBarDarkModeEnable(isEnable: Boolean): ImmersionBar? {
        return autoNavigationBarDarkModeEnable(isEnable, 0.2f)
    }

    /**
     * 是否启用自动根据NavigationBar颜色调整深色模式与亮色模式
     * Auto navigation bar dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    fun autoNavigationBarDarkModeEnable(isEnable: Boolean, @FloatRange(from = 0.0, to = 1.0) autoDarkModeAlpha: Float): ImmersionBar? {
        mBarParams!!.autoNavigationBarDarkModeEnable = isEnable
        mBarParams!!.autoNavigationBarDarkModeAlpha = autoDarkModeAlpha
        return this
    }


    /**
     * 状态栏字体深色或亮色
     *
     * @param isDarkFont true 深色
     * @return the immersion bar
     */
    fun statusBarDarkFont(isDarkFont: Boolean): ImmersionBar? {
        return statusBarDarkFont(isDarkFont, 0.2f)
    }














    override fun onNavigationBarChange(show: Boolean, type: NavigationBarType) {

    }

    override fun run() {

    }

}