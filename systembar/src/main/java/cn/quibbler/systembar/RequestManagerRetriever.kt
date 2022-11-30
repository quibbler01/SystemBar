package cn.quibbler.systembar

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * The type Request manager retriever.
 */
object RequestManagerRetriever : Handler.Callback {

    private val mTag = "${ImmersionBar::class.java.name}."
    private const val mNotOnly = ".tag.notOnly."

    private const val ID_REMOVE_FRAGMENT_MANAGER = 1
    private const val ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2
    private const val ID_REMOVE_FRAGMENT_MANAGER_REMOVE = 3
    private const val ID_REMOVE_SUPPORT_FRAGMENT_MANAGER_REMOVE = 4

    private val mHandler = Handler(Looper.getMainLooper(), this)

    private val mPendingFragments: Map<FragmentManager, RequestBarManagerFragment> = HashMap()
    private val mPendingSupportFragments: Map<FragmentManager, SupportRequestBarManagerFragment> = HashMap()

    private val mPendingRemoveFragments: Map<String, RequestBarManagerFragment> = HashMap()
    private val mPendingSupportRemoveFragments: Map<String, SupportRequestBarManagerFragment> = HashMap()

    /**
     * Get immersion bar.
     *
     * @param activity the activity
     * @param isOnly   the is only
     * @return the immersion bar
     */
    fun get(activity: Activity?, isOnly: Boolean): ImmersionBar {
        checkNotNull(activity) { "activity is null" }
        var tag: String = mTag
        tag += activity.javaClass.name
        if (!isOnly) {
            tag += "${System.identityHashCode(activity)}$mNotOnly"
        }
        if (activity is FragmentActivity) {

        }else{

        }
    }

    override fun handleMessage(msg: Message): Boolean {

    }

}