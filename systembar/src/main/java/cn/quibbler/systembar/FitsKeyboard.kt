package cn.quibbler.systembar

import android.view.View
import android.view.ViewTreeObserver
import android.view.Window

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
    }

    override fun onGlobalLayout() {

    }

}