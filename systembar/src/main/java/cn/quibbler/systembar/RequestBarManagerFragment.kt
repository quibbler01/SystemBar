package cn.quibbler.systembar

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.content.res.Configuration
import android.os.Bundle


class RequestBarManagerFragment : Fragment() {

    private var mDelegate: ImmersionDelegate? = null

    fun get(a: Any): ImmersionBar? {
        if (mDelegate == null) {
            mDelegate = ImmersionDelegate(a)
        }
        return mDelegate?.get()
    }

    fun get(activity: Activity, dialog: Dialog): ImmersionBar? {
        if (mDelegate == null) {
            mDelegate = ImmersionDelegate(activity, dialog)
        }
        return mDelegate?.get()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDelegate?.onActivityCreated(resources.configuration)
    }

    override fun onResume() {
        super.onResume()
        mDelegate?.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mDelegate != null) {
            mDelegate?.onDestroy()
            mDelegate = null
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDelegate?.onConfigurationChanged(newConfig)
    }

}