package cn.quibbler.systembar

enum class NavigationBarType(val type: Int) {

    /**
     * Classic navigation key
     */
    CLASSIC(0),

    /**
     * Gesture navigation
     */
    GESTURES(1),

    /**
     * Gesture navigation, three-stage, small button
     */
    GESTURES_THREE_STAGE(2),

    /**
     * Two buttons
     */
    DOUBLE(3),

    /**
     * unknown
     */
    UNKNOWN(-1)

}