package cn.quibbler.systembar

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

/**
 * Android 4.4 and above immersive and bar management
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class ImmersionBar : ImmersionCallback {

    companion object {

        /**
         * Use in Activity
         * With immersion bar.
         *
         * @param activity the activity
         * @return the immersion bar
         */
        fun with(activity: Activity): ImmersionBar {
            return getRetriever().get(activity, false)!!
        }

        /**
         * Use in Activity
         *
         * @param activity the activity
         * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
         * @return the immersion bar
         */
        fun with(activity: Activity, isOnly: Boolean): ImmersionBar {
            return getRetriever().get(activity, isOnly)!!
        }

        /**
         * Used in Fragment
         * With immersion bar.
         *
         * @param fragment the fragment
         * @return the immersion bar
         */
        fun with(fragment: Fragment): ImmersionBar {
            return getRetriever().get(fragment, false)!!
        }

        /**
         * Used in Fragment
         * With immersion bar.
         *
         * @param fragment the fragment
         * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
         * @return the immersion bar
         */
        fun with(fragment: Fragment, isOnly: Boolean): ImmersionBar {
            return getRetriever().get(fragment, isOnly)!!
        }

        /**
         * Used in Fragment
         * With immersion bar.
         *
         * @param fragment the fragment
         * @return the immersion bar
         */
        fun with(fragment: android.app.Fragment): ImmersionBar {
            return getRetriever().get(fragment, false)!!
        }

        /**
         * Used in Fragment
         * With immersion bar.
         *
         * @param fragment the fragment
         * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
         * @return the immersion bar
         */
        fun with(fragment: android.app.Fragment, isOnly: Boolean): ImmersionBar {
            return getRetriever().get(fragment, isOnly)!!
        }

        /**
         * Used in Dialog Fragment
         * With immersion bar.
         *
         * @param dialogFragment the dialog fragment
         * @return the immersion bar
         */
        fun with(dialogFragment: DialogFragment): ImmersionBar {
            return getRetriever().get(dialogFragment, false)!!
        }

        /**
         * Used in Dialog Fragment
         *
         * @param dialogFragment the dialog fragment
         * @param isOnly         the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
         * @return the immersion bar
         */
        fun with(dialogFragment: DialogFragment, isOnly: Boolean): ImmersionBar {
            return getRetriever().get(dialogFragment, isOnly)!!
        }

        /**
         * Used in Dialog Fragment
         * With immersion bar.
         *
         * @param dialogFragment the dialog fragment
         * @return the immersion bar
         */
        fun with(dialogFragment: android.app.DialogFragment): ImmersionBar {
            return getRetriever().get(dialogFragment, false)!!
        }

        /**
         * Used in Dialog Fragment
         *
         * @param dialogFragment the dialog fragment
         * @param isOnly         the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
         * @return the immersion bar
         */
        fun with(dialogFragment: android.app.DialogFragment, isOnly: Boolean): ImmersionBar {
            return getRetriever().get(dialogFragment, isOnly)!!
        }

        /**
         * Use in dialog
         * With immersion bar.
         *
         * @param activity the activity
         * @param dialog   the dialog
         * @return the immersion bar
         */
        fun with(activity: Activity, dialog: Dialog): ImmersionBar {
            return getRetriever().get(activity, dialog, false)!!
        }

        /**
         * Use in dialog
         *
         * @param activity the activity
         * @param dialog   the dialog
         * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
         * @return the immersion bar
         */
        fun with(activity: Activity, dialog: Dialog, isOnly: Boolean): ImmersionBar {
            return getRetriever().get(activity, dialog, isOnly)!!
        }

        /**
         * Destroy Fragment
         *
         * @param fragment the Fragment
         */
        fun destroy(fragment: Fragment): ImmersionBar {
            return getRetriever().get(fragment, false)!!
        }

        /**
         * Destroy Fragment
         *
         * @param fragment the Fragment
         * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
         */
        fun destroy(fragment: Fragment, isOnly: Boolean) {
            getRetriever().destroy(fragment, isOnly)
        }

        /**
         * Destroy Fragment
         *
         * @param fragment the android.app.Fragment
         */
        fun destroy(fragment: android.app.Fragment) {
            getRetriever().destroy(fragment, false)
        }

        /**
         * Destroy Fragment
         *
         * @param fragment the android.app.Fragment
         * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
         */
        fun destroy(fragment: android.app.Fragment, isOnly: Boolean) {
            getRetriever().destroy(fragment, isOnly)
        }

        /**
         * Destroy in Dialog, excluding Dialog Fragment
         *
         * @param activity the activity
         * @param dialog   the dialog
         */
        fun destroy(activity: Activity, dialog: Dialog) {
            getRetriever().destroy(activity, dialog, false)
        }

        /**
         * Destroy in Dialog, excluding Dialog Fragment
         *
         * @param activity the activity
         * @param dialog   the dialog
         * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
         */
        fun destroy(activity: Activity, dialog: Dialog, isOnly: Boolean) {
            getRetriever().destroy(activity, dialog, isOnly)
        }

        private fun getRetriever(): RequestManagerRetriever {
            return RequestManagerRetriever
        }

        private fun isEmpty(str: String?): Boolean {
            return str == null || str.trim().isEmpty()
        }

    }

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
    private var mBarParams: BarParams

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
    private val mTagMap: HashMap<String, BarParams> = HashMap()

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mBarParams?.barEnable == true) {
            //Update Bar's parameters
            updateBarParams()
            //Set Immersion
            setBar()
            //Correction interface display
            fitsWindows()
            //Conflicts between the adaptive soft keyboard and the bottom input box
            fitsKeyboard()
            //change view color
            transformView()
            mInitialized = true
        }
    }

    fun onDestroy() {
        //Cancel listening
        cancelListener();
        if (mIsDialog) {
            mParentBar?.let {
                it.mBarParams?.keyboardEnable = it.mKeyboardTempEnable
                if (it.mBarParams?.barHide != BarHide.FLAG_SHOW_BAR) {
                    it.setBar()
                }
            }
        }
        mInitialized = false
    }

    fun onResume() {
        updateBarConfig()
        if (!mIsFragment && mInitialized) {
            mBarParams?.let {
                if (isEMUI3_x() && it.navigationBarWithEMUI3Enable) {
                    init()
                } else {
                    if (it.barHide != BarHide.FLAG_SHOW_BAR) {
                        setBar()
                    }
                }
            }
        }
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        updateBarConfig()
        if (isEMUI3_x() || Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            if (mInitialized && !mIsFragment && mBarParams?.navigationBarWithKitkatEnable == true) {
                init()
            } else {
                fitsWindow()
            }
        } else {
            fitsWindow()
        }
    }

    /**
     * Update bar params.
     */
    private fun updateBarParams() {
        adjustDarkModeParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Get information about Bar
            if (!mInitialized || mIsFragment) {
                updateBarConfig()
            }
            mParentBar?.let {
                //If used in Fragment, let Activity synchronize the Bar Params parameter of Fragment
                if (mIsFragment) {
                    it.mBarParams = mBarParams
                }
                //If the keyboard Enable is set to true in the dialog, the keyboard Enable set in the activity is false
                if (mIsDialog) {
                    if (it.mKeyboardTempEnable) {
                        it.mBarParams?.keyboardEnable = false
                    }
                }
            }
        }
    }

    /**
     * Initialize the status bar and navigation bar
     */
    fun setBar() {
        var uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isEMUI3_x()) {
            //Suitable for Liu Haiping
            fitsNotchScreen()
            //Initialize 5.0 or above, including 5.0
            uiFlags = initBarAboveLOLLIPOP(uiFlags)
            //Set the status bar font to dark color above Android 6.0
            uiFlags = setStatusBarDarkFont(uiFlags)
            //Set the navigation bar icon to dark color above Android 8.0
            uiFlags = setNavigationIconDark(uiFlags)
            //Adapt to Android 11 or above
            setBarDarkFontAboveR()
        } else {
            //Initialize below 5.0 and above 4.4 immersed
            initBarBelowLOLLIPOP()
        }
        //Hide the status bar or navigation bar
        uiFlags = hideBarBelowR(uiFlags)
        //Apply flag
        mDecorView?.systemUiVisibility = uiFlags
        //Adapt to black and white of Xiaomi and Meizu status bar
        setSpecialBarDarkMode()
        //Adapt to Android 11 or above
        hideBarAboveR()
        //The navigation bar shows hidden monitoring. Currently, only Huawei and Xiaomi mobile phones with the navigation bar are supported
        if (mBarParams?.onNavigationBarListener != null) {
            NavigationBarObserver.register(mActivity.application)
        }
    }

    private fun setBarDarkFontAboveR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setStatusBarDarkFontAboveR()
            setNavigationIconDarkAboveR()
        }
    }

    private fun setSpecialBarDarkMode() {
        if (isMIUI6Later()) {
            //Modify miui status bar font color
            setMIUIBarDark(mWindow, IMMERSION_STATUS_BAR_DARK_MIUI, mBarParams.statusBarDarkFont)
            //Modify the miui navigation bar icon to black
            if (mBarParams.navigationBarEnable) {
                setMIUIBarDark(mWindow, IMMERSION_STATUS_BAR_DARK_MIUI, mBarParams.navigationBarDarkIcon)
            }
        }
        //Modify Flyme OS status bar font color
        if (isFlymeOS4Later()) {
            if (mBarParams.flymeOSStatusBarFontColor != 0) {
                setStatusBarDarkIcon(mActivity, mBarParams.flymeOSStatusBarFontColor)
            } else {
                setStatusBarDarkIcon(mActivity, mBarParams.statusBarDarkFont)
            }
        }
    }

    /**
     * Fits notch screen.
     */
    private fun fitsNotchScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !mInitialized) {
            try {
                val lp = mWindow?.attributes
                lp?.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                mWindow?.attributes = lp
            } catch (ignored: Exception) {
            }
        }
    }

    /**
     * Initialize the status bar and navigation bar above android 5.0
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun initBarAboveLOLLIPOP(uiFlags: Int): Int {
        //Get default navigation bar color
        if (!mInitialized) {
            mBarParams.defaultNavigationBarColor = mWindow.navigationBarColor
        }
        //The activity is displayed in full screen, but the status bar will not be hidden and overwritten. The status bar is still visible, and the top layout of the activity will be covered by the status bar.
        var uiFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or uiFlags
        if (mBarParams.fullScreen && mBarParams.navigationBarEnable) {
            //The activity is displayed in full screen, but the navigation bar will not be hidden and overwritten. The navigation bar is still visible, and the bottom layout of the activity will be covered by the navigation bar.
            uiFlag = uiFlag or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        mWindow?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //Determine whether there is a navigation bar
        if (mBarConfig.mHasNavigationBar) {
            mWindow?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        //You need to set this to set the color of the status bar and navigation bar
        mWindow?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //Set status bar color
        if (mBarParams.statusBarColorEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mWindow?.isStatusBarContrastEnforced = false
            }
            mWindow?.statusBarColor = ColorUtils.blendARGB(mBarParams.statusBarColor, mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha)
        } else {
            mWindow?.statusBarColor = ColorUtils.blendARGB(mBarParams.statusBarColor, Color.TRANSPARENT, mBarParams.statusBarAlpha)
        }
        //
        if (mBarParams.navigationBarEnable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mWindow?.isNavigationBarContrastEnforced = false
            }
            mWindow?.navigationBarColor = ColorUtils.blendARGB(mBarParams.navigationBarColor, mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha)
        } else {
            mWindow?.navigationBarColor = mBarParams.defaultNavigationBarColor;
        }
        return uiFlag
    }

    /**
     * Initialize the status bar and navigation bar of Android 4.4 and emui3.1
     */
    private fun initBarBelowLOLLIPOP() {
        //Transparent status bar
        mWindow?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //Create a fake status bar
        setupStatusBarView()
        if (mBarConfig.mHasNavigationBar || isEMUI3_x()) {
            if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
                //Transparent navigation bar, set this. If there is a navigation bar, the bottom layout will be covered by the navigation bar
                mWindow?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            } else {
                mWindow?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
            if (mNavigationBarHeight == 0) {
                mNavigationBarHeight = mBarConfig.mNavigationBarHeight
            }
            if (mNavigationBarWidth == 0) {
                mNavigationBarWidth = mBarConfig.mNavigationBarWidth
            }
            //Create a fake navigation bar
            setupNavBarView()
        }
    }

    /**
     * Set a status bar with customizable colors
     */
    private fun setupStatusBarView() {
        var statusBarView: View? = mDecorView?.findViewById<View?>(IMMERSION_STATUS_BAR_VIEW_ID)
        if (statusBarView == null) {
            statusBarView = View(mActivity)
            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mBarConfig.mStatusBarHeight)
            params.gravity = Gravity.TOP
            statusBarView.layoutParams = params
            statusBarView.visibility = View.VISIBLE
            statusBarView.id = IMMERSION_STATUS_BAR_VIEW_ID
            mDecorView?.addView(statusBarView)
        }
        if (mBarParams.statusBarColorEnabled) {
            statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor, mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));
        } else {
            statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor, Color.TRANSPARENT, mBarParams.statusBarAlpha))
        }
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
        return this.statusBarColorTransformInt(ContextCompat.getColor(mActivity, statusBarColorTransform))
    }

    /**
     * The final color of the status bar according to transparency
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    fun statusBarColorTransform(statusBarColorTransform: String): ImmersionBar {
        return this.statusBarColorTransformInt(Color.parseColor(statusBarColorTransform));
    }

    /**
     * The final color of the status bar according to transparency
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    fun statusBarColorTransformInt(@ColorInt statusBarColorTransform: Int): ImmersionBar {
        mBarParams?.statusBarColorTransform = statusBarColorTransform
        return this
    }

    /**
     * The final color of the navigation bar according to transparency
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    fun navigationBarColorTransform(@ColorRes navigationBarColorTransform: Int): ImmersionBar {
        return this.navigationBarColorTransformInt(ContextCompat.getColor(mActivity, navigationBarColorTransform));
    }

    /**
     * The final color of the navigation bar according to transparency
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    fun navigationBarColorTransform(navigationBarColorTransform: String): ImmersionBar {
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
    fun barColorTransform(@ColorRes barColorTransform: Int): ImmersionBar {
        return this.barColorTransformInt(ContextCompat.getColor(mActivity, barColorTransform));
    }

    /**
     * The final color of the status bar and navigation bar according to transparency
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    fun barColorTransform(barColorTransform: String): ImmersionBar {
        return this.barColorTransformInt(Color.parseColor(barColorTransform));
    }

    /**
     * The final color of the status bar and navigation bar according to transparency
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    fun barColorTransformInt(@ColorInt barColorTransform: Int): ImmersionBar {
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
    fun addViewSupportTransformColor(view: View): ImmersionBar {
        return this.addViewSupportTransformColorInt(view, mBarParams?.statusBarColorTransform)
    }

    /**
     * Add Color Transformation Support View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(view: View, @ColorRes viewColorAfterTransform: Int): ImmersionBar {
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
    fun removeSupportView(view: View?): ImmersionBar {
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
    fun removeSupportAllView(): ImmersionBar {
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
    fun fullScreen(isFullScreen: Boolean): ImmersionBar {
        mBarParams!!.fullScreen = isFullScreen
        return this
    }

    /**
     * 状态栏透明度
     *
     * @param statusAlpha the status alpha
     * @return the immersion bar
     */
    fun statusBarAlpha(@FloatRange(from = 0.0, to = 1.0) statusAlpha: Float): ImmersionBar {
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
    fun navigationBarAlpha(@FloatRange(from = 0.0, to = 1.0) navigationAlpha: Float): ImmersionBar {
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
    fun barAlpha(@FloatRange(from = 0.0, to = 1.0) barAlpha: Float): ImmersionBar {
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
    fun autoDarkModeEnable(isEnable: Boolean): ImmersionBar {
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
    fun autoDarkModeEnable(isEnable: Boolean, @FloatRange(from = 0.0, to = 1.0) autoDarkModeAlpha: Float): ImmersionBar {
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
    fun autoStatusBarDarkModeEnable(isEnable: Boolean): ImmersionBar {
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
    fun autoStatusBarDarkModeEnable(isEnable: Boolean, @FloatRange(from = 0.0, to = 1.0) autoDarkModeAlpha: Float): ImmersionBar {
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
    fun autoNavigationBarDarkModeEnable(isEnable: Boolean): ImmersionBar {
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
    fun autoNavigationBarDarkModeEnable(isEnable: Boolean, @FloatRange(from = 0.0, to = 1.0) autoDarkModeAlpha: Float): ImmersionBar {
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
    fun statusBarDarkFont(isDarkFont: Boolean): ImmersionBar {
        return statusBarDarkFont(isDarkFont, 0.2f)
    }

    /**
     * The font of the status bar is dark or bright. Determine whether the device supports color change of the status bar to set the transparency of the status bar
     * Status bar dark font immersion bar.
     *
     * @param isDarkFont  the is dark font
     * @param statusAlpha the status alpha 如果不支持状态栏字体变色可以使用statusAlpha来指定状态栏透明度，比如白色状态栏的时候可以用到
     * @return the immersion bar
     */
    fun statusBarDarkFont(isDarkFont: Boolean, @FloatRange(from = 0.0, to = 1.0) statusAlpha: Float): ImmersionBar {
        mBarParams!!.statusBarDarkFont = isDarkFont
        if (isDarkFont && !ImmersionBar.isSupportStatusBarDarkFont()) {
            mBarParams!!.statusBarAlpha = statusAlpha
        } else {
            mBarParams!!.flymeOSStatusBarFontColor = mBarParams!!.flymeOSStatusBarFontTempColor
            mBarParams!!.statusBarAlpha = mBarParams!!.statusBarTempAlpha
        }
        return this
    }

    /**
     * The navigation bar icon is dark or bright, and only supports android o or above
     * Navigation bar dark icon immersion bar.
     *
     * @param isDarkIcon the is dark icon
     * @return the immersion bar
     */
    fun navigationBarDarkIcon(isDarkIcon: Boolean): ImmersionBar {
        return navigationBarDarkIcon(isDarkIcon, 0.2f)
    }

    /**
     * 导航栏图标深色或亮色，只支持android o以上版本，判断设备支不支持导航栏图标变色来设置导航栏透明度
     * Navigation bar dark icon immersion bar.
     *
     * @param isDarkIcon      the is dark icon
     * @param navigationAlpha the navigation alpha 如果不支持导航栏图标变色可以使用navigationAlpha来指定导航栏透明度，比如白色导航栏的时候可以用到
     * @return the immersion bar
     */
    fun navigationBarDarkIcon(isDarkIcon: Boolean, @FloatRange(from = 0.0, to = 1.0) navigationAlpha: Float): ImmersionBar {
        mBarParams!!.navigationBarDarkIcon = isDarkIcon
        if (isDarkIcon && !ImmersionBar.isSupportNavigationIconDark()) {
            mBarParams?.navigationBarAlpha = navigationAlpha
        } else {
            mBarParams?.navigationBarAlpha = mBarParams!!.navigationBarTempAlpha
        }
        return this
    }

    /**
     * Modify the font color of the status bar of the mobile phone in the Flyme OS system, which has priority over the statusBarDarkFont (boolean isDarkFont) method
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    fun flymeOSStatusBarFontColor(@ColorRes flymeOSStatusBarFontColor: Int): ImmersionBar {
        mBarParams?.flymeOSStatusBarFontColor = ContextCompat.getColor(mActivity, flymeOSStatusBarFontColor)
        mBarParams?.flymeOSStatusBarFontTempColor = mBarParams!!.flymeOSStatusBarFontColor
        return this
    }

    /**
     * Modify the font color of the status bar of the mobile phone in the Flyme OS system, which has priority over the statusBarDarkFont (boolean isDarkFont) method
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    fun flymeOSStatusBarFontColor(flymeOSStatusBarFontColor: String?): ImmersionBar {
        mBarParams!!.flymeOSStatusBarFontColor = Color.parseColor(flymeOSStatusBarFontColor)
        mBarParams!!.flymeOSStatusBarFontTempColor = mBarParams!!.flymeOSStatusBarFontColor
        return this
    }

    /**
     * Modify the font color of the status bar of the mobile phone in the Flyme OS system, which has priority over the statusBarDarkFont (boolean isDarkFont) method
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    fun flymeOSStatusBarFontColorInt(@ColorInt flymeOSStatusBarFontColor: Int): ImmersionBar {
        mBarParams!!.flymeOSStatusBarFontColor = flymeOSStatusBarFontColor
        mBarParams!!.flymeOSStatusBarFontTempColor = mBarParams!!.flymeOSStatusBarFontColor
        return this
    }

    /**
     * Hide the navigation bar or status bar
     *
     * @param barHide the bar hide
     * @return the immersion bar
     */
    fun hideBar(barHide: BarHide?): ImmersionBar {
        mBarParams!!.barHide = barHide!!
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || OSUtils.isEMUI3_x()) {
            mBarParams!!.hideNavigationBar = mBarParams!!.barHide === BarHide.FLAG_HIDE_NAVIGATION_BAR ||
                    mBarParams!!.barHide === BarHide.FLAG_HIDE_BAR
        }
        return this
    }

    /**
     * To solve the overlapping problem between the layout and the status bar,
     * this method will call the setFitsSystemWindows method of the system view.
     * Once the window has been focused and set to false, it will not take effect.
     * This method will not be used by default.
     * If it is a gradient color status bar and a top picture,
     * please do not call this method or set it to false Apply system fits instant bar
     * Apply system fits immersion bar.
     *
     * @param applySystemFits the apply system fits
     * @return the immersion bar
     */
    fun applySystemFits(applySystemFits: Boolean): ImmersionBar {
        mBarParams!!.fitsLayoutOverlapEnable = !applySystemFits
        setFitsSystemWindows(mActivity, applySystemFits)
        return this
    }

    /**
     * Solve the problem of overlapping layout and status bar
     *
     * @param fits the fits
     * @return the immersion bar
     */
    fun fitsSystemWindows(fits: Boolean): ImmersionBar {
        mBarParams!!.fits = fits
        if (mBarParams!!.fits) {
            if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
                mFitsStatusBarType = FLAG_FITS_SYSTEM_WINDOWS
            }
        } else {
            mFitsStatusBarType = FLAG_FITS_DEFAULT
        }
        return this
    }

    /**
     * Solve the overlapping problem between layout and status bar, and support sideslip return
     * Fits system windows immersion bar.
     *
     * @param fits         the fits
     * @param contentColor the content color 整体界面背景色
     * @return the immersion bar
     */
    fun fitsSystemWindows(fits: Boolean, @ColorRes contentColor: Int): ImmersionBar {
        return fitsSystemWindowsInt(fits, ContextCompat.getColor(mActivity, contentColor))
    }

    /**
     * Solve the overlapping problem between layout and status bar, and support sideslip return
     * Fits system windows immersion bar.
     *
     * @param fits                  the fits
     * @param contentColor          the content color 整体界面背景色
     * @param contentColorTransform the content color transform  整体界面变换后的背景色
     * @param contentAlpha          the content alpha 整体界面透明度
     * @return the immersion bar
     */
    fun fitsSystemWindows(
        fits: Boolean, @ColorRes contentColor: Int, @ColorRes contentColorTransform: Int, @FloatRange(from = 0f, to = 1f) contentAlpha: Float
    ): ImmersionBar {
        return fitsSystemWindowsInt(
            fits, ContextCompat.getColor(mActivity, contentColor),
            ContextCompat.getColor(mActivity, contentColorTransform), contentAlpha
        )
    }

    /**
     * Solve the overlapping problem between layout and status bar, and support sideslip return
     * Fits system windows int immersion bar.
     *
     * @param fits         the fits
     * @param contentColor the content color 整体界面背景色
     * @return the immersion bar
     */
    fun fitsSystemWindowsInt(fits: Boolean, @ColorInt contentColor: Int): ImmersionBar {
        return fitsSystemWindowsInt(fits, contentColor, Color.BLACK, 0f)
    }

    /**
     * Solve the overlapping problem between layout and status bar, and support sideslip return
     * Fits system windows int immersion bar.
     *
     * @param fits                  the fits
     * @param contentColor          the content color Overall interface background color
     * @param contentColorTransform the content color transform Background color after overall interface transformation
     * @param contentAlpha          the content alpha Overall interface transparency
     * @return the immersion bar
     */
    fun fitsSystemWindowsInt(
        fits: Boolean, @ColorInt contentColor: Int, @ColorInt contentColorTransform: Int, @FloatRange(from = 0f, to = 1f) contentAlpha: Float
    ): ImmersionBar {
        mBarParams?.fits = fits
        mBarParams?.contentColor = contentColor
        mBarParams?.contentColorTransform = contentColorTransform
        mBarParams?.contentAlpha = contentAlpha
        if (mBarParams!!.fits) {
            if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
                mFitsStatusBarType = FLAG_FITS_SYSTEM_WINDOWS
            }
        } else {
            mFitsStatusBarType = FLAG_FITS_DEFAULT
        }
        mContentView?.setBackgroundColor(
            ColorUtils.blendARGB(
                mBarParams!!.contentColor,
                mBarParams!!.contentColorTransform, mBarParams!!.contentAlpha
            )
        )
        return this
    }

    /**
     * Whether it is possible to repair the overlap between the status bar and the layout. The default value is true, which is only applicable to the Immersion Barstatus Bar View,
     * ImmersionBar#titleBar，ImmersionBar#titleBarMarginTop
     * Fits layout overlap enable immersion bar.
     *
     * @param fitsLayoutOverlapEnable the fits layout overlap enable
     * @return the immersion bar
     */
    fun fitsLayoutOverlapEnable(fitsLayoutOverlapEnable: Boolean): ImmersionBar? {
        mBarParams?.fitsLayoutOverlapEnable = fitsLayoutOverlapEnable
        return this
    }

    /**
     * Dynamically set status bar layout by status bar height
     *
     * @param view the view
     * @return the immersion bar
     */
    fun statusBarView(view: View?): ImmersionBar? {
        if (view == null) {
            return this
        }
        mBarParams!!.statusBarView = view
        if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
            mFitsStatusBarType = FLAG_FITS_STATUS
        }
        return this
    }

    /**
     * The status bar layout is dynamically set through the status bar height and can only be used in activities
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    fun statusBarView(@IdRes viewId: Int): ImmersionBar? {
        return statusBarView(mActivity.findViewById(viewId))
    }

    /**
     * Dynamically set status bar layout by status bar height
     * Status bar view immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    fun statusBarView(@IdRes viewId: Int, rootView: View): ImmersionBar? {
        return statusBarView(rootView.findViewById(viewId))
    }

    /**
     * There are more ways to solve the overlap between the status bar and the top of the layout
     * Title bar immersion bar.
     *
     * @param view the view
     * @return the immersion bar
     */
    fun titleBar(view: View?): ImmersionBar? {
        return if (view == null) {
            this
        } else titleBar(view, true)
    }

    /**
     * There are more ways to solve the overlap between the status bar and the top of the layout
     * Title bar immersion bar.
     *
     * @param view                          the view
     * @param statusBarColorTransformEnable the status bar flag 默认为true false表示状态栏不支持变色，true表示状态栏支持变色
     * @return the immersion bar
     */
    fun titleBar(view: View?, statusBarColorTransformEnable: Boolean): ImmersionBar {
        if (view == null) {
            return this
        }
        if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
            mFitsStatusBarType = FLAG_FITS_TITLE
        }
        mBarParams!!.titleBarView = view
        mBarParams!!.statusBarColorEnabled = statusBarColorTransformEnable
        return this
    }

    /**
     * There are more ways to solve the overlap between the status bar and the top of the layout.
     * Only Activity is supported
     * Title bar immersion bar.
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    fun titleBar(@IdRes viewId: Int): ImmersionBar? {
        return titleBar(viewId, true)
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId                        the view id
     * @param statusBarColorTransformEnable the status bar flag
     * @return the immersion bar
     */
    fun titleBar(@IdRes viewId: Int, statusBarColorTransformEnable: Boolean): ImmersionBar? {
        return if (mSupportFragment != null && mSupportFragment!!.view != null) {
            titleBar(mSupportFragment!!.view!!.findViewById(viewId), statusBarColorTransformEnable)
        } else if (mFragment != null && mFragment!!.view != null) {
            titleBar(mFragment!!.view!!.findViewById(viewId), statusBarColorTransformEnable)
        } else {
            titleBar(mActivity.findViewById(viewId), statusBarColorTransformEnable)
        }
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    fun titleBar(@IdRes viewId: Int, rootView: View): ImmersionBar {
        return titleBar(rootView.findViewById(viewId), true)
    }

    /**
     * There are more ways to solve the overlap between the status bar and the top of the layout. It supports any view
     * Title bar immersion bar.
     *
     * @param viewId                        the view id
     * @param rootView                      the root view
     * @param statusBarColorTransformEnable the status bar flag 默认为true false表示状态栏不支持变色，true表示状态栏支持变色
     * @return the immersion bar
     */
    fun titleBar(@IdRes viewId: Int, rootView: View, statusBarColorTransformEnable: Boolean): ImmersionBar {
        return titleBar(rootView.findViewById(viewId), statusBarColorTransformEnable)
    }

    /**
     * The height of the title block drawn from the top is the height of the status bar
     * Title bar margin top immersion bar.
     *
     * @param viewId the view id   标题栏资源id
     * @return the immersion bar
     */
    fun titleBarMarginTop(@IdRes viewId: Int): ImmersionBar {
        if (mSupportFragment?.view != null) {
            return titleBarMarginTop(mSupportFragment?.view?.findViewById(viewId))
        } else if (mFragment?.view != null) {
            return titleBarMarginTop(mFragment?.view?.findViewById(viewId))
        } else {
            return titleBarMarginTop(mActivity?.findViewById(viewId))
        }
    }

    /**
     * The height of the title block drawn from the top is the height of the status bar
     * Title bar margin top immersion bar.
     *
     * @param viewId   the view id  标题栏资源id
     * @param rootView the root view  布局view
     * @return the immersion bar
     */
    fun titleBarMarginTop(@IdRes viewId: Int, rootView: View): ImmersionBar {
        return titleBarMarginTop(rootView.findViewById(viewId))
    }

    /**
     * The height of the title block drawn from the top is the height of the status bar
     * Title bar margin top immersion bar.
     *
     * @param view the view  要改变的标题栏view
     * @return the immersion bar
     */
    fun titleBarMarginTop(view: View?): ImmersionBar {
        view?.let {
            if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
                mFitsStatusBarType = FLAG_FITS_TITLE_MARGIN_TOP
            }
            mBarParams?.titleBarView = it
        }
        return this
    }

    /**
     * Support the interface with action bar, call this method, and start drawing from the bottom of the action bar
     * Support action bar immersion bar.
     *
     * @param isSupportActionBar the is support action bar
     * @return the immersion bar
     */
    fun supportActionBar(isSupportActionBar: Boolean): ImmersionBar {
        mBarParams?.isSupportActionBar = isSupportActionBar
        return this
    }

    /**
     * Status bar color transform enable immersion bar.
     *
     * @param statusBarColorTransformEnable the status bar flag
     * @return the immersion bar
     */
    fun statusBarColorTransformEnable(statusBarColorTransformEnable: Boolean): ImmersionBar {
        mBarParams?.statusBarColorEnabled = statusBarColorTransformEnable
        return this
    }

    /**
     * One touch reset of all parameters
     * Reset immersion bar.
     *
     * @return the immersion bar
     */
    fun reset(): ImmersionBar {
        mBarParams = BarParams()
        mFitsStatusBarType = FLAG_FITS_DEFAULT
        return this
    }

    /**
     * Set a tag for a page to identify the bar attribute of the page.
     * Add tag bar tag.
     *
     * @param tag the tag
     * @return the bar tag
     */
    fun addTag(tag: String?): ImmersionBar {
        if (tag?.isNotEmpty() != true) {
            throw IllegalArgumentException("tag cannot be empty")
        }
        mBarParams?.clone()?.let {
            mTagMap[tag] = it
        }
        return this
    }


    /**
     * Restore the parameters of a call according to the tag
     * Recover immersion bar.
     *
     * @param tag the tag
     * @return the immersion bar
     */
    fun getTag(tag: String?): ImmersionBar {
        if (tag == null || tag.isEmpty()) throw IllegalArgumentException("tag cannot be empty")
        mTagMap[tag]?.let {
            mBarParams = it.clone()
        }
        return this
    }

    /**
     * Solve the conflict between the soft keyboard and the bottom input box. The default is false
     * Keyboard enable immersion bar.
     *
     * @param enable the enable
     * @return the immersion bar
     */
    fun keyboardEnable(enable: Boolean): ImmersionBar {
        return keyboardEnable(enable, mBarParams?.keyboardMode)
    }

    /**
     * Solve the conflict between the soft keyboard and the bottom input box. The default is false
     *
     * @param enable       the enable
     * @param keyboardMode the keyboard mode
     * @return the immersion bar
     */
    fun keyboardEnable(enable: Boolean, keyboardMode: Int?): ImmersionBar {
        mBarParams?.keyboardEnable = enable
        mBarParams?.keyboardMode = keyboardMode!!
        mKeyboardTempEnable = enable
        return this
    }

    /**
     * Modify keyboard mode
     * Keyboard mode immersion bar.
     *
     * @param keyboardMode the keyboard mode
     * @return the immersion bar
     */
    fun keyboardMode(keyboardMode: Int): ImmersionBar {
        mBarParams?.keyboardMode = keyboardMode
        return this
    }

    /**
     * Soft keyboard pop-up closed callback monitoring
     * Sets on keyboard listener.
     *
     * @param onKeyboardListener the on keyboard listener
     * @return the on keyboard listener
     */
    fun setOnKeyboardListener(onKeyboardListener: OnKeyboardListener?): ImmersionBar {
        if (mBarParams?.onKeyboardListener == null) {
            mBarParams?.onKeyboardListener = onKeyboardListener
        }
        return this
    }

    /**
     * Navigation bar shows hidden listeners
     * Sets on navigation bar listener.
     *
     * @param onNavigationBarListener the on navigation bar listener
     * @return the on navigation bar listener
     */
    fun setOnNavigationBarListener(onNavigationBarListener: OnNavigationBarListener?): ImmersionBar {
        if (onNavigationBarListener != null) {
            if (mBarParams?.onNavigationBarListener == null) {
                mBarParams?.onNavigationBarListener = onNavigationBarListener
                NavigationBarObserver.addOnNavigationBarListener(mBarParams?.onNavigationBarListener)
            }
        } else {
            if (mBarParams?.onNavigationBarListener != null) {
                NavigationBarObserver.removeOnNavigationBarListener(mBarParams?.onNavigationBarListener)
                mBarParams?.onNavigationBarListener = null
            }
        }
        return this
    }

    /**
     * Bar monitoring. This method will be triggered by the first call and horizontal/vertical screen switching.
     * For example, it can solve the problem of horizontal/vertical screen switching.
     * In horizontal/horizontal screen cases, the layout is blocked by the banging screen
     * Sets on bar listener.
     *
     * @param onBarListener the on bar listener
     * @return the on bar listener
     */
    fun setOnBarListener(onBarListener: OnBarListener?): ImmersionBar {
        if (onBarListener != null) {
            if (mBarParams?.onBarListener == null) {
                mBarParams?.onBarListener = onBarListener
            }
        } else {
            if (mBarParams?.onBarListener != null) {
                mBarParams?.onBarListener = null
            }
        }
        return this
    }

    /**
     * Can I modify the color of the navigation bar? The default is true
     * 优先级 navigationBarEnable  > navigationBarWithKitkatEnable > navigationBarWithEMUI3Enable
     * Navigation bar enable immersion bar.
     *
     * @param navigationBarEnable the enable
     * @return the immersion bar
     */
    fun navigationBarEnable(navigationBarEnable: Boolean): ImmersionBar {
        mBarParams?.navigationBarEnable = navigationBarEnable
        return this
    }

    /**
     * Can modify the 4.4 device navigation bar color? The default is true
     * 优先级 navigationBarEnable  > navigationBarWithKitkatEnable > navigationBarWithEMUI3Enable
     *
     * @param navigationBarWithKitkatEnable the navigation bar with kitkat enable
     * @return the immersion bar
     */
    fun navigationBarWithKitkatEnable(navigationBarWithKitkatEnable: Boolean): ImmersionBar {
        mBarParams?.navigationBarWithKitkatEnable = navigationBarWithKitkatEnable
        return this
    }

    /**
     * Whether the color of Huawei emui 3.1 navigation bar can be modified. The default value is true.
     * 优先级 navigationBarEnable  > navigationBarWithKitkatEnable > navigationBarWithEMUI3Enable
     * Navigation bar with emui 3 enable immersion bar.
     *
     * @param navigationBarWithEMUI3Enable the navigation bar with emui 3 1 enable
     * @return the immersion bar
     */
    fun navigationBarWithEMUI3Enable(navigationBarWithEMUI3Enable: Boolean): ImmersionBar {
        if (isEMUI3_x()) {
            mBarParams?.navigationBarWithEMUI3Enable = navigationBarWithEMUI3Enable
            mBarParams?.navigationBarWithKitkatEnable = navigationBarWithEMUI3Enable
        }
        return this
    }

    /**
     * Whether immersion can be used.
     * If it is already true, the immersion effect will not disappear before it is changed to false, and the immersion effect set later will not take effect
     * Bar enable immersion bar.
     *
     * @param barEnable the bar enable
     * @return the immersion bar
     */
    fun barEnable(barEnable: Boolean): ImmersionBar {
        mBarParams?.barEnable = barEnable
        return this
    }

    override fun onNavigationBarChange(show: Boolean, type: NavigationBarType) {

    }

    override fun run() {
        postFitsWindowsBelowLOLLIPOP()
    }

    private fun postFitsWindowsBelowLOLLIPOP() {
        //Solve the problem that the bottom of the activity is blocked by the navigation bar when Android 4.4 has a navigation bar, and solve the problem that the status bar and layout overlap when Android 5.0 or below
        fitsWindowsKITKAT()
        //Solve the problem of manually hiding the navigation bar of Huawei EMUI 3.1 or 3.0
        if (!mIsFragment && isEMUI3_x()) {
            fitsWindowsEMUI()
        }
    }

}