package com.rob.gab.appokemon.ui.widget

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.rob.gab.appokemon.R


class ProgressWheelDialog : FullScreenDialogFragment() {

    companion object {
        const val TAG = "ProgressWheelDialog"
        var showingLoading = false
        private const val BUNDLE_KEY_MESSAGE = TAG.plus("bundle_key_message")

        fun newInstance(loadingText: String? = null): ProgressWheelDialog {
            val args = Bundle().apply {
                putString(BUNDLE_KEY_MESSAGE, loadingText)
            }

            val fragment = ProgressWheelDialog()
            fragment.arguments = args
            return fragment
        }

        fun show(activity: FragmentActivity, loadingText: String? = null) = synchronized(activity) {
            if(showingLoading) return
            retrieveDialogFragment(activity)?.dismissAllowingStateLoss()
            newInstance(loadingText).show(activity.supportFragmentManager, TAG)
            showingLoading = true
        }

        fun dismiss(activity: FragmentActivity) = synchronized(activity) {
            showingLoading = false
            retrieveDialogFragment(activity)?.dismissAllowingStateLoss()
                ?: run { // wait to allow the fragment to be attached
                    Handler(Looper.getMainLooper()).postDelayed(
                        { if (!showingLoading) retrieveDialogFragment(activity)?.dismissAllowingStateLoss() },
                        1000
                    )
                }
        }

        private fun retrieveDialogFragment(activity: FragmentActivity): DialogFragment? {
            return activity.supportFragmentManager.findFragmentByTag(TAG)
                ?.takeIf { it is DialogFragment }
                ?.let { it as DialogFragment }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.LoadingDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_progress_wheel, container, false)
    }

}