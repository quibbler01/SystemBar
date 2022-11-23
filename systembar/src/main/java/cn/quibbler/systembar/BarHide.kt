package cn.quibbler.systembar

/**
 * Status of bar
 */
enum class BarHide {
    /**
     * hide the status bar
     */
    FLAG_HIDE_STATUS_BAR,

    /**
     * Hide Navigation Bar
     */
    FLAG_HIDE_NAVIGATION_BAR,

    /**
     * Hide the status bar and navigation bar
     */
    FLAG_HIDE_BAR,

    /**
     * Show status bar and navigation bar
     */
    FLAG_SHOW_BAR
}