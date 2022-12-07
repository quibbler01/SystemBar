package cn.quibbler.systembar

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
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

    private val mPendingFragments: HashMap<android.app.FragmentManager, RequestBarManagerFragment> = HashMap()
    private val mPendingSupportFragments: HashMap<FragmentManager, SupportRequestBarManagerFragment> = HashMap()

    private val mPendingRemoveFragments: HashMap<String, RequestBarManagerFragment> = HashMap()
    private val mPendingSupportRemoveFragments: HashMap<String, SupportRequestBarManagerFragment> = HashMap()

    /**
     * Get immersion bar.
     *
     * @param activity the activity
     * @param isOnly   the is only
     * @return the immersion bar
     */
    fun get(activity: Activity?, isOnly: Boolean): ImmersionBar? {
        checkNotNull(activity) { "activity is null" }
        var tag: String = mTag
        tag += activity.javaClass.name
        if (!isOnly) {
            tag += "${System.identityHashCode(activity)}$mNotOnly"
        }
        if (activity is FragmentActivity) {
            return getSupportFragment(activity.supportFragmentManager, tag)?.get(activity)
        } else {
            return getFragment(activity.getFragmentManager(), tag)?.get(activity)
        }
    }

    /**
     * Get immersion bar.
     *
     * @param fragment the fragment
     * @param isOnly   the is only
     * @return the immersion bar
     */
    fun get(fragment: Fragment?, isOnly: Boolean): ImmersionBar? {
        checkNotNull(fragment) { "fragment is null" }
        checkNotNull(fragment.activity) { "fragment.getActivity() is null" }
        if (fragment is DialogFragment) {
            checkNotNull((fragment as DialogFragment).dialog) { "fragment.getDialog() is null" }
        }
        var tag = mTag
        tag += fragment.javaClass.name
        if (!isOnly) {
            tag += "${System.identityHashCode(fragment)}$mNotOnly"
        }
        return getSupportFragment(fragment.childFragmentManager, tag)?.get(fragment)
    }

    /**
     * Get immersion bar.
     *
     * @param fragment the fragment
     * @param isOnly   the is only
     * @return the immersion bar
     */
    fun get(fragment: android.app.Fragment?, isOnly: Boolean): ImmersionBar? {
        checkNotNull(fragment) { "fragment is null" }
        checkNotNull(fragment.activity) { "fragment.getActivity() is null" }
        if (fragment is android.app.DialogFragment) {
            checkNotNull((fragment as android.app.DialogFragment).dialog) { "fragment.getDialog() is null" }
        }
        var tag = mTag
        tag += fragment.javaClass.name
        if (!isOnly) {
            tag += "${System.identityHashCode(fragment)}$mNotOnly"
        }
        return getFragment(fragment.getChildFragmentManager(), tag)?.get(fragment)
    }

    /**
     * @param activity the activity
     * @param dialog   the dialog
     * @param isOnly   the is only
     * @return the immersion bar
     */
    fun get(activity: Activity?, dialog: Dialog?, isOnly: Boolean): ImmersionBar? {
        checkNotNull(activity) { "activity is null" }
        checkNotNull(dialog) { "dialog is null" }
        var tag = mTag
        tag += dialog.javaClass.name
        if (!isOnly) {
            tag += "${System.identityHashCode(dialog)}$mNotOnly"
        }
        if (activity is FragmentActivity) {
            return getSupportFragment(activity.supportFragmentManager, tag)?.get(activity, dialog)
        } else {
            return getFragment(activity.fragmentManager, tag)?.get(activity, dialog)
        }
    }

    fun destroy(fragment: Fragment?, isOnly: Boolean) {
        if (fragment == null) return
        var tag = mTag
        tag += fragment.javaClass.name
        if (!isOnly) {
            tag += "${System.identityHashCode(fragment)}$mNotOnly"
        }
        getSupportFragment(fragment.childFragmentManager, tag, true)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun destroy(fragment: android.app.Fragment?, isOnly: Boolean) {
        if (fragment == null) return
        var tag = mTag
        tag += fragment.javaClass.name
        if (!isOnly) {
            tag += "${System.identityHashCode(fragment)}$mNotOnly"
        }
        getFragment(fragment.childFragmentManager, tag, true)
    }

    /**
     * Destroy
     *
     * @param activity the activity
     * @param dialog   the dialog
     * @param isOnly   the isOnly
     */
    fun destroy(activity: Activity?, dialog: Dialog?, isOnly: Boolean) {
        if (activity == null || dialog == null) {
            return
        }
        var tag = mTag
        tag += dialog.javaClass.name
        if (!isOnly) {
            tag += "${System.identityHashCode(dialog)}$mNotOnly"
        }
        if (activity is FragmentActivity) {
            getSupportFragment(activity.supportFragmentManager, tag, true)
        } else {
            getFragment(activity.fragmentManager, tag, true)
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        var handled = true
        when (msg.what) {
            ID_REMOVE_FRAGMENT_MANAGER -> {
                val fm = msg.obj as android.app.FragmentManager
                mPendingFragments.remove(fm)
            }
            ID_REMOVE_SUPPORT_FRAGMENT_MANAGER -> {
                val supportFm = msg.obj as FragmentManager
                mPendingSupportFragments.remove(supportFm)
            }
            ID_REMOVE_FRAGMENT_MANAGER_REMOVE -> {
                val tag = msg.obj as String
                mPendingRemoveFragments.remove(tag)
            }
            ID_REMOVE_SUPPORT_FRAGMENT_MANAGER_REMOVE -> {
                val supportTag = msg.obj as String
                mPendingSupportRemoveFragments.remove(supportTag)
            }
            else -> {
                handled = false
            }
        }
        return handled
    }

    private fun getFragment(fm: android.app.FragmentManager, tag: String): RequestBarManagerFragment? {
        return getFragment(fm, tag, false)
    }

    private fun getFragment(fm: android.app.FragmentManager, tag: String, destroy: Boolean): RequestBarManagerFragment? {
        var fragment: RequestBarManagerFragment? = fm.findFragmentByTag(tag) as RequestBarManagerFragment?
        if (fragment == null) {
            fragment = mPendingFragments.get(fm)
            if (fragment == null) {
                if (destroy) {
                    return null
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        for (fmFragment: android.app.Fragment in fm.fragments) {
                            if (fmFragment is RequestBarManagerFragment) {
                                var oldTag: String? = fmFragment.tag
                                if (oldTag == null) {
                                    fm.beginTransaction().remove(fmFragment).commitAllowingStateLoss()
                                } else {
                                    if (oldTag.contains(mNotOnly)) {
                                        fm.beginTransaction().remove(fmFragment).commitAllowingStateLoss()
                                    }
                                }
                            }
                        }
                    }
                }
                fragment = RequestBarManagerFragment()
                mPendingFragments[fm] = fragment
                fm.beginTransaction().add(fragment, tag).commitNowAllowingStateLoss()
                mHandler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER, fm).sendToTarget()
            }
        }
        if (destroy) {
            if (mPendingRemoveFragments[tag] == null) {
                mPendingRemoveFragments[tag] = fragment
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
                mHandler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER_REMOVE, tag).sendToTarget()
            }
            return null
        }
        return fragment
    }

    private fun getSupportFragment(fm: FragmentManager, tag: String): SupportRequestBarManagerFragment? {
        return getSupportFragment(fm, tag, false)
    }

    private fun getSupportFragment(fm: FragmentManager, tag: String, destroy: Boolean): SupportRequestBarManagerFragment? {
        var fragment = fm.findFragmentByTag(tag) as SupportRequestBarManagerFragment?
        if (fragment == null) {
            fragment = mPendingSupportFragments[fm]
            if (fragment == null) {
                if (destroy) {
                    return null
                } else {
                    for (fmFragment in fm.fragments) {
                        if (fmFragment is SupportRequestBarManagerFragment) {
                            var oldTag: String? = fmFragment.tag
                            if (oldTag == null) {
                                fm.beginTransaction().remove(fmFragment).commitAllowingStateLoss()
                            } else {
                                if (oldTag.contains(mNotOnly)) {
                                    fm.beginTransaction().remove(fmFragment).commitAllowingStateLoss()
                                }
                            }
                        }
                    }
                }
                fragment = SupportRequestBarManagerFragment()
                mPendingSupportFragments[fm] = fragment
                fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss()
                mHandler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget()
            }
        }
        if (destroy) {
            if (mPendingSupportRemoveFragments[tag] == null) {
                mPendingSupportRemoveFragments[tag] = fragment
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
                mHandler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER_REMOVE, tag).sendToTarget()
            }
            return null
        }
        return fragment
    }

}