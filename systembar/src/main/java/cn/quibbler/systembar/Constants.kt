package cn.quibbler.systembar

/**
 * Android 4.4 or emui3 status bar ID identification
 */
val IMMERSION_STATUS_BAR_VIEW_ID = R.id.immersion_status_bar_view

/**
 * android 4.4 or emui3 navigation bar ID identification
 */
val IMMERSION_NAVIGATION_BAR_VIEW_ID = R.id.immersion_navigation_bar_view

/**
 * Status bar height identifier
 */
const val IMMERSION_STATUS_BAR_HEIGHT = "status_bar_height"

/**
 * Navigation bar vertical screen height indicator
 */
const val IMMERSION_NAVIGATION_BAR_HEIGHT = "navigation_bar_height"

/**
 * Navigation bar horizontal screen height indicator
 */
const val IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE = "navigation_bar_height_landscape"

/**
 * Navigation bar width identification bit
 */
const val IMMERSION_NAVIGATION_BAR_WIDTH = "navigation_bar_width"

/**
 * Xiaomi navigation bar shows hidden signs:
 * 0-three button navigation
 * 1-gesture navigation
 */
const val IMMERSION_NAVIGATION_BAR_MODE_MIUI = "force_fsg_nav_bar"

/**
 * Whether to hide the gesture prompt line in Xiaomi navigation bar gesture navigation:
 * 0: display
 * 1: hide
 */
const val IMMERSION_NAVIGATION_BAR_MODE_MIUI_HIDE = "hide_gesture_line"

/**
 * Huawei navigation bar shows hidden sign bit:
 * 0 - three button navigation,
 * 1 - gesture navigation
 */
const val IMMERSION_NAVIGATION_BAR_MODE_EMUI = "navigationbar_is_min"

/**
 * The VIVO navigation bar shows hidden sign bit:
 * 0 - three button navigation
 * 1 - classic three-stage
 * 2 - full screen gesture
 */
const val IMMERSION_NAVIGATION_BAR_MODE_VIVO = "navigation_gesture_on"

/**
 * OPPO navigation bar shows hidden sign position:
 * 0 - three button navigation,
 * 1 - gesture navigation,
 * 2 - upward stroke gesture,
 * 3 - sideslip gesture
 */
const val IMMERSION_NAVIGATION_BAR_MODE_OPPO = "hide_navigationbar_enable"

/**
 * SAMSUNG navigation bar shows hidden sign bit
 * 0 - three button navigation
 * 1 - gesture navigation
 */
const val IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG = "navigation_bar_gesture_while_hidden"

/**
 * In the case of gesture navigation on Samsung navigation bar, gesture type:
 * 0: three segment line
 * 1: single line
 */
const val IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE_TYPE = "navigation_bar_gesture_detail_type"

/**
 * Whether to hide the gesture prompt line in Samsung navigation bar gesture navigation
 * 0: hide
 * 1: display
 */
const val IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE = "navigation_bar_gesture_hint"

/**
 * The SAMSUNG navigation bar shows hidden signs
 * 0-three button navigation
 * 1-gesture navigation
 */
const val IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_OLD = "navigationbar_hide_bar_enabled"

/**
 * Default gesture navigation 0-three button navigation
 * 1-double button navigation
 * 2-gesture navigation
 */
const val IMMERSION_NAVIGATION_BAR_MODE_DEFAULT = "navigation_mode"

/**
 * MIUI status bar font black and white flag bit
 */
const val IMMERSION_STATUS_BAR_DARK_MIUI = "EXTRA_FLAG_STATUS_BAR_DARK_MODE"

/**
 * MIUI navigation bar icon black and white identification bit
 */
const val IMMERSION_NAVIGATION_BAR_DARK_MIUI = "EXTRA_FLAG_NAVIGATION_BAR_DARK_MODE"

/**
 * Threshold flag bit for automatically changing font color
 */
const val IMMERSION_BOUNDARY_COLOR = -0x454546

/**
 * Repair the overlap flag of the status bar and the layout. It is not repaired by default
 */
const val FLAG_FITS_DEFAULT = 0X00

/**
 * Repair the overlapping flag bit of the status bar and the layout by using the title Bar method
 */
const val FLAG_FITS_TITLE = 0X01

/**
 * Repair the overlapping flag bit of the status bar and layout by using the title Bar Margin Top method
 */
const val FLAG_FITS_TITLE_MARGIN_TOP = 0X02

/**
 * Repair the overlapping flag bit of the status bar and layout by using the Status Bar View method
 */
const val FLAG_FITS_STATUS = 0X03

/**
 * Repair the overlap flag of the status bar and the layout by using the fits System Windows method
 */
const val FLAG_FITS_SYSTEM_WINDOWS = 0X04