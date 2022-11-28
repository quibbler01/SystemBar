package cn.quibbler.systembar

/**
 * Soft keyboard monitoring
 */
interface OnKeyboardListener {

    /**
     * On keyboard change.
     *
     * @param isPopup        the is popup  是否弹出
     * @param keyboardHeight the keyboard height  软键盘高度
     */
    fun onKeyboardChange(isPopup: Boolean, keyboardHeight: Int)

}