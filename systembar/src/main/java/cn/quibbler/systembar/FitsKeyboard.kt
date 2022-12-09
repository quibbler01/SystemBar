package cn.quibbler.systembar

import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.widget.FrameLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

/**
 * Pop up problem of adaptive soft keyboard
 */
class FitsKeyboard : ViewTreeObserver.OnGlobalLayoutListener {

    private var mImmersionBar: ImmersionBar? = null
    private var mWindow: Window? = null
    private var mDecorView: View? = null
    private var mContentView: View? = null
    private var mChildView: View? = null
    private var mPaddingLeft: Int = 0
    private var mPaddingTop: Int = 0
    private var mPaddingRight: Int = 0
    private var mPaddingBottom: Int = 0
    private var mTempKeyboardHeight: Int = 0
    private var mIsAddListener: Boolean = false

    constructor(immersionBar: ImmersionBar) {
        mImmersionBar = immersionBar
        mWindow = immersionBar.window
        mDecorView = mWindow?.decorView
        val frameLayout: FrameLayout? = mDecorView?.findViewById<FrameLayout>(android.R.id.content)
        if (immersionBar.isDialogFragment()) {
            val supportFragment: Fragment? = immersionBar.getSupportFragment()
            if (supportFragment != null) {
                mChildView = supportFragment.view
            } else {
                val fragment: android.app.Fragment? = immersionBar.getFragment()
                if (fragment != null) {
                    mChildView = fragment.view
                }
            }
        } else {
            mChildView = frameLayout?.getChildAt(0)
            if (mChildView != null) {
                if (mChildView is DrawerLayout) {
                    mChildView = (mChildView as DrawerLayout).getChildAt(0)
                }
            }
        }
        mChildView?.let {
            mPaddingLeft = it.paddingLeft
            mPaddingTop = it.paddingTop
            mPaddingRight = it.paddingRight
            mPaddingBottom = it.paddingBottom
        }
        mContentView = if (mChildView != null) mChildView else frameLayout
    }

    fun enable(mode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWindow?.setSoftInputMode(mode)
            if (!mIsAddListener) {
                mDecorView?.viewTreeObserver?.addOnGlobalLayoutListener(this)
                mIsAddListener = true
            }
        }
    }

    fun disable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mIsAddListener) {
            if (mChildView != null) {
                mContentView?.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            } else {
                mContentView?.setPadding(
                    mImmersionBar.getPaddingLeft(),
                    mImmersionBar.getPaddingTop(),
                    mImmersionBar.getPaddingRight(),
                    mImmersionBar.getPaddingBottom()
                )
            }
        }
    }

    fun cancel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mIsAddListener) {
            mDecorView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            mIsAddListener = false;
        }
    }

    fun resetKeyboardHeight() {
        mTempKeyboardHeight = 0
    }

    override fun onGlobalLayout() {
        if (mImmersionBar != null && mImmersionBar.getBarParams() != null && mImmersionBar.getBarParams().keyboardEnable) {
            val barConfig: BarConfig = mImmersionBar.getBarConfig()
            var bottom = 0
            var keyboardHeight: Int = if (barConfig.isNavigationAtBottom()) barConfig.getNavigationBarHeight() else barConfig.getNavigationBarWidth()
            val navigationBarHeight = if (barConfig.isNavigationAtBottom()) barConfig.getNavigationBarHeight() else barConfig.getNavigationBarWidth()
            var isPopup = false
            val rect = Rect()
            //Get the viewing area size of the current window
            mDecorView?.getWindowVisibleDisplayFrame(rect)
            mContentView?.let {
                keyboardHeight = it.height - rect.bottom
                if (keyboardHeight != mTempKeyboardHeight) {
                    mTempKeyboardHeight = keyboardHeight
                    if (!ImmersionBar.checkFitsSystemWindows(mWindow?.decorView?.findViewById(android.R.id.content))) {
                        if (mChildView != null) {
                            if (mImmersionBar.getBarParams().isSupportActionBar) {
                                keyboardHeight += mImmersionBar.getActionBarHeight() + barConfig.getStatusBarHeight()
                            }
                            if (mImmersionBar.getBarParams().fits) {
                                keyboardHeight += barConfig.getStatusBarHeight()
                            }
                            if (keyboardHeight > navigationBarHeight) {
                                bottom = keyboardHeight + mPaddingBottom
                                isPopup = true
                            }
                            mContentView?.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, bottom)
                        } else {
                            bottom = mImmersionBar.getPaddingBottom()
                            keyboardHeight -= navigationBarHeight
                            if (keyboardHeight > navigationBarHeight) {
                                bottom = keyboardHeight + navigationBarHeight
                                isPopup = true
                            }
                            mContentView?.setPadding(
                                mImmersionBar.getPaddingLeft(),
                                mImmersionBar.getPaddingTop(),
                                mImmersionBar.getPaddingRight(),
                                bottom
                            )
                        }
                    } else {
                        keyboardHeight -= navigationBarHeight
                        if (keyboardHeight > navigationBarHeight) {
                            isPopup = true
                        }
                    }
                    if (keyboardHeight < 0) {
                        keyboardHeight = 0
                    }
                    if (mImmersionBar.getBarParams().onKeyboardListener != null) {
                        mImmersionBar.getBarParams().onKeyboardListener.onKeyboardChange(isPopup, keyboardHeight)
                    }
                    if (!isPopup && mImmersionBar.getBarParams().barHide !== BarHide.FLAG_SHOW_BAR) {
                        mImmersionBar.setBar()
                    }
                    if (!isPopup) {
                        mImmersionBar.fitsParentBarKeyboard()
                    }
                }
            }
        }
    }

}