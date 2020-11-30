package com.rob.gab.appokemon.ui.widget

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.DialogFragment
import com.rob.gab.appokemon.R

abstract class FullScreenDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
        isCancelable = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //This is the filter
                if (event.action != KeyEvent.ACTION_DOWN) {
                    true
                } else {
                    onBackPressed()
                    if (isCancelable)
                        dismiss()
                    true // pretend we've processed it
                }
            } else {
                false // pass on to be processed rounded_layout_card_operations normal
            }
        }
    }

    protected open fun onBackPressed() {
        //do nothing
    }

}