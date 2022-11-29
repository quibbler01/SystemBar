package cn.quibbler.systembar

/**
 * The interface On navigation bar listener.
 */
interface OnNavigationBarListener {

    /**
     * On navigation bar change.
     *
     * @param type the NavigationBarType
     */
    fun onNavigationBarChange(show: Boolean, type: NavigationBarType)

}