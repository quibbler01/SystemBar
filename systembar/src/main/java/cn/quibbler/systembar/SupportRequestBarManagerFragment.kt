package cn.quibbler.systembar

import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment

class SupportRequestBarManagerFragment : Fragment() {

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
        mDelegate?.let {
            it.onDestroy()
            mDelegate = null
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDelegate?.onConfigurationChanged(newConfig)
    }

}